package com.example.iot_lab4_20210740.apis;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import java.util.List;

import com.example.iot_lab4_20210740.models.LocationResponse;
import com.example.iot_lab4_20210740.models.ForecastResponse;
import com.example.iot_lab4_20210740.models.FutureResponse;
import com.example.iot_lab4_20210740.models.HistoryResponse;

public interface WeatherApiService {

    @GET("v1/search.json")
    Call<List<LocationResponse>> searchLocations(
        @Query("key") String apiKey,
        @Query("q") String query
    );

    @GET("v1/forecast.json")
    Call<ForecastResponse> getForecast(
        @Query("key") String apiKey,
        @Query("q") String query,
        @Query("days") int days
    );

    @GET("v1/future.json")
    Call<FutureResponse> getFutureForecast(
        @Query("key") String apiKey,
        @Query("q") String query,
        @Query("dt") String date
    );

    @GET("v1/history.json")
    Call<HistoryResponse> getHistoryForecast(
        @Query("key") String apiKey,
        @Query("q") String query,
        @Query("dt") String date
    );
}
