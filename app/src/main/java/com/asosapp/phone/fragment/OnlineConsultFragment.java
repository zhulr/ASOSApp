package com.asosapp.phone.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asosapp.phone.R;
import com.asosapp.phone.activity.FeedbackActivity;
import com.asosapp.phone.activity.LoginNewActivity;
import com.asosapp.phone.activity.ServiceActivity;
import com.asosapp.phone.bean.ServiceInfo;
import com.asosapp.phone.initprogram.MyApplication;
import com.asosapp.phone.utils.Const;
import com.asosapp.phone.view.SlideShowView;
import com.asosapp.phone.view.ToastView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import de.greenrobot.event.EventBus;

/**
 * Created by ASOS_zhulr on 2015/12/7.
 */
public class OnlineConsultFragment extends BaseFragment implements View.OnClickListener {

    private static final Object TAG = "OnlineConsultFragment";
    private View view;
    private View online_1, online_2, online_3, online_4;
    private View calculator_1;
    private boolean isLogin = false;
    private TextView titleName;
    private ImageView asos111_pic, asos222_pic, asos333_pic, asos444_pic;
    //客服ID
    private String serviceID = "";
    //客户ID
    private String mID = "";
    SharedPreferences sharedPreferences;
    private Activity mContext;
    private List<Conversation> mListView;
    private Conversation convItem;
    private static JSONObject DATA = null;

    private SlideShowView slideshowView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_online_consult, container, false);
        mContext = this.getActivity();
        JMessageClient.registerEventReceiver(this);
        sharedPreferences = getActivity().getSharedPreferences("UserInfo", 1);
        init();
        return view;
    }


    private void init() {
        online_1 = view.findViewById(R.id.asos111);
        online_2 = view.findViewById(R.id.asos222);
        online_3 = view.findViewById(R.id.asos333);
        online_4 = view.findViewById(R.id.asos444);
        calculator_1 = view.findViewById(R.id.asos555);
        asos111_pic = (ImageView) view.findViewById(R.id.asos111_pic);
        asos222_pic = (ImageView) view.findViewById(R.id.asos222_pic);
        asos333_pic = (ImageView) view.findViewById(R.id.asos333_pic);
        asos444_pic = (ImageView) view.findViewById(R.id.asos444_pic);
        slideshowView = (SlideShowView) view.findViewById(R.id.slideshowView);
        titleName = (TextView) view.findViewById(R.id.title_name);
        titleName.setText(R.string.mess);
        online_1.setOnClickListener(this);
        online_2.setOnClickListener(this);
        online_3.setOnClickListener(this);
        online_4.setOnClickListener(this);
        calculator_1.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
//        final Conversation conv = mChatController.getConversation();
        isLogin = sharedPreferences.getBoolean("isLogin", false);
        switch (v.getId()) {
            case R.id.asos111:
                serviceID = ServiceInfo.getServiceName_5();
                isSelfLogin();
                break;
            case R.id.asos222:
                serviceID = ServiceInfo.getServiceName_5();
                isSelfLogin();
                break;
            case R.id.asos333:
                serviceID = ServiceInfo.getServiceName_5();
                isSelfLogin();
                break;
            case R.id.asos444:
                serviceID = ServiceInfo.getServiceName_5();
                isSelfLogin();
                break;
            case R.id.asos555:

                break;
        }
    }


    //提示框
    protected void dialog() {
        Dialog alertDialog = new AlertDialog.Builder(getActivity()).
                setTitle("提示").
                setMessage("您还没有登录，请登录！").
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
        Intent intent = new Intent();
        if (isService()) {
            intent.putExtra("service", serviceID);
            intent.putExtra("myID", mID);
            intent.putExtra("nameID", "0");
            intent.setClass(getActivity(), FeedbackActivity.class);
        } else {
            intent.setClass(getActivity(), ServiceActivity.class);
        }
        startActivity(intent);
    }


    private boolean isService() {
        String userName = JMessageClient.getMyInfo().getUserName();
        if (userName.equals(ServiceInfo.getServiceName_1())
                || userName.equals(ServiceInfo.getServiceName_2())
                || userName.equals(ServiceInfo.getServiceName_3())
                || userName.equals(ServiceInfo.getServiceName_4())
                || userName.equals(ServiceInfo.getServiceName_5()))
            return false;
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
