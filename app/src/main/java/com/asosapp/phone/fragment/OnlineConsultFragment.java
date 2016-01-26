package com.asosapp.phone.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asosapp.phone.R;
import com.asosapp.phone.activity.FeedbackActivity;
import com.asosapp.phone.activity.LoginActivity;
import com.asosapp.phone.activity.LoginNewActivity;
import com.asosapp.phone.activity.SQLiteActivity;

import java.util.Locale;

/**
 * Created by ASOS_zhulr on 2015/12/7.
 */
public class OnlineConsultFragment extends Fragment implements View.OnClickListener {

    private View view;
    private View online_1, online_2, online_3, online_4;
    private boolean isLogin = true;
    private TextView titleView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_online_consult, container, false);
        init();
        return view;
    }

    private void init() {
        online_1 = view.findViewById(R.id.online_1);
        online_2 = view.findViewById(R.id.online_2);
        online_3 = view.findViewById(R.id.online_3);
        online_4 = view.findViewById(R.id.online_4);
        titleView=(TextView)view.findViewById(R.id.title_name);
        titleView.setText(R.string.mess);
        online_1.setOnClickListener(this);
        online_2.setOnClickListener(this);
        online_3.setOnClickListener(this);
        online_4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (isLogin) {
            dialog();
        } else {
            startActivity(new Intent(getActivity(), FeedbackActivity.class));
        }


    }

    //提示框
    protected void dialog() {
        Dialog alertDialog = new AlertDialog.Builder(getActivity()).
                setTitle("提示").
                setMessage("您还没有登录，请登录！").
//                setIcon(R.drawable.ic_launcher).
        setPositiveButton("确定", new DialogInterface.OnClickListener() {

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Intent intent = new Intent(getActivity(), LoginNewActivity.class);
        startActivity(intent);
    }
}).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).
                create();
        alertDialog.show();
    }
}
