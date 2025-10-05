package com.example.iot_lab4_20210740.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.iot_lab4_20210740.R;
import com.example.iot_lab4_20210740.models.ForecastDay;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private List<ForecastDay> forecasts;
    private OnForecastClickListener listener;
    private String locationName;
    
    public interface OnForecastClickListener {
        void onForecastClick(ForecastDay forecast);
    }
    
    public ForecastAdapter(List<ForecastDay> forecasts, OnForecastClickListener listener) {
        this.forecasts = forecasts;
        this.listener = listener;
        this.locationName = "";
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        ForecastDay forecast = forecasts.get(position);
        holder.bind(forecast);
    }

    @Override
    public int getItemCount() {
        return forecasts != null ? forecasts.size() : 0;
    }
    
    public void updateForecasts(List<ForecastDay> newForecasts) {
        this.forecasts = newForecasts;
        notifyDataSetChanged();
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewLocationName;
        private TextView textViewWeatherIcon;
        private TextView textViewDate;
        private TextView textViewMaxTemp;
        private TextView textViewMinTemp;
        private TextView textViewCondition;
        private TextView textViewHumidity;
        private TextView textViewRainChance;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLocationName = itemView.findViewById(R.id.textViewLocationName);
            textViewWeatherIcon = itemView.findViewById(R.id.textViewWeatherIcon);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewMaxTemp = itemView.findViewById(R.id.textViewMaxTemp);
            textViewMinTemp = itemView.findViewById(R.id.textViewMinTemp);
            textViewCondition = itemView.findViewById(R.id.textViewCondition);
            textViewHumidity = itemView.findViewById(R.id.textViewHumidity);
            textViewRainChance = itemView.findViewById(R.id.textViewRainChance);
            
            // Configuración de clicks
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    ForecastDay forecast = forecasts.get(position);
                    listener.onForecastClick(forecast);
                }
            });
        }

        public void bind(ForecastDay forecast) {
            if (forecast != null && forecast.getDay() != null) {
                // Mostrar nombre de ubicación
                textViewLocationName.setText(locationName.isEmpty() ? "Ubicación" : locationName);

                // Mostrar icono según condición del clima
                if (forecast.getDay().getCondition() != null) {
                    String condition = forecast.getDay().getCondition().getText().toLowerCase();
                    String weatherIcon = getWeatherIcon(condition);
                    textViewWeatherIcon.setText(weatherIcon);
                    textViewCondition.setText(forecast.getDay().getCondition().getText());
                }

                textViewDate.setText(forecast.getDate());
                textViewMaxTemp.setText(String.format("Máx: %.1f°C", forecast.getDay().getMaxtempC()));
                textViewMinTemp.setText(String.format("Mín: %.1f°C", forecast.getDay().getMintempC()));
                textViewHumidity.setText(String.format("Humedad: %.0f%%", forecast.getDay().getAvghumidity()));
                textViewRainChance.setText(String.format("Probabilidad de lluvia: %d%%",
                    forecast.getDay().getDailyChanceOfRain()));
            }
        }

        private String getWeatherIcon(String condition) {
            if (condition.contains("rain") || condition.contains("lluvia") || condition.contains("shower")) {
                return "🌧️";
            } else if (condition.contains("cloud") || condition.contains("nublado") || condition.contains("overcast")) {
                return "☁️";
            } else if (condition.contains("sun") || condition.contains("clear") || condition.contains("despejado") || condition.contains("sunny")) {
                return "☀️";
            } else if (condition.contains("snow") || condition.contains("nieve")) {
                return "❄️";
            } else if (condition.contains("thunder") || condition.contains("tormenta")) {
                return "⛈️";
            } else if (condition.contains("fog") || condition.contains("niebla") || condition.contains("mist")) {
                return "🌫️";
            } else if (condition.contains("wind") || condition.contains("viento")) {
                return "💨";
            } else {
                return "🌤️"; // Opción por defecto
            }
        }
    }
}
