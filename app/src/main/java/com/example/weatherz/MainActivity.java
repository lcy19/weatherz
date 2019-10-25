package com.example.weatherz;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt;
    private TextView address_time;
    private TextView future;
    final String key = "a074e539d9c04e1296a46124e45b1c24";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = (Button) findViewById(R.id.update);
        address_time = (TextView) findViewById(R.id.address_time);
        future = (TextView)findViewById(R.id.future);
    }

    @Override
    public void onClick(View v) {
        if( v.getId() == R.id.update){
//            https://free-api.heweather.net/s6/weather/now?location=beijing&key=
            String address = address_time.getText().toString().split(" ")[0];
            Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(MainActivity.this)));
            address = Pinyin.toPinyin(address, "");
            future.setText(address);

//            String urlString = "https://free-api.heweather.net/s6/weather/now?location="
        }
    }
}

