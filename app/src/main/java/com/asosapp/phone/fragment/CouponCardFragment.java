package com.asosapp.phone.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asosapp.phone.R;

/**
 * Created by ASOS_zhulr on 2016/2/23.
 */
public class CouponCardFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_coupon_card,null);
        return view;
    }
}
