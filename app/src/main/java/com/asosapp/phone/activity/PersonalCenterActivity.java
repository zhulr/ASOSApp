package com.asosapp.phone.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.asosapp.phone.R;

/**
 * Created by ASOS_zhulr on 2015/12/11.
 * 个人中心
 */
public class PersonalCenterActivity extends Activity implements View.OnClickListener {

    private View myCollectView;
    private View myInformationView;
    private View updateView;
    private View logOutView;
    private ToggleButton isPush;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        sharedPreferences = getSharedPreferences("UserInfo", 1);
        init();
    }

    private void init() {
        myCollectView = findViewById(R.id.my_collect);
        myInformationView = findViewById(R.id.my_information);
        updateView = findViewById(R.id.update);
        logOutView = findViewById(R.id.log_out);
        isPush = (ToggleButton) findViewById(R.id.ispush);
        myCollectView.setOnClickListener(this);
        myInformationView.setOnClickListener(this);
        updateView.setOnClickListener(this);
        logOutView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_collect:
                toast("暂未开放！");
                break;
            case R.id.my_information:
                toast("暂时无法查看及修改个人信息！");
                break;
            case R.id.update:
                toast("已经是最新版本，不需要更新！");
                break;
            case R.id.log_out:
                logOut();
                break;
        }
    }

    private void logOut() {
        edit = sharedPreferences.edit();
        edit.putBoolean("isLogin", false);
        edit.commit();
        intent();
    }

    private void intent() {
        if (false) {
            return;
        }
        if (true) {
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
    }

    //输出屏幕信息
    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }
}
