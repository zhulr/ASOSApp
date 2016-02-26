package com.asosapp.phone.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.asosapp.phone.R;
import com.asosapp.phone.utils.DataCleanManager;

import java.util.Set;

/**
 * Created by ASOS_zhulr on 2016/2/24.
 */
public class SetActivity extends BasicActivity implements View.OnClickListener {

    private TextView titleName;
    private View aboutView;
    private View cleanView;
    private Intent intent;
    private TextView versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        init();
    }

    private void init() {
        aboutView = findViewById(R.id.aboutLL);
        cleanView = findViewById(R.id.clean);
        versionName = (TextView) findViewById(R.id.version_name);
        aboutView.setOnClickListener(this);
        cleanView.setOnClickListener(this);
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText(R.string.set);
        versionName.setText(getVersionName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aboutLL:
                intent.setClass(SetActivity.this, AboutAppActivity.class);
                startActivity(intent);
                break;
            case R.id.clean:
                DataCleanManager.cleanInternalCache(this);
                toast("ª∫¥Ê“—«Â¿Ì");
                break;

        }
    }


    private String getVersionName() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;
        return version;
    }
}
