package com.asosapp.phone.network;

import com.android.volley.VolleyError;

/**
 * Created by ASOS_zhulr on 2015/12/23.
 */
public interface HttpRequestCallback<T> {
    public void onResult(T result);

    void onErrorResponse(VolleyError error);
}