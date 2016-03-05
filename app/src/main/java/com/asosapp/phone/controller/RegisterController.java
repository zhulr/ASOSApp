package com.asosapp.phone.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asosapp.phone.R;
import com.asosapp.phone.activity.RegisterNewActivity;
import com.asosapp.phone.initprogram.MyApplication;
import com.asosapp.phone.utils.Const;
import com.asosapp.phone.utils.DialogCreator;
import com.asosapp.phone.utils.HandleResponseCode;
import com.asosapp.phone.utils.SharePreferenceManager;
import com.asosapp.phone.view.LoginDialog;
import com.asosapp.phone.view.RegisterView;
import com.asosapp.phone.view.ToastView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterController implements RegisterView.Listener, OnClickListener {

    private String TAG = "RegisterNewActivity";
    private RegisterView mRegisterView;
    private RegisterNewActivity mContext;
    private Dialog mLoginDialog;
    private String userId;
    private String password;
    private String passwordsure;
    private String userName;
    private String userAge;
    private String code;
    private int i = 30;
    private String userSexy;

    public RegisterController(RegisterView registerView, RegisterNewActivity context) {
        this.mRegisterView = registerView;
        this.mContext = context;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regist_btn:
                SMSSDK.submitVerificationCode("86", mRegisterView.getUserId(), mRegisterView.getCode());
                Log.i("Tag", "[register]register event execute!");
                userId = mRegisterView.getUserId();
                password = mRegisterView.getPassword();
                passwordsure = mRegisterView.getPasswordSure();
                userName = mRegisterView.getUserName();
                userAge = mRegisterView.getUserAge();
                code = mRegisterView.getCode();
                userSexy = mRegisterView.getSexy();

                if (isMobileNO(userId) == false) {
                    mRegisterView.isMobileError(mContext);
                    break;
                }
                if (userId.equals("")) {
                    mRegisterView.userNameError(mContext);
                    break;
                } else if (password.equals("") || passwordsure.equals("")) {
                    mRegisterView.passwordError(mContext);
                    break;
                } else if (password.length() > 128 || password.length() < 4) {
                    mRegisterView.passwordLengthError(mContext);
                    break;
                } else if (userName.equals("") || userAge.equals("") || userSexy.equals("") || code.equals("")) {
                    mRegisterView.Error(mContext);
                    break;
                } else if (!password.equals(passwordsure)) {
                    mRegisterView.passwordSureError(mContext);
                    break;
                }
//                gethandler();


                break;
            case R.id.return_btn:
                mContext.finish();
                break;
            case R.id.sexy:
                showSexDialog();
                break;
            case R.id.code_button:
                if (mRegisterView.getUserId().equals("")) {
                    mRegisterView.userNameError(mContext);
                    break;
                } else {
                    smsCode();//获取验证码


                }
                break;
        }
    }
/**
 * 短信验证
 *
 */
    private void smsCode() {
        SMSSDK.getVerificationCode("86", mRegisterView.getUserId());
        Toast.makeText(mContext, "获取成功", Toast.LENGTH_SHORT).show();
        mRegisterView.setcode_button().setClickable(false);
        mRegisterView.setcode_button().setText("重新发送(" + i-- + ")");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 30; i > 0; i--) {
                    handler.sendEmptyMessage(-9);
                    if (i <= 0) {
                        i = 30;
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                i = 30;
                handler.sendEmptyMessage(-8);
            }
        }).start();
    }

    /**
     * 判断验证码
     */
    public void gethandler() {
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                mRegisterView.setcode_button().setText("重新发送(" + i-- + ")");
            } else if (msg.what == -8) {
                mRegisterView.setcode_button().setText("获取验证码");
                mRegisterView.setcode_button().setClickable(true);
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示新好友
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        Toast.makeText(mContext, "提交验证码成功",
                                Toast.LENGTH_SHORT).show();
                        //调用极光验证，写入数据库
                        successRegister();

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(mContext, "验证码已经发送",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    Toast.makeText(mContext, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    /**
     * get请求
     * <p/>
     * *
     */
    JSONObject DATA = null;

    private void volley_Get() throws UnsupportedEncodingException {

        String url = Const.SERVICE_URL + Const.REGISTER + "?userPhone=" + userId + "&userName=" + userName + "&userPassword=" + password + "&userAge=" + userAge + "&userSex=" + userSexy;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("CODE").toString().equals("200")) {
                        DATA = (JSONObject) jsonObject.get("DATA");
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
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 性别选择器
     */
    public void showSexDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_set_sex, null);
        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.show();
        RelativeLayout manRl = (RelativeLayout) view.findViewById(R.id.man_rl);
        RelativeLayout womanRl = (RelativeLayout) view.findViewById(R.id.woman_rl);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.man_rl:
                        mRegisterView.setGender(true);
//                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserInfo", 1); //私有数据
//                        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
//                        editor.putString("user_sexy", "男");
//                        editor.commit();//提交修改
                        dialog.cancel();
                        break;
                    case R.id.woman_rl:
                        mRegisterView.setGender(false);
//                        SharedPreferences sp = mContext.getSharedPreferences("UserInfo", 1); //私有数据
//                        SharedPreferences.Editor et = sp.edit();//获取编辑器
//                        et.putString("user_sexy", "女");
//                        et.commit();//提交修改
                        dialog.cancel();
                        break;
                }
            }
        };
        manRl.setOnClickListener(listener);
        womanRl.setOnClickListener(listener);
    }



private void successRegister(){
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
                        if (status == 0) {
                            try {
                                volley_Get();//上传到本地数据库
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
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
}

}
