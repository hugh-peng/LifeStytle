package cn.fengcrush.lifestyle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_city;
    private TextView tv_now_date;
    private TextView tv_now_temp;
    private TextView tv_now_wind;
    private TextView tv_now_type;
    private TextView tv_now_ganmao;
    private Button btn_weather;

    private String cityName = "茂名";
    private static final String address = "http://wthrcdn.etouch.cn/weather_mini?city=";
    private MHandler mHandler;
    public static final int WMSG_OK = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mHandler = new MHandler();
        init();
        getWeather();
    }

    public void init() {
        et_city = findViewById(R.id.et_city);
        tv_now_date = findViewById(R.id.tv_now_date);
        tv_now_temp = findViewById(R.id.tv_now_temp);
        tv_now_wind = findViewById(R.id.tv_now_wind);
        tv_now_type = findViewById(R.id.tv_now_type);
        tv_now_ganmao = findViewById(R.id.tv_now_ganmao);

        btn_weather = findViewById(R.id.btn_weather);
        btn_weather.setOnClickListener(this);
        et_city.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_DOWN) {
                    sendData();
                }
                return false;
            }
        });
    }

    private void sendData() {
        cityName = et_city.getText().toString();
        if (TextUtils.isEmpty(cityName)) {
            Toast.makeText(WeatherActivity.this,"您还没输入任何城市名称呢",Toast.LENGTH_LONG).show();
            return;
        }
        cityName = cityName.replaceAll(" ","").replaceAll("\n","").trim();
        getWeather();
    }

    @Override
    public void onClick(View v) {
        sendData();
    }

    private void getWeather() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(address+cityName).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res = response.body().string();
                Message msg = new Message();
                msg.what = WMSG_OK;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }

    class MHandler extends Handler {
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case WMSG_OK:
                    if(msg.obj != null){
                        String result = (String) msg.obj;
                        paresWeather(result);
                    }
            }
        }
    }

    private void paresWeather(String JsonData) {
        try {
            JSONObject obj = new JSONObject(JsonData);
            int status = obj.optInt("status");
            if(status == 1000) {
                if(obj.has("data")) {
                    JSONObject data = obj.getJSONObject("data");
                    tv_now_ganmao.setText(data.optString("ganmao"));
                    JSONArray forecast = data.getJSONArray("forecast");
                    JSONObject o = (JSONObject) forecast.get(0);
                    tv_now_date.setText(o.getString("date"));
                    tv_now_wind.setText(o.optString("fengxiang"));
                    tv_now_type.setText(o.optString("type"));
                    tv_now_temp.setText(o.optString("high")+"-"+o.optString("low"));
                }
            }else {
                Toast.makeText(WeatherActivity.this,"请求失败，请检查网络。注意城市不能是省份！",Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
