package com.asosapp.phone.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.asosapp.phone.utils.Const;
import com.asosapp.phone.utils.DeviceHelper;

import org.json.JSONObject;
import java.util.Map;

/**
 * Created by ASOS_zhulr on 2015/12/23.
 */
public class HttpRequest implements HttpRequestCallback {

    public static final String TAG = HttpRequest.class.getSimpleName();
    // 单例
    private static HttpRequest instance;
    // 请求队列
    private RequestQueue requestQueue = null;
    // 提示对话框
    private ProgressDialog mpDialog;
    private HttpRequest() {
    }

    public static HttpRequest getInstance() {
        if (instance == null) {
            instance = new HttpRequest();
        }
        return instance;
    }
    /**
     * map方式提交
     *
     * @param context
     * @param url
     * @param requestMap
     * @param isShow
     * @param tag
     * @param callback
     */
    public void postMap(Context context, final String url,
                     final Map<String, String> requestMap, final boolean isShow,
                     final String tag, final HttpRequestCallback<JSONObject> callback) {
        requestQueue = Volley.newRequestQueue(context);
        if (isShow) {
            showProgress(context, tag);
        }

        final Context fContext = context;
        Request<JSONObject> postRequest = new NormalPostRequest(
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(tag, "response -> " + response.toString());

                if (isShow) {
                    dismissProgress();
                }
                callback.onResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, error.getMessage(), error);

                if (isShow) {
                    dismissProgress();
                }
                if (error instanceof NoConnectionError) {
                    Toast.makeText(fContext, "网络异常，请检测网络连接。", Toast.LENGTH_LONG).show();
                } else if (error instanceof com.android.volley.TimeoutError) {
                    Toast.makeText(fContext, "网络连接超时，请检测网络连接。", Toast.LENGTH_LONG)
                            .show();
                }
                callback.onErrorResponse(error);

            }
        }, requestMap);

        postRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addToRequestQueue(postRequest, tag);
        requestQueue.start();
    }

    /**
     * jsonobject方式提交
     *
     * @param context
     * @param url
     * @param requestMap
     * @param isShow
     * @param tag
     * @param callback
     */
    public void postJson(final Context context, final String url, final JSONObject json,
                      final boolean isShow, final String tag,
                      final HttpRequestCallback<JSONObject> callback) {

        requestQueue = Volley.newRequestQueue(context);
        // 判断网络状况,无网络情况直接返回错误信息
        if (!DeviceHelper.isWifiConnected(context)
                && !DeviceHelper.isMobileNetworkConnected(context)) {
            callback.onErrorResponse(new VolleyError(Const.CONN_ERR));
            return;
        }
        final Context fContext = context;
        if (isShow) {
            showProgress(context, tag);
        }

        Log.i(TAG, "Params:" + json);
        JsonObjectRequest postRequest = new JsonObjectRequest(
                Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject json) {
                        if (isShow) {
                            dismissProgress();
                        }
                        Log.i(tag, "Url:" + url);
                        Log.i(tag, "Request:" + json);
                        callback.onResult(json);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (isShow) {
                    dismissProgress();
                }

                Log.i(TAG, "Error:" + error.getMessage());
                if (error instanceof NoConnectionError) {
                    Toast.makeText(fContext, "网络异常，请检测网络连接。", Toast.LENGTH_LONG).show();
                } else if (error instanceof com.android.volley.TimeoutError) {
                    Toast.makeText(fContext, "网络连接超时，请检测网络连接。", Toast.LENGTH_LONG)
                            .show();
                }
                callback.onErrorResponse(error);
//                Toast.makeText(context, Const.CONN_ERR,Toast.LENGTH_SHORT).show();
            }
        });
        // 设置超时
        postRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 3, 1.0f));
        addToRequestQueue(postRequest, tag);
        requestQueue.start();
    }
    private void showProgress(final Context context, final String tag) {
        if (mpDialog == null) {
            mpDialog = new ProgressDialog(context);
            mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
            mpDialog.setMessage("正在加载...");
            mpDialog.setIndeterminate(false);// 设置进度条是否为不明确
            mpDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
            mpDialog.setCanceledOnTouchOutside(false);
            mpDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    if (!TextUtils.isEmpty(tag)) {
                        cancelPendingRequests(tag);
                    }
                    mpDialog.dismiss();
                }
            });
            if (!mpDialog.isShowing()) {
                mpDialog.show();
            }
        }
    }
    private void dismissProgress() {
        if (mpDialog != null && mpDialog.isShowing()) {
            mpDialog.dismiss();
            mpDialog = null;
        }
    }
    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(tag);
        req.setShouldCache(false);
        requestQueue.add(req);
    }
    /**
     * Cancels all pending requests by the specified TAG, it is important to
     * specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
            Log.d(TAG, "request canceled..." + tag);
        }
    }

    @Override
    public void onResult(Object result) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }


}
