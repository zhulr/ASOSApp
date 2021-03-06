package com.asosapp.phone.fragment;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.asosapp.phone.R;
import com.asosapp.phone.activity.CardsDetailsActivity;
import com.asosapp.phone.activity.CouponActivity;
import com.asosapp.phone.activity.LoginNewActivity;
import com.asosapp.phone.activity.MainActivity;
import com.asosapp.phone.activity.MyQRActivity;
import com.asosapp.phone.activity.PersonInformationActivity;
import com.asosapp.phone.activity.SetActivity;
import com.asosapp.phone.activity.UserFeedbackActivity;
import com.asosapp.phone.bean.UserInfo;
import com.asosapp.phone.view.RoundImage;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ASOS_zhulr on 2016/2/15.
 * 个人中心
 */
public class SetCenterFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView titleName;
    private RoundImage userHead;
    private TextView userName;
    private TextView isVIP;
    private TextView isSVIP;
    private View orderView;
    private View messageView;
    private View accountView;
    private View myAsosView;
    private View myCouponView;
    private View inviteView;
    private View userFeedbackView;
    private View setView;
    private View outLogView;
    private ToggleButton isPush;
    private boolean is_push;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserInfo", 1);
        is_push = sharedPreferences.getBoolean("is_push", true);
        view = inflater.inflate(R.layout.fragment_set_center, container, false);
        init();

        initData();
        return view;
    }


    private void init() {
        titleName = (TextView) view.findViewById(R.id.title_name);
        userHead = (RoundImage) view.findViewById(R.id.user_head_img);
        userName = (TextView) view.findViewById(R.id.user_name);
        isVIP = (TextView) view.findViewById(R.id.isvip);
        isSVIP = (TextView) view.findViewById(R.id.issvip);
        orderView = view.findViewById(R.id.view_order);
        messageView = view.findViewById(R.id.view_message);
        accountView = view.findViewById(R.id.view_account);
        myAsosView = view.findViewById(R.id.view_my_asos);
        myCouponView = view.findViewById(R.id.view_my_coupon);
        inviteView = view.findViewById(R.id.view_invite_friends);
        userFeedbackView = view.findViewById(R.id.view_user_feedback);
        setView = view.findViewById(R.id.view_set);
        outLogView = view.findViewById(R.id.view_out_login);
        isPush = (ToggleButton) view.findViewById(R.id.ispush);
        userName.setOnClickListener(this);
        userHead.setOnClickListener(this);
        isVIP.setOnClickListener(this);
        isSVIP.setOnClickListener(this);
        orderView.setOnClickListener(this);
        messageView.setOnClickListener(this);
        accountView.setOnClickListener(this);
        myAsosView.setOnClickListener(this);
        myCouponView.setOnClickListener(this);
        inviteView.setOnClickListener(this);
        userFeedbackView.setOnClickListener(this);
        setView.setOnClickListener(this);
        outLogView.setOnClickListener(this);
        isPush.setChecked(is_push);
        isPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    JPushInterface.resumePush(getActivity().getApplicationContext());
                    JPushInterface.getRegistrationID(getActivity().getApplicationContext());
                    Toast.makeText(getActivity(), "开启推送", Toast.LENGTH_SHORT).show();
                } else {
                    JPushInterface.stopPush(getActivity().getApplicationContext());
                    Toast.makeText(getActivity(), "关闭推送", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserInfo", 1);
        titleName.setText(R.string.mine);
        userName.setText(sharedPreferences.getString("user_name","安邦医疗救援"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_head_img:
                break;
            case R.id.isvip:
                break;
            case R.id.issvip:
                break;
            case R.id.view_order:
                break;
            case R.id.view_message:
                break;
            case R.id.view_account:
                break;
            case R.id.view_my_asos:
                Toast.makeText(getActivity(), "功能临时关闭", Toast.LENGTH_SHORT).show();
                break;
            case R.id.view_my_coupon:
                intent(4);
                break;
            case R.id.view_invite_friends:
                intent(3);
                break;
            case R.id.view_user_feedback:
                intent(2);
                break;
            case R.id.view_set:
                intent(1);
                break;
            case R.id.view_out_login:
                NotificationManager manager = (NotificationManager) this.getActivity().getApplicationContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancelAll();
                UserInfo.instance().logOut(getActivity());
                intent(99);
                Toast.makeText(getActivity(), "已退出登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.user_name:
                intent(0);
                break;
            default:
                break;
        }

    }

    private void intent(int CLASS) {
        Intent intent = new Intent();
        switch (CLASS) {
            case 0:
                intent.setClass(getActivity(), PersonInformationActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent.setClass(getActivity(), SetActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent.setClass(getActivity(), UserFeedbackActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent.setClass(getActivity(), MyQRActivity.class);
                startActivity(intent);
                break;
            case 4:
//                Toast.makeText(getActivity(),"功能临时关闭请谅解！",Toast.LENGTH_SHORT).show();
                intent.setClass(getActivity(), CouponActivity.class);
                startActivity(intent);
                break;
            case 5:
                break;
            case 6:
                break;
            case 99:
                intent.setClass(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
        }

    }
}
