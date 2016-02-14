package com.asosapp.phone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asosapp.phone.R;
import com.asosapp.phone.initprogram.MyApplication;
import com.asosapp.phone.utils.Const;
import com.asosapp.phone.view.ToastView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ASOS_zhulr on 2016/2/14.
 */
public class NewsHTMLActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "NewsHTMLActivity";
    private ImageView backBtn;
    private WebView newsHTML;
    private String newsID;

    private String newsHTMLUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_html);
        Intent intent = getIntent();
        newsID = intent.getStringExtra("newsID");
        volley_Get();
        init();
    }

    private void init() {
        newsHTML = (WebView) findViewById(R.id.news_html);
        WebSettings webSettings = newsHTML.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //设置Web视图
        newsHTML.setWebViewClient(new webViewClient());
        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
        }
    }

    /**
     * get请求
     * <p/>
     * *
     */
    JSONObject DATA = null;

    private void volley_Get() {
        String url = Const.SERVICE_URL + Const.NEWSHTML + "?newsID=" + newsID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("CODE").toString().equals("200")) {
                        DATA = (JSONObject) jsonObject.get("DATA");
                        newsHTMLUrl = DATA.get("NEWS_HTML").toString();
                        //加载需要显示的网页
                        newsHTML.loadUrl(newsHTMLUrl);
                    } else if (jsonObject.get("CODE").toString().equals("100")) {
                        ToastView.toast(getApplication(), jsonObject.get("MESSAGE").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof NoConnectionError) {
                    ToastView.NetError(getApplication());
                } else if (volleyError instanceof com.android.volley.TimeoutError) {
                    ToastView.NetTimeOut(getApplication());
                } else {
                    ToastView.toast(getApplication(), volleyError.toString());
                }
            }
        });
        request.setTag(TAG);
        MyApplication.getHttpQueues().add(request);
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
