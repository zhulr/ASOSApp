package com.asosapp.phone.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asosapp.phone.R;
import com.asosapp.phone.activity.CardsDetailsActivity;
import com.asosapp.phone.activity.LoginActivity;
import com.asosapp.phone.activity.PersonalCenterActivity;
import com.asosapp.phone.view.ToastView;

/**
 * Created by ASOS_zhulr on 2015/12/7.
 */
public class BuyCardsFragment extends Fragment implements View.OnClickListener {
    private View view;
    private View personalCenterView;
    private TextView userName;
    private ImageView userHead;
    private View cardToBuyView;
    SharedPreferences sharedPreferences;
    private boolean isLogin = false;
    private TextView titleName;

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
//        personalCenterView = view.findViewById(R.id.login_top_item);
//        userName = (TextView) view.findViewById(R.id.user_nickname);
//        userHead = (ImageView) view.findViewById(R.id.user_head);
//        if (isLogin) {
//            userName.setText(sharedPreferences.getString("user_nickname", ""));
//            userName.setTextSize(20);
//            userHead.setImageResource(R.mipmap.asos_round_logo);
//        }
//        personalCenterView.setOnClickListener(this);
        titleName= (TextView) view.findViewById(R.id.title_name);
        titleName.setText(R.string.cards);
        cardToBuyView = view.findViewById(R.id.cardtobuy);
        cardToBuyView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.login_top_item:
//                intent(1);
//                break;
            case R.id.cardtobuy:
                intent(2);
                break;
        }

    }

    private void intent(int i) {
        if (i == 1) {
            isLogin = sharedPreferences.getBoolean("isLogin", false);
            if (isLogin)
                startActivity(new Intent(getActivity(), PersonalCenterActivity.class));
            else
                startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            startActivity(new Intent(getActivity(), CardsDetailsActivity.class));
        }
    }
}
