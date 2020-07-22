package com.example.myweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    //------------URL------------
    String place;
    String appId = "81ef8c608332d394a9b8d337fc128545";
    String units = "metric";

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

    public  void find_weather(){
        //------------Bundle_Load------------
        Bundle info = getIntent().getExtras();

        //------------String_Load------------
        tempCountry = info.getString("COUNTRY");
        tempCity = info.getString("CITY");

        //------------Text_Set------------
        textCountry.setText(tempCountry);
        textCity.setText(tempCity);

        //------------Place_Load------------
        place = tempCity + "," + tempCountry;

        NetworkService.getInstance()
                .getJSONApi()
                .getCurrentWeatherData(place, units, appId)
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                        WeatherResponse weatherResponse = response.body();

                        try {
                            //------------InfoJson_SetText------------
                            textPressure.setText((int) (weatherResponse.main.pressure * 0.75006375541921) + " hPa");
                            textTemperature.setText(String.valueOf((int) weatherResponse.main.temp) + " ℃");
                            textHumidity.setText(String.valueOf(((int) weatherResponse.main.humidity)) + " %");
                            textWind.setText(String.valueOf((int) weatherResponse.wind.speed) + " м/с");

                            timeZone = weatherResponse.timezone;

                            textSunRise.setText(date(weatherResponse.sys.sunrise));
                            textSunSet.setText(date(weatherResponse.sys.sunset));

                        //------------SwitchIMG------------
                        switch(weatherResponse.weather.get(0).id){
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
                        }catch (NullPointerException e){
                            startActivity(new Intent(WeatherActivity.this, MainActivity.class));
                            Toast toast = Toast.makeText(getApplicationContext(), "Этого места нет в базе данных погоды!", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {

                        Log.d("Error", "Error");
                        t.printStackTrace();
                    }
                });
    }

    //------------FunctionTimeZone------------
    String date(long unix){
        Date date = new Date((unix + timeZone)*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+0"));
        return sdf.format(date);
    }
}
