package com.example.coolweather2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.coolweather2021.gson.AQI;
import com.example.coolweather2021.gson.Daily;
import com.example.coolweather2021.gson.Now;
import com.example.coolweather2021.gson.Suggestion;
import com.example.coolweather2021.gson.Weather;
import com.example.coolweather2021.util.HttpUtil;
import com.example.coolweather2021.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 本类使用了ButterKnife注解
 */
public class WeatherActivity extends AppCompatActivity {

    private Context mContext;
    @BindView(R.id.weather_layout) ScrollView weatherLayout;
    @BindView(R.id.title_city) TextView titleCity;
    @BindView(R.id.title_update_time) TextView titleUpdateTime;
    @BindView(R.id.degree_text) TextView degreeText;
    @BindView(R.id.weather_info_text) TextView weatherInfoText;
    @BindView(R.id.forecast_layout) LinearLayout forecastLayout;
    @BindView(R.id.aqi_text) TextView aqiText;
    @BindView(R.id.pm25_text) TextView pm25Text;
    @BindView(R.id.health_text) TextView healthText;
    @BindView(R.id.face_painting_text) TextView paintingText;
    @BindView(R.id.wear_text) TextView wearText;
    @BindView(R.id.icon_now) ImageView weatherImage;


    static final String key = "&key=98c2e401cf4b46908da304061da6bc16";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);//Use butterKnife

        SharedPreferences nowWeather = getSharedPreferences("weatherNow", MODE_PRIVATE);
        SharedPreferences aqiWeather = getSharedPreferences("weatherAQI", MODE_PRIVATE);
        SharedPreferences dailyWeather = getSharedPreferences("weatherDaily", MODE_PRIVATE);
        SharedPreferences suggestionWeather = getSharedPreferences("weatherSuggestion", MODE_PRIVATE);

        String weatherNowString = nowWeather.getString("Now", null);
        String weatherAQIString = aqiWeather.getString("AQI", null);
        String weatherDailyString = dailyWeather.getString("Daily", null);
        String weatherSuggestionString = suggestionWeather.getString("Suggestion", null);
        String Name = nowWeather.getString("Name",null);


        String weatherId= getIntent().getStringExtra("weather_id");

        //WeatherId weatherData = new WeatherId();
        //weatherData.weatherId = weatherId1;
        //String weatherId = splitData(weatherData);//都没用

        String weatherName = getIntent().getStringExtra("weather_name");

        //showToast(weatherId);
        if (weatherNowString != null && weatherAQIString != null && weatherDailyString != null
                && weatherSuggestionString != null){
            Weather weather = new Weather();
            weather.now = Utility.handlerWeatherNowResponse(weatherNowString);
            weather.aqi = Utility.handlerWeatherAQIResponse(weatherAQIString);
            weather.daily = Utility.handlerWeatherDailyResponse(weatherDailyString);
            weather.suggestion = Utility.handlerWeatherSuggestionResponse(weatherSuggestionString);

            showWeatherInfo(weather,Name);
        }else {
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeatherNow(weatherId,weatherName);
            requestWeatherAQI(weatherId,weatherName);
            requestWeatherDaily(weatherId,weatherName);
            requestWeatherSuggestion(weatherId,weatherName);
            //recreate();以下为刷新Activity机制
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);


        }
    }

    /**
     * 去和风天气服务器获取实时天气信息Now
     * @param weatherId 传入城市ID
     */
    public void requestWeatherNow(final String weatherId,final String weatherName){
        String weatherNowUrl = "https://devapi.qweather.com/v7/weather/now?location="+weatherId+
                key;
        HttpUtil.sendOkHttpRequest(weatherNowUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("获取天气信息失败");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = new Weather();
                weather.now = Utility.handlerWeatherNowResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather.now != null && "200".equals(weather.now.status)){
                            SharedPreferences.Editor editor = getSharedPreferences("weatherNow",
                                    MODE_PRIVATE).edit();
                            editor.putString("Now",responseText);
                            editor.putString("Name",weatherName);//借这里储存城市名
                            editor.apply();

                        }else {
                            showToast("获取天气信息失败");
                        }
                    }
                });
            }
        });

    }

    /**
     * 去和风天气获取多天天气信息Daily
     * @param weatherId 传入城市ID
     */
    public void requestWeatherDaily(final String weatherId,final String weatherName){
        String weatherNowUrl = "https://devapi.qweather.com/v7/weather/7d?location="+weatherId+
                key;
        HttpUtil.sendOkHttpRequest(weatherNowUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("获取天气信息失败");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = new Weather();
                weather.daily = Utility.handlerWeatherDailyResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather.daily != null && "200".equals(weather.daily.status)){
                            SharedPreferences.Editor editor = getSharedPreferences("weatherDaily",
                                    MODE_PRIVATE).edit();
                            editor.putString("Daily",responseText);
                            editor.apply();

                        }else {
                            showToast("获取天气信息失败");
                        }
                    }
                });
            }
        });
    }

    /**
     *去和风天气获取空气质量信息AQI
     * @param weatherId 传入城市ID
     */
    public void requestWeatherAQI(final String weatherId,final String weatherName){
        String weatherNowUrl = "https://devapi.qweather.com/v7/air/now?location="+weatherId+
                "&key=98c2e401cf4b46908da304061da6bc16";
        HttpUtil.sendOkHttpRequest(weatherNowUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("获取天气信息失败");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = new Weather();
                weather.aqi = Utility.handlerWeatherAQIResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather.aqi != null && "200".equals(weather.aqi.status)){
                            SharedPreferences.Editor editor = getSharedPreferences("weatherAQI",
                                    MODE_PRIVATE).edit();
                            editor.putString("AQI",responseText);
                            editor.apply();

                        }else {
                            showToast("获取天气信息失败");
                        }
                    }
                });
            }
        });
    }

    /**
     * 去和风天气获取生活建议信息Suggestion
     * @param weatherId 传入城市ID
     */
    public void requestWeatherSuggestion(final String weatherId,final String weatherName){
        String weatherNowUrl = "https://devapi.qweather.com/v7/indices/1d?type=3,9,13&location="+weatherId+
                "&key=98c2e401cf4b46908da304061da6bc16";
        HttpUtil.sendOkHttpRequest(weatherNowUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("获取天气信息失败");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = new Weather();
                weather.suggestion = Utility.handlerWeatherSuggestionResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather.suggestion != null && "200".equals(weather.suggestion.status)){
                            SharedPreferences.Editor editor = getSharedPreferences("weatherSuggestion",
                                    MODE_PRIVATE).edit();
                            editor.putString("Suggestion",responseText);
                            editor.apply();

                        }else {
                            showToast("获取天气信息失败");
                        }
                    }
                });
            }
        });
    }


    /**
     * 界面信息展示逻辑
     * @param weather 打包好的weather类
     * @param localName 传入选择了的城市名
     */
    private void showWeatherInfo(@NotNull Weather weather, String localName){
        String updateTime = weather.now.now.checkTime.split("T|\\+")[1];
        String degree = weather.now.now.temperature + "°C";
        String weatherInfo = weather.now.now.information;
        String weatherIcon = "icon_" + weather.now.now.iconImage;
        /*ApplicationInfo applicationInfo = mContext.getApplicationInfo();
        int intID = mContext.getResources().getIdentifier(weatherIcon,"drawable",
                applicationInfo.packageName);//获取drawable文件下边的文件id
        weatherImage.setImageResource(intID);//这样不一定能对，不行就人工case

         */


        titleCity.setText(localName);
        titleUpdateTime.setText(updateTime);
        weatherInfoText.setText(weatherInfo);
        degreeText.setText(degree);

        forecastLayout.removeAllViews();
        for(Daily.Forecast forecast : weather.daily.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.information);
            maxText.setText(String.format("%s°C", forecast.TemperatureMax));//直接在后面加“°C”有警告，换成这个就没了
            minText.setText(new StringBuilder().append(forecast.TemperatureMin).append("°C").toString());
            forecastLayout.addView(view);
        }



        if (weather.aqi != null){
            aqiText.setText(weather.aqi.aqiNow.aqi);
            pm25Text.setText(weather.aqi.aqiNow.pm25);
        }

        for (Suggestion.SuggestionTo suggestion : weather.suggestion.suggestionToList){
            String suggestText = suggestion.name + ": " + suggestion.suggestionText;
            switch (suggestion.name) {
                case "穿衣指数":
                    wearText.setText(suggestText);
                    break;
                case "感冒指数":
                    healthText.setText(suggestText);
                    break;
                case "化妆指数":
                    paintingText.setText(suggestText);
                    break;
            }
        }



        weatherLayout.setVisibility(View.VISIBLE);

    }

    /**
     * 弹出框的封装
     * @param msg 弹出框的内容
     */
    public void showToast(String msg){
        Toast.makeText(WeatherActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

}