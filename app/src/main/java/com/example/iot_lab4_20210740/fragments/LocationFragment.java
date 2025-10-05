package com.example.iot_lab4_20210740.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import java.util.List;

import com.example.iot_lab4_20210740.R;
import com.example.iot_lab4_20210740.Constants;
import com.example.iot_lab4_20210740.adapters.LocationAdapter;
import com.example.iot_lab4_20210740.apis.WeatherApiService;
import com.example.iot_lab4_20210740.models.LocationResponse;

public class LocationFragment extends Fragment implements LocationAdapter.OnLocationClickListener {
    

    
    private TextInputEditText editTextLocation;
    private Button buttonSearch;
    private RecyclerView recyclerViewLocations;
    private ProgressBar progressBar;
    private LocationAdapter adapter;
    private WeatherApiService weatherApiService;
    private List<LocationResponse> locations;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupRecyclerView();
        setupRetrofit();
        setupClickListeners();
    }

    private void initViews(View view) {
        editTextLocation = view.findViewById(R.id.editTextLocation);
        buttonSearch = view.findViewById(R.id.buttonSearch);
        recyclerViewLocations = view.findViewById(R.id.recyclerViewLocations);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        locations = new ArrayList<>();
        adapter = new LocationAdapter(locations, this);

        recyclerViewLocations.setAdapter(adapter);
        recyclerViewLocations.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupRetrofit() {
        // Retrofit
        weatherApiService = new Retrofit.Builder()
                .baseUrl(Constants.WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService.class);
    }

    private void setupClickListeners() {
        buttonSearch.setOnClickListener(v -> {
            String query = editTextLocation.getText().toString().trim();
            if (!query.isEmpty()) {
                searchLocations(query);
            } else {
                Toast.makeText(getContext(), "Ingrese un nombre de ciudad", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchLocations(String query) {
        progressBar.setVisibility(View.VISIBLE);
        
        // Llamada a API
        weatherApiService.searchLocations(Constants.WEATHER_API_KEY, query).enqueue(new Callback<List<LocationResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<LocationResponse>> call, @NonNull Response<List<LocationResponse>> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    locations.clear();
                    locations.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    
                    if (locations.isEmpty()) {
                        Toast.makeText(getContext(), "No se encontraron ubicaciones", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error en la búsqueda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<LocationResponse>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLocationClick(LocationResponse location) {
        NavController navController = Navigation.findNavController(requireView());

        Bundle args = new Bundle();
        args.putString("locationName", location.getName() + ", " + location.getCountry());
        args.putInt("locationId", location.getId());
        args.putFloat("locationLat", (float) location.getLat());
        args.putFloat("locationLon", (float) location.getLon());

        navController.navigate(R.id.action_locationFragment_to_forecasterFragment, args);
    }
}
