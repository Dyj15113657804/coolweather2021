package com.example.coolweather2021.util;

import android.text.TextUtils;

import com.example.coolweather2021.db.City;
import com.example.coolweather2021.db.County;
import com.example.coolweather2021.db.Province;
import com.example.coolweather2021.gson.AQI;
import com.example.coolweather2021.gson.Daily;
import com.example.coolweather2021.gson.Now;
import com.example.coolweather2021.gson.Suggestion;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Utility {
    /**
     * 解析服务端返回的省级数据
     *
     * @param response 传入的json字符串
     * @return 成功就true，不成功就false
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvince = new JSONArray(response);
                for (int i = 0; i < allProvince.length(); i++) {
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析处理服务端返回的市级数据
     *
     * @param response   传入的json字符串
     * @param provinceId 省份代码
     * @return 成功就true，不成功就false
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCity = new JSONArray(response);
                for (int i = 0; i < allCity.length(); i++) {
                    JSONObject cityObject = allCity.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析处理服务端返回的县级数据
     *
     * @param response 传入的json字符串
     * @param cityId   城市代码
     * @return 成功就true，不成功就false
     */
    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounty = new JSONArray(response);
                for (int i = 0; i < allCounty.length(); i++) {
                    JSONObject countyObject = allCounty.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析实时天气
     * @param response 原数据
     * @return
     */
    public static Now handlerWeatherNowResponse(String response) {
        try {
            return new Gson().fromJson(response, Now.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析AQI数据
     * @param response 原数据
     * @return
     */
    public static AQI handlerWeatherAQIResponse(String response) {
        try {
            return new Gson().fromJson(response, AQI.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析多日数据
     * @param response 原数据
     * @return
     */
    public static Daily handlerWeatherDailyResponse(String response) {
        try {
            return new Gson().fromJson(response, Daily.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解析生活建议
     * @param response 原数据
     * @return
     */
    public static Suggestion handlerWeatherSuggestionResponse(String response) {
        try {
            return new Gson().fromJson(response, Suggestion.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


}
