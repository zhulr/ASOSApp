package com.asosapp.phone.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asosapp.phone.R;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by ASOS_zhulr on 2015/12/7.
 * 新闻启动页
 */
public class UpstartActivity extends BasicActivity {
    private TimerTask mTask;
    private Timer mTimer = new Timer();
    private long mMills = 2 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upstart);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        init();
        mTimer.schedule(mTask, 0, 1000);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init() {
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
    }

    private void intent() {
        startActivity(new Intent(UpstartActivity.this, MainActivity.class));
    }
}
