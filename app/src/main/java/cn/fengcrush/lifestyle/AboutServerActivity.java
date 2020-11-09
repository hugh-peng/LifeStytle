package cn.fengcrush.lifestyle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AboutServerActivity extends AppCompatActivity implements View.OnClickListener {
    private Switch ser_switch;
    private TextView tv_blog;

    Intent intentmusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_server);
        ser_switch = findViewById(R.id.ser_switch);
        ser_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    intentmusic = new Intent(AboutServerActivity.this,MyService.class);
                    startService(intentmusic);
                    Toast.makeText(AboutServerActivity.this,"正在为您播放音乐",Toast.LENGTH_LONG).show();

                } else {
                    stopService(intentmusic);
                }
            }
        });
        tv_blog = findViewById(R.id.tv_blog);
        tv_blog.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        //点击跳转到系统浏览器
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse("http://fengcrush.cn");
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出页面时停止服务，否则会出错
        boolean isRun = isServiceRunning(this,"cn.fengcrush.lifestyle.MyService");
        if(isRun) {
            stopService(intentmusic);
        }
    }

    public boolean isServiceRunning(Context context,String className) {
        boolean isRun = false;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(40);
        int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRun = true;
                break;
            }
        }
        return isRun;
    }
}
