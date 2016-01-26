package com.asosapp.phone.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.asosapp.phone.R;
import com.asosapp.phone.controller.RegisterController;
import com.asosapp.phone.view.RegisterView;

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
    }

    //×¢²á³É¹¦
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
        super.onDestroy();
    }
}
