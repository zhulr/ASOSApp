package com.asosapp.phone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asosapp.phone.R;
import com.asosapp.phone.activity.MainActivity;
import com.asosapp.phone.activity.NewsHTMLActivity;
import com.asosapp.phone.adapter.NewsAdapter;
import com.asosapp.phone.bean.News;
import com.asosapp.phone.initprogram.MyApplication;
import com.asosapp.phone.utils.Const;
import com.asosapp.phone.utils.DividerItemDecoration;
import com.asosapp.phone.view.SingleNewsPopupWindow;
import com.asosapp.phone.view.SlideShowView;
import com.asosapp.phone.view.ToastView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ASOS_zhulr on 2015/12/7.
 */
public class NewsFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView newsView;
    private Handler handler = new Handler();
    private RecyclerView mRecyclerView;
    private List<Map<String, Object>> mDatas;
    private NewsAdapter mAdapter;
    private ImageView none;
    private SlideShowView slideshowView;
    private String TAG = "NewsFragment";
    private List<String> idList = new ArrayList<String>();
    private List<String> introList = new ArrayList<String>();
    private List<String> titleList = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, null);
        initView();
        jsonData();
        //设置RecycleView对的布局管理
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置RecycleView的上下滑动监听事件
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (slideshowView.getVisibility() == View.VISIBLE) {
                        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.view_visible));
                        slideshowView.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });
        //设置RecycleView的Item间分割线
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        return view;
    }

    //调用接口获取数据
    private void jsonData() {
        introList.clear();
        titleList.clear();
        idList.clear();

        String url = Const.SERVICE_URL + Const.NEWS;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    if (jsonObject.get("CODE").toString().equals("200")) {
                        JSONArray array = jsonObject.getJSONArray("DATA");
                        for (int i = array.length() - 1; i >= 0; i--) {
                            JSONObject object = array.getJSONObject(i);
                            idList.add(object.getString("ID"));
                            introList.add(object.getString("NEWS_INTRO"));
                            titleList.add(object.getString("NEWS_TITLE"));
                        }

                    } else if (jsonObject.get("CODE").toString().equals("100")) {
                        ToastView.toast(getActivity(), jsonObject.get("MESSAGE").toString());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDatas = getDatas();
                mAdapter = new NewsAdapter(getActivity(), mDatas);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), NewsHTMLActivity.class);
                        intent.putExtra("newsID", idList.get(position).toString());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
//                Toast.makeText(getActivity(), "long click:" + position, Toast.LENGTH_SHORT).show();
                        SingleNewsPopupWindow singleNewsPopupWindow = new SingleNewsPopupWindow(getActivity(), Integer.parseInt(idList.get(position).toString()), mDatas);
                        singleNewsPopupWindow.showPopupWindow(none);
                    }
                });
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

    //将数据分装到map里，返回list
    private List<Map<String, Object>> getDatas() {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < introList.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("intro", introList.get(i).toString());
            map.put("title", titleList.get(i).toString());
            map.put("id", idList.get(i).toString());

            listItems.add(map);
        }

        return listItems;
    }


    private void initView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        slideshowView = (SlideShowView) view.findViewById(R.id.slideshowView);
        none = (ImageView) view.findViewById(R.id.none);
    }

    public void newDetailed() {
        startActivity(new Intent(getActivity(), NewsHTMLActivity.class));
    }

    @Override
    public void onClick(View v) {
        newDetailed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
