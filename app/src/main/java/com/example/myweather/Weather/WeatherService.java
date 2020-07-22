package com.example.myweather.Weather;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.Call;

public interface WeatherService {
    @GET("data/2.5/weather?")
    Call<WeatherResponse> getCurrentWeatherData(@Query("q") String place, @Query("units") String units, @Query("APPID") String AppId);
}
