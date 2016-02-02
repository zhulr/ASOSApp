package com.asosapp.phone.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.asosapp.phone.R;
import com.asosapp.phone.initprogram.MyApplication;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by ASOS_zhulr on 2015/12/7.
 * 新闻启动页
 */
public class UpstartActivity extends BasicActivity {

    private TextView startSkip;
    private ImageView ad;
    private ImageView logo;
    private TimerTask mTask;
    private Timer mTimer = new Timer();
    private long mMills = 3 * 1000;
    //二期开发中isLogin初始值为false
    private Boolean isLogin = true;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upstart);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        sharedPreferences = getSharedPreferences("UserInfo", 1);
//        isLogin = sharedPreferences.getBoolean("isLogin", false);
        init();
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init() {
        ad = (ImageView) findViewById(R.id.upstart_ad);
        logo = (ImageView) findViewById(R.id.upstart_logo);
        startSkip = (TextView) findViewById(R.id.upstart_skip);
        startSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent();
                mTimer.cancel();
                finish();
                return;
            }
        });

        mTask = new TimerTask() {
            @Override
            public void run() {
                if (mMills > 0) {
                    mMills -= 1000;
                } else {
                    intent();
                    mTimer.cancel();
                    finish();
                }
            }
        };
        mTimer.schedule(mTask, 1000, 1000);
    }

    private void intent() {
        if (isLogin)
            startActivity(new Intent(UpstartActivity.this, MainActivity.class));
        else
            startActivity(new Intent(UpstartActivity.this, LoginActivity.class));
    }
}
