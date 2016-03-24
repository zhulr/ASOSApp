package com.asosapp.phone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.asosapp.phone.R;
import com.asosapp.phone.adapter.MsgListAdapter;
import com.asosapp.phone.initprogram.MyApplication;
import com.asosapp.phone.view.DropDownListView;

import java.lang.ref.WeakReference;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;

/**
 * Created by Leo on 2016/3/24.
 * 聊天记录
 */
public class ChatNewActivity extends BaseActivity implements View.OnClickListener{
    private final MyHandler myHandler = new MyHandler(ChatNewActivity.this);
    private static final int REFRESH_LAST_PAGE = 1023;
    private static MsgListAdapter msgListAdapter;
    private static DropDownListView mList;
    private String mTargetId;
    private Conversation conv = null;
    private Button btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatnew);
        init();

    }

    /**
     * 初始化控件
     */
    private void init() {
        btn_back= (Button) findViewById(R.id.btn_back);
        mList= (DropDownListView) findViewById(R.id.chat_list);
        Intent intent = getIntent();
        mTargetId = intent.getStringExtra(MyApplication.TARGET_ID);
        conv = JMessageClient.getSingleConversation(mTargetId);
        msgListAdapter=new MsgListAdapter(ChatNewActivity.this,mTargetId);
        mList.setAdapter(msgListAdapter);
        //监听下拉刷新
        mList.setOnDropDownListener(new DropDownListView.OnDropDownListener() {
            @Override
            public void onDropDown() {
                myHandler.sendEmptyMessageDelayed(REFRESH_LAST_PAGE, 1000);
            }
        });
        setToBottom();
        listener();

    }

    /**
     * 初始化点击事件
     */
    private void listener() {
        btn_back.setOnClickListener(this);
    }

    /**
     * 返回到底部
     */
    public void setToBottom() {
        mList.post(new Runnable() {
            @Override
            public void run() {
                mList.setSelection(mList.getBottom());
            }
        });
    }

    /**
     *
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
        }

    }

    /**
     * 下拉刷新
     */
    private static class MyHandler extends Handler {

        private final WeakReference<ChatNewActivity> mController;

        public MyHandler(ChatNewActivity controller) {
            mController = new WeakReference<ChatNewActivity>(controller);
        }


        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_LAST_PAGE:
                    msgListAdapter.dropDownToRefresh();
                    mList.onDropDownComplete();
                    if (msgListAdapter.isHasLastPage()) {
                        mList.setSelection(msgListAdapter.getOffset());
                        msgListAdapter.refreshStartPosition();
                    } else {
                        mList.setSelection(0);
                    }
                    mList.setOffset(msgListAdapter.getOffset());
                    break;
            }
        }
    }
}



