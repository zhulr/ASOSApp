package com.asosapp.phone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import java.net.URLEncoder;

/**
 * Created by ASOS_zhulr on 2016/2/22.
 * 留言用户反馈界面
 */
public class UserFeedbackActivity extends BasicActivity implements View.OnClickListener{

    private EditText message;
    private TextView messageBtn;
    private TextView titleName;
    private static final Object TAG = "UserFeedbackActivity";
    private String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        URL = Const.SERVICE_URL + Const.LM;
        init();
    }

    private void init() {
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText(R.string.feedback);
        message = (EditText) findViewById(R.id.message);
        messageBtn = (TextView) findViewById(R.id.message_btn);
        messageBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.message_btn:
                sendMessage();
                break;

        }
    }

    /**
     * 发送留言
     */
    private void sendMessage()  {
        String url = null;
        try {
            url = URL + "?content=" + URLEncoder.encode(message.getText().toString().trim(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("CODE").toString().equals("200")) {
                        ToastView.toast(UserFeedbackActivity.this, "发送成功");
                        message.setText("");
                    } else if (jsonObject.get("CODE").toString().equals("100")) {
                        ToastView.toast(UserFeedbackActivity.this, jsonObject.get("MESSAGE").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        request.setTag(TAG);
        MyApplication.getHttpQueues().add(request);
    }
}
