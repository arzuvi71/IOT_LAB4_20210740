package com.example.iot_lab4_20210740.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.iot_lab4_20210740.R;
import com.example.iot_lab4_20210740.Constants;
import com.example.iot_lab4_20210740.adapters.HourlyAdapter;
import com.example.iot_lab4_20210740.apis.WeatherApiService;
import com.example.iot_lab4_20210740.models.ForecastDay;
import com.example.iot_lab4_20210740.models.ForecastResponse;
import com.example.iot_lab4_20210740.models.FutureResponse;
import com.example.iot_lab4_20210740.models.HistoryResponse;
import com.example.iot_lab4_20210740.models.HourlyForecast;

public class DateFragment extends Fragment implements DatePickerFragment.OnDateSelectedListener, HourlyAdapter.OnHourlyClickListener {
    

    
    private TextView textViewLocationName;
    private TextView textViewSelectedDate;
    private Button buttonSearchForecast;
    private RecyclerView recyclerViewHourly;
    private ProgressBar progressBar;
    private com.google.android.material.textfield.TextInputEditText editTextLocationId;
    private com.google.android.material.textfield.TextInputEditText editTextDate;
    
    private HourlyAdapter adapter;
    private WeatherApiService weatherApiService;
    private List<HourlyForecast> hourlyForecasts;
    
