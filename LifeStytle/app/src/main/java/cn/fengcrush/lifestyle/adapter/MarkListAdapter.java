package cn.fengcrush.lifestyle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.fengcrush.lifestyle.R;
import cn.fengcrush.lifestyle.room.MarkBean;

public class MarkListAdapter extends RecyclerView.Adapter<MarkListAdapter.MyViewHolder>{
    private LayoutInflater layoutInflater;
    private List<MarkBean> list;
    private OnItemClickListener mOnItemClickListener;
    public MarkListAdapter(Context context,List<MarkBean> list) {
        this.layoutInflater=LayoutInflater.from(context);
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(layoutInflater.inflate(R.layout.mark_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MarkBean markBean = list.get(position);
        holder.tvContent.setText(markBean.getMark());
        holder.tvTime.setText(markBean.getTime());

        //为子项绑定事件响应
        if(mOnItemClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    return true; //消费掉click事件
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0: list.size();
    }

    //定义点击事件接口
    public interface OnItemClickListener {
        void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    //内部类用static来防止内存泄露
    static class MyViewHolder extends RecyclerView.ViewHolder {
        //关联组件
        TextView tvContent;
        TextView tvTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.item_content);
            tvTime = itemView.findViewById(R.id.item_time);
        }
    }
}
