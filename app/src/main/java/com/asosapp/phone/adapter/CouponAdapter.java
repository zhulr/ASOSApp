package com.asosapp.phone.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asosapp.phone.R;
import com.asosapp.phone.view.ToastView;

import java.util.List;

/**
 * Created by Leo on 2016/2/26.
 */
public class CouponAdapter extends RecyclerView.Adapter<ViewHolders> {

    private LayoutInflater mInfalater;
    private Context context;
    private List<String> mDatas;
    public CouponAdapter(Context context,List<String> mDatas) {
        this.context = context;
        this.mDatas=mDatas;
        mInfalater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInfalater.inflate(R.layout.businesse_information_detail, parent, false);
        ViewHolders viewHolder = new ViewHolders(view);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolders holder, final int position) {
//    holder.spare_time.setVisibility(View.VISIBLE);
//        holder.use_changecolor.setBackgroundColor(R.color.gray);
//        holder.use_click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastView.toast(context,"点击成功"+position);
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}

class ViewHolders extends RecyclerView.ViewHolder {
    TextView spare_time;
    LinearLayout use_changecolor;
    LinearLayout use_click;

    public ViewHolders(View itemView) {
        super(itemView);
        spare_time= (TextView) itemView.findViewById(R.id.spare_time);
        use_changecolor= (LinearLayout) itemView.findViewById(R.id.use_after);
        use_click= (LinearLayout) itemView.findViewById(R.id.use_click);

    }
}

