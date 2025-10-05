package com.example.iot_lab4_20210740.models;

import com.google.gson.annotations.SerializedName;

public class ForecastResponse {
    @SerializedName("location")
    private Location location;
    
    @SerializedName("current")
    private Current current;
    
    @SerializedName("forecast")
    private Forecast forecast;

    public static class Location {
        @SerializedName("name")
        private String name;
        
        @SerializedName("region")
        private String region;
        
        @SerializedName("country")
        private String country;
        
        @SerializedName("lat")
        private double lat;
        
        @SerializedName("lon")
        private double lon;
        
        @SerializedName("tz_id")
        private String tzId;
        
        @SerializedName("localtime_epoch")
        private long localtimeEpoch;
        
        @SerializedName("localtime")
        private String localtime;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public String getLocaltime() {
            return localtime;
        }

        public void setLocaltime(String localtime) {
            this.localtime = localtime;
        }
    }

    public static class Current {
        @SerializedName("last_updated_epoch")
        private long lastUpdatedEpoch;
        
        @SerializedName("last_updated")
        private String lastUpdated;
        
        @SerializedName("temp_c")
        private double tempC;
        
        @SerializedName("temp_f")
        private double tempF;
        
        @SerializedName("is_day")
        private int isDay;
        
        @SerializedName("condition")
        private HourlyForecast.Condition condition;
        
        @SerializedName("wind_mph")
        private double windMph;
        
        @SerializedName("wind_kph")
        private double windKph;
        
        @SerializedName("wind_degree")
        private int windDegree;
        
        @SerializedName("wind_dir")
        private String windDir;
        
        @SerializedName("pressure_mb")
        private double pressureMb;
        
        @SerializedName("pressure_in")
        private double pressureIn;
        
        @SerializedName("precip_mm")
        private double precipMm;
        
        @SerializedName("precip_in")
        private double precipIn;
        
        @SerializedName("humidity")
        private int humidity;
        
        @SerializedName("cloud")
        private int cloud;
        
        @SerializedName("feelslike_c")
        private double feelslikeC;
        
        @SerializedName("feelslike_f")
        private double feelslikeF;
        
        @SerializedName("vis_km")
        private double visKm;
        
        @SerializedName("vis_miles")
        private double visMiles;
        
        @SerializedName("uv")
        private double uv;
        
        @SerializedName("gust_mph")
        private double gustMph;
        
        @SerializedName("gust_kph")
        private double gustKph;

        public double getTempC() {
            return tempC;
        }

        public void setTempC(double tempC) {
            this.tempC = tempC;
        }

        public HourlyForecast.Condition getCondition() {
            return condition;
        }

        public void setCondition(HourlyForecast.Condition condition) {
            this.condition = condition;
        }

        public double getWindKph() {
            return windKph;
        }

        public void setWindKph(double windKph) {
            this.windKph = windKph;
        }

        public String getWindDir() {
            return windDir;
        }

        public void setWindDir(String windDir) {
            this.windDir = windDir;
        }

        public double getPrecipMm() {
            return precipMm;
        }

        public void setPrecipMm(double precipMm) {
            this.precipMm = precipMm;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public double getFeelslikeC() {
            return feelslikeC;
        }

        public void setFeelslikeC(double feelslikeC) {
            this.feelslikeC = feelslikeC;
        }

        public double getUv() {
            return uv;
        }

        public void setUv(double uv) {
            this.uv = uv;
        }
    }


    public static class Forecast {
        @SerializedName("forecastday")
        private java.util.List<ForecastDay> forecastday;

        public java.util.List<ForecastDay> getForecastday() {
            return forecastday;
        }

        public void setForecastday(java.util.List<ForecastDay> forecastday) {
            this.forecastday = forecastday;
        }
    }


    public ForecastResponse() {}

    // Getters y Setters
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }
}
