package com.asosapp.phone.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.asosapp.phone.R;
import com.asosapp.phone.bean.ServiceInfo;
import com.asosapp.phone.controller.LoginController;
import com.asosapp.phone.utils.HandleResponseCode;
import com.asosapp.phone.view.LoginView;
import com.asosapp.phone.view.ToastView;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by ASOS_lijianfeng on 2016/1/20.
 */
public class LoginNewActivity extends BaseActivity {
    private LoginView mLoginView = null;
    private LoginController mLoginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        mLoginView = (LoginView) findViewById(R.id.login_view);
        mLoginView.initModule();
        mLoginController = new LoginController(mLoginView, this);
        mLoginView.setListener(mLoginController);
        mLoginView.setListeners(mLoginController);
        mLoginView.setOnCheckedChangeListener(mLoginController);
        Intent intent = this.getIntent();
        mLoginView.isShowReturnBtn(intent.getBooleanExtra("fromSwitch", false));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public Context getContext() {
        return this;
    }


    public void startRegisterActivity() {
        Intent intent = new Intent();
        intent.setClass(this, RegisterNewActivity.class);
        startActivity(intent);
    }

    public void startMainActivity() {
        long id = JMessageClient.getMyInfo().getUserID();
        ToastView.toast(LoginNewActivity.this, String.valueOf(id));
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (isService()) {
            intent.setClass(getContext(), ServiceActivity.class);
        } else {
            finish();
        }
        startActivity(intent);
    }

    /**
     * 判断是不是客服
     */
    private boolean isService() {
        String userName = mLoginView.getResult();
        if (userName.equals(ServiceInfo.getServiceName_1())
                || userName.equals(ServiceInfo.getServiceName_2())
                || userName.equals(ServiceInfo.getServiceName_3())
                || userName.equals(ServiceInfo.getServiceName_4()))
            return true;
        return false;
    }
}
