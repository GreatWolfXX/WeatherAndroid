package com.example.myweather.Weather;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherResponse {
    @SerializedName("coord")
    public Coord coord;
    @SerializedName("weather")
    public ArrayList<Weather> weather = new ArrayList<>();
    @SerializedName("base")
    public String base;
    @SerializedName("main")
    public Main main;
    public String visivility;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("clouds")
    public Clouds clouds;
    @SerializedName("dt")
    public float dt;
    @SerializedName("sys")
    public Sys sys;
    @SerializedName("timezone")
    public int timezone;
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("cod")
    public float cod;
}

class Coord {
    @SerializedName("lon")
    public float lon;
    @SerializedName("lat")
    public float lat;
}
class Weather{
    @SerializedName("id")
    public int id;
    @SerializedName("main")
    public String main;
    @SerializedName("description")
    public String description;
    @SerializedName("icon")
    public String icon;
}
class Main {
    @SerializedName("temp")
    public float temp;
    @SerializedName("humidity")
    public float humidity;
    @SerializedName("pressure")
    public float pressure;
    @SerializedName("temp_min")
    public float temp_min;
    @SerializedName("temp_max")
    public float temp_max;
}
class Wind {
    @SerializedName("speed")
    public float speed;
    @SerializedName("deg")
    public float deg;
}
class Clouds {
    @SerializedName("all")
    public float all;
}
class Sys {
    @SerializedName("country")
    public String country;
    @SerializedName("sunrise")
    public long sunrise;
    @SerializedName("sunset")
    public long sunset;
}
