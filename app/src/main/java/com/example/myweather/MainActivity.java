package com.example.myweather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myweather.Weather.WeatherActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //------------Button------------
    Button buttonWeather;
    Button buttonAutoPlace;

    //------------JSON_Value------------
    JSONObject city_object;
    JSONArray city_array;

    //------------ArrayAdapterValue------------
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;

    //------------ArrayValue------------
    ArrayList<String> city_list;
    ArrayList<String> cityCountryID_list;
    ArrayList<String> country_list;
    ArrayList<String> countryID_list;

    //------------TextInputValue------------
    AutoCompleteTextView editCountry;
    AutoCompleteTextView editCity;
    //------------StringInputValue------------
    String tempCountry;
    String tempCity;

    //------------Location_Varible------------
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //------------Button_Load------------
        buttonWeather = findViewById(R.id.buttonWeather);
        buttonAutoPlace = findViewById(R.id.buttonAutoPlace);

        //------------loadList------------
        country_list = new ArrayList<>();
        countryID_list = new ArrayList<>();
        city_list = new ArrayList<>();
        cityCountryID_list = new ArrayList<>();

        //------------loadTextEdit------------
        editCountry = findViewById(R.id.editCountry);
        editCity = findViewById(R.id.editCity);

        //------------Button_Function------------
        buttonWeather.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);

            //------------String_Load------------
            tempCountry = editCountry.getText() + "";
            tempCity = editCity.getText() + "";

            //------------PushInfoForActivity------------
            intent.putExtra("COUNTRY", tempCountry);
            intent.putExtra("CITY", tempCity);

            //------------StartActivity------------
            startActivity(intent);
        });

        buttonAutoPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoSearchPlace();
            }
        });

        //------------GetJSONValues------------
        String jsonFile = loadJSONFromAsset();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonFile);

            JSONObject main_object = jsonObject.getJSONObject("rocid");
            JSONArray country_array = main_object.getJSONArray("country");
            city_array = main_object.getJSONArray("city");

            for (int i = 0; i < country_array.length(); i++) {
                JSONObject country_object = country_array.getJSONObject(i);
                country_list.add(country_object.getString("name"));
                countryID_list.add(country_object.getString("country_id"));
            }

            for (int i = 0; i < city_array.length(); i++) {
                city_object = city_array.getJSONObject(i);
                cityCountryID_list.add(city_object.getString("country_id"));
            }
        } catch (JSONException jsone) {
            jsone.printStackTrace();
        }

        //------------SetItemInEditCountry------------
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, country_list);
        editCountry.setAdapter(adapter);

        //------------SetItemInEditCity------------
        editCountry.setOnItemClickListener((parent, v, position, id) -> {
            city_list.clear();
            String idc = countryID_list.get(country_list.indexOf(adapter.getItem(position) + ""));
            adapter2 = new ArrayAdapter<String>(this, R.layout.list_item, city_list);
            try{
                for(int i = 0; i < city_array.length(); i++) {
                    if (idc.equals(cityCountryID_list.get(i))) {
                        city_object = city_array.getJSONObject(i);
                        city_list.add(city_object.getString("name"));
                    }
                }
            }catch (JSONException jsone) {
                jsone.printStackTrace();
            }
            editCity.setAdapter(adapter2);
        });

    }

    //------------LoadJsonFile------------
    public String loadJSONFromAsset() {
        byte[] buffer = null;
        InputStream is;
        try {
            is = MainActivity.this.getAssets().open("rocid.json");
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        String json = new String(buffer, StandardCharsets.UTF_8);
        return json;
    }

    //------------AutoSearchPlace------------
    public void autoSearchPlace(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geoCoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                try {
                                    List<Address> address = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);

                                    intent.putExtra("COUNTRY",address.get(0).getCountryName());
                                    intent.putExtra("CITY", address.get(0).getLocality());

                                    startActivity(intent);
                                } catch (IOException | NullPointerException e) {

                                }
                            }
                        }
                    });
        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},0);
            autoSearchPlace();
        }
    }
}
