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
        //����WebView���ԣ��ܹ�ִ��Javascript�ű�
        webSettings.setJavaScriptEnabled(true);
        //���ÿ��Է����ļ�
        webSettings.setAllowFileAccess(true);
        //����֧������
        webSettings.setBuiltInZoomControls(true);
        //����Web��ͼ
        about.setWebViewClient(new webViewClient());
        //������Ҫ��ʾ����ҳ
        about.loadUrl("http://mp.weixin.qq.com/s?__biz=MzA4ODY3MTU0MQ==&mid=401741723&idx=1&sn=a58c3652f9373edc095a605b64215006#rd");
    }


    //Web��ͼ
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
