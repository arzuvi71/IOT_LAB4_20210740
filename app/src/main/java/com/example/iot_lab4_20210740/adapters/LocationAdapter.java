package com.example.iot_lab4_20210740.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.iot_lab4_20210740.R;
import com.example.iot_lab4_20210740.models.LocationResponse;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    
    private List<LocationResponse> locations;
    private OnLocationClickListener listener;
    
    public interface OnLocationClickListener {
        void onLocationClick(LocationResponse location);
    }
    
    public LocationAdapter(List<LocationResponse> locations, OnLocationClickListener listener) {
        this.locations = locations;
        this.listener = listener;
    }

    @Override
    @NonNull
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        LocationResponse location = locations.get(position);
        holder.bind(location);
    }

    @Override
    public int getItemCount() {
        return locations != null ? locations.size() : 0;
    }
    
    public void updateLocations(List<LocationResponse> newLocations) {
        this.locations = newLocations;
        notifyDataSetChanged();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewLocationName;
        private TextView textViewLocationDetails;
        private TextView textViewLocationCoords;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLocationName = itemView.findViewById(R.id.textViewLocationName);
            textViewLocationDetails = itemView.findViewById(R.id.textViewLocationDetails);
            textViewLocationCoords = itemView.findViewById(R.id.textViewLocationCoords);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    LocationResponse location = locations.get(position);
                    listener.onLocationClick(location);
                }
            });
        }

        public void bind(LocationResponse location) {
            textViewLocationName.setText(location.getName());
            textViewLocationDetails.setText(location.getRegion() + ", " + location.getCountry());
            textViewLocationCoords.setText(String.format("ID: %d | Lat: %.2f, Lon: %.2f",
                location.getId(), location.getLat(), location.getLon()));
        }
    }
}
