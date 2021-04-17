package com.example.coolweather2021.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Suggestion {
    @SerializedName("code")
    public String status;

    @SerializedName("daily")
    public List<SuggestionTo> suggestionToList;

    public class SuggestionTo{
        @SerializedName("name")
        public String name;

        @SerializedName("text")
        public String suggestionText;
    }
}
