package com.asosapp.phone.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.asosapp.phone.R;

/**
 * Created by ASOS_zhulr on 2015/12/11.
 * 卡详情介绍页面
 */
public class CardsDetailsActivity extends Activity {
    private Button btnBuy;
    private boolean isLogin;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        sharedPreferences = getSharedPreferences("UserInfo", 1);
        isLogin = sharedPreferences.getBoolean("isLogin", false);
        init();
    }

    private void init() {
        btnBuy = (Button) findViewById(R.id.buy);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin)
                    Toast.makeText(getApplication(), "购买成功", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplication(), "请先登录", Toast.LENGTH_LONG).show();
            }
        });
    }
}
