package com.example.musa.weatherapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.musa.weatherapp.Hourly_Weather.H_List;
import com.example.musa.weatherapp.Hourly_Weather.Weather;
import com.example.musa.weatherapp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Musa on 5/13/2018.
 */

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourViewHolder> {

    ImageView imgHourly;
    String imageurl;
    boolean convert;

    Context context;
    List<H_List> hourlyData;
    List<Weather> weathers;

    public HourlyAdapter(Context context, List<H_List> hourlyData,boolean convert) {
        this.context = context;
        this.hourlyData = hourlyData;
        this.convert = convert;
     }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.hourly_row,parent,false);
        return new HourlyAdapter.HourViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {

        /*SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM | HH:MM");

        Date date = new Date(hourlyData.get(position).getDt()*1000);
        String currentDate = timeFormat.format(date);*/

        holder.tvTime.setText(hourlyData.get(position).getDtTxt());

       if (convert)
       {
           holder.tvTemp.setText("Temp : "+String.valueOf(hourlyData.get(position).getMain().getTemp())+"° F");
           holder.tvCloud.setText("Cloud : "+String.valueOf(hourlyData.get(position).getMain().getHumidity())+"° F");
       }
       else {
           holder.tvTemp.setText("Temp : "+String.valueOf(hourlyData.get(position).getMain().getTemp())+"° C");
           holder.tvCloud.setText("Cloud : "+String.valueOf(hourlyData.get(position).getMain().getHumidity())+"° C");
       }


        weathers = hourlyData.get(position).getWeather();
        for(Weather w: weathers)
        {
            imageurl= w.getIcon();
        }
        String imgURL = String.format("https://openweathermap.org/img/w/%s.png",imageurl);
        Picasso.with(context).load(imgURL).resize(50,50).into(imgHourly);
    }

    @Override
    public int getItemCount() {
        return hourlyData.size();
    }

    public class HourViewHolder extends RecyclerView.ViewHolder{

        TextView tvTemp,tvCloud,tvTime;

        public HourViewHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvTemp = (TextView) itemView.findViewById(R.id.tvTemp);
            tvCloud = (TextView) itemView.findViewById(R.id.tvCloud);
            imgHourly = (ImageView) itemView.findViewById(R.id.imgHourly);
        }
    }
}
