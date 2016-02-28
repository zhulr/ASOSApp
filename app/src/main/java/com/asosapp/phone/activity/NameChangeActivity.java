package com.asosapp.phone.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.asosapp.phone.R;

/**
 * Created by Leo on 2016/2/26.
 */
public class NameChangeActivity extends Activity implements View.OnClickListener {
    private TextView keep;
    private EditText name_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_change);
        init();
    }

    private void init() {

        keep= (TextView) findViewById(R.id.name_keep);
        name_edit= (EditText) findViewById(R.id.name_edit);
        name_edit.setHint(getIntent().getStringExtra("oldname"));
        listener();

    }

    private void listener() {
        keep.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String result=name_edit.getText().toString();
        switch (v.getId()){
            case R.id.keep:
                if (result.equals("")){
                    Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
                }else{
                    keepData();
                    Intent intent = new Intent();
                    intent.putExtra("username", name_edit.getText().toString().trim());
                    setResult(2, intent);
                    finish();
                }
                break;
            default:
                break;
        }

    }

    /**
     *
     * 保存事件
     */
    private void keepData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", 1); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString("user_name", name_edit.getText().toString());
        editor.commit();//提交修改
    }
}
