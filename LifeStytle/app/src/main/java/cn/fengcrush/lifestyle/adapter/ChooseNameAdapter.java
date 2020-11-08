package cn.fengcrush.lifestyle.adapter;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.fengcrush.lifestyle.R;
import cn.fengcrush.lifestyle.entity.CompanyName;

public class ChooseNameAdapter extends BaseQuickAdapter<CompanyName, BaseViewHolder> {
    private int ischeckUser = 0;
    Context mcontext;
    private ItemClickListener mItemClickListener;

    public ChooseNameAdapter(Context mcontext,@Nullable List<CompanyName> data) {
        super(R.layout.express_item,data);
        this.mcontext = mcontext;
    }

    @Override
    protected void convert(BaseViewHolder helper, CompanyName item) {
        //helper.addOnClickListener(R.id.recycler);
        helper.setText(R.id.tv_username,item.getCom())
                .setChecked(R.id.checkBox,item.isIscheck());
        helper.itemView.setOnClickListener(v -> {
            try {
                if (item.isIscheck()) {
                    ischeckUser = 0;
                    helper.setChecked(R.id.checkbox, false);
                    item.setIscheck(false);
                } else {
                    if (ischeckUser == 1) {
                        Toast.makeText(mcontext,"只能选择一个用户!",Toast.LENGTH_LONG).show();
                    } else {
                        ischeckUser = 1;
                        helper.setChecked(R.id.checkbox, true);
                        item.setIscheck(true);
                    }
                }
                mItemClickListener.onItemClick(helper.getLayoutPosition(), item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public  void setmItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position,CompanyName item);
    }
}
