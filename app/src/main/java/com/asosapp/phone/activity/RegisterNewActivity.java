package com.asosapp.phone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
    EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            Log.e("Leo", "event:" + event + "    result:" + result + "    data:" + data.toString());
            switch (event) {
                case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        ToastView.toast(RegisterNewActivity.this, "验证成功");
                    } else {
                        ToastView.toast(RegisterNewActivity.this,"验证失败");
                    }
                    break;
                case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        ToastView.toast(RegisterNewActivity.this,"获取验证码成功");  //默认的智能验证是开启的,我已经在后台关闭
                    }
                    else{
                        ToastView.toast(RegisterNewActivity.this,"获取验证码失败");
                    }
                    break;
            }
        }

    };
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
        super.onDestroy();
    }
}
