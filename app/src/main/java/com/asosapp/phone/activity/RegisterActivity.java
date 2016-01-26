package com.asosapp.phone.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asosapp.phone.R;
import com.asosapp.phone.bean.UserInfo;
import com.asosapp.phone.initprogram.MyApplication;
import com.asosapp.phone.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ASOS_zhulr on 2015/12/15.
 * 注册页面
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    private static JSONObject DATA = null;
    String TAG = "RegisterActivity";
    private EditText phonenum;
    private EditText userNickname;
    private EditText userPassword;
    private EditText userPasswordagin;
    private EditText verificationCode;
    private Button codeGet;
    private Button register;
    private TimeCount time;
    private boolean isLoginFlag = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sharedPreferences = getSharedPreferences("UserInfo", 1);
        init();
    }

    private void init() {
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        phonenum = (EditText) findViewById(R.id.user_phone);
        userNickname = (EditText) findViewById(R.id.user_nickname);
        userPassword = (EditText) findViewById(R.id.user_password);
        userPasswordagin = (EditText) findViewById(R.id.user_password_again);
        verificationCode = (EditText) findViewById(R.id.verification_code);
        codeGet = (Button) findViewById(R.id.get_verification_code);
        register = (Button) findViewById(R.id.register_do);
        codeGet.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    //返回键监听事件，返回登录页面
    @Override
    public void onBackPressed() {
        intent(false);
        super.onBackPressed();
    }

    private void verify() {
        time.start();//开始计时
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_verification_code:
                if (phonenum.getText().toString().equals("")) {
                    toast("号码不能为空！");
                } else {
                    verify();
                }
                break;
            case R.id.register_do:
                register();
                break;
        }
    }

    private void register() {
        if (validation()) {
            volley_Get();
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            codeGet.setText("重新验证");
            codeGet.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            codeGet.setClickable(false);
            codeGet.setText(millisUntilFinished / 1000 + "秒");
        }

    }

    //输出屏幕信息
    private void toast(String str) {
        Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    private boolean validation() {
        if (phonenum.getText().toString().equals("")) {
            toast("号码不能为空！");
            return false;
        }
        if (userNickname.getText().toString().equals("")) {
            toast("用户名不能为空！");
            return false;
        }
        if (userPassword.getText().toString().equals("") || userPasswordagin.getText().toString().equals("")) {
            toast("密码不能为空！");
            return false;
        }
        if (verificationCode.getText().toString().equals("")) {
            toast("验证码不能为空！");
            return false;
        }
        if (!userPassword.getText().toString().equals(userPasswordagin.getText().toString())) {
            toast("两次密码输入不一致！");
            return false;
        }
        return true;
    }

    /**
     * 保存用户信息
     */
    private void setUser(JSONObject result) throws JSONException {
        UserInfo userInfo = UserInfo.instance();
        userInfo.setIsLogin(true);
        userInfo.setUser_id(result.getString("ID"));
        userInfo.setUser_phone(phonenum.getText().toString().trim());
        userInfo.setUser_nickname(userNickname.getText().toString().trim());

        edit = sharedPreferences.edit();
        edit.putBoolean("isLogin", true);
        edit.putString("user_id", result.getString("ID"));
        edit.putString("user_phone", phonenum.getText().toString().trim());
        edit.putString("user_nickname", userNickname.getText().toString().trim());
        edit.commit();
    }

    //跳转
    private void intent(boolean flag) {
        Intent intent;
        if (flag) {
            intent = new Intent(getApplication(), MainActivity.class);
        } else {
            intent = new Intent(getApplication(), LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private void volley_Get() {
        String url = Const.SERVICE_URL + Const.REGISTER + "?userPhone=" + phonenum.getText().toString().trim()
                + "&userNickname=" + userNickname.getText().toString().trim() + "&userPassword=" + userPasswordagin.getText().toString().trim();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("CODE").toString().equals("200")) {
                        toast(jsonObject.get("MESSAGE").toString());
                        DATA = (JSONObject) jsonObject.get("DATA");
                        setUser(DATA);
                        intent(true);
                    } else if (jsonObject.get("CODE").toString().equals("100")) {
                        toast(jsonObject.get("MESSAGE").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof NoConnectionError) {
                    toast("网络异常，请检测网络连接。");
                } else if (volleyError instanceof com.android.volley.TimeoutError) {
                    toast("网络连接超时，请检测网络连接。");
                } else {
                    toast(volleyError.toString());
                }
            }
        });
        request.setTag(TAG);
        MyApplication.getHttpQueues().add(request);
    }
}
