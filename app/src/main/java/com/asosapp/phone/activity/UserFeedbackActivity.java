package com.asosapp.phone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.asosapp.phone.R;

/**
 * Created by ASOS_zhulr on 2016/2/22.
 * 留言用户反馈界面
 */
public class UserFeedbackActivity extends BasicActivity {

    private EditText message;
    private TextView messageBtn;
    private TextView titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        init();
    }

    private void init() {
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText(R.string.feedback);
        message = (EditText) findViewById(R.id.message);
        messageBtn = (TextView) findViewById(R.id.message_btn);
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("留言成功");
                message.setText("");
            }
        });
    }
}
