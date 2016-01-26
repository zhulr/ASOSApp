package com.asosapp.phone.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asosapp.phone.R;
import com.asosapp.phone.initprogram.MyApplication;
import com.asosapp.phone.utils.BitmapCache;
import com.asosapp.phone.utils.Const;
import com.asosapp.phone.view.ToastView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ASOS_zhulr on 2015/12/9.
 * 新闻详情页面
 */
public class NewsDetailedActivity extends Activity implements View.OnClickListener{
    private Context context=NewsDetailedActivity.this;
    private ImageView back_btn;
    private TextView detail_title;
    private TextView detail_time;
    private TextView detail_intro;
    private TextView detail_content;
    private ImageView detail_img;
    private String newsID;
    private String TAG = "NewsDetailedActivity";
    private ImageLoader mImageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detailed);
        Intent intent = getIntent();
        newsID = intent.getStringExtra("newsID");
        ToastView.toast(context, newsID);
        mImageLoader = new ImageLoader(MyApplication.getHttpQueues(), new BitmapCache());
        init();
//        jsonData();
        volley_Get();
    }

    /**
     * post请求
     * */
    private void jsonData(){

        String Url= Const.SERVICE_URL+Const.NEWSDETAILS;
        JSONObject object=new JSONObject();
        try {
            object.put("newsID",newsID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("CODE").toString().equals("200")) {
                       JSONObject detailObject= (JSONObject) jsonObject.get("DATA");
                        detail_title.setText(detailObject.get("NEWS_TITLE").toString());
                        detail_time.setText(detailObject.get("NEWS_DATE").toString());
                        detail_content.setText(detailObject.get("NEWS_CONTENT").toString());
                    } else if (jsonObject.get("CODE").toString().equals("100")) {
                        Toast.makeText(NewsDetailedActivity.this, jsonObject.get("MESSAGE").toString(), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof NoConnectionError) {
                    Toast.makeText(NewsDetailedActivity.this, "网络连接超时，请检测网络连接", Toast.LENGTH_SHORT).show();
                } else if (volleyError instanceof com.android.volley.TimeoutError) {
                    Toast.makeText(NewsDetailedActivity.this, "网络连接超时，请检测网络连接", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewsDetailedActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        request.setTag(TAG);
        MyApplication.getHttpQueues().add(request);

    }

    private void init() {
        detail_title= (TextView) findViewById(R.id.detail_title);
        detail_time= (TextView) findViewById(R.id.detail_time);
        detail_content= (TextView) findViewById(R.id.detail_content);
        detail_img= (ImageView) findViewById(R.id.detail_img);
        back_btn= (ImageView) findViewById(R.id.back_btn);

        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_btn:
                onBackPressed();
        }
    }


/**
 * get请求
 *
 * **/
    JSONObject DATA = null;
    private void volley_Get() {
        String url = Const.SERVICE_URL + Const.NEWSDETAILS + "?newsID=" + newsID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("CODE").toString().equals("200")) {
                        DATA = (JSONObject) jsonObject.get("DATA");
                        detail_title.setText(DATA.get("NEWS_TITLE").toString());
                        detail_time.setText(DATA.get("NEWS_DATE").toString());
                        detail_content.setText(DATA.get("NEWS_CONTENT").toString().replace("<br>","\n"));
                        ImageLoader.ImageListener listener = ImageLoader.getImageListener(detail_img, 0, R.drawable.mz_img_error);
                        mImageLoader.get(DATA.get("NEWS_IMG").toString(), listener);


                    } else if (jsonObject.get("CODE").toString().equals("100")) {
                        ToastView.toast(context,jsonObject.get("MESSAGE").toString() );
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
                    ToastView.toast(context,volleyError.toString());
                }
            }
        });
        request.setTag(TAG);
        MyApplication.getHttpQueues().add(request);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO 取消所有未执行完的网络请求
        MyApplication.getHttpQueues().cancelAll(this);
    }
}
