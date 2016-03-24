package com.asosapp.phone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.asosapp.phone.R;
import com.asosapp.phone.initprogram.MyApplication;

/**
 * Created by Leo on 2016/3/24.
 * 点击进入查看聊天记录
 */
public class ChatDetailActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout mLayout;
    private Button btn_back;
    private String mTargetId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_chatdetail);
        Intent intent=getIntent();
        mTargetId=intent.getStringExtra(MyApplication.TARGET_ID);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        mLayout= (LinearLayout) findViewById(R.id.group_chat_record_ll);
        btn_back= (Button) findViewById(R.id.btn_back);
        listener();

    }

    /**
     * 初始化点击事件
     */
    private void listener() {
        mLayout.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.group_chat_record_ll:
                Intent intent=new Intent(ChatDetailActivity.this,ChatNewActivity.class);
                intent.putExtra(MyApplication.TARGET_ID,mTargetId);
                startActivity(intent);
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
        }

    }
}
