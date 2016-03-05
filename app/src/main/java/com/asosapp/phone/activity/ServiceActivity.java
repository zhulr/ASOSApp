package com.asosapp.phone.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.asosapp.phone.R;
import com.asosapp.phone.adapter.ConversationListAdapter;
import com.asosapp.phone.utils.Event;
import com.asosapp.phone.utils.HandleResponseCode;
import com.asosapp.phone.utils.SortConvList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import de.greenrobot.event.EventBus;

/**
 * Created by Leo on 2016/1/27.
 */
public class ServiceActivity extends BaseActivity implements View.OnClickListener{
    private ListView chatList;
    private Button btn_back;
    private TextView chat_title;
    private List<Conversation> mListView;
    private Conversation mConv;
    private ConversationListAdapter mListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JMessageClient.registerEventReceiver(this);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_service);
        init();
        data();

    }

    /**
     *
     * 数据设置
     */
    private void data() {
//        if (mConv == null) {
//            mConv = Conversation.createSingleConversation("123456");
//            UserInfo userInfo = (UserInfo)mConv.getTargetInfo();
//            if (TextUtils.isEmpty(userInfo.getNickname())) {
//                chat_title.setText(userInfo.getUserName());
//            }else {
//                chat_title.setText(userInfo.getNickname());
//            }
//        }
        List<String> t=new ArrayList<String>();
//
            for (int i=0;i<1;i++){
//                getUserInfo(t.get(i).toString());
            }


    }
    @Override
    public void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    /**
     * 添加用户
     * @param targetId
     */
    private void getUserInfo(final String targetId){
        JMessageClient.getUserInfo(targetId, new GetUserInfoCallback() {
            @Override
            public void gotResult(final int status, String desc, final UserInfo userInfo) {
//                mLoadingDialog.dismiss();
                if (status == 0) {
                    Conversation conv = Conversation.createSingleConversation(targetId);
                    if (!TextUtils.isEmpty(userInfo.getAvatar())) {
                        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                            @Override
                            public void gotResult(int status, String desc, Bitmap bitmap) {
                                if (status == 0) {
                                    mListAdapter.notifyDataSetChanged();
                                } else {
                                    HandleResponseCode.onHandle(ServiceActivity.this, status, false);
                                }
                            }
                        });
                        mListAdapter.setToTop(conv);
                    } else {
                        mListAdapter.setToTop(conv);
                    }
//                    mAddFriendDialog.cancel();
                } else {
                    HandleResponseCode.onHandle(ServiceActivity.this, status, true);
                }
            }
        });
    }

    /**
     *
     * 初始化控件
     */
    private void init() {
        chatList= (ListView) findViewById(R.id.chat_list);
        btn_back= (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        chat_title= (TextView) findViewById(R.id.chat_title_type);
        mListView=new ArrayList<Conversation>();
        mListView = JMessageClient.getConversationList();
        if (mListView.size() > 1) {
            SortConvList sortList = new SortConvList();
            Collections.sort(mListView, sortList);
        }

        mListAdapter = new ConversationListAdapter(ServiceActivity.this, mListView,250);
        chatList.setAdapter(mListAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        mListAdapter.notifyDataSetChanged();
        super.onResume();
    }

    /**
     * 在会话列表中接收消息
     *
     * @param event 消息事件
     */

    public void onEvent(MessageEvent event) {
        Message msg = event.getMessage();
        Log.d("JMessage", "收到消息：msg = " + msg.toString());
            final UserInfo userInfo = (UserInfo) msg.getTargetInfo();
            final String targetID = userInfo.getUserName();
            final Conversation conv = JMessageClient.getSingleConversation(targetID);
            if (conv != null) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //如果设置了头像
                        if (!TextUtils.isEmpty(userInfo.getAvatar())){
                            //如果本地不存在头像
                            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                                @Override
                                public void gotResult(int status, String desc, Bitmap bitmap) {
                                    if (status == 0) {
                                        mListAdapter.notifyDataSetChanged();
                                    } else {
                                        HandleResponseCode.onHandle(getApplicationContext(), status, false);
                                    }
                                }
                            });
                        }
                    }
                });
                mListAdapter.setToTop(conv);
            }
        }

    /**
     * 收到创建单聊的消息
     *
     * @param event 可以从event中得到targetID
     */
    public void onEventMainThread(Event.StringEvent event) {
        String targetID = event.getTargetID();
        Conversation conv = JMessageClient.getSingleConversation(targetID);
        if (conv != null) {
            mListAdapter.addNewConversation(conv);
        }
    }

    }

