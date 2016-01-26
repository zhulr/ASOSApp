package com.asosapp.phone.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by ASOS_zhulr on 2016/1/22.
 */
public class TagInfo {
    String url;
    int position;
    Drawable drawable;

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public Drawable getDrawable() {
        return drawable;
    }
    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

}

