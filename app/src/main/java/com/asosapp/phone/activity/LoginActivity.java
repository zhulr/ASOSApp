package com.asosapp.phone.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.asosapp.phone.R;
import com.asosapp.phone.bean.UserInfo;
import com.asosapp.phone.initprogram.MyApplication;
import com.asosapp.phone.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASOS_zhulr on 2015/12/9.
 * 登录界面
 */
public class LoginActivity extends BasicActivity implements View.OnClickListener {

    private static JSONObject DATA = null;
    String TAG = "LoginActivity";
    private TextView loginSkip;
    private EditText ETuserName;
    private EditText ETuserPassword;
    private Button btnLogin;
    private TextView TVLoginProblems;
    private TextView TVRegister;
    private ImageView IVLoginThirdSina;
    private ImageView IVLoginThirdWeChat;
    private ImageView IVLoginThirdQQ;

    private static int LOGIN_THIRD_SINA = 1;
    private static int LOGIN_THIRD_WECHAT = 2;
    private static int LOGIN_THIRD_QQ = 3;
    private String USER_PASSWORD = "";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("UserInfo", 1);
        init();
    }


    private void init() {
        loginSkip = (TextView) findViewById(R.id.tv_login_skip);
        ETuserName = (EditText) findViewById(R.id.user_name);
        ETuserName.setText(sharedPreferences.getString("user_phone", ""));
        ETuserPassword = (EditText) findViewById(R.id.user_password);
        btnLogin = (Button) findViewById(R.id.user_login);
        IVLoginThirdSina = (ImageView) findViewById(R.id.sina_login);
        IVLoginThirdWeChat = (ImageView) findViewById(R.id.wechat_login);
        IVLoginThirdQQ = (ImageView) findViewById(R.id.qq_login);
        TVLoginProblems = (TextView) findViewById(R.id.login_problems);
        TVRegister = (TextView) findViewById(R.id.login_new_user_register);
        loginSkip.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        IVLoginThirdSina.setOnClickListener(this);
        IVLoginThirdWeChat.setOnClickListener(this);
        IVLoginThirdQQ.setOnClickListener(this);
        TVLoginProblems.setOnClickListener(this);
        TVRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //暂不登录
            case R.id.tv_login_skip:
                skipLogin();
                break;
            //用户登录
            case R.id.user_login:
                login();
                break;
            //第三方微博登录
            case R.id.sina_login:
                loginThird(LOGIN_THIRD_SINA);
                break;
            //第三方微信登录
            case R.id.wechat_login:
                loginThird(LOGIN_THIRD_WECHAT);
                break;
            //第三方QQ登录
            case R.id.qq_login:
                loginThird(LOGIN_THIRD_QQ);
                break;
            //登录遇到问题
            case R.id.login_problems:
                toast("登录有问题");
                break;
            //注册
            case R.id.login_new_user_register:
                intent(false);
                break;
        }
    }


    //第三方登录
    private void loginThird(int loginThirdType) {
        switch (loginThirdType) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
        toast("暂未开放第三登录功能！");
    }

    //跳过登录
    private void skipLogin() {
        edit = sharedPreferences.edit();
        edit.putBoolean("isLogin", false);
        edit.commit();
        intent(true);
    }

    //登录
    private void login() {
        volley_Get();
    }

    private void volley_Get() {
        String url = Const.SERVICE_URL + Const.LOGIN + "?userPhone=" + ETuserName.getText().toString().trim();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("CODE").toString().equals("200")) {
//                        toast(jsonObject.get("MESSAGE").toString());
                        DATA = (JSONObject) jsonObject.get("DATA");
                        //保存用户信息
                        USER_PASSWORD = DATA.get("USER_PASSWORD").toString();
                        if (USER_PASSWORD.equals(ETuserPassword.getText().toString().trim())) {
                            setUser(DATA);
                            toast("登录成功");
                            intent(true);
                        } else {
                            toast("密码错误");
                        }
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

    //跳转
    private void intent(boolean flag) {
        Intent intent;
        if (flag) {
            intent = new Intent(getApplication(), MainActivity.class);
        } else {
            intent = new Intent(getApplication(), RegisterActivity.class);
        }
        startActivity(intent);
        finish();
    }

    /**
     * 封装用户登录参数
     *
     * @return
     */
    private Map<String, String> getMap() {
        Map<String, String> jsonObject = new HashMap();
        jsonObject.put("userPhone", ETuserName.getText().toString().trim());
//        jsonObject.put("flag", "0");
        return jsonObject;
    }

    /**
     * 保存用户信息
     *
     * @param result
     * @throws JSONException
     */
    private void setUser(JSONObject result) throws JSONException {
        UserInfo userInfo = UserInfo.instance();
        userInfo.setIsLogin(true);
        userInfo.setUser_id(result.getString("ID"));
        userInfo.setUser_name(result.getString("USER_NAME"));
        userInfo.setUser_nickname(result.getString("USER_NICKNAME"));
        userInfo.setUser_phone(ETuserName.getText().toString().trim());

        edit = sharedPreferences.edit();
        edit.putBoolean("isLogin", true);
        edit.putString("user_phone", ETuserName.getText().toString().trim());
        edit.putString("user_name", result.getString("USER_NAME"));
        edit.putString("user_nickname", result.getString("USER_NICKNAME"));
        edit.putString("user_id", result.getString("ID"));
        edit.commit();
    }

}
