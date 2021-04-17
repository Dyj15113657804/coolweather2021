package com.example.coolweather2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences nowWeather = getSharedPreferences("weatherNow", MODE_PRIVATE);
        SharedPreferences dailyWeather = getSharedPreferences("weatherDaily", MODE_PRIVATE);
        SharedPreferences aqiWeather = getSharedPreferences("weatherAQI", MODE_PRIVATE);
        SharedPreferences suggestionWeather = getSharedPreferences("weatherSuggestion", MODE_PRIVATE);
        String weatherNowString = nowWeather.getString("Now", null);
        String weatherDailyString = dailyWeather.getString("Daily", null);
        String weatherAQIString = aqiWeather.getString("AQI", null);
        String weatherSuggestionString = suggestionWeather.getString("Suggestion", null);

        if (weatherNowString != null && weatherAQIString != null && weatherDailyString != null
                && weatherSuggestionString != null){
            Intent intent = new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }
}