    private String locationName;
    private int locationId;
    private float locationLat;
    private float locationLon;
    private String selectedDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        getArgumentsData();
        initViews(view);
        setupRecyclerView();
        setupRetrofit();
        setupClickListeners();
    }

    private void getArgumentsData() {
        // Recepción de parámetros
        if (getArguments() != null) {
            locationName = getArguments().getString("locationName", "Ubicación desconocida");
            locationId = getArguments().getInt("locationId", 0);
            locationLat = getArguments().getFloat("locationLat", 0.0f);
            locationLon = getArguments().getFloat("locationLon", 0.0f);
        }
    }

    private void initViews(View view) {
        textViewLocationName = view.findViewById(R.id.textViewLocationName);
        textViewSelectedDate = view.findViewById(R.id.textViewSelectedDate);
        buttonSearchForecast = view.findViewById(R.id.buttonSearchForecast);
        recyclerViewHourly = view.findViewById(R.id.recyclerViewHourly);
        progressBar = view.findViewById(R.id.progressBar);
        editTextLocationId = view.findViewById(R.id.editTextLocationId);
        editTextDate = view.findViewById(R.id.editTextDate);

        textViewLocationName.setText(locationName);

        if (locationId != 0) {
            editTextLocationId.setText(String.valueOf(locationId));
        }
    }

    private void setupRecyclerView() {
        hourlyForecasts = new ArrayList<>();
        adapter = new HourlyAdapter(hourlyForecasts, this);

        recyclerViewHourly.setAdapter(adapter);
        recyclerViewHourly.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupRetrofit() {
        weatherApiService = new Retrofit.Builder()
                .baseUrl(Constants.WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService.class);
    }

    private void setupClickListeners() {
        editTextDate.setOnClickListener(v -> showDatePicker());

        buttonSearchForecast.setOnClickListener(v -> {
            String locationIdText = editTextLocationId.getText().toString().trim();
            String dateText = editTextDate.getText().toString().trim();

            if (!locationIdText.isEmpty() && !dateText.isEmpty()) {
                try {
                    int searchLocationId = Integer.parseInt(locationIdText);
                    selectedDate = dateText;
                    locationId = searchLocationId;
                    loadHourlyForecast();
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Ingrese un ID de ubicación válido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSelectedListener(this);
        
        //Dialog
        datePickerFragment.show(getParentFragmentManager(), "datepicker");
    }

    @Override
    public void onDateSelected(int year, int month, int day) {
        // Formateo dela fecha
        selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day);

        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.set(year, month, day);

        // Actualizar el campo de la fecha
        editTextDate.setText(selectedDate);

        String displayDate = String.format("Fecha seleccionada: %02d/%02d/%04d", day, month + 1, year);
        textViewSelectedDate.setText(displayDate);

        Toast.makeText(getContext(), "Fecha seleccionada: " + displayDate, Toast.LENGTH_SHORT).show();
    }

    private void loadHourlyForecast() {
        progressBar.setVisibility(View.VISIBLE);

        // Usar ID de ubicación si está disponible, sino usar coordenadas
        String query = (locationId != 0) ? "id:" + locationId : locationLat + "," + locationLon;

        // Determinar qué método usar según la fecha
        Calendar today = Calendar.getInstance();
        Calendar selectedCalendar = Calendar.getInstance();
        String[] dateParts = selectedDate.split("-");
        selectedCalendar.set(Integer.parseInt(dateParts[0]),
                           Integer.parseInt(dateParts[1]) - 1,
                           Integer.parseInt(dateParts[2]));

        long daysDifference = (selectedCalendar.getTimeInMillis() - today.getTimeInMillis()) / (1000 * 60 * 60 * 24);

        if (daysDifference >= 15) {
            // Fechas futuras
            loadFutureForecast(query);
        } else if (daysDifference < 0) {
            // Fechas pasadas
            loadHistoryForecast(query);
        } else {
            // Caso contrario
            loadRegularForecast(query);
        }
    }

    private void loadRegularForecast(String query) {
        // Llamada API
        weatherApiService.getForecast(Constants.WEATHER_API_KEY, query, Constants.FORECAST_DAYS).enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(@NonNull Call<ForecastResponse> call, @NonNull Response<ForecastResponse> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    ForecastResponse forecastResponse = response.body();
                    if (forecastResponse.getForecast() != null && 
                        forecastResponse.getForecast().getForecastday() != null) {
                        
                        // Buscar el día específico seleccionado
                        for (ForecastDay forecastDay : forecastResponse.getForecast().getForecastday()) {
                            if (selectedDate.equals(forecastDay.getDate())) {
                                hourlyForecasts.clear();
                                if (forecastDay.getHour() != null) {
                                    hourlyForecasts.addAll(forecastDay.getHour());
                                }
                                adapter.notifyDataSetChanged();
                                
                                if (hourlyForecasts.isEmpty()) {
                                    Toast.makeText(getContext(), "No hay datos por hora para esta fecha", Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }
                        }
                        
                        Toast.makeText(getContext(), "No se encontraron datos para la fecha seleccionada", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al cargar pronóstico por hora", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForecastResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFutureForecast(String query) {
        weatherApiService.getFutureForecast(Constants.WEATHER_API_KEY, query, selectedDate).enqueue(new Callback<FutureResponse>() {
            @Override
            public void onResponse(@NonNull Call<FutureResponse> call, @NonNull Response<FutureResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    processFutureResponse(response.body());
                } else {
                    Toast.makeText(getContext(), "Error al cargar pronóstico futuro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FutureResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadHistoryForecast(String query) {
        weatherApiService.getHistoryForecast(Constants.WEATHER_API_KEY, query, selectedDate).enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<HistoryResponse> call, @NonNull Response<HistoryResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    processHistoryResponse(response.body());
                } else {
                    Toast.makeText(getContext(), "Error al cargar pronóstico histórico", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<HistoryResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processForecastResponse(ForecastResponse forecastResponse) {
        if (forecastResponse.getForecast() != null &&
            forecastResponse.getForecast().getForecastday() != null) {

            if (forecastResponse.getLocation() != null) {
                String fullLocationName = forecastResponse.getLocation().getName() +
                    ", " + forecastResponse.getLocation().getCountry();
                textViewLocationName.setText(fullLocationName);
                adapter.setLocationInfo(fullLocationName, locationId);
            }

            // Busqueda del día  seleccionado
            for (ForecastDay forecastDay : forecastResponse.getForecast().getForecastday()) {
                if (selectedDate.equals(forecastDay.getDate())) {
                    hourlyForecasts.clear();
                    if (forecastDay.getHour() != null) {
                        hourlyForecasts.addAll(forecastDay.getHour());
                    }
                    adapter.notifyDataSetChanged();

                    if (hourlyForecasts.isEmpty()) {
                        Toast.makeText(getContext(), "No hay datos por hora para esta fecha", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }

            Toast.makeText(getContext(), "No se encontraron datos para la fecha seleccionada", Toast.LENGTH_SHORT).show();
        }
    }

    private void processFutureResponse(FutureResponse futureResponse) {
        if (futureResponse.getForecast() != null &&
            futureResponse.getForecast().getForecastday() != null) {


            if (futureResponse.getLocation() != null) {
                String fullLocationName = futureResponse.getLocation().getName() +
                    ", " + futureResponse.getLocation().getCountry();
                textViewLocationName.setText(fullLocationName);
                adapter.setLocationInfo(fullLocationName, locationId);
            }

            // Busqueda del día  seleccionado
            for (ForecastDay forecastDay : futureResponse.getForecast().getForecastday()) {
                if (selectedDate.equals(forecastDay.getDate())) {
                    hourlyForecasts.clear();
                    if (forecastDay.getHour() != null) {
                        hourlyForecasts.addAll(forecastDay.getHour());
                    }
                    adapter.notifyDataSetChanged();

                    if (hourlyForecasts.isEmpty()) {
                        Toast.makeText(getContext(), "No hay datos por hora para esta fecha futura", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }

            Toast.makeText(getContext(), "No se encontraron datos para la fecha futura seleccionada", Toast.LENGTH_SHORT).show();
        }
    }

    private void processHistoryResponse(HistoryResponse historyResponse) {
        if (historyResponse.getForecast() != null &&
            historyResponse.getForecast().getForecastday() != null) {


            if (historyResponse.getLocation() != null) {
                String fullLocationName = historyResponse.getLocation().getName() +
                    ", " + historyResponse.getLocation().getCountry();
                textViewLocationName.setText(fullLocationName);
                adapter.setLocationInfo(fullLocationName, locationId);
            }

            // Busqueda del día  seleccionado
            for (ForecastDay forecastDay : historyResponse.getForecast().getForecastday()) {
                if (selectedDate.equals(forecastDay.getDate())) {
                    hourlyForecasts.clear();
                    if (forecastDay.getHour() != null) {
                        hourlyForecasts.addAll(forecastDay.getHour());
                    }
                    adapter.notifyDataSetChanged();

                    if (hourlyForecasts.isEmpty()) {
                        Toast.makeText(getContext(), "No hay datos históricos por hora para esta fecha", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }

            Toast.makeText(getContext(), "No se encontraron datos históricos para la fecha seleccionada", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onHourlyClick(HourlyForecast hourlyForecast) {
        String message = String.format("Hora: %s\nTemperatura: %.1f°C\nCondición: %s",
            hourlyForecast.getTime(),
            hourlyForecast.getTempC(),
            hourlyForecast.getCondition() != null ? hourlyForecast.getCondition().getText() : "N/A");

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
