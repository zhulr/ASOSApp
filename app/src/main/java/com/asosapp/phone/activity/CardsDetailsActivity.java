package com.asosapp.phone.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asosapp.phone.R;
import com.asosapp.phone.initprogram.MyApplication;
import com.asosapp.phone.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * Created by ASOS_zhulr on 2015/12/11.
 * 卡购买信息登记页面
 */
public class CardsDetailsActivity extends BasicActivity implements View.OnClickListener {
    String TAG = "CardsDetailsActivity";
    private Button btnBuy;
    private int price = 0;
    private String cardName;
    String value = "";
    //布局控件
    private String servicePhoeNum = "4006086655";
    private WebView cardDetailsHtml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        Intent intent = getIntent();
        value = intent.getStringExtra("type");
        init();
        loading();
    }


    private void init() {
        btnBuy = (Button) findViewById(R.id.buy);
        btnBuy.setOnClickListener(this);
        cardDetailsHtml = (WebView) findViewById(R.id.card_details_html);
        WebSettings webSettings = cardDetailsHtml.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //设置Web视图
        cardDetailsHtml.setWebViewClient(new webViewClient());
        //获取url地址

        if (value.equals("DRIVER")) {
            cardDetailsHtml.loadUrl("http://223.68.152.158:65500/Home/Card/Home.html");
        } else if (value.equals("BOSS")) {
            cardDetailsHtml.loadUrl("http://223.68.152.158:65500/Home/Card/home-2.html");
        }
    }


    /**
     * 数据载入
     */
    private void loading() {
        if (value.equals("BOSS")) {
            cardName = "Boss-护身符";
            price = 999;
        } else if (value.equals("DRIVER")) {
            cardName = "司机-安心符";
            price = 299;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy:
                pay();
                break;
        }
    }

    /**
     * 点击客服电话弹出窗口是否拨打
     */
    private void dialog(String flag) {
        switch (flag) {
            case "service": {
                new AlertDialog.Builder(this)
                        .setMessage("是否拨打客服电话")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callServicePhone();
                            }
                        })
                        .setNegativeButton("否", null)
                        .show();
                break;
            }
            case "pay": {
                Intent intent = new Intent();
                intent.putExtra("name", cardName);
                intent.putExtra("type", value);
                intent.putExtra("content", "安邦卡：" + cardName + "购买支付费用");
                intent.putExtra("totalPrices", String.valueOf(price));
                intent.setClass(CardsDetailsActivity.this, PayActivity.class);
                startActivity(intent);
                break;
            }
        }

    }

    /**
     * 点击拨打客服电话
     */
    private void callServicePhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + servicePhoeNum);
        intent.setData(data);
        startActivity(intent);
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    /**
     * 支付宝购买卡片
     */
    private void pay() {
        dialog("pay");
    }
}
