package com.example.iot_lab4_20210740.models;

import com.google.gson.annotations.SerializedName;

public class FutureResponse {
    @SerializedName("location")
    private LocationResponse location;
    
    @SerializedName("forecast")
    private Forecast forecast;
    
    public LocationResponse getLocation() {
        return location;
    }
    
    public void setLocation(LocationResponse location) {
        this.location = location;
    }
    
    public Forecast getForecast() {
        return forecast;
    }
    
    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }
    
    public static class Forecast {
        @SerializedName("forecastday")
        private ForecastDay[] forecastday;
        
        public ForecastDay[] getForecastday() {
            return forecastday;
        }
        
        public void setForecastday(ForecastDay[] forecastday) {
            this.forecastday = forecastday;
        }
    }
}
