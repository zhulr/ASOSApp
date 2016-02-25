package com.asosapp.phone.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asosapp.phone.R;
import com.asosapp.phone.activity.CardsDetailsActivity;
import com.asosapp.phone.initprogram.MyApplication;
import com.asosapp.phone.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ASOS_zhulr on 2015/12/7.
 */
public class BuyCardsFragment extends Fragment implements View.OnClickListener {

    String TAG = "BuyCardsFragment";
    private View view;
    private View heartView;
    private View driverView;
    private ImageView heartCard;
    private ImageView driverCard;
    SharedPreferences sharedPreferences;
    private boolean isLogin = false;
    private TextView titleName;
    Button heartCardBuybtn;
    Button driverCardBuybtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_buy_cards, container, false);
        sharedPreferences = getActivity().getSharedPreferences("UserInfo", 1);
        isLogin = sharedPreferences.getBoolean("isLogin", false);
        init();
        return view;
    }

    private void init() {
        titleName = (TextView) view.findViewById(R.id.title_name);
        titleName.setText(R.string.cards);
        heartView = view.findViewById(R.id.heartcard_details);
        driverView = view.findViewById(R.id.drivercard_details);
        heartCard = (ImageView) view.findViewById(R.id.iv_card1);
        heartCard.setOnClickListener(this);
        driverCard = (ImageView) view.findViewById(R.id.iv_card2);
        driverCard.setOnClickListener(this);
        heartCardBuybtn = (Button) view.findViewById(R.id.heat_buy);
        heartCardBuybtn.setOnClickListener(this);
        driverCardBuybtn = (Button) view.findViewById(R.id.drvier_buy);
        driverCardBuybtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_card1:
                BeTOGone("HEART");
                break;
            case R.id.iv_card2:
                BeTOGone("DRIVER");
                break;
            case R.id.heat_buy:
                intent("HEART");
                break;
            case R.id.drvier_buy:
                intent("DRIVER");
                break;
        }

    }

    /**
     * 卡片详情展示隐藏
     * return
     */
    private void BeTOGone(String i) {
        switch (i) {
            case "HEART":
                if (heartView.getVisibility() == View.GONE) {
                    heartView.setVisibility(View.VISIBLE);
                } else if (heartView.getVisibility() == View.VISIBLE) {
                    heartView.setVisibility(View.GONE);
                } else {
                    heartView.setVisibility(View.VISIBLE);
                }
                break;
            case "DRIVER":
                if (driverView.getVisibility() == View.GONE) {
                    driverView.setVisibility(View.VISIBLE);
                } else if (driverView.getVisibility() == View.VISIBLE) {
                    driverView.setVisibility(View.GONE);
                } else {
                    driverView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void intent(String i) {
        Intent intent = new Intent();
        intent.putExtra("type", i);
        intent.setClass(getActivity(), CardsDetailsActivity.class);
        startActivity(intent);
    }

    private void toast(String mess) {
        Toast.makeText(getActivity(), mess, Toast.LENGTH_LONG).show();
    }

}
