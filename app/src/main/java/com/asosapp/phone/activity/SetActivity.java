package com.asosapp.phone.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.asosapp.phone.R;
import com.asosapp.phone.utils.DataCleanManager;
import com.asosapp.phone.view.ToastView;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by ASOS_zhulr on 2016/2/24.
 */
public class SetActivity extends BasicActivity implements View.OnClickListener {

    private TextView titleName;
    private View aboutView;
    private View cleanView;
    private View updateView;
    private Intent intent;
    private TextView versionName;
    private TextView change_psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        intent=new Intent();
        init();
    }

    private void init() {
        aboutView = findViewById(R.id.aboutLL);
        cleanView = findViewById(R.id.clean);
        updateView = findViewById(R.id.update);
        change_psw = (TextView) findViewById(R.id.change_psw);
        change_psw.setOnClickListener(this);
        versionName = (TextView) findViewById(R.id.version_name);
        aboutView.setOnClickListener(this);
        cleanView.setOnClickListener(this);
        updateView.setOnClickListener(this);
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText(R.string.set);
        versionName.setText(getVersionName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aboutLL:
                Intent intentAbout = new Intent();
                intentAbout.setClass(SetActivity.this, AboutAppActivity.class);
                startActivity(intentAbout);
                break;
            case R.id.clean:
                DataCleanManager.cleanInternalCache(this);
                toast("缓存已清理");
                break;
            case R.id.change_psw:
                Dialog dialog =createResetPwdDialog();
                dialog.show();
                break;
            case R.id.update:
                toast("已经是最新版！");
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

    private Dialog createResetPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_reset_password, null);
        builder.setView(view);
        final Dialog dialog = builder.create();
        final EditText pwdEt = (EditText) view.findViewById(R.id.password_et);
        final Button cancel = (Button) view.findViewById(R.id.cancel_btn);
        final Button commit = (Button) view.findViewById(R.id.commit_btn);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.cancel_btn:
                        dialog.cancel();
                        break;
                    case R.id.commit_btn:
                        String input = pwdEt.getText().toString().trim();
                        if (JMessageClient.isCurrentUserPasswordValid(input)) {
                            Intent intent = new Intent();
                            intent.putExtra("oldPassword", input);
                            intent.setClass(SetActivity.this, ChangePasswordActivity.class);
                            startActivity(intent);
                            dialog.cancel();
                        } else {
                            Toast toast = Toast.makeText(SetActivity.this, getString(R.string.input_password_error_toast), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        break;
                }

            }
        };
        cancel.setOnClickListener(listener);
        commit.setOnClickListener(listener);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
}