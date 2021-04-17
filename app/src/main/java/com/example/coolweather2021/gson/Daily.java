package com.example.coolweather2021.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Daily {

    @SerializedName("code")
    public String status;

    @SerializedName("daily")
    public List<Forecast> forecastList;

    public class Forecast{

        @SerializedName("fxDate")
        public  String date;

        @SerializedName("tempMax")
        public String TemperatureMax;

        @SerializedName("tempMin")
        public String TemperatureMin;

        @SerializedName("textDay")
        public String information;
    }

}
