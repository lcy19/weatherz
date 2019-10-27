package com.example.weatherz;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherz.Utils.RequestCodeInfo;
import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;


public class MainActivity extends AppCompatActivity{
    private TextView address_time;
    private TextView tmp;
    private TextView weather;
    private TextView future;
    private UserUrl userUrl;
    private MyView myView;
    final String key = "dfff904798c948a7b07d71ae94f658ab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDate();

    }

    public void initView(){
        address_time = (TextView) findViewById(R.id.address_time);
        tmp = (TextView)findViewById(R.id.tmp);
        weather = (TextView)findViewById(R.id.weather);
        future = (TextView)findViewById(R.id.future);
    }

    public void initDate(){
        userUrl = new UserUrl();
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        String location =  pref.getString("location","");
        if(location.length() < 2){
            location = "beijing";
        }

        String weather_type = "now";
        userUrl.setWeather_type(weather_type);
        userUrl.setLocation(location);

        updateWeather();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.update_view:
                updateWeather();
                break;
            case R.id.change_city:
                Intent intent = new Intent(MainActivity.this,CityPickerActivity.class);
                startActivityForResult(intent, RequestCodeInfo.GETCITY);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RequestCodeInfo.GETCITY:
                    String city = data.getExtras().getString("city");
                    Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(MainActivity.this)));
                    city = Pinyin.toPinyin(city, "").toLowerCase();
                    SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString("location", city);
                    editor.apply();
                    userUrl.setLocation(city);
                    updateWeather();
                    break;
                    default:
            }
        }
    }

    public void updateWeather(){
        // https://free-api.heweather.net/s6/weather/now?location=shanxi&key=dfff904798c948a7b07d71ae94f658ab

        String urlString = "https://free-api.heweather.net/s6/weather/" + userUrl.getWeather_type()
                + "?location=" + userUrl.getLocation() + "&key=" + key;
        sendRequest(urlString);
    }

    private void sendRequest(final String urlString){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try{
                    URL url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line = null;
                    while((line = reader.readLine()) != null ) {
                        response.append(line);
                    }
                    parseJSON(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(reader != null){
                        try{
                            reader.close();
                        } catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                    if(connection != null ){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void parseJSON(String jsonDate){
        try{
            JSONObject root = new JSONObject(jsonDate);
            JSONArray weatherArray = root.getJSONArray("HeWeather6");
            JSONObject weather = weatherArray.getJSONObject(0);
            JSONObject basic = weather.getJSONObject("basic");
            JSONObject update = weather.getJSONObject("update");
            JSONObject now = weather.getJSONObject("now");
            String address = basic.getString("location");
            String time = update.getString("loc");
            String tmp = now.getString("tmp");
            String cond_txt = now.getString("cond_txt");
            myView = new MyView(address, time, tmp, cond_txt);

            showResponse();

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void showResponse(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                address_time.setText(myView.getAddress_time());
                tmp.setText(myView.getTmp());
                weather.setText(myView.getCond_txt());
            }
        });
    }

}

