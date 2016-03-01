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
    private ImageView bossCard;
    private ImageView driverCard;
    private TextView titleName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_buy_cards, container, false);
        init();
        return view;
    }

    private void init() {
        titleName = (TextView) view.findViewById(R.id.title_name);
        titleName.setText(R.string.cards);
        driverCard = (ImageView) view.findViewById(R.id.iv_card1);
        driverCard.setOnClickListener(this);
        bossCard = (ImageView) view.findViewById(R.id.iv_card2);
        bossCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_card1:
                intent("DRIVER");
                break;
            case R.id.iv_card2:
                intent("BOSS");
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
