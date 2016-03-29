package com.asosapp.phone.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asosapp.phone.R;
import com.asosapp.phone.activity.LoginNewActivity;
import com.asosapp.phone.activity.RegisterNewActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.smssdk.SMSSDK;


public class RegisterView extends LinearLayout {

    private EditText mUserId;
    private EditText mPassword;
    private Button mRegistBtn;
    private ImageButton mReturnBtn;
    private Listener mListener;
    private Context mContext;
    private EditText mSurePsw;
    private EditText mName;
    private TextView mSexy;
    private EditText mAge;
    private EditText mCode;
    private TextView code_button;
    private EditText invite_code;


    public RegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void initModule() {
        // 获取注册所用的用户名、密码、昵称
        mUserId = (EditText) findViewById(R.id.username);
        mUserId.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        mPassword = (EditText) findViewById(R.id.password);
        mRegistBtn = (Button) findViewById(R.id.regist_btn);
        mReturnBtn = (ImageButton) findViewById(R.id.return_btn);
        mSurePsw = (EditText) findViewById(R.id.passwordsure);
        mName = (EditText) findViewById(R.id.name);
        mAge = (EditText) findViewById(R.id.age);
        mCode = (EditText) findViewById(R.id.code);
        mSexy = (TextView) findViewById(R.id.sexy);
        code_button = (TextView) findViewById(R.id.code_button);
        invite_code = (EditText) findViewById(R.id.invite_code);
        mUserId.requestFocus();
    }


    public TextView setcode_button() {
        return code_button;
    }

    public String getUserId() {
        return mUserId.getText().toString().trim();
    }

    public String getinviteCode() {
        return invite_code.getText().toString().trim();
    }

    public String getPassword() {
        return mPassword.getText().toString().trim();
    }

    public String getPasswordSure() {
        return mSurePsw.getText().toString().trim();
    }

    public String getUserNickName() {
        return mName.getText().toString().trim();
    }

    public String getUserName() {
        try {
            return URLEncoder.encode(mName.getText().toString().trim(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "ASOS";
    }

    public String getUserAge() {
        return mAge.getText().toString().trim();
    }

    public String getCode() {
        return mCode.getText().toString().trim();
    }

    public String getSexy() {
        try {
            return URLEncoder.encode(mSexy.getText().toString().trim(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "男";
    }


    public void setListeners(OnClickListener onclickListener) {
        mRegistBtn.setOnClickListener(onclickListener);
        mReturnBtn.setOnClickListener(onclickListener);
        mSexy.setOnClickListener(onclickListener);
        code_button.setOnClickListener(onclickListener);
    }

    public void isMobileError(Context context) {
        Toast.makeText(context, context.getString(R.string.moblie_not_null_toast), Toast.LENGTH_SHORT).show();
    }

    public void userNameError(Context context) {
        Toast.makeText(context, context.getString(R.string.username_not_null_toast), Toast.LENGTH_SHORT).show();
    }

    public void passwordError(Context context) {
        Toast.makeText(context, context.getString(R.string.password_not_null_toast), Toast.LENGTH_SHORT).show();
    }

    public void passwordSureError(Context context) {
        Toast.makeText(context, context.getString(R.string.passwordsure_not_null_toast), Toast.LENGTH_SHORT).show();
    }

    public void passwordLengthError(RegisterNewActivity context) {
        Toast.makeText(context, context.getString(R.string.password_length_illegal), Toast.LENGTH_SHORT).show();
    }

    public void Error(RegisterNewActivity context) {
        Toast.makeText(context, context.getString(R.string.error_null), Toast.LENGTH_SHORT).show();
    }

    public void inviteCodeError(RegisterNewActivity context) {
        Toast.makeText(context, context.getString(R.string.inviteCode_null), Toast.LENGTH_SHORT).show();
    }

    public void setGender(boolean isMan) {
        if (isMan) {
            mSexy.setText("男");
        } else {
            mSexy.setText("女");
        }
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public interface Listener {
        void onSoftKeyboardShown(int w, int h, int oldw, int oldh);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mListener != null) {
            mListener.onSoftKeyboardShown(w, h, oldw, oldh);
        }
    }
}
