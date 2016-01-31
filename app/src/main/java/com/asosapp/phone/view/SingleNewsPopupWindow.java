package com.asosapp.phone.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
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

import java.util.List;
import java.util.Map;

/**
 * Created by ASOS_zhulr on 2015/12/28.
 * 新闻快速阅读弹框
 */
public class SingleNewsPopupWindow extends PopupWindow {
    private View conentView;
    private int ID;
    private Activity context;
    private TextView titleView;
    private TextView introView;
    private String TAG = "SingleNewsPopupWindow";

    public SingleNewsPopupWindow(final Activity context, int ID, List<Map<String, Object>> newsList) {
        this.ID = ID;
        this.context=context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.layout_singlenews_popupwindow, null);

        titleView = (TextView) conentView.findViewById(R.id.pop_single_news_title);
        introView = (TextView) conentView.findViewById(R.id.pop_single_news_content);
        getdata();


        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w - 200);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(h / 2 + 300);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xEEFFFFFF);
        ColorDrawable dw = new ColorDrawable(0x000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 0.4f;
        context.getWindow().setAttributes(lp);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(android.R.style.Animation_Dialog);
//        this.setAnimationStyle(R.style.AnimationPreview);
        showSingleNews();
//        conentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = context.getWindow().getAttributes();
                lp.alpha = 1f;
                context.getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 获取数据
     */
    JSONObject DATA;
    private void getdata() {
        String url = Const.SERVICE_URL + Const.NEWSDETAILS + "?newsID=" + ID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("CODE").toString().equals("200")) {
                        DATA = (JSONObject) jsonObject.get("DATA");
                        titleView.setText(DATA.get("NEWS_TITLE").toString());
                        introView.setText(DATA.get("NEWS_CONTENT").toString());
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
                    ToastView.NetError(context);
                } else if (volleyError instanceof com.android.volley.TimeoutError) {
                    ToastView.NetTimeOut(context);
                } else {
                    ToastView.toast(context, volleyError.toString());
                }
            }
        });
        request.setTag(TAG);
        MyApplication.getHttpQueues().add(request);
    }

    private void showSingleNews() {

    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 100, 0);
        } else {
            this.dismiss();
        }
    }


}

