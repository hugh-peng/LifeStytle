package cn.fengcrush.lifestyle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cn.fengcrush.lifestyle.room.MarkBean;
import cn.fengcrush.lifestyle.room.MarkDao;
import cn.fengcrush.lifestyle.room.MarkDatabase;
import cn.fengcrush.lifestyle.utils.Time;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author:yaofeng耀锋
 * time：2020/05/02
 *
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_refresh;
    private Button btn_save;
    private Button btn_search;
    private TextView day_remark;
    private TextView tv_express;
    private TextView tv_server;
    private TextView tv_weather;
    private String daymark;

    private static final String ADDERSS = "https://v1.hitokoto.cn";
    private String type = "";
    private String [] typeArr ={"/?c=b","/?c=c","/?c=h","/?c=i","/?c=j","/?c=l"};
    int chosse = 1;
    public static final int MSG_OK = 1;
    private MHandler mHandler;

    private MarkDatabase markDatabase;
    private MarkDao markDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //实例化Handler对象
        mHandler = new MHandler();
        //获取单例下Room的对象
        markDatabase = MarkDatabase.getmInstance(this);
        markDao = markDatabase.getMarkDao();
        init();
        getDayRemark();
    }

    public void init() {
        btn_refresh = findViewById(R.id.btn_refresh);
        btn_save = findViewById(R.id.btn_save);
        btn_search = findViewById(R.id.btn_search);
        day_remark = findViewById(R.id.day_remark);
        tv_express = findViewById(R.id.tv_exprss);
        tv_server = findViewById(R.id.tv_server);
        tv_weather = findViewById(R.id.tv_weather);
        btn_refresh.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        tv_express.setOnClickListener(this);
        tv_server.setOnClickListener(this);
        tv_weather.setOnClickListener(this);
    }

    private void getDayRemark() {
        //UI进程不能进行耗时操作，必须进行异步处理，不然会造成程序假死
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(ADDERSS+type).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res = response.body().string();
                Message msg = new Message();
                msg.what = MSG_OK;
                msg.obj = res;
                //发信息给Handler，通知其更新UI界面
                mHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_refresh:
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("请选择你想要的类型")
                        .setIcon(R.drawable.launcher)
                        .setSingleChoiceItems(new String[]{"漫画", "游戏", "影视", "诗词", "网易云",
                                "抖机灵"}, chosse, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                chosse = which;
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                type = typeArr[chosse];
                                getDayRemark();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.btn_save:
                MarkBean markBean = new MarkBean(Time.getTime(),daymark);
                markDao.insertMarks(markBean);
                Toast.makeText(this,"保存成功!",Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_search:
                startActivity(new Intent(this,MarkActivity.class));
                break;
            case R.id.tv_exprss:
                startActivity(new Intent(this,ExpressActivity.class));
                break;
            case R.id.tv_server:
                startActivity(new Intent(this,AboutServerActivity.class));
                break;
            case R.id.tv_weather:
                startActivity(new Intent(this,WeatherActivity.class));
        }
    }

    class MHandler extends Handler {
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_OK:
                    if(msg.obj != null) {
                        String vResult = (String) msg.obj;
                        //解析数据
                        paresData(vResult);
                    }
                    break;
            }
        }
    }

    /**
     * Json格式：{"id":682,"hitokoto":"我战斗到了最后一刻！","type":"c","from":"战争守卫沙尔图拉","from_who":null,"creator":"树形图设计者","creator_uid":55,"reviewer":0,"uuid":"9cc9d04a-33cd-4398-bdf5-5450f3556b67","created_at":"1476361427"}
     */
    private void paresData(String JsonData) {
        try {
            JSONObject obj = new JSONObject(JsonData);
            //获取每日一句
            daymark = obj.optString("hitokoto");
            day_remark.setText(daymark);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"网络不好哟！",Toast.LENGTH_LONG).show();
        }

    }

    protected long exitTime;//记录第一次点击时的时间
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出日常助手",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MainActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
