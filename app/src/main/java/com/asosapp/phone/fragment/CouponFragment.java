package com.asosapp.phone.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asosapp.phone.R;
import com.asosapp.phone.adapter.CouponAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASOS_zhulr on 2016/2/23.
 */
public class CouponFragment extends Fragment {
    private View view;
    private CouponAdapter mAdapter;
    private RecyclerView card_recyclerView;
    private List<String> mData;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_coupon,null);
        init();
        return view;
    }

    private void init() {
        mData=new ArrayList<String>();

        card_recyclerView= (RecyclerView) view.findViewById(R.id.card_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        card_recyclerView.setLayoutManager(linearLayoutManager);
        for (int i=0;i<1;i++){
            mData.add(i+"");
        }
        mAdapter=new CouponAdapter(getActivity(),mData);
        card_recyclerView.setAdapter(mAdapter);
    }
}
