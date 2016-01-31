package com.asosapp.phone.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asosapp.phone.R;
import com.asosapp.phone.activity.FeedbackActivity;
import com.asosapp.phone.activity.LoginNewActivity;
import com.asosapp.phone.activity.ServiceActivity;
import com.asosapp.phone.bean.ServiceInfo;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by ASOS_zhulr on 2015/12/7.
 */
public class OnlineConsultFragment extends Fragment implements View.OnClickListener {

    private View view;
    private View online_1, online_2, online_3, online_4;
    private boolean isLogin = false;
    private TextView titleView;
    //客服ID
    private String serviceID = "";
    //客户ID
    private String mID = "";
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_online_consult, container, false);
        sharedPreferences = getActivity().getSharedPreferences("UserInfo",1);
        init();
        return view;
    }

    private void init() {
        online_1 = view.findViewById(R.id.asos111);
        online_2 = view.findViewById(R.id.asos222);
        online_3 = view.findViewById(R.id.asos333);
        online_4 = view.findViewById(R.id.asos444);
        titleView = (TextView) view.findViewById(R.id.title_name);
        titleView.setText(R.string.mess);
        online_1.setOnClickListener(this);
        online_2.setOnClickListener(this);
        online_3.setOnClickListener(this);
        online_4.setOnClickListener(this);
        serviceID = ServiceInfo.getServiceName_1();
    }


    @Override
    public void onClick(View v) {
//        final Conversation conv = mChatController.getConversation();
        isLogin = sharedPreferences.getBoolean("isLogin", false);
        switch (v.getId()) {
            case R.id.asos111:
                serviceID = ServiceInfo.getServiceName_1();
                isSelfLogin();
                break;
            case R.id.asos222:
                serviceID = ServiceInfo.getServiceName_2();
                isSelfLogin();
                break;
            case R.id.asos333:
                serviceID = ServiceInfo.getServiceName_3();
                isSelfLogin();
                break;
            case R.id.asos444:
                serviceID = ServiceInfo.getServiceName_4();
                isSelfLogin();
                break;

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
        intent.putExtra("service", serviceID);
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

    /**
     * 是否登陆
     */
    private void isSelfLogin() {
        if (isLogin) {
            mID = sharedPreferences.getString("user_name", "");
            intent();
        } else {
            dialog();
        }

    }

    /**
     * 跳转activity
     */
    private void intent() {
        Intent intent=new Intent();
        if (isService()){
            intent.putExtra("service", serviceID);
            intent.putExtra("myID", mID);
            intent.putExtra("nameID","0");
            intent.setClass(getActivity(), FeedbackActivity.class);
        }else{
            intent.setClass(getActivity(), ServiceActivity.class);
        }

        startActivity(intent);
    }

    private boolean isService() {
        String userName = JMessageClient.getMyInfo().getUserName();
        if (userName.equals(ServiceInfo.getServiceName_1())
                || userName.equals(ServiceInfo.getServiceName_2())
                || userName.equals(ServiceInfo.getServiceName_3())
                || userName.equals(ServiceInfo.getServiceName_4()))
            return false;
        return true;
    }
}
