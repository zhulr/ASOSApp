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

    @Override
    public void onBindViewHolder(ViewHolders holder, int position) {
//        holder.title_text.setText("安邦");

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}

class ViewHolders extends RecyclerView.ViewHolder {
    TextView title_text;

    public ViewHolders(View itemView) {
        super(itemView);
        title_text = (TextView) itemView.findViewById(R.id.title_text);
    }
}

