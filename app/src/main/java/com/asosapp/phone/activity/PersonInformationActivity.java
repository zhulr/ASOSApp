package com.asosapp.phone.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asosapp.phone.R;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by Leo on 2016/2/24.
 */
public class PersonInformationActivity extends Activity implements View.OnClickListener {

    private ImageView back_btn;
    private TextView sexy;
    private LinearLayout sexy_layout;
    private TextView account;
    private LinearLayout age_layout;
    private TextView age;
    private LinearLayout name_layout;
    private TextView name;
    private final static int SELECT_AGE_REQUEST_CODE = 1;
    private final static int SELECT_NAME_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_person_information);
        init();//初始化
    }

    private void init() {
        back_btn= (ImageView) findViewById(R.id.back);
        sexy= (TextView) findViewById(R.id.sexy);
        sexy_layout= (LinearLayout) findViewById(R.id.sexy_layout);
        account= (TextView) findViewById(R.id.account);
        age_layout= (LinearLayout) findViewById(R.id.age_layout);
        age= (TextView) findViewById(R.id.age);
        name_layout= (LinearLayout) findViewById(R.id.name_layout);
        name= (TextView) findViewById(R.id.name);
        SharedPreferences sp = this.getSharedPreferences("UserInfo", 1); //私有数据
        account.setText(sp.getString("user_name", null));
        sexy.setText(sp.getString("user_sexy", null));
        age.setText(sp.getString("user_age", null));
        name.setText(sp.getString("new_name",null));
        onclickListen();//点击事件
    }

    private void onclickListen() {
        back_btn.setOnClickListener(this);
        sexy_layout.setOnClickListener(this);
        age_layout.setOnClickListener(this);
        name_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                onBackPressed();
                finish();
                break;
            case R.id.sexy_layout:
                showSexDialog();
                break;
            case R.id.age_layout:
                changeActivity(1);
                break;
            case  R.id.name_layout:
                changeActivity(0);
                break;
            default :
                break;
        }

    }

    private void changeActivity(int type) {
        Intent intent=new Intent();
        switch (type){
            case 0:
                intent.setClass(this,AgeChangeActivity.class);
                intent.putExtra("oldage",age.getText().toString());
                startActivityForResult(intent, SELECT_AGE_REQUEST_CODE);
                break;
            case 1:
                intent.setClass(this,NameChangeActivity.class);
                intent.putExtra("oldname",name.getText().toString());
                startActivityForResult(intent, SELECT_NAME_REQUEST_CODE);
                break;
            default:
                break;
        }



    }

    /**
     * 性别选择器
     */
    public void showSexDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_set_sex, null);
        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.show();
        RelativeLayout manRl = (RelativeLayout) view.findViewById(R.id.man_rl);
        RelativeLayout womanRl = (RelativeLayout) view.findViewById(R.id.woman_rl);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.man_rl:
                        setGender(true);
                        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", 1); //私有数据
                        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                        editor.putString("user_sexy", "男");
                        editor.commit();//提交修改
                        dialog.cancel();
                        break;
                    case R.id.woman_rl:
                        setGender(false);
                        SharedPreferences sp = getSharedPreferences("UserInfo", 1); //私有数据
                        SharedPreferences.Editor et = sp.edit();//获取编辑器
                        et.putString("user_sexy", "女");
                        et.commit();//提交修改
                        dialog.cancel();
                        break;
                }
            }
        };
        manRl.setOnClickListener(listener);
        womanRl.setOnClickListener(listener);
    }

    public void setGender(boolean isMan) {
        if (isMan) {
            sexy.setText("男");
        } else {
            sexy.setText("女");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            if (requestCode==SELECT_AGE_REQUEST_CODE){
                age.setText(data.getStringExtra("userage"));
            }else if (requestCode==SELECT_NAME_REQUEST_CODE){
                name.setText(data.getStringExtra("newname"));
            }
        }
    }
}
