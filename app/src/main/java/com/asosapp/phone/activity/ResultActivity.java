package com.asosapp.phone.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.asosapp.phone.R;

/**
 * Created by Leo on 2016/4/7.
 */
public class ResultActivity extends Activity {

    private TextView result_c;//医疗费
    private TextView result_d;//住院伙食补助
    private TextView result_e;//营养费
    private TextView result_f;//误工费
    private TextView result_g;//陪护理费
    private TextView result_h;//交通费
    private TextView result_j;//残疾赔偿金
    private TextView result_k;//丧葬费
    private TextView result_l;//死亡赔偿金
    private TextView result_m;//被扶养费
    private TextView result_o;//精神损失费
    private TextView result_p;//财产损失费
    private TextView result_q;//预估损失合计
    private TextView result_r;//交强险合计
    private TextView result_s;//商业险合计



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        init();
        result();
    }

    private void result() {
        Intent intent=getIntent();
        result_c.setText(String.valueOf(intent.getFloatExtra("result_c", 0)));
        result_d.setText(String.valueOf(intent.getFloatExtra("result_d", 0)));
        result_e.setText(String.valueOf(intent.getFloatExtra("result_e", 0)));
        result_f.setText(String.valueOf(intent.getFloatExtra("result_f", 0)));
        result_g.setText(String.valueOf(intent.getFloatExtra("result_g", 0)));
        result_h.setText(String.valueOf(intent.getFloatExtra("result_h", 0)));
        result_j.setText(String.valueOf(intent.getFloatExtra("result_j", 0)));
        result_k.setText(String.valueOf(intent.getFloatExtra("result_k", 0)));
        result_l.setText(String.valueOf(intent.getFloatExtra("result_l", 0)));
        result_m.setText(String.valueOf(intent.getFloatExtra("result_m", 0)));
        result_o.setText(String.valueOf(intent.getFloatExtra("result_o", 0)));
        result_p.setText(String.valueOf(intent.getFloatExtra("result_p", 0)));
        result_q.setText(String.valueOf(intent.getFloatExtra("result_q", 0)));
        result_r.setText(String.valueOf(intent.getFloatExtra("result_r", 0)));
        result_s.setText(String.valueOf(intent.getFloatExtra("result_s", 0)));

    }

    private void init() {
        result_c=(TextView)findViewById(R.id.result_c);
        result_d=(TextView)findViewById(R.id.result_d);
        result_e=(TextView)findViewById(R.id.result_e);
        result_f=(TextView)findViewById(R.id.result_f);
        result_g=(TextView)findViewById(R.id.result_g);
        result_h=(TextView)findViewById(R.id.result_h);
        result_j=(TextView)findViewById(R.id.result_j);
        result_k=(TextView)findViewById(R.id.result_k);
        result_l=(TextView)findViewById(R.id.result_l);
        result_m=(TextView)findViewById(R.id.result_m);
        result_o=(TextView)findViewById(R.id.result_o);
        result_p=(TextView)findViewById(R.id.result_p);
        result_q=(TextView)findViewById(R.id.result_q);
        result_r=(TextView)findViewById(R.id.result_r);
        result_s=(TextView)findViewById(R.id.result_s);

    }
}

