package com.asosapp.phone.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.asosapp.phone.R;

import org.w3c.dom.Text;

/**
 * Created by ASOS_zhulr on 2015/12/11.
 * 卡购买信息登记页面
 */
public class CardsDetailsActivity extends Activity implements View.OnClickListener {
    private Button btnBuy;
    private String HEART = "暖心卡";
    private String DRIVER = "司机卡";
    private int price = 0;
    private int totalPrices = 0;
    private EditText phoneET;
    private EditText nameET;
    private RadioButton manRB;
    private RadioButton womenRB;
    private TextView cardTypeTV;
    private EditText timeET;
    private TextView pricesTV;
    private View serviceET;
    private String servicePhoeNum = "4006086655";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        init();
        Intent intent = getIntent();
        String value = intent.getStringExtra("type");
        if (value.equals("HEART")) {
            cardTypeTV.setText(HEART);
            price = 68;
        } else if (value.equals("DRIVER")) {
            price = 198;
            cardTypeTV.setText(DRIVER);
        }
    }

    private void init() {
        phoneET = (EditText) findViewById(R.id.card_buy_phone);
        nameET = (EditText) findViewById(R.id.card_buy_name);
        manRB = (RadioButton) findViewById(R.id.card_buy_man);
        womenRB = (RadioButton) findViewById(R.id.card_buy_woman);
        cardTypeTV = (TextView) findViewById(R.id.card_type);
        timeET = (EditText) findViewById(R.id.card_buy_time);
        pricesTV = (TextView) findViewById(R.id.card_total_prices);
        pricesTV.setText(String.valueOf(totalPrices));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.service_call:
                dialog();
                break;
            case R.id.buy:
                break;
        }
    }

    /**
     * 点击客服电话弹出窗口是否拨打
     */
    private void dialog() {
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

}
