package com.example.musa.weatherapp;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musa.weatherapp.Adapter.HourlyAdapter;
import com.example.musa.weatherapp.Current_Weather.CurrentWeatherData;
import com.example.musa.weatherapp.Current_Weather.Weather;
import com.example.musa.weatherapp.GetLocation.GeoCode;
import com.example.musa.weatherapp.GetLocation.Result;
import com.example.musa.weatherapp.Hourly_Weather.HourlyWeatherData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class Current_Fragment extends Fragment {

    TextView tvTemp,tvCity,tvWeather,tvSky,tvSunRise,tvDate,tvDay,tvMin,tvMax,tvSunSet,tvHumanity,tvPressure;
    ImageView imageView;
    String imageurl,weatherCondition,skyCondition;

    private FusedLocationProviderClient client;
    private double latitude=0, longitude=0;
    private  double lat,lon;

    String city="";
    boolean convert;
    String url;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    public Current_Fragment() {
        // Required empty public constructor
    }

    public static Current_Fragment newInstance(String city,boolean convert) {
        Current_Fragment result = new Current_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("CITY", city);
        bundle.putBoolean("CONVERT",convert);
        result.setArguments(bundle);
        return result;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.test, container, false);

        city = getArguments().getString("CITY");
        convert = getArguments().getBoolean("CONVERT");

        recyclerView = (RecyclerView) v.findViewById(R.id.hourlyList);

        tvTemp = (TextView) v.findViewById(R.id.temp);
        tvCity= (TextView) v.findViewById(R.id.city);
        tvWeather = (TextView) v.findViewById(R.id.weather);
        //tvSky= (TextView) v.findViewById(R.id.typeofsky);
        tvSunRise= (TextView) v.findViewById(R.id.tvSunrise);
        imageView= (ImageView) v.findViewById(R.id.imageView);
        tvDate = (TextView) v.findViewById(R.id.date);
        //tvDay= (TextView) v.findViewById(R.id.day);
        tvMin = (TextView) v.findViewById(R.id.tvMin);
        tvMax= (TextView) v.findViewById(R.id.tvMax);
        tvSunSet= (TextView) v.findViewById(R.id.tvSunset);
        tvHumanity = (TextView) v.findViewById(R.id.tvHumidity);
        //tvPressure= (TextView) v.findViewById(R.id.tvPressure);

        client = LocationServices.getFusedLocationProviderClient(getActivity());
        getDeviceCurrentLocation();


        return v;
    }

    private boolean checkLocationPermission(){
        if(ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},111);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getDeviceCurrentLocation();
        }
    }

    public void getDeviceCurrentLocation() {

        if(checkLocationPermission()){
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location == null){
                        return;
                    }
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    Log.e("lat : ",latitude+"",null);
                    Log.e("lon : ",longitude+"",null);

                    if (city == ""){
                        callAPI(latitude,longitude);
                        hourlyWeather(latitude,longitude);
                        //Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
                    }else {
                        searchLocation();
                    }


                }
            });
        }
    }

    public void callAPI(double lat,double lon)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Api api = retrofit.create(Api.class);
        //String url ="weather?lat=22.33&lon=91.84&appid=9f51149abc3d8adac7a9103e8da92927";
        //String url = String.format("weather?lat=%f&lon=%f&appid=9f51149abc3d8adac7a9103e8da92927",lat,lon);
        if (convert){
            url = String.format("weather?lat=%f&lon=%f&units=imperial&appid=9f51149abc3d8adac7a9103e8da92927",lat,lon);
        }else {
           url = String.format("weather?lat=%f&lon=%f&units=metric&appid=9f51149abc3d8adac7a9103e8da92927",lat,lon);
        }





        Call<CurrentWeatherData> call = api.getAllWeather(url);

        call.enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(Call<CurrentWeatherData> call, Response<CurrentWeatherData> response) {
                CurrentWeatherData currentWeatherData = response.body();

                if(convert){
                    double C = currentWeatherData.getMain().getTemp();
                    tvTemp.setText(String.format(" %.2f",C)+"° F");

                    tvMin.setText(String.valueOf("Min : "+currentWeatherData.getMain().getTempMin())+"° F");
                    tvMax.setText(String.valueOf("Max : "+currentWeatherData.getMain().getTempMax())+"° C");
                }else {
                    double C = currentWeatherData.getMain().getTemp();
                    tvTemp.setText(String.format(" %.2f",C)+"° C");

                    tvMin.setText(String.valueOf("Min : "+currentWeatherData.getMain().getTempMin())+"° C");
                    tvMax.setText(String.valueOf("Max : "+currentWeatherData.getMain().getTempMax())+"° C");
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("EE,dd MMM");
                SimpleDateFormat dayFormate = new SimpleDateFormat("EEEE");
                Date date = new Date(currentWeatherData.getDt()*1000);

                String currentDate = dateFormat.format(date);
                String currentDay = dayFormate.format(date);
                tvDate.setText(currentDate);
                //tvDay.setText(currentDay);

                tvCity.setText(currentWeatherData.getName());

                List<Weather> weathers = currentWeatherData.getWeather();
                for (Weather weather:weathers){
                    imageurl= weather.getIcon();
                    weatherCondition = weather.getDescription();
                    skyCondition = weather.getMain();
                }
                tvWeather.setText(weatherCondition);
                //tvSky.setText(skyCondition);
                String imgURL = String.format("https://openweathermap.org/img/w/%s.png",imageurl);
                Picasso.with(getActivity()).load(imgURL).resize(150,100).into(imageView);


                SimpleDateFormat timeFormate = new SimpleDateFormat("HH:MM");
                Date sunRise = new Date(currentWeatherData.getSys().getSunrise()*1000);
                Date sunSet = new Date(currentWeatherData.getSys().getSunset()*1000);

                String sunRISE = timeFormate.format(sunRise);
                String sunSET = timeFormate.format(sunSet);

                tvSunRise.setText("Sunrise : "+sunRISE);
                tvSunSet.setText("Sunset : "+sunSET);

                tvHumanity.setText("Cloud : "+String.valueOf(currentWeatherData.getMain().getHumidity())+"%");
                //tvPressure.setText(String.valueOf(currentWeatherData.getMain().getPressure())+"hPa");

            }

            @Override
            public void onFailure(Call<CurrentWeatherData> call, Throwable t) {

            }
        });


    }

    public void searchLocation()
    {
        Retrofit retroCity = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/geocode/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Api apiCity = retroCity.create(Api.class);
        //String url ="json?address=Nurjahan+Road,+Mohammadpur,+Dhaka&key=AIzaSyCy4IOHbNB0he6ASe5XqixIF4Fr0ezM8aI";
        String url = String.format("json?address=%s&key=AIzaSyCy4IOHbNB0he6ASe5XqixIF4Fr0ezM8aI", city);
        Call<GeoCode> call = apiCity.getAllLatLon(url);

        call.enqueue(new Callback<GeoCode>() {
            @Override
            public void onResponse(Call<GeoCode> call, Response<GeoCode> response) {
                GeoCode geoCode = response.body();

                List<Result> results = geoCode.getResults();
                for(Result r:  results){
                    lat = r.getGeometry().getLocation().getLat();
                    lon = r.getGeometry().getLocation().getLng();
                }

                callAPI(lat,lon);
                hourlyWeather(lat,lon);
            }

            @Override
            public void onFailure(Call<GeoCode> call, Throwable t) {

            }
        });
    }

    public void hourlyWeather(double lat,double lon)
    {
        String hourURL;
        Retrofit retroHour = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Api api = retroHour.create(Api.class);

        if (convert){
            hourURL = String.format("forecast?lat=%f&lon=%f&cnt=10&units=imperial&appid=9f51149abc3d8adac7a9103e8da92927",lat,lon);
        }else {
            //hourURL = String.format("weather?lat=%f&lon=%f&units=metric&appid=9f51149abc3d8adac7a9103e8da92927",lat,lon);
            hourURL = String.format("forecast?lat=%f&lon=%f&cnt=10&units=metric&appid=9f51149abc3d8adac7a9103e8da92927",lat,lon);

        }

        Call<HourlyWeatherData> call = api.getHourlyData(hourURL);
        call.enqueue(new Callback<HourlyWeatherData>() {
            @Override
            public void onResponse(Call<HourlyWeatherData> call, Response<HourlyWeatherData> response) {

                HourlyWeatherData hourlyData = response.body();

                layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                //recyclerView.setLayoutManager(layoutManager);
                recyclerView.setLayoutManager(layoutManager);
                HourlyAdapter hourlyAdapter = new HourlyAdapter(getContext(),hourlyData.getList(),convert);

                recyclerView.setAdapter(hourlyAdapter);
            }

            @Override
            public void onFailure(Call<HourlyWeatherData> call, Throwable t) {

            }
        });

    }

}
