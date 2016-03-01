package com.asosapp.phone.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asosapp.phone.R;
import com.asosapp.phone.activity.LoginNewActivity;
import com.asosapp.phone.initprogram.MyApplication;
import com.asosapp.phone.utils.Const;
import com.asosapp.phone.utils.DialogCreator;
import com.asosapp.phone.utils.HandleResponseCode;
import com.asosapp.phone.utils.SharePreferenceManager;
import com.asosapp.phone.view.LoginView;
import com.asosapp.phone.view.ToastView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class LoginController implements LoginView.Listener, OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private String TAG = "LoginNewActivity";
    private LoginView mLoginView;
    private LoginNewActivity mContext;

    public LoginController(LoginView mLoginView, LoginNewActivity context) {
        this.mLoginView = mLoginView;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.return_btn:
                mContext.finish();
                break;
            case R.id.login_btn:
                //隐藏软键盘
                InputMethodManager manager = ((InputMethodManager) mContext
                        .getSystemService(Activity.INPUT_METHOD_SERVICE));
                if (mContext.getWindow().getAttributes().softInputMode
                        != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (mContext.getCurrentFocus() != null) {
                        manager.hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                final String userId = mLoginView.getUserId();
                final String password = mLoginView.getPassword();
                if (userId.equals("")) {
                    mLoginView.userNameError(mContext);
                    break;
                } else if (password.equals("")) {
                    mLoginView.passwordError(mContext);
                    break;
                }
                final Dialog dialog = DialogCreator.createLoadingDialog(mContext,
                        mContext.getString(R.string.login_hint));
                dialog.show();
                JMessageClient.login(userId, password, new BasicCallback() {
                    @Override
                    public void gotResult(final int status, final String desc) {
                        dialog.dismiss();
                        if (status == 0) {
                            long userId = JMessageClient.getMyInfo().getUserID();
                            String name = JMessageClient.getMyInfo().getUserName();
                            volley_Get();
                        } else {
                            Log.i("LoginController", "status = " + status);
                            HandleResponseCode.onHandle(mContext, status, false);
                        }
                    }
                });
                break;

            case R.id.register_btn:
                mContext.startRegisterActivity();
                break;
            case R.id.forget_password:
                ToastView.toast(mContext,"请拨打客服热线400-608-6655");
                break;
        }
    }

    @Override
    public void onSoftKeyboardShown(int w, int h, int oldw, int oldh) {
        int softKeyboardHeight = oldh - h;
        if (softKeyboardHeight > 300) {
            mLoginView.setRegistBtnVisable(View.INVISIBLE);
            boolean writable = SharePreferenceManager.getCachedWritableFlag();
            if (writable) {
                Log.i("LoginController", "commit h: " + softKeyboardHeight);
                SharePreferenceManager.setCachedKeyboardHeight(softKeyboardHeight);
                SharePreferenceManager.setCachedWritableFlag(false);
            }
        } else {
            mLoginView.setRegistBtnVisable(View.VISIBLE);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d("sdfs", "onCheckedChanged !!!! isChecked = " + isChecked);
        if (isChecked) {
            swapEnvironment(true);
        } else {
            swapEnvironment(false);
        }
    }

    private void swapEnvironment(boolean isTest) {
        try {
            Method method = JMessageClient.class.getDeclaredMethod("swapEnvironment", Context.class, Boolean.class);
            method.invoke(null, mContext, isTest);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * get请求
     * <p/>
     * *
     */
    JSONObject DATA = null;

    private void volley_Get() {
        String url = Const.SERVICE_URL + Const.LOGIN + "?userPhone=" + mLoginView.getUserId();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("CODE").toString().equals("200")) {
                        DATA=jsonObject.getJSONObject("DATA");
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserInfo", 1); //私有数据
                        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                        editor.putString("user_id", mLoginView.getUserId());
//                        editor.putString("user_psw", DATA.getString("USER_PASSWORD"));
                        editor.putString("user_phone", DATA.getString("USER_PHONE"));
                        editor.putString("user_name", DATA.getString("USER_NAME"));
                        editor.putString("user_age", DATA.getString("USER_AGE"));
                        editor.putString("user_sexy", DATA.getString("USER_SEX"));
                        editor.putBoolean("isLogin", true);
                        editor.putBoolean("is_push", true);
                        editor.commit();//提交修改
                        mContext.startMainActivity();
                    } else if (jsonObject.get("CODE").toString().equals("100")) {
                        ToastView.toast(mContext, jsonObject.get("MESSAGE").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof NoConnectionError) {
                    ToastView.NetError(mContext);
                } else if (volleyError instanceof com.android.volley.TimeoutError) {
                    ToastView.NetTimeOut(mContext);
                } else {
                    ToastView.toast(mContext, volleyError.toString());
                }
            }
        });
        request.setTag(TAG);
        MyApplication.getHttpQueues().add(request);
    }

}
