package cn.fengcrush.lifestyle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.fengcrush.lifestyle.adapter.MarkListAdapter;
import cn.fengcrush.lifestyle.room.MarkBean;
import cn.fengcrush.lifestyle.room.MarkDao;
import cn.fengcrush.lifestyle.room.MarkDatabase;

public class MarkActivity extends AppCompatActivity {
    private MarkDatabase markDatabase;
    private MarkDao markDao;
    private List<MarkBean> list;
    private RecyclerView recyclerView;
    private MarkListAdapter markListAdapter;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);
        //获取单例下Room的对象
        markDatabase = MarkDatabase.getmInstance(this);
        markDao = markDatabase.getMarkDao();
        //得到全部数据列表
        list = markDao.getAllMarks();
        init();
        //隐藏处理过程
        progressBar.setVisibility(View.GONE);
        //将recycler可视化与适配
        recyclerView.setVisibility(View.VISIBLE);
        markListAdapter = new MarkListAdapter(this,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(markListAdapter);
        clickItem();

    }

    public void init() {
        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recycler);
    }

    public void clickItem() {
        markListAdapter.setOnItemClickListener(new MarkListAdapter.OnItemClickListener() {
            @Override
            public void onLongClick(int position) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(MarkActivity.this)
                        .setMessage("确定删除你的这条宝贝语句么？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MarkBean bean = list.get(position);
                                //Room与list同步删除对应Item
                                markDao.deleteMarks(bean);
                                list.remove(position);
                                markListAdapter.notifyDataSetChanged();
                                Toast.makeText(MarkActivity.this,"你的宝贝离你而去了！",Toast.LENGTH_LONG).show();
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
            }
        });
    }
}
