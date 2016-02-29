package com.asosapp.phone.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
    private static JSONObject DATA = null;
    String TAG = "CardsDetailsActivity";
    private Button btnBuy;
    private String BOSS = "司机-安心符";
    private String DRIVER = "司机-安心符";
    private int price = 0;
    private int totalPrices = 0;
    //布局控件
    private ImageView cardIV;
    private TextView cardTypeTV;
    private EditText timeET;
    private TextView pricesTV;
    private View serviceET;
    private String servicePhoeNum = "4006086655";
    private String URL = "";
    String phoneNum, sex, name, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        init();
        loading();
    }


    private void init() {
        cardIV = (ImageView) findViewById(R.id.card_img);
        cardTypeTV = (TextView) findViewById(R.id.card_type);
        timeET = (EditText) findViewById(R.id.card_buy_time);
        pricesTV = (TextView) findViewById(R.id.card_total_prices);
        serviceET = findViewById(R.id.service_call);
        serviceET.setOnClickListener(this);
        btnBuy = (Button) findViewById(R.id.buy);
        btnBuy.setOnClickListener(this);
        //键盘输入监听
        timeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                pricesTV.setText(String.valueOf(price));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!timeET.getText().toString().trim().equals("") && Integer.parseInt(timeET.getText().toString().trim()) > 1) {
                    totalPrices = Integer.parseInt(timeET.getText().toString().trim()) * price;
                    pricesTV.setText(String.valueOf(totalPrices));
                } else if (timeET.getText().toString().trim().equals("")) {
                    pricesTV.setText(String.valueOf(0));
                } else if (Integer.parseInt(timeET.getText().toString().trim()) == 0) {
                    pricesTV.setText(String.valueOf(0));
                } else {
                    pricesTV.setText(String.valueOf(price));
                }
            }
        });
    }


    /**
     * 数据载入
     */
    private void loading() {
        Intent intent = getIntent();
        String value = intent.getStringExtra("type");
        if (value.equals("HEART")) {
            URL = Const.SERVICE_URL + Const.HEARTCARD;
            price = 999;
            totalPrices = price;
            cardTypeTV.setText(BOSS);
            cardIV.setImageResource(R.mipmap.heartcard);
            pricesTV.setText(String.valueOf(price));
        } else if (value.equals("DRIVER")) {
            URL = Const.SERVICE_URL + Const.DRIVERCARD;
            price = 299;
            totalPrices = price;
            cardTypeTV.setText(DRIVER);
            cardIV.setImageResource(R.mipmap.drivercard);
            pricesTV.setText(String.valueOf(price));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.service_call:
                //拨打客服电话
                dialog("service");
                break;
            case R.id.buy:
                //确认购买卡片信息
                    dialog("showInfo");
                break;
        }
    }

    /**
     * 点击客服电话弹出窗口是否拨打
     */
    private void dialog(String flag) {
        switch (flag) {
            case "showInfo": {
                getInfo();
                String info = "尊敬的  " + name + sex + "  ,您将使用号码 " + phoneNum + "  \n\n购买 " + time
                        + " 张 " + cardTypeTV.getText().toString().trim() + " \n\n总价为：  "
                        + totalPrices + "  元。\n\n\n请确认手机上已安装支付宝，点击确认购买进行购买";
                new AlertDialog.Builder(this)
                        .setTitle("确认购买信息")
                        .setMessage(info)
                        .setPositiveButton("确认购买", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pay();
                            }
                        })
                        .setNegativeButton("稍稍考虑", null)
                        .show();
                break;
            }
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
                intent.putExtra("name", cardTypeTV.getText().toString().trim());
                intent.putExtra("content", cardTypeTV.getText().toString().trim()+"购买支付费用");
                intent.putExtra("totalPrices", String.valueOf(totalPrices));
                intent.setClass(CardsDetailsActivity.this,PayActivity.class);
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

    /**
     * 支付宝购买卡片
     */
    private void pay() {
        dialog("pay");
    }

    /**
     * 获取用户输入信息
     */
    private void getInfo() {
        time = timeET.getText().toString();
    }



}
