package cn.fengcrush.lifestyle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cn.fengcrush.lifestyle.adapter.TraceListAdapter;
import cn.fengcrush.lifestyle.entity.ExpressTraces;
import cn.fengcrush.lifestyle.utils.GetRequest;

public class ExpressActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_query;
    private EditText ev_query;
    private TextView tv_choose;
    private ProgressBar progress;   //处理过程过渡动画

    private String queryMsg;        //输入单号
    private String cname;           //快递公司名称
    private List<ExpressTraces> tracesList;     //轨迹集合
    private String cno = "";        //快递公司请求代码
    private MyHandler myHandler;    //handler异步消息处理
    public static  final int MSG_OK = 1;

    private RecyclerView recyclerView;
    private TextView tv_nodata;
    private TraceListAdapter traceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);
        myHandler = new MyHandler();
        tracesList = new ArrayList<>();
        init();
    }
    public void init() {
        progress = findViewById(R.id.progress);
        btn_query = findViewById(R.id.btn_query);
        ev_query = findViewById(R.id.ev_query);
        tv_choose = findViewById(R.id.tv_choose);
        tv_nodata = findViewById(R.id.tv_nodata);
        btn_query.setOnClickListener(this);
        tv_choose.setOnClickListener(this);
        //先隐藏处理过程
        progress.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recycler);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query:
                //显示处理过程
                progress.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
                queryMsg = ev_query.getText().toString();
                if(TextUtils.isEmpty(queryMsg)) {
                    showToast("您还没有输入任何消息！");
                    return;
                }
                ev_query.setText("");
                queryMsg = queryMsg.replaceAll(" ","").replaceAll("\n","").trim();
                //耗时操作不能在主线程进行，必须开子线程进行网络读取；在方法内开启子线程
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        String result = GetRequest.getRequest(ExpressActivity.this,cno,queryMsg);
                        Message msg = new Message();
                        msg.what = MSG_OK;
                        msg.obj = result;
                        myHandler.sendMessage(msg);
                    }
                }.start();
                break;
            case R.id.tv_choose:
                //跳转到选择界面
                Intent intent = new Intent(ExpressActivity.this,Express_choose.class);
                startActivityForResult(intent,1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 & resultCode == 2) {
            cname = data.getStringExtra("cname");
            cno = data.getStringExtra("cno");
            if(cname != null){
                tv_choose.setText(cname);
            }else {
                cno = "";
                tv_choose.setText("选择");
            }
            Toast.makeText(ExpressActivity.this,"公司："+cname+",编号："+cno,Toast.LENGTH_SHORT).show();
        }
    }

    class MyHandler extends Handler {
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_OK:
                    if(msg.obj != null) {
                        String vlResult = (String) msg.obj;
                        parseData(vlResult);
                    }
                    break;
            }
        }
    }

    private void parseData(String JsonData) {
        //清理列表的旧数据
        if(tracesList != null) {
            tracesList.clear();
        }
        try {
            JSONObject obj = new JSONObject(JsonData);
            int ret = obj.optInt("ret");
            if(ret ==200) {     //请求成功
                if(obj.has("data")) {
                    JSONObject data = obj.getJSONObject("data");
                    JSONArray traces = data.getJSONArray("traces");
                    if(traces.length()>0) {     //处理成功
                        for(int i = 0;i < traces.length();i ++) {
                            JSONObject o = (JSONObject) traces.get(i);
                            ExpressTraces ex = new ExpressTraces();
                            ex.setTime(o.optString("time"));
                            ex.setDesc(o.optString("desc"));
                            tracesList.add(ex);
                        }
                    } else {        //请求成功，但返回空数组
                        progress.setVisibility(View.GONE);
                        tv_nodata.setVisibility(View.VISIBLE);
                        return;
                    }

                    //隐藏处理过程
                    progress.setVisibility(View.GONE);
                    //将列表内容倒置，使得上面的是最新消息，符合人们的习惯
                    Collections.reverse(tracesList);
                    //将recycler可视化与适配
                    recyclerView.setVisibility(View.VISIBLE);
                    traceListAdapter = new TraceListAdapter(this,tracesList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(traceListAdapter);
                }
            } else {            //处理失败
                errorAsk(ret);
                tv_nodata.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showToast("您的网络不好呀！");
        }
    }

    private void errorAsk(int error_code) {
        switch (error_code) {
            case 40001:
                showToast("快递单号不能为空！");
               break;
            case 40002:
                showToast("快递公司识别错误（该单号识别存在多个快递公司，须指定快递公司)");
                break;
            case 40003:
                showToast("快递公司识别失败！");
                break;
            case 40004:
                showToast("暂不支持该快递公司物流查询！");
                break;
        }
    }

    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

}
