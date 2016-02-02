package com.asosapp.phone.controller;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.asosapp.phone.R;
import com.asosapp.phone.activity.RegisterNewActivity;
import com.asosapp.phone.utils.DialogCreator;
import com.asosapp.phone.utils.HandleResponseCode;
import com.asosapp.phone.utils.SharePreferenceManager;
import com.asosapp.phone.view.LoginDialog;
import com.asosapp.phone.view.RegisterView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class RegisterController implements RegisterView.Listener, OnClickListener {

    private RegisterView mRegisterView;
    private RegisterNewActivity mContext;
    private Dialog mLoginDialog;

    public RegisterController(RegisterView registerView, RegisterNewActivity context) {
        this.mRegisterView = registerView;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regist_btn:
                Log.i("Tag", "[register]register event execute!");
                final String userId = mRegisterView.getUserId();
                final String password = mRegisterView.getPassword();

                if (isMobileNO(userId)==false){
                    mRegisterView.isMobileError(mContext);
                    break;
                }

                if (userId.equals("")) {
                    mRegisterView.userNameError(mContext);
                    break;
                } else if (password.equals("")) {
                    mRegisterView.passwordError(mContext);
                    break;
                } else if (password.length() > 128 || password.length() < 4) {
                    mRegisterView.passwordLengthError(mContext);
                    break;
                }

                final Dialog dialog = DialogCreator.createLoadingDialog(mContext, mContext.getString(R.string.registering_hint));
                dialog.show();
                JMessageClient.register(userId, password, new BasicCallback() {

                    @Override
                    public void gotResult(final int status, final String desc) {
                        dialog.dismiss();
                        if (status == 0) {
                            LoginDialog loginDialog = new LoginDialog();
                            mLoginDialog = loginDialog.createLoadingDialog(mContext);
                            mLoginDialog.show();
                            JMessageClient.login(userId, password, new BasicCallback() {
                                @Override
                                public void gotResult(final int status, String desc) {
                                    Log.e("Leo-->", "" + status);
                                    if (status == 0) {
                                        mContext.onRegistSuccess();
                                    } else {
                                        mLoginDialog.dismiss();
                                        HandleResponseCode.onHandle(mContext, status, false);
                                    }
                                }
                            });
                        } else {
                            HandleResponseCode.onHandle(mContext, status, false);
                        }
                    }
                });
                break;
            case R.id.return_btn:
                mContext.finish();
                break;
        }
    }

    public void dismissDialog() {
        if (mLoginDialog != null)
            mLoginDialog.dismiss();
    }

    @Override
    public void onSoftKeyboardShown(int w, int h, int oldw, int oldh) {
        int softKeyboardHeight = oldh - h;
        if (softKeyboardHeight > 300) {
            boolean writable = SharePreferenceManager.getCachedWritableFlag();
            if (writable) {
                SharePreferenceManager.setCachedKeyboardHeight(softKeyboardHeight);
                SharePreferenceManager.setCachedWritableFlag(false);
            }
        }
    }

    /**
     * ��֤�ֻ���ʽ
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    �ƶ���134��135��136��137��138��139��150��151��157(TD)��158��159��187��188
    ��ͨ��130��131��132��152��155��156��185��186
    ���ţ�133��153��180��189����1349��ͨ��
    �ܽ��������ǵ�һλ�ض�Ϊ1���ڶ�λ�ض�Ϊ3��5��8������λ�õĿ���Ϊ0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"�����1λΪ����1��"[358]"����ڶ�λ����Ϊ3��5��8�е�һ����"\\d{9}"��������ǿ�����0��9�����֣���9λ��
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

//    public static boolean isMobileNO(String phoneNumber)
//    {
//        boolean isValid = false;
//
//        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
//
//        String expression2 ="^\\(?(\\d{2})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
//
//        CharSequence inputStr = phoneNumber;
//
//        Pattern pattern = Pattern.compile(expression);
//
//        Matcher matcher = pattern.matcher(inputStr);
//
//        Pattern pattern2 =Pattern.compile(expression2);
//
//        Matcher matcher2= pattern2.matcher(inputStr);
//        if(matcher.matches()||matcher2.matches())
//        {
//            isValid = true;
//        }
//        return isValid;
//    }
 }
