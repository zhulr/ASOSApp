package com.asosapp.phone.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;

import com.asosapp.phone.R;
import com.asosapp.phone.adapter.ConversationListAdapter;
import com.asosapp.phone.adapter.MsgListAdapter;
import com.asosapp.phone.bean.FeedbackEntity;
import com.asosapp.phone.initprogram.MyApplication;
import com.asosapp.phone.utils.BitmapLoader;
import com.asosapp.phone.utils.HandleResponseCode;
import com.asosapp.phone.view.DropDownListView;

import android.widget.TextView;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import de.greenrobot.event.EventBus;


public class FeedbackActivity extends BaseActivity implements OnClickListener {
    private String TAG= "FeedbackActivity";
    private Button mBtnSend;
    private Button mBtnBack;
    private EditText mEditTextContent;
    private MsgListAdapter mChatAdapter;
    private DropDownListView mListView;
    private final MyHandler myHandler = new MyHandler(FeedbackActivity.this);
    Conversation mConv;
    private static final int REFRESH_LAST_PAGE = 1023;
    private static final int UPDATE_CHAT_LISTVIEW = 1026;
    private String mTargetId;
    private String serviceTargetId;
    private TextView titleTV;
    private List<FeedbackEntity> mDataArrays = new ArrayList<FeedbackEntity>();
    private String nameID;
    InputMethodManager imm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        JMessageClient.registerEventReceiver(this);
        setContentView(R.layout.activity_feedback);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();

    }



    private void initView() {

//        mEditTextContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        Intent intent = this.getIntent();
        nameID=intent.getStringExtra("nameID");
//        if (nameID=="1"){
//            serviceTargetId = intent.getStringExtra("service");
//            mTargetId = intent.getStringExtra("myID");
//        }else{
        serviceTargetId = intent.getStringExtra("service");
        mTargetId = intent.getStringExtra("myID");
//        }


        mListView = (DropDownListView) findViewById(R.id.listview);
        this.mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

            }
        });
        titleTV = (TextView) findViewById(R.id.chat_title_type);
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);

        if (mConv == null) {

            mConv = Conversation.createSingleConversation(serviceTargetId);
            UserInfo userInfo = (UserInfo) mConv.getTargetInfo();
            if (TextUtils.isEmpty(userInfo.getNickname())) {
                if (nameID=="1"){
                    titleTV.setText(userInfo.getUserName());
                }else{
                    setTitle();
                }

            } else {
                titleTV.setText(userInfo.getNickname());
            }
        }
        if (mConv != null) {
            mChatAdapter = new MsgListAdapter(this, serviceTargetId);
            mListView.setAdapter(mChatAdapter);
            //监听下拉刷新
            mListView.setOnDropDownListener(new DropDownListView.OnDropDownListener() {
                @Override
                public void onDropDown() {
                    myHandler.sendEmptyMessageDelayed(REFRESH_LAST_PAGE, 1000);
                }
            });
        }
        // 滑动到底部
        setToBottom();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
    @Override
    public boolean onTouchEvent(android.view.MotionEvent event) {

        return imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.btn_send:
                sendMessage();
                break;
            case R.id.btn_back:
                mConv.resetUnreadCount();
                JMessageClient.exitConversaion();
                FeedbackActivity.this.finish();
        }
    }

    private void sendMessage() {
        String editTextContent = mEditTextContent.getText().toString();
        Log.e("Leo-->", mEditTextContent.getText().toString());

        mEditTextContent.setText("");
        if (editTextContent.equals("")) {
            return;
        }
        TextContent content = new TextContent(editTextContent);
        final Message msg = mConv.createSendMessage(content);
        msg.setOnSendCompleteCallback(new BasicCallback() {

            @Override
            public void gotResult(final int status, String desc) {
                Log.i("ChatController", "send callback " + status + " desc " + desc);
                if (status == 803008) {
                    CustomContent customContent = new CustomContent();
                    customContent.setBooleanValue("blackList", true);
                    Message customMsg = mConv.createSendMessage(customContent);
                    mChatAdapter.addMsgToList(customMsg);
                } else if (status != 0) {
                    HandleResponseCode.onHandle(FeedbackActivity.this, status, false);
                }
                // 发送成功或失败都要刷新一次
                myHandler.sendEmptyMessage(UPDATE_CHAT_LISTVIEW);
            }
        });
        mChatAdapter.addMsgToList(msg);
        JMessageClient.sendMessage(msg);
        // 滑动到底部
        setToBottom();
        imm.hideSoftInputFromWindow(mEditTextContent.getWindowToken(), 0); //强制隐藏键盘
    }



    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    private class MyHandler extends Handler {
        private final WeakReference<FeedbackActivity> mController;

        public MyHandler(FeedbackActivity controller) {
            mController = new WeakReference<FeedbackActivity>(controller);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            FeedbackActivity controller = mController.get();
            if (controller != null) {
                switch (msg.what) {
                    case REFRESH_LAST_PAGE:
                        controller.mChatAdapter.dropDownToRefresh();

                        mListView.onDropDownComplete();
                        if (controller.mChatAdapter.isHasLastPage()) {
                            mListView.setSelection(controller.mChatAdapter.getOffset());
                            controller.mChatAdapter.refreshStartPosition();
                        } else {
                            mListView.setSelection(0);
                        }
                        mListView.setOffset(controller.mChatAdapter.getOffset());
                        break;
                    case UPDATE_CHAT_LISTVIEW:
                        controller.mChatAdapter.notifyDataSetChanged();
                        break;
                }

            }
        }
    }
    private void setToBottom(){
        // 滑动到底部
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mListView.setSelection(mListView.getBottom());
            }
        });
    }

    @Override
    protected void onResume() {
        String targetID = getIntent().getStringExtra("myID");

        if (null != targetID) {
            JMessageClient.enterSingleConversaion(targetID);
        }
        super.onResume();
    }

    private void setTitle(){
        if (serviceTargetId.equals("asos111"))
            titleTV.setText("医疗康复在线客服");
        else if (serviceTargetId.equals("asos222"))
            titleTV.setText("法律服务问答客服");
        else if (serviceTargetId.equals("asos333"))
            titleTV.setText("伤残鉴定咨询客服");
        else if (serviceTargetId.equals("asos444"))
            titleTV.setText("损伤赔偿问答客服");
    }


}

