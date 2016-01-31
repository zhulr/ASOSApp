package com.asosapp.phone.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by ASOS_zhulr on 2015/12/23.
 */
public class NormalPostRequest extends Request<JSONObject> {
    // private static final int SOCKET_TIMEOUT = 10000;
    private Map<String, String> mMap;
    private Response.Listener<JSONObject> mListener;

    public NormalPostRequest(String url, Listener<JSONObject> listener, ErrorListener errorListener, Map<String, String> map) {
        super(Request.Method.POST, url, errorListener);

        mListener = listener;
        mMap = map;
    }

    // mMap是已经按照前面的方式,设置了参数的实例
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    // 此处因为response返回值需要json数据,和JsonObjectRequest类一样即可
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.i("message", jsonString);
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    // 访问超时时间设置
//	@Override
//	public RetryPolicy getRetryPolicy() {
//		RetryPolicy retryPolicy = new DefaultRetryPolicy(30 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//		return retryPolicy;
//	}
}