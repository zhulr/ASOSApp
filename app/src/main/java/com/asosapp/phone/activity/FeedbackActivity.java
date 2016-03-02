package com.asosapp.phone.activity;

import java.io.File;
import java.lang.ref.WeakReference;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import com.asosapp.phone.R;
import com.asosapp.phone.adapter.MsgListAdapter;
import com.asosapp.phone.initprogram.MyApplication;
import com.asosapp.phone.utils.BitmapLoader;
import com.asosapp.phone.utils.FileHelper;
import com.asosapp.phone.utils.HandleResponseCode;
import com.asosapp.phone.utils.SharePreferenceManager;
import com.asosapp.phone.view.DropDownListView;
import com.asosapp.phone.view.ToastView;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.ImageContent;
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


public class FeedbackActivity extends BaseActivity implements OnClickListener,View.OnTouchListener{
    private String TAG= "FeedbackActivity";
    private Button mBtnSend;
    private Button mBtnBack;
    private EditText mEditTextContent;
    private MsgListAdapter mChatAdapter;
    private DropDownListView mListView;
    private boolean isInputByKeyBoard = true;
    private final MyHandler myHandler = new MyHandler(FeedbackActivity.this);
    Conversation mConv;
    private static final int REFRESH_LAST_PAGE = 1023;
    private static final int UPDATE_CHAT_LISTVIEW = 1026;
    private String mTargetId;
    private String serviceTargetId;
    private TextView titleTV;
    private String nameID;
    InputMethodManager imm;

