package com.example.iot_lab4_20210740.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.iot_lab4_20210740.R;
import com.example.iot_lab4_20210740.models.HourlyForecast;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder> {

    private List<HourlyForecast> hourlyForecasts;
    private OnHourlyClickListener listener;
    private String locationName;
    private int locationId;
    
    public interface OnHourlyClickListener {
        void onHourlyClick(HourlyForecast hourlyForecast);
    }
    
    public HourlyAdapter(List<HourlyForecast> hourlyForecasts, OnHourlyClickListener listener) {
        this.hourlyForecasts = hourlyForecasts;
        this.listener = listener;
        this.locationName = "";
        this.locationId = 0;
    }

    public void setLocationInfo(String locationName, int locationId) {
        this.locationName = locationName;
        this.locationId = locationId;
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hourly, parent, false);
        return new HourlyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {
        HourlyForecast hourlyForecast = hourlyForecasts.get(position);
        holder.bind(hourlyForecast);
    }

    @Override
    public int getItemCount() {
        return hourlyForecasts != null ? hourlyForecasts.size() : 0;
    }
    
    public void updateHourlyForecasts(List<HourlyForecast> newHourlyForecasts) {
        this.hourlyForecasts = newHourlyForecasts;
        notifyDataSetChanged();
    }

    public class HourlyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewLocationName;
        private TextView textViewLocationId;
        private TextView textViewTime;
        private TextView textViewTemp;
        private TextView textViewCondition;
        private TextView textViewWind;
        private TextView textViewHumidity;
        private TextView textViewRainChance;
        private TextView textViewUV;

        public HourlyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLocationName = itemView.findViewById(R.id.textViewLocationName);
            textViewLocationId = itemView.findViewById(R.id.textViewLocationId);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewTemp = itemView.findViewById(R.id.textViewTemp);
            textViewCondition = itemView.findViewById(R.id.textViewCondition);
            textViewWind = itemView.findViewById(R.id.textViewWind);
            textViewHumidity = itemView.findViewById(R.id.textViewHumidity);
            textViewRainChance = itemView.findViewById(R.id.textViewRainChance);
            textViewUV = itemView.findViewById(R.id.textViewUV);
            
            // Configuración de clicks
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    HourlyForecast hourlyForecast = hourlyForecasts.get(position);
                    listener.onHourlyClick(hourlyForecast);
                }
            });
        }

        public void bind(HourlyForecast hourlyForecast) {
            if (hourlyForecast != null) {
                // Mostrar información de ubicación
                textViewLocationName.setText(locationName.isEmpty() ? "Ubicación" : locationName);
                textViewLocationId.setText("ID: " + locationId);

                // Extraer de la hora
                String time = hourlyForecast.getTime();
                if (time != null && time.length() >= 16) {
                    textViewTime.setText(time.substring(11, 16)); // Extrae "14:00"
                } else {
                    textViewTime.setText("--:--");
                }

                textViewTemp.setText(String.format("%.1f°C", hourlyForecast.getTempC()));

                if (hourlyForecast.getCondition() != null) {
                    textViewCondition.setText(hourlyForecast.getCondition().getText());
                }

                textViewWind.setText(String.format("Viento: %.1f km/h %s",
                    hourlyForecast.getWindKph(), hourlyForecast.getWindDir()));
                textViewHumidity.setText(String.format("Humedad: %d%%", hourlyForecast.getHumidity()));
                textViewRainChance.setText(String.format("Lluvia: %d%%", hourlyForecast.getChanceOfRain()));
                textViewUV.setText(String.format("UV: %.1f", hourlyForecast.getUv()));
            }
        }
    }
}
