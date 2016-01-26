package com.asosapp.phone.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ASOS_zhulr on 2015/12/7.
 */
public class TabFragment extends Fragment {

    private String mTitle = "Default";
    public static final String TITLE = "title";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments().getString("") != null) {
            mTitle = getArguments().getString(TITLE);
        }
        TextView tv = new TextView(getActivity());
        tv.setTextSize(30);
        tv.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        tv.setText(mTitle);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
