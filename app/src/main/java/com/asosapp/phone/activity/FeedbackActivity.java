package com.asosapp.phone.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.asosapp.phone.R;
import com.asosapp.phone.adapter.MsgListAdapter;
import com.asosapp.phone.bean.FeedbackEntity;
import com.asosapp.phone.utils.HandleResponseCode;
import com.asosapp.phone.view.DropDownListView;

import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import de.greenrobot.event.EventBus;


public class FeedbackActivity extends BaseActivity implements OnClickListener{
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
        Log.e("Leo--->",serviceTargetId+"="+mTargetId);
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

    /**
     * 接收消息类事件
     *
     * @param event 消息事件
     */
    public void onEvent(MessageEvent event) {
        final Message msg = event.getMessage();
        //若为群聊相关事件，如添加、删除群成员
        Log.i(TAG, event.getMessage().toString());
        if (msg.getContentType() == ContentType.eventNotification) {
            GroupInfo groupInfo = (GroupInfo) msg.getTargetInfo();
            long groupID = groupInfo.getGroupID();
            UserInfo myInfo = JMessageClient.getMyInfo();
            EventNotificationContent.EventNotificationType type = ((EventNotificationContent) msg
                    .getContent()).getEventNotificationType();
//            if (groupID == mChatController.getGroupId()) {
//                switch (type) {
//                    case group_member_added:
//                        //添加群成员事件
//                        List<String> userNames = ((EventNotificationContent) msg.getContent()).getUserNames();
//                        //群主把当前用户添加到群聊，则显示聊天详情按钮
//                        refreshGroupNum();
//                        if (userNames.contains(myInfo.getNickname()) || userNames.contains(myInfo.getUserName())) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mChatView.showRightBtn();
//                                }
//                            });
//                        }
//
//                        break;
//                    case group_member_removed:
//                        //删除群成员事件
//                        userNames = ((EventNotificationContent) msg.getContent()).getUserNames();
//                        //群主删除了当前用户，则隐藏聊天详情按钮
//                        if (userNames.contains(myInfo.getNickname()) || userNames.contains(myInfo.getUserName())) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mChatView.dismissRightBtn();
//                                    GroupInfo groupInfo = (GroupInfo) mChatController.getConversation()
//                                            .getTargetInfo();
//                                    if (TextUtils.isEmpty(groupInfo.getGroupName())) {
//                                        mChatView.setChatTitle(ChatActivity.this.getString(R.string.group));
//                                    } else {
//                                        mChatView.setChatTitle(groupInfo.getGroupName());
//                                    }
//                                    mChatView.dismissGroupNum();
//                                }
//                            });
//                        } else {
//                            refreshGroupNum();
//                        }
//
//                        break;
//                    case group_member_exit:
//                        refreshGroupNum();
//                        break;
//                }
//            }
        }
        //刷新消息
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //收到消息的类型为单聊
                if (msg.getTargetType().equals(ConversationType.single)) {
                    String targetID = ((UserInfo) msg.getTargetInfo()).getUserName();
                    //判断消息是否在当前会话中
                    if (targetID.equals(serviceTargetId)) {
                        Message lastMsg = mChatAdapter.getLastMsg();
                        if (lastMsg == null || msg.getId() != lastMsg.getId()) {
                            mChatAdapter.addMsgToList(msg);
                        } else {
                            mChatAdapter.notifyDataSetChanged();
                        }
                    }
//                } else {
//                    long groupID = ((GroupInfo)msg.getTargetInfo()).getGroupID();
//                    if (mChatController.isGroup() && groupID == mChatController.getGroupId()) {
//                        Message lastMsg = mChatAdapter.getLastMsg();
//                        if (lastMsg == null || msg.getId() != lastMsg.getId()) {
//                            mChatAdapter.addMsgToList(msg);
//                        } else {
//                            mChatAdapter.notifyDataSetChanged();
//                        }
//                    }
                }
            }
        });
    }




}

