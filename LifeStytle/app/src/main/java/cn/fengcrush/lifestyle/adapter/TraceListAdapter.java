package cn.fengcrush.lifestyle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.fengcrush.lifestyle.R;
import cn.fengcrush.lifestyle.entity.ExpressTraces;

public class TraceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private LayoutInflater inflater;
    private List<ExpressTraces> tracesList = new ArrayList<>();
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL = 0x0001;

    public TraceListAdapter(Context context, List<ExpressTraces> tracesList) {
        inflater = LayoutInflater.from(context);
        this.tracesList = tracesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.trace_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder itemHolder = (ViewHolder) holder;
        if(getItemViewType(position)==TYPE_TOP) {
            //第一行不显示竖线
            itemHolder.tvTopLine.setVisibility(View.INVISIBLE);
            //将字体颜色加深
            itemHolder.tvTime.setTextColor(0xff555555);
            itemHolder.tvStation.setTextColor(0xff555555);
            itemHolder.tvDot.setBackgroundResource(R.drawable.timeline_dot_first);
        } else if(getItemViewType(position) == TYPE_NORMAL) {
            itemHolder.tvTopLine.setVisibility(View.VISIBLE);
            itemHolder.tvTime.setTextColor(0xff999999);
            itemHolder.tvStation.setTextColor(0xff999999);
            itemHolder.tvDot.setBackgroundResource(R.drawable.timeline_dot_normal);
        }
        itemHolder.bindHolder(tracesList.get(position));
    }

    @Override
    public int getItemCount() {
        return tracesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTime,tvStation;
        private TextView tvTopLine, tvDot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStation = itemView.findViewById(R.id.tvStation);
            tvTopLine = itemView.findViewById(R.id.tvTopLine);
            tvDot = itemView.findViewById(R.id.tvDot);
        }

        public void bindHolder(ExpressTraces traces) {
            tvTime.setText(traces.getTime());
            tvStation.setText(traces.getDesc());
        }

    }
}
