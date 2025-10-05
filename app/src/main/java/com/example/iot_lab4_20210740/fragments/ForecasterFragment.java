package com.example.iot_lab4_20210740.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import java.util.List;

import com.example.iot_lab4_20210740.R;
import com.example.iot_lab4_20210740.Constants;
import com.example.iot_lab4_20210740.adapters.ForecastAdapter;
import com.example.iot_lab4_20210740.apis.WeatherApiService;
import com.example.iot_lab4_20210740.models.ForecastDay;
import com.example.iot_lab4_20210740.models.ForecastResponse;

public class ForecasterFragment extends Fragment implements SensorEventListener, ForecastAdapter.OnForecastClickListener {
    

    
    private TextView textViewLocationName;
    private TextView textViewSensorInfo;
    private Button buttonSearchManual;
    private RecyclerView recyclerViewForecasts;
    private ProgressBar progressBar;
    private com.google.android.material.textfield.TextInputEditText editTextLocationId;
    private com.google.android.material.textfield.TextInputEditText editTextDays;
    
    private ForecastAdapter adapter;
    private WeatherApiService weatherApiService;
    private List<ForecastDay> forecasts;
    
    // Sensores
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    
    private String locationName;
    private int locationId;
    private float locationLat;
    private float locationLon;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecaster, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        getArgumentsData();
        initViews(view);
        setupSensors();
        setupRecyclerView();
        setupRetrofit();
        setupClickListeners();
        loadForecastData();
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
        textViewSensorInfo = view.findViewById(R.id.textViewSensorInfo);
        buttonSearchManual = view.findViewById(R.id.buttonSearchManual);
        recyclerViewForecasts = view.findViewById(R.id.recyclerViewForecasts);
        progressBar = view.findViewById(R.id.progressBar);
        editTextLocationId = view.findViewById(R.id.editTextLocationId);
        editTextDays = view.findViewById(R.id.editTextDays);

        textViewLocationName.setText(locationName);

        if (locationId != 0) {
            editTextLocationId.setText(String.valueOf(locationId));
        }
    }

    private void setupSensors() {
        // Configuración de sensores
        mSensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        // Validación de sensor
        if (mAccelerometer != null) {
            textViewSensorInfo.setText("Sensor de Acelerómetro: Activo\nÚltima lectura: Esperando datos...");
        } else {
            textViewSensorInfo.setText("Sensor de Acelerómetro: No disponible en este dispositivo");
        }
    }

    private void setupRecyclerView() {
        forecasts = new ArrayList<>();
        adapter = new ForecastAdapter(forecasts, this);

        recyclerViewForecasts.setAdapter(adapter);
        recyclerViewForecasts.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupRetrofit() {
        weatherApiService = new Retrofit.Builder()
                .baseUrl(Constants.WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService.class);
    }

    private void setupClickListeners() {

        buttonSearchManual.setOnClickListener(v -> {
            String locationIdText = editTextLocationId.getText().toString().trim();
            String daysText = editTextDays.getText().toString().trim();

            if (!locationIdText.isEmpty() && !daysText.isEmpty()) {
                try {
                    int searchLocationId = Integer.parseInt(locationIdText);
                    int searchDays = Integer.parseInt(daysText);

                    if (searchDays > 0 && searchDays <= 14) {
                        loadManualForecast(searchLocationId, searchDays);
                    } else {
                        Toast.makeText(getContext(), "Los días deben estar entre 1 y 14", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Ingrese valores numéricos válidos", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadManualForecast(int searchLocationId, int searchDays) {
        progressBar.setVisibility(View.VISIBLE);

        String query = "id:" + searchLocationId;

        // Llamada API
        weatherApiService.getForecast(Constants.WEATHER_API_KEY, query, searchDays).enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(@NonNull Call<ForecastResponse> call, @NonNull Response<ForecastResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    ForecastResponse forecastResponse = response.body();

                    if (forecastResponse.getForecast() != null &&
                        forecastResponse.getForecast().getForecastday() != null) {

                        forecasts.clear();
                        forecasts.addAll(forecastResponse.getForecast().getForecastday());
                        adapter.notifyDataSetChanged();

                        if (forecastResponse.getLocation() != null) {
                            String fullLocationName = forecastResponse.getLocation().getName() +
                                ", " + forecastResponse.getLocation().getCountry();
                            adapter.setLocationName(fullLocationName);
                        }

                        Toast.makeText(getContext(), "Pronósticos cargados: " + forecasts.size() + " días", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "No se encontraron pronósticos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al cargar pronósticos. Verifique el ID de ubicación.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForecastResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadForecastData() {
        progressBar.setVisibility(View.VISIBLE);


        String query = (locationId != 0) ? "id:" + locationId : locationLat + "," + locationLon;
        
        // Llamada API
        weatherApiService.getForecast(Constants.WEATHER_API_KEY, query, Constants.FORECAST_DAYS).enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(@NonNull Call<ForecastResponse> call, @NonNull Response<ForecastResponse> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    ForecastResponse forecastResponse = response.body();
                    if (forecastResponse.getForecast() != null &&
                        forecastResponse.getForecast().getForecastday() != null) {

                        forecasts.clear();
                        forecasts.addAll(forecastResponse.getForecast().getForecastday());

                        if (forecastResponse.getLocation() != null) {
                            String fullLocationName = forecastResponse.getLocation().getName() +
                                ", " + forecastResponse.getLocation().getCountry();
                            adapter.setLocationName(fullLocationName);
                        }

                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al cargar pronósticos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForecastResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            
            textViewSensorInfo.setText("Sensor de Acelerómetro: Activo");

            double acceleration = Math.sqrt(x*x + y*y + z*z);
            if (acceleration > Constants.SHAKE_THRESHOLD) {
                showShakeConfirmationDialog();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void showShakeConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Agitación Detectada");
        builder.setMessage("Se detectó una agitación del dispositivo. ¿Desea eliminar los últimos pronósticos obtenidos?");

        builder.setPositiveButton("SÍ, ELIMINAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearForecasts();
                Toast.makeText(getContext(), "Pronósticos eliminados", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void clearForecasts() {
        forecasts.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onForecastClick(ForecastDay forecast) {
        Toast.makeText(getContext(), "Pronóstico para: " + forecast.getDate(), Toast.LENGTH_SHORT).show();
    }
}
