package com.example.myweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class WeatherActivity extends Activity {

    //------------Button------------
    Button buttonBack;

    //------------ImageView------------
    ImageView imageWeather;

    //------------TextView------------
    TextView textPressure;
    TextView textHumidity;
    TextView textWind;
    TextView textTemperature;
    TextView textSunRise;
    TextView textSunSet;
    TextView textCountry;
    TextView textCity;

    //------------String------------
    String tempCountry;
    String tempCity;

    //------------Int------------
    int timeZone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //------------Image_Load------------
        buttonBack = findViewById(R.id.buttonBack);

        //------------Image_Load------------
        imageWeather = findViewById(R.id.imageTempWeather);

        //------------Text_Load------------
        textPressure = findViewById(R.id.textPressureScore);
        textHumidity = findViewById(R.id.textHumidityScore);
        textWind = findViewById(R.id.textWindScore);
        textTemperature = findViewById(R.id.textTemperatura);
        textSunRise = findViewById(R.id.textInfoSunR);
        textSunSet = findViewById(R.id.textInfoSunS);
        textCountry = findViewById(R.id.textInfoCountry);
        textCity = findViewById(R.id.textInfoCity);

        //------------Button_Function------------
        buttonBack.setOnClickListener(view -> {
            Intent intent = new Intent(WeatherActivity.this, MainActivity.class);

            //------------StartActivity------------
            startActivity(intent);
        });

        //------------Start_Function------------
        find_weather();
    }

    public void find_weather(){
        //------------Bundle_Load------------
        Bundle info = getIntent().getExtras();

        //------------String_Load------------
        tempCountry = info.getString("COUNTRY");
        tempCity = info.getString("CITY");

        //------------Text_Set------------
        textCountry.setText(tempCountry);
        textCity.setText(tempCity);

        //------------URL_Weather------------
        String url =  "http://api.openweathermap.org/data/2.5/weather?q="+ tempCity + "," + tempCountry + "&units=metric&appid=81ef8c608332d394a9b8d337fc128545&lang=ru";

        //------------Json_Request------------
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {

                //------------JsonObject_Load------------
                JSONObject main_object = response.getJSONObject("main");
                JSONObject wind_object = response.getJSONObject("wind");
                JSONObject sys_object = response.getJSONObject("sys");

                JSONArray weather_array = response.getJSONArray("weather");
                JSONObject weather_id = weather_array.getJSONObject(0);

                //------------StringInfoJson_Load------------
                String temp = String.valueOf(main_object.getDouble("temp"));
                String press = String.valueOf(main_object.getDouble("pressure"));
                String humi = String.valueOf(main_object.getDouble("humidity"));
                String windS = String.valueOf(wind_object.getDouble("speed"));

                //------------IntTimeZone_Load------------
                timeZone = Integer.valueOf(response.get("timezone") + "");

                //------------Int(Double,Long)Info_Load------------
                long sunri = sys_object.getLong("sunrise");
                long sunse = sys_object.getLong("sunset");
                int idWeather = weather_id.getInt("id");

                double cels = Double.parseDouble(temp);

                double pres = Double.parseDouble(press);
                double normPress = pres * 0.75006375541921;

                double humid = Double.parseDouble(humi);

                double windy = Double.parseDouble(windS);

                //------------InfoJson_SetText------------
                textPressure.setText(String.valueOf((int)normPress) + " hPa");
                textTemperature.setText(String.valueOf((int)cels) + " ℃");
                textHumidity.setText(String.valueOf(((int)humid)) + " %");
                textWind.setText(String.valueOf((int)windy) + " м/с");
                textSunRise.setText(date(sunri));
                textSunSet.setText(date(sunse));

                //------------SwitchIMG------------
                switch(idWeather){
                    case 800:
                        imageWeather.setImageDrawable(getResources().getDrawable(R.drawable.android_01d));
                        break;
                    case 801:
                        imageWeather.setImageDrawable(getResources().getDrawable(R.drawable.android_02d));
                        break;
                    case 802:
                        imageWeather.setImageDrawable(getResources().getDrawable(R.drawable.android_03d));
                        break;
                    case 803: case 804:
                        imageWeather.setImageDrawable(getResources().getDrawable(R.drawable.android_04d));
                        break;
                    case 300: case 301: case 302: case 310: case 311: case 312: case 313: case 314: case 321: case 520: case 521: case 522: case 531:
                        imageWeather.setImageDrawable(getResources().getDrawable(R.drawable.android_09d));
                        break;
                    case 500: case 501: case 502: case 503: case 504:
                        imageWeather.setImageDrawable(getResources().getDrawable(R.drawable.android_10d));
                        break;
                    case 200: case 201: case 202: case 210: case 211: case 212: case 221: case 230: case 231: case 232:
                        imageWeather.setImageDrawable(getResources().getDrawable(R.drawable.android_11d));
                        break;
                    case 511: case 600: case 601: case 602: case 611: case 612: case 613: case 615: case 616: case 620: case 621: case 622:
                        imageWeather.setImageDrawable(getResources().getDrawable(R.drawable.android_13d));
                        break;
                    case 701: case 711: case 721: case 731: case 741: case 751: case 761: case 762: case 771: case 781:
                        imageWeather.setImageDrawable(getResources().getDrawable(R.drawable.android_50d));
                        break;
                }
            } catch (JSONException  e) {
                e.printStackTrace();
            }
        }, error -> {

        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    //------------FunctionTimeZone------------
    String date(long unix){
        Date date = new Date((unix + timeZone)*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+0"));
        return sdf.format(date);
    }
}
