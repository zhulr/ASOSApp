package com.asosapp.phone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asosapp.phone.R;

import java.util.List;

/**
 * Created by Leo on 2016/2/27.
 */
public class CouponCardAdapter extends RecyclerView.Adapter<ViewHolderc> {
    private LayoutInflater mInfalater;
    private Context context;
    private List<String> mDatas;

    public CouponCardAdapter(Context context,List<String> mDatas) {
        this.context = context;
        this.mDatas=mDatas;
        mInfalater = LayoutInflater.from(context);
    }
    @Override
    public ViewHolderc onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInfalater.inflate(R.layout.businesse_card_detail, parent, false);
        ViewHolderc viewHolder = new ViewHolderc(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderc holder, int position) {
//        holder.tv_card_name1.setText("");
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
class ViewHolderc extends RecyclerView.ViewHolder {
    TextView tv_card_name1;

    public ViewHolderc (View itemView) {
        super(itemView);
        tv_card_name1 = (TextView) itemView.findViewById(R.id.tv_card_name1);
    }
}
