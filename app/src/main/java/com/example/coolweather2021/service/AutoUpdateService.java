package com.example.coolweather2021.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;

import com.bumptech.glide.Glide;
import com.example.coolweather2021.WeatherActivity;
import com.example.coolweather2021.gson.Weather;
import com.example.coolweather2021.util.HttpUtil;
import com.example.coolweather2021.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    static final String key = "&key=98c2e401cf4b46908da304061da6bc16";
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8*60*60*1000;
        long triggerAtTime = SystemClock.elapsedRealtime() +anHour;
        Intent intent1 = new Intent(this,AutoUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,0,intent1,0);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 服务更新天气数据
     */
    private void updateWeather(){
        SharedPreferences nowWeather = getSharedPreferences("weatherNow", MODE_PRIVATE);
        SharedPreferences aqiWeather = getSharedPreferences("weatherAQI", MODE_PRIVATE);
        SharedPreferences dailyWeather = getSharedPreferences("weatherDaily", MODE_PRIVATE);
        SharedPreferences suggestionWeather = getSharedPreferences("weatherSuggestion", MODE_PRIVATE);

        String weatherNowString = nowWeather.getString("Now", null);
        String weatherAQIString = aqiWeather.getString("AQI", null);
        String weatherDailyString = dailyWeather.getString("Daily", null);
        String weatherSuggestionString = suggestionWeather.getString("Suggestion", null);
        String weatherName = nowWeather.getString("Name",null);
        String weatherId = nowWeather.getString("ID",null);
        if (weatherNowString != null){
            String weatherNowUrl = "https://devapi.qweather.com/v7/weather/now?location="+weatherId+
                    key;
            HttpUtil.sendOkHttpRequest(weatherNowUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Weather weather = new Weather();
                    weather.now = Utility.handlerWeatherNowResponse(responseText);
                    if (weather.now != null && "200".equals(weather.now.status)){
                        SharedPreferences.Editor editor = getSharedPreferences("weatherNow",
                                MODE_PRIVATE).edit();
                        editor.putString("Now",responseText);
                        editor.putString("Name",weatherName);//借这里储存城市名
                        editor.putString("ID",weatherId);
                        editor.apply();
                    }

                }
            });
        }

        if (weatherAQIString != null){
            String weatherNowUrl = "https://devapi.qweather.com/v7/air/now?location="+weatherId+
                    "&key=98c2e401cf4b46908da304061da6bc16";
            HttpUtil.sendOkHttpRequest(weatherNowUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Weather weather = new Weather();
                    weather.aqi = Utility.handlerWeatherAQIResponse(responseText);
                    if (weather.aqi != null && "200".equals(weather.aqi.status)){
                        SharedPreferences.Editor editor = getSharedPreferences("weatherAQI",
                                MODE_PRIVATE).edit();
                        editor.putString("AQI",responseText);
                        editor.apply();
                    }

                }
            });
        }

        if (weatherDailyString != null){
            String weatherNowUrl = "https://devapi.qweather.com/v7/weather/7d?location="+weatherId+
                    key;
            HttpUtil.sendOkHttpRequest(weatherNowUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Weather weather = new Weather();
                    weather.daily = Utility.handlerWeatherDailyResponse(responseText);
                    if (weather.daily != null && "200".equals(weather.daily.status)){
                        SharedPreferences.Editor editor = getSharedPreferences("weatherDaily",
                                MODE_PRIVATE).edit();
                        editor.putString("Daily",responseText);
                        editor.apply();
                    }

                }
            });
        }

        if (weatherSuggestionString != null){
            String weatherNowUrl = "https://devapi.qweather.com/v7/indices/1d?type=3,9,13&location="+weatherId+
                    "&key=98c2e401cf4b46908da304061da6bc16";
            HttpUtil.sendOkHttpRequest(weatherNowUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Weather weather = new Weather();
                    weather.suggestion = Utility.handlerWeatherSuggestionResponse(responseText);
                    if (weather.suggestion != null && "200".equals(weather.suggestion.status)){
                        SharedPreferences.Editor editor = getSharedPreferences("weatherSuggestion",
                                MODE_PRIVATE).edit();
                        editor.putString("Suggestion",responseText);
                        editor.apply();
                    }

                }
            });
        }

    }

    /**
     * 服务更新背景图数据
     */
    private void updateBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = getSharedPreferences("BingPic",MODE_PRIVATE).edit();
                editor.putString("bingPic",bingPic);
                editor.apply();
            }
        });
    }
}