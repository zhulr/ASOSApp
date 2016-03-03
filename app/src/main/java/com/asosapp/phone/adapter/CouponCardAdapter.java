package com.asosapp.phone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private String CARD_A="A";
    private String CARD_B="B";

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


            if (mDatas.get(position).toString().equals(CARD_A)){
                holder.tv_card1.setVisibility(View.VISIBLE);
            }
            if (mDatas.get(position).toString().equals(CARD_B)){
                holder.tv_card2.setVisibility(View.VISIBLE);
            }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
class ViewHolderc extends RecyclerView.ViewHolder {
    ImageView tv_card1;
    ImageView tv_card2;

    public ViewHolderc (View itemView) {
        super(itemView);
        tv_card1 = (ImageView) itemView.findViewById(R.id.card1);
        tv_card2 = (ImageView) itemView.findViewById(R.id.card2);
    }
}
