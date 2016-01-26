package com.asosapp.phone.activity;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by ASOS_zhulr on 2016/1/22.
 */
public class BasicActivity extends Activity {
    public void toast(String mess) {
        Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT);
    }
}
