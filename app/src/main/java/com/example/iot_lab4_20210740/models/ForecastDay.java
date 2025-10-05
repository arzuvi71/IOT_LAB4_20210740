package com.example.iot_lab4_20210740.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ForecastDay {
    @SerializedName("date")
    private String date;
    
    @SerializedName("date_epoch")
    private long dateEpoch;
    
    @SerializedName("day")
    private Day day;
    
    @SerializedName("astro")
    private Astro astro;
    
    @SerializedName("hour")
    private List<HourlyForecast> hour;

    public static class Day {
        @SerializedName("maxtemp_c")
        private double maxtempC;
        
        @SerializedName("maxtemp_f")
        private double maxtempF;
        
        @SerializedName("mintemp_c")
        private double mintempC;
        
        @SerializedName("mintemp_f")
        private double mintempF;
        
        @SerializedName("avgtemp_c")
        private double avgtempC;
        
        @SerializedName("avgtemp_f")
        private double avgtempF;
        
        @SerializedName("maxwind_mph")
        private double maxwindMph;
        
        @SerializedName("maxwind_kph")
        private double maxwindKph;
        
        @SerializedName("totalprecip_mm")
        private double totalprecipMm;
        
        @SerializedName("totalprecip_in")
        private double totalprecipIn;
        
        @SerializedName("totalsnow_cm")
        private double totalsnowCm;
        
        @SerializedName("avgvis_km")
        private double avgvisKm;
        
        @SerializedName("avgvis_miles")
        private double avgvisMiles;
        
        @SerializedName("avghumidity")
        private double avghumidity;
        
        @SerializedName("daily_will_it_rain")
        private int dailyWillItRain;
        
        @SerializedName("daily_chance_of_rain")
        private int dailyChanceOfRain;
        
        @SerializedName("daily_will_it_snow")
        private int dailyWillItSnow;
        
        @SerializedName("daily_chance_of_snow")
        private int dailyChanceOfSnow;
        
        @SerializedName("condition")
        private HourlyForecast.Condition condition;
        
        @SerializedName("uv")
        private double uv;

        // Getters y Setters para Day
        public double getMaxtempC() {
            return maxtempC;
        }

        public void setMaxtempC(double maxtempC) {
            this.maxtempC = maxtempC;
        }

        public double getMintempC() {
            return mintempC;
        }

        public void setMintempC(double mintempC) {
            this.mintempC = mintempC;
        }

        public double getAvgtempC() {
            return avgtempC;
        }

        public void setAvgtempC(double avgtempC) {
            this.avgtempC = avgtempC;
        }

        public double getMaxwindKph() {
            return maxwindKph;
        }

        public void setMaxwindKph(double maxwindKph) {
            this.maxwindKph = maxwindKph;
        }

        public double getTotalprecipMm() {
            return totalprecipMm;
        }

        public void setTotalprecipMm(double totalprecipMm) {
            this.totalprecipMm = totalprecipMm;
        }

        public double getAvghumidity() {
            return avghumidity;
        }

        public void setAvghumidity(double avghumidity) {
            this.avghumidity = avghumidity;
        }

        public int getDailyChanceOfRain() {
            return dailyChanceOfRain;
        }

        public void setDailyChanceOfRain(int dailyChanceOfRain) {
            this.dailyChanceOfRain = dailyChanceOfRain;
        }

        public HourlyForecast.Condition getCondition() {
            return condition;
        }

        public void setCondition(HourlyForecast.Condition condition) {
            this.condition = condition;
        }

        public double getUv() {
            return uv;
        }

        public void setUv(double uv) {
            this.uv = uv;
        }
    }

    public static class Astro {
        @SerializedName("sunrise")
        private String sunrise;
        
        @SerializedName("sunset")
        private String sunset;
        
        @SerializedName("moonrise")
        private String moonrise;
        
        @SerializedName("moonset")
        private String moonset;
        
        @SerializedName("moon_phase")
        private String moonPhase;
        
        @SerializedName("moon_illumination")
        private String moonIllumination;

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public String getMoonPhase() {
            return moonPhase;
        }

        public void setMoonPhase(String moonPhase) {
            this.moonPhase = moonPhase;
        }
    }

    public ForecastDay() {}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getDateEpoch() {
        return dateEpoch;
    }

    public void setDateEpoch(long dateEpoch) {
        this.dateEpoch = dateEpoch;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Astro getAstro() {
        return astro;
    }

    public void setAstro(Astro astro) {
        this.astro = astro;
    }

    public List<HourlyForecast> getHour() {
        return hour;
    }

    public void setHour(List<HourlyForecast> hour) {
        this.hour = hour;
    }
}
