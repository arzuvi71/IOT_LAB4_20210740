package com.example.iot_lab4_20210740.models;

import com.google.gson.annotations.SerializedName;

public class HourlyForecast {
    @SerializedName("time_epoch")
    private long timeEpoch;
    
    @SerializedName("time")
    private String time;
    
    @SerializedName("temp_c")
    private double tempC;
    
    @SerializedName("temp_f")
    private double tempF;
    
    @SerializedName("is_day")
    private int isDay;
    
    @SerializedName("condition")
    private Condition condition;
    
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
    
    @SerializedName("windchill_c")
    private double windchillC;
    
    @SerializedName("windchill_f")
    private double windchillF;
    
    @SerializedName("heatindex_c")
    private double heatindexC;
    
    @SerializedName("heatindex_f")
    private double heatindexF;
    
    @SerializedName("dewpoint_c")
    private double dewpointC;
    
    @SerializedName("dewpoint_f")
    private double dewpointF;
    
    @SerializedName("will_it_rain")
    private int willItRain;
    
    @SerializedName("chance_of_rain")
    private int chanceOfRain;
    
    @SerializedName("will_it_snow")
    private int willItSnow;
    
    @SerializedName("chance_of_snow")
    private int chanceOfSnow;
    
    @SerializedName("vis_km")
    private double visKm;
    
    @SerializedName("vis_miles")
    private double visMiles;
    
    @SerializedName("gust_mph")
    private double gustMph;
    
    @SerializedName("gust_kph")
    private double gustKph;
    
    @SerializedName("uv")
    private double uv;

    public static class Condition {
        @SerializedName("text")
        private String text;
        
        @SerializedName("icon")
        private String icon;
        
        @SerializedName("code")
        private int code;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }


    public HourlyForecast() {}

    // Getters mass usados)
    public long getTimeEpoch() {
        return timeEpoch;
    }

    public String getTime() {
        return time;
    }

    public double getTempC() {
        return tempC;
    }

    public double getTempF() {
        return tempF;
    }

    public int getIsDay() {
        return isDay;
    }

    public Condition getCondition() {
        return condition;
    }

    public double getWindKph() {
        return windKph;
    }

    public String getWindDir() {
        return windDir;
    }

    public double getPrecipMm() {
        return precipMm;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getFeelslikeC() {
        return feelslikeC;
    }

    public int getChanceOfRain() {
        return chanceOfRain;
    }

    public double getUv() {
        return uv;
    }

    // Setters principales
    public void setTimeEpoch(long timeEpoch) {
        this.timeEpoch = timeEpoch;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTempC(double tempC) {
        this.tempC = tempC;
    }

    public void setTempF(double tempF) {
        this.tempF = tempF;
    }

    public void setIsDay(int isDay) {
        this.isDay = isDay;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setWindKph(double windKph) {
        this.windKph = windKph;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public void setPrecipMm(double precipMm) {
        this.precipMm = precipMm;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setFeelslikeC(double feelslikeC) {
        this.feelslikeC = feelslikeC;
    }

    public void setChanceOfRain(int chanceOfRain) {
        this.chanceOfRain = chanceOfRain;
    }

    public void setUv(double uv) {
        this.uv = uv;
    }
}
