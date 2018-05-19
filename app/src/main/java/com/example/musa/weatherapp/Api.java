package com.example.musa.weatherapp;


import com.example.musa.weatherapp.Current_Weather.CurrentWeatherData;
import com.example.musa.weatherapp.Forecast_Weather.ForecastWeatherData;
import com.example.musa.weatherapp.GetLocation.GeoCode;
import com.example.musa.weatherapp.GetLocation.Geometry;
import com.example.musa.weatherapp.GetLocation.Location_;
import com.example.musa.weatherapp.Hourly_Weather.HourlyWeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Musa on 5/8/2018.
 */

public interface Api {
    String BASE_URL ="http://api.openweathermap.org/data/2.5/";

    @GET("")
    Call<CurrentWeatherData> getAllWeather(@Url String url);

    @GET("")
    Call<ForecastWeatherData> getAllForecast(@Url String url);

   @GET("")
    Call<GeoCode> getAllLatLon(@Url String url);

    @GET("")
    Call<HourlyWeatherData> getHourlyData(@Url String url);

}
