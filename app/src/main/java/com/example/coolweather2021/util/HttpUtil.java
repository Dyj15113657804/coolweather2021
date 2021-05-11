package com.example.coolweather2021.util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        /*
         OkHttpClient.Builder builder = new OkHttpClient.Builder();
         builder.connectTimeout(20, TimeUnit.SECONDS);
         builder.callTimeout(20,TimeUnit.SECONDS);
         client  = builder.build();
         */
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
