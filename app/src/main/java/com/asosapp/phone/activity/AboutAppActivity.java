package com.asosapp.phone.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.asosapp.phone.R;

/**
 * Created by ASOS_zhulr on 2016/2/24.
 */
public class AboutAppActivity extends BasicActivity {
    private WebView about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        about = (WebView) findViewById(R.id.about_webview);
        WebSettings webSettings = about.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //设置Web视图
        about.setWebViewClient(new webViewClient());
        //加载需要显示的网页
        about.loadUrl("http://mp.weixin.qq.com/s?__biz=MzA4ODY3MTU0MQ==&mid=401741723&idx=1&sn=a58c3652f9373edc095a605b64215006#rd");
    }


    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
