package com.example.iot_lab4_20210740;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

public class AppActivity extends AppCompatActivity {

    private NavController navController;
    private Button buttonLocations, buttonPronosticos, buttonFuturo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        setupNavigation();
        setupNavigationButtons();
    }

    private void setupNavigation() {
        //Navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        } else {
            // Metodo alternativo
            navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        }
    }

    private void setupNavigationButtons() {
        buttonLocations = findViewById(R.id.buttonLocations);
        buttonPronosticos = findViewById(R.id.buttonPronosticos);
        buttonFuturo = findViewById(R.id.buttonFuturo);

        buttonLocations.setOnClickListener(v -> {
            navController.navigate(R.id.locationFragment);
        });

        buttonPronosticos.setOnClickListener(v -> {
            navController.navigate(R.id.forecasterFragment);
        });

        buttonFuturo.setOnClickListener(v -> {
            navController.navigate(R.id.dateFragment);
        });
    }

    public NavController getNavController() {
        return navController;
    }
}
