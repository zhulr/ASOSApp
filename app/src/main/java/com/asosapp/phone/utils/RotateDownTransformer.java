package com.asosapp.phone.utils;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Leo on 2016/3/16.
 */
public class RotateDownTransformer implements ViewPager.PageTransformer {

    // 旋转的最大角度为20度
    private static final float MAX_ROTATE = 20.0f;
    // 旋转过程中的角度
    private float currentRotate;

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        Log.i("TAG", "view = " + view + ",position = " + position);
        if (position < -1) {
            ViewHelper.setRotation(view, 0);
        } else if (position <= 0) {
            // position范围[-1.0,0.0],此时A页动画移出屏幕
            currentRotate = position * MAX_ROTATE;
            // 设置当前页的旋转中心点，横坐标是屏幕宽度的1/2,纵坐标为屏幕的高度
            ViewHelper.setPivotX(view, pageWidth / 2);
            ViewHelper.setPivotY(view, view.getHeight());
            ViewHelper.setRotation(view, currentRotate);
        } else if (position <= 1) {
            // position范围(0.0,1.0],此时B页动画移到屏幕
            currentRotate = position * MAX_ROTATE;
            // 设置当前页的旋转中心点，横坐标是屏幕宽度的1/2,纵坐标为屏幕的高度
            ViewHelper.setPivotX(view, pageWidth / 2);
            ViewHelper.setPivotY(view, view.getHeight());
            ViewHelper.setRotation(view, currentRotate);
        } else {
            ViewHelper.setRotation(view, 0);
        }
    }
}