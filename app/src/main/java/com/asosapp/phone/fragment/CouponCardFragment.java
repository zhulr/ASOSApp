package com.asosapp.phone.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asosapp.phone.R;
import com.asosapp.phone.adapter.CouponCardAdapter;
import com.asosapp.phone.initprogram.MyApplication;
import com.asosapp.phone.utils.Const;
import com.asosapp.phone.view.ToastView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASOS_zhulr on 2016/2/23.
 */
public class CouponCardFragment extends Fragment {
    private View view;
    private CouponCardAdapter mAdapter;
    private RecyclerView card_recyclerView;
    private List<String> mData;

    private String TAG = "CouponCardFragment";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_coupon_card,null);
        init();
        json();
        return view;
    }

    /**
     *
     * 数据获取
     */
    private void json() {
        SharedPreferences sp = getActivity().getSharedPreferences("UserInfo", 1); //私有数据
        String url = Const.SERVICE_URL + Const.SEARCHCARD + "?userPhone=" + sp.getString("user_phone",null);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("CODE").toString().equals("200")) {
                        JSONArray array = jsonObject.getJSONArray("DATA");
                        for (int i =0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            mData.add(object.getString("TYPE"));
                            mAdapter=new CouponCardAdapter(getActivity(),mData);
                            card_recyclerView.setAdapter(mAdapter);
                        }


                    } else if (jsonObject.get("CODE").toString().equals("100")) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof NoConnectionError) {
                    ToastView.NetError(getActivity());
                } else if (volleyError instanceof com.android.volley.TimeoutError) {
                    ToastView.NetTimeOut(getActivity());
                } else {
                    ToastView.toast(getActivity(), volleyError.toString());
                }
            }
        });
        request.setTag(TAG);
        MyApplication.getHttpQueues().add(request);
    }


    private void init() {
        mData=new ArrayList<String>();
        card_recyclerView= (RecyclerView) view.findViewById(R.id.card_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        card_recyclerView.setLayoutManager(linearLayoutManager);


    }
}
