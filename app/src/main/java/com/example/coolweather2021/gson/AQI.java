package com.example.coolweather2021.gson;

import com.google.gson.annotations.SerializedName;

public class AQI {
    @SerializedName("code")
    public String status;

    @SerializedName("now")
    public AqiNow aqiNow;

    public class AqiNow{

        public String aqi;

        @SerializedName("pm2p5")
        public String pm25;
    }
}
