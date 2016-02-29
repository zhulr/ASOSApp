package com.asosapp.phone.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by ASOS_lijianfeng on 2016/1/22.
 *
 * 输出屏幕信息
 */
public class ToastView extends LinearLayout{
    public ToastView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static void toast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static void NetError(Context context){
        Toast.makeText(context,"网络异常，请检测网络连接。",Toast.LENGTH_SHORT).show();
    }

    public static void NetTimeOut(Context context){
        Toast.makeText(context,"网络连接超时，请检测网络连接。",Toast.LENGTH_SHORT).show();
    }
}
