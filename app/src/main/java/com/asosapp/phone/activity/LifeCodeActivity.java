package com.asosapp.phone.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asosapp.phone.R;
import com.asosapp.phone.initprogram.MyApplication;
import com.asosapp.phone.utils.Const;
import com.asosapp.phone.view.ToastView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by ASOS_zhulr on 2016/3/24.
 */
public class LifeCodeActivity extends BasicActivity {

    private String userPhone = "";
    private TextView myLifeCode;
    private Button myEarnings;
    private EditText myLifeID;
    private Button applyBtn;
    private String TAG = "LifeCodeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_code);
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserInfo", 0);
        userPhone = sharedPreferences.getString("user_phone", "0");
        init();
    }

    private void init() {
        myLifeCode = (TextView) findViewById(R.id.my_life_code);
        myLifeCode.setText("*****************************");
        myEarnings = (Button) findViewById(R.id.show_my_earnings);
        myLifeID = (EditText) findViewById(R.id.user_life_id_et);
        myLifeID.setVisibility(View.INVISIBLE);
        applyBtn = (Button) findViewById(R.id.apply_life_code);
        applyBtn.setVisibility(View.INVISIBLE);
        myEarnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    hasFocusLifeCode();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    applyLifeCode();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    JSONObject DATAs = null;

    /**
     * 申请终身码
     * *
     */
    private void applyLifeCode() throws UnsupportedEncodingException {
        String url = Const.SERVICE_URL + Const.APPLYLIFECODE + "?userPhone=" + userPhone + "&userLifeID=" + myLifeID.getText().toString().trim();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("CODE").toString().equals("200")) {
                        DATAs = (JSONObject) jsonObject.get("DATA");
                        myLifeCode.setText(DATAs.getString("LIFECODE"));
                        myLifeID.setVisibility(View.INVISIBLE);
                        applyBtn.setVisibility(View.INVISIBLE);
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
                    ToastView.NetError(getApplicationContext());
                } else if (volleyError instanceof com.android.volley.TimeoutError) {
                    ToastView.NetTimeOut(getApplicationContext());
                } else {
                    ToastView.toast(getApplicationContext(), volleyError.toString());
                }
            }
        });
        request.setTag(TAG);
        MyApplication.getHttpQueues().add(request);
    }

    private void hasFocusLifeCode() throws UnsupportedEncodingException {
        String url = Const.SERVICE_URL + Const.SEARCHLIFECODE + "?userPhone=" + userPhone;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("CODE").toString().equals("200")) {
                        DATAs = (JSONObject) jsonObject.get("DATA");
                        myLifeCode.setText(DATAs.getString("LIFECODE"));
                        myEarnings.setVisibility(View.INVISIBLE);
                        myLifeID.setVisibility(View.INVISIBLE);
                        applyBtn.setVisibility(View.INVISIBLE);
                    } else if (jsonObject.get("CODE").toString().equals("100")) {
//                        toast(jsonObject.get("MESSAGE").toString());
                        myLifeCode.setText("暂无终身码，请输入身份证号进行申请！");
                        myEarnings.setVisibility(View.INVISIBLE);
                        myLifeID.setVisibility(View.VISIBLE);
                        applyBtn.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof NoConnectionError) {
                    ToastView.NetError(getApplicationContext());
                } else if (volleyError instanceof com.android.volley.TimeoutError) {
                    ToastView.NetTimeOut(getApplicationContext());
                } else {
                    ToastView.toast(getApplicationContext(), volleyError.toString());
                }
            }
        });
        request.setTag(TAG);
        MyApplication.getHttpQueues().add(request);
    }
}