    private boolean mShowSoftInput = false;
    private ImageButton mAddFileIb;
    private TableLayout mMoreMenuTl;
//    private ImageButton mExpressionIb;//表情
    private String mPhotoPath = null;
    private ImageButton mPickPictureIb;
    private ImageButton mTakePhotoIb;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        JMessageClient.registerEventReceiver(this);
        setContentView(R.layout.activity_feedback);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        initView();

    }



    private void initView() {

        mMoreMenuTl = (TableLayout) findViewById(R.id.more_menu_tl);
        Intent intent = this.getIntent();
        nameID=intent.getStringExtra("nameID");
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
        mTakePhotoIb = (ImageButton) findViewById(R.id.pick_from_camera_btn);
        mTakePhotoIb.setOnClickListener(this);
        mPickPictureIb = (ImageButton) findViewById(R.id.pick_from_local_btn);
        mPickPictureIb.setOnClickListener(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
        mEditTextContent.addTextChangedListener(watcher);
        mEditTextContent.setOnFocusChangeListener(listener);
//        mEditTextContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mEditTextContent.setSingleLine(false);
        mEditTextContent.setOnClickListener(this);
        mEditTextContent.setOnTouchListener(this);
        mEditTextContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mMoreMenuTl.setVisibility(View.GONE);
                    Log.i("ChatView", "dismissMoreMenu()----------");
                }
                return false;
            }
        });
        mEditTextContent.setMaxLines(4);
        mAddFileIb = (ImageButton) findViewById(R.id.add_file_btn);
        mAddFileIb.setOnClickListener(this);

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
        setMoreMenuHeight();
        // 滑动到底部
        setToBottom();
    }
    /**
     * 发送按钮的显示和隐藏
     */
    private TextWatcher watcher = new TextWatcher() {
        private CharSequence temp = "";
        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
            if (temp.length() > 0) {
                mAddFileIb.setVisibility(View.GONE);
                mBtnSend.setVisibility(View.VISIBLE);
            }else {
                mAddFileIb.setVisibility(View.VISIBLE);
                mBtnSend.setVisibility(View.GONE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
            temp = s;
        }

    };

    View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Log.i("ChatView", "Input focus");
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mMoreMenuTl.setVisibility(View.GONE);
                    }
                });
            }
        }
    };


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
////        return imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
//    }


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
                break;
            //图片发送点击事件
            case R.id.add_file_btn:
                //如果在语音输入时点击了添加按钮，则显示菜单并切换到输入框
                if (!isInputByKeyBoard) {
                    isKeyBoard();
                    isInputByKeyBoard = true;
                    mMoreMenuTl.setVisibility(View.VISIBLE);
                } else {
                    //如果弹出软键盘 则隐藏软键盘
                    if (mMoreMenuTl.getVisibility() != View.VISIBLE) {
                        dismissSoftInputAndShowMenu();
                        focusToInput(false);
                        //如果弹出了更多选项菜单，则隐藏菜单并显示软键盘
                    } else {
                        showSoftInputAndDismissMenu();
                    }
                }
                break;
            //拍照
            case R.id.pick_from_camera_btn:
                takePhoto();
                if (mMoreMenuTl.getVisibility() == View.VISIBLE) {
                    mMoreMenuTl.setVisibility(View.GONE);
                }
                break;
            //相册选择
            case R.id.pick_from_local_btn:
                if (mMoreMenuTl.getVisibility() == View.VISIBLE) {
                    mMoreMenuTl.setVisibility(View.GONE);
                }
                Intent intent = new Intent();
                    intent.putExtra(MyApplication.TARGET_ID, serviceTargetId);
                startPickPictureTotalActivity(intent);
                break;
        }
    }

    /**
     * 跳转到选择图片的页面
     * @param intent
     */
    public void startPickPictureTotalActivity(Intent intent) {
        if (!FileHelper.isSdCardExist()) {
            Toast.makeText(this, this.getString(R.string.sdcard_not_exist_toast), Toast.LENGTH_SHORT).show();
        } else {
            intent.setClass(this, PickPictureTotalActivity.class);
            startActivityForResult(intent, MyApplication.REQUEST_CODE_SELECT_PICTURE);
        }
    }

    /**
     *
     * 拍照
     */
    private void takePhoto() {
        if (FileHelper.isSdCardExist()) {
            mPhotoPath = FileHelper.createAvatarPath(null);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPhotoPath)));
            try {
                startActivityForResult(intent, MyApplication.REQUEST_CODE_TAKE_PHOTO);
            } catch (ActivityNotFoundException anf) {
                Toast.makeText(this, getString(R.string.camera_not_prepared),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.sdcard_not_exist_toast),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void focusToInput(boolean inputFocus) {
        if (inputFocus) {
            mEditTextContent.requestFocus();
            Log.i("ChatView", "show softInput");
        } else {
            mAddFileIb.requestFocusFromTouch();
        }
    }


    private void sendMessage() {
        String editTextContent = mEditTextContent.getText().toString();
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

    //如果是文字输入
    public void isKeyBoard() {
//        mSwitchIb.setBackgroundResource(R.drawable.voice);
        mEditTextContent.setVisibility(View.VISIBLE);
//        mVoiceBtn.setVisibility(View.GONE);
//        mExpressionIb.setVisibility(View.GONE);
        if (mEditTextContent.getText().length() > 0) {
            mBtnSend.setVisibility(View.VISIBLE);
            mAddFileIb.setVisibility(View.GONE);
        }else {
            mBtnSend.setVisibility(View.GONE);
            mAddFileIb.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                switch (view.getId()) {
                    case R.id.et_sendmessage:
                        if (mMoreMenuTl.getVisibility() == View.VISIBLE && !mShowSoftInput) {
                            showSoftInputAndDismissMenu();
                            return false;
                        }else {
                            return false;
                        }
                }
                if (mMoreMenuTl.getVisibility() == View.VISIBLE){
                    mMoreMenuTl.setVisibility(View.GONE);
                }else if (mShowSoftInput){
                    View v =getCurrentFocus();
                    if (imm != null && v != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        mShowSoftInput = false;
                    }
                }
                break;
        }
        return false;
    }


/**
 *
 *菜单项的显示
 */
     private void showSoftInputAndDismissMenu() {
         getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                 | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE); // 隐藏软键盘
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mMoreMenuTl.setVisibility(View.INVISIBLE);
        mEditTextContent.requestFocus();
        if (imm != null) {
            imm.showSoftInput(mEditTextContent,
                    InputMethodManager.SHOW_FORCED);//强制显示键盘
        }
        mShowSoftInput = true;
        setMoreMenuHeight();
    }
    /**
     *
     *菜单项的隐藏
     */
    public void dismissSoftInputAndShowMenu() {
        //隐藏软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE); // 隐藏软键盘
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mMoreMenuTl.setVisibility(View.VISIBLE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mEditTextContent.getWindowToken(), 0); //强制隐藏键盘
        }
       setMoreMenuHeight();
        mShowSoftInput = false;
    }


    public void setMoreMenuHeight() {
        int softKeyboardHeight = SharePreferenceManager.getCachedKeyboardHeight();
        if(softKeyboardHeight > 0){
            mMoreMenuTl.setLayoutParams(new LinearLayout
                    .LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, softKeyboardHeight));
        }

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
                }
            }
        });
    }

    /**
     * 用于处理拍照发送图片返回结果以及从其他界面回来后刷新聊天标题
     * 或者聊天消息
     *
     * @param requestCode 请求码
     * @param resultCode  返回码
     * @param data        intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == MyApplication.REQUEST_CODE_TAKE_PHOTO) {
            final Conversation conv = mConv;
            try {
                String originPath = mPhotoPath;
                Bitmap bitmap = BitmapLoader.getBitmapFromFile(originPath, 720, 1280);
                ImageContent.createImageContentAsync(bitmap, new ImageContent.CreateImageContentCallback() {
                    @Override
                    public void gotResult(int status, String desc, ImageContent imageContent) {
                        if (status == 0) {
                            Message msg = conv.createSendMessage(imageContent);
                            Intent intent = new Intent();
                            intent.putExtra(MyApplication.MsgIDs, new int[]{msg.getId()});
//                            if (conv.getType() == ConversationType.group) {
//                                intent.putExtra(JChatDemoApplication.GROUP_ID,
//                                        ((GroupInfo) conv.getTargetInfo()).getGroupID());
//                            } else {
                                intent.putExtra(MyApplication.TARGET_ID,
                                        ((UserInfo) conv.getTargetInfo()).getUserName());
//                            }
                            handleImgRefresh(intent);
                        }
                    }
                });
            }  catch (NullPointerException e) {
                Log.i(TAG, "onActivityResult unexpected result");
            }
        } else if (resultCode == MyApplication.RESULT_CODE_SELECT_PICTURE) {
            handleImgRefresh(data);
        } else if (resultCode == MyApplication.RESULT_CODE_CHAT_DETAIL) {
//            if (mChatController.isGroup()) {
//                GroupInfo groupInfo = (GroupInfo) mChatController.getConversation().getTargetInfo();
//                UserInfo userInfo = groupInfo.getGroupMemberInfo(JMessageClient.getMyInfo().getUserName());
//                //如果自己在群聊中，同时显示群人数
//                if (userInfo != null) {
//                    if (TextUtils.isEmpty(data.getStringExtra(JChatDemoApplication.NAME))) {
//                        mChatView.setChatTitle(this.getString(R.string.group),
//                                data.getIntExtra("currentCount", 0));
//                    } else {
//                        mChatView.setChatTitle(data.getStringExtra(JChatDemoApplication.NAME),
//                                data.getIntExtra("currentCount", 0));
//                    }
//                } else {
//                    if (TextUtils.isEmpty(data.getStringExtra(JChatDemoApplication.NAME))) {
//                        mChatView.setChatTitle(this.getString(R.string.group));
//                        mChatView.dismissGroupNum();
//                    } else {
//                        mChatView.setChatTitle(data.getStringExtra(JChatDemoApplication.NAME));
//                        mChatView.dismissGroupNum();
//                    }
//                }
//
//            } else mChatView.setChatTitle(data.getStringExtra(JChatDemoApplication.NAME));
            if (data.getBooleanExtra("deleteMsg", false)) {
                mChatAdapter.clearMsgList();
            }
        }
//        else if (resultCode == MyApplication.RESULT_CODE_FRIEND_INFO) {
//            if (!mChatController.isGroup()) {
//                String nickname = data.getStringExtra(JChatDemoApplication.NICKNAME);
//                if (nickname != null) {
//                    mChatView.setChatTitle(nickname);
//                }
//            }
//        }
    }
    /**
     * 处理发送图片，刷新界面
     *
     * @param data intent
     */
    private void handleImgRefresh(Intent data) {
        String targetId = data.getStringExtra(MyApplication.TARGET_ID);
        Log.e("LEO--", targetId);
        //判断是否在当前会话中发图片
        if (targetId != null) {
            if (targetId.equals(serviceTargetId)) {
                mChatAdapter.setSendImg(targetId, data.getIntArrayExtra(MyApplication.MsgIDs));
                setToBottom();
            }
        }
    }


}

