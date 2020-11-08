package cn.fengcrush.lifestyle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.fengcrush.lifestyle.adapter.ChooseNameAdapter;
import cn.fengcrush.lifestyle.entity.CompanyName;
import cn.fengcrush.lifestyle.utils.JsonParse;

public class Express_choose extends AppCompatActivity implements View.OnClickListener,ChooseNameAdapter.ItemClickListener {
    private Button btn_confirm;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ChooseNameAdapter mChooseNameAdapter;
    private List<CompanyName> companyNamesList;  //快递公司名称编号集合
    private List<CompanyName> mCheckList;       //已经选择列表
    private List<CompanyName> mList;            //显示列表

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.express_choose);
        companyNamesList = JsonParse.getInstance().getInfoFromJson(Express_choose.this);
        mCheckList = new ArrayList<>();
        mList = new ArrayList<>();
        init();
    }

    public void init() {
        btn_confirm = findViewById(R.id.btn_confirm);
        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recycler);
        progressBar.setVisibility(View.GONE);
        btn_confirm.setOnClickListener(this);
        recyclerView.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChooseNameAdapter = new ChooseNameAdapter(this,mList);
        mChooseNameAdapter.setmItemClickListener(this);
        mChooseNameAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN); //添加缩放动画效果
        recyclerView.setAdapter(mChooseNameAdapter);
        mList.addAll(companyNamesList);
        mChooseNameAdapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        String cname = null;
        String con = null;
        if(mCheckList.size() >0) {
            cname = mCheckList.get(0).getCom();
            con = mCheckList.get(0).getNo();
        }
        intent.putExtra("cname",cname);
        intent.putExtra("cno",con);
        setResult(2,intent);
        finish();
    }

    /**
     * 列表的单击事件
     *
     * @param position
     * @param item
     */

    @Override
    public void onItemClick(int position, CompanyName item) {
        for (int i = 0; i < mCheckList.size(); i++) {
            CompanyName user = mCheckList.get(i);
            //判断是否存在
            if (user.getNo().equals(item.getNo())) {
                mCheckList.remove(i);
                //通知刷新界面取消checkbox
                mChooseNameAdapter.notifyDataSetChanged();
            }
        }
        //添加进选择的集合
        if (item.isIscheck()) {
            mCheckList.add(item);
            //通知刷新界面选上checkbox
            mChooseNameAdapter.notifyDataSetChanged();
        }
    }
}
