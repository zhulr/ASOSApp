package com.asosapp.phone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.asosapp.phone.R;
import com.asosapp.phone.controller.RegisterController;
import com.asosapp.phone.view.RegisterView;
import com.asosapp.phone.view.ToastView;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by ASOS_lijianfeng on 2016/1/20.
 */
public class RegisterNewActivity extends BaseActivity {
    private RegisterView mRegisterView = null;
    private RegisterController mRegisterController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new);
        mRegisterView = (RegisterView) findViewById(R.id.regist_view);
        mRegisterView.initModule();
        mRegisterController = new RegisterController(mRegisterView,this);
        mRegisterView.setListener(mRegisterController);
        mRegisterView.setListeners(mRegisterController);
        SMSSDK.initSDK(this, "fcb8bfe539c0", "56d9864ccf775584ab58c146dc82f75f");
        mRegisterController.gethandler();
    }

    //注册成功
    public void onRegistSuccess(){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setClass(this, LoginNewActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        mRegisterController.dismissDialog();
        Log.i("RegisterActivity", "onDestroy!");
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
