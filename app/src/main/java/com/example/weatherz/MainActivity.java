package com.example.weatherz;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt;
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
        bt.setOnClickListener(this);

    }

    public void initView(){
        bt = (Button) findViewById(R.id.update);
        address_time = (TextView) findViewById(R.id.address_time);
        tmp = (TextView)findViewById(R.id.tmp);
        weather = (TextView)findViewById(R.id.weather);
        future = (TextView)findViewById(R.id.future);
    }

    public void initDate(){
        userUrl = new UserUrl();
        String location = address_time.getText().toString().split(" ")[0];
        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(MainActivity.this)));
        location = Pinyin.toPinyin(location, "").toLowerCase();
        String weather_type = "now";
        userUrl.setWeather_type(weather_type);
        userUrl.setLocation(location);

        String urlString = "https://free-api.heweather.net/s6/weather/" + userUrl.getWeather_type()
                + "?location=" + userUrl.getLocation() + "&key=" + key;
        sendRequest(urlString);
    }

    @Override
    public void onClick(View v) {
        if( v.getId() == R.id.update){
//            https://free-api.heweather.net/s6/weather/now?location=shanxi&key=dfff904798c948a7b07d71ae94f658ab

            String urlString = "https://free-api.heweather.net/s6/weather/" + userUrl.getWeather_type()
                    + "?location=" + userUrl.getLocation() + "&key=" + key;
            sendRequest(urlString);
        }
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
                //
                Toast.makeText(this, "update view", Toast.LENGTH_LONG).show();
                break;
            case R.id.change_city:
                //
                Toast.makeText(this, "chang city", Toast.LENGTH_LONG).show();
                break;
            default:
        }
        return true;
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
            String address_time = basic.getString("location") + " " + update.getString("loc");
            String tmp = now.getString("tmp");
            String cond_txt = now.getString("cond_txt");
            myView = new MyView(address_time, tmp, cond_txt);

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

