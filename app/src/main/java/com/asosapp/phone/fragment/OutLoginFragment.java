package com.asosapp.phone.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.asosapp.phone.R;
import com.asosapp.phone.activity.LoginNewActivity;
import com.asosapp.phone.view.RoundImage;

/**
 * Created by ASOS_zhulr on 2016/3/1.
 */
public class OutLoginFragment extends Fragment {

    private View view;
    private TextView titleName;
    private TextView outLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_out_login_set_center, container, false);
        initOutLogin();
        return view;
    }

    private void initOutLogin() {
        titleName = (TextView) view.findViewById(R.id.title_name);
        titleName.setText(R.string.mine);
        outLogin = (TextView) view.findViewById(R.id.to_login);
        outLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), LoginNewActivity.class);
                startActivity(intent);
            }
        });
    }
}
