package com.example.coolweather2021.gson;

import com.google.gson.annotations.SerializedName;

public class Now {

    @SerializedName("code")
    public String status;

    @SerializedName("obsTime")
    public String checkTime;

    @SerializedName("temp")
    public String temperature;

    @SerializedName("icon")
    public String iconImage;

    @SerializedName("text")
    public String information;




}
