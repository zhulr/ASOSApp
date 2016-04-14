package com.asosapp.phone.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.asosapp.phone.R;
import com.asosapp.phone.view.ToastView;

/**
 * Created by Leo on 2016/4/7.
 */
public class CalculatorsActivity extends Activity implements View.OnClickListener{


    private Spinner domicile;//户籍所在地
    private Spinner  state;//伤者状态
    private EditText age;//伤者年龄
    private EditText one_disability;//一级伤残几处
    private EditText two_disability;//二级伤残几处
    private EditText three_disability;//三级伤残几处
    private EditText four_disability;//四级伤残几处
    private EditText five_disability;//五级伤残几处
    private EditText six_disability;//六级伤残几处
    private EditText seven_disability;//七级伤残几处
    private EditText eight_disability;//八级伤残几处
    private EditText nine_disability;//九级伤残几处
    private EditText ten_disability;//十级伤残几处
    private EditText user_dispensed;//已用医疗费
    private EditText need_dispensed;//还需医疗费
    private EditText dispensed;//后续医疗费
    private EditText hospitalized;//住院总时间
    private EditText delay;//误工期
    private EditText month_money;//月收入损失
    private EditText bus_money;//交通费
    private EditText dependents_life;//被抚养人生活费
    private EditText disaster_money;//精神损失费
    private EditText property_money;//财产损失费
    private Spinner blamed;//事故责任

    private TextView btn;
    private LinearLayout disability;
    private LinearLayout death;

    private float C_sum=0;//医疗费
    private float D_sum=0;//住院伙食补助
    private float E_sum=0;//营养费
    private float F_sum=0;//误工费
    private float G_sum=0;//陪护理费
    private float H_sum=0;//交通费
    private float J_sum=0;//残疾赔偿金
    private float K_sum=0;//丧葬费
    private float L_sum=0;//死亡赔偿金
    private float M_sum=0;//被扶养费
    private float O_sum=0;//精神损失费
    private float P_sum=0;//财产损失费
    private float Q_sum=0;//预估损失合计
    private float R_sum=0;//交强险合计
    private float S_sum=0;//商业险合计

    private float z_c=0;//伤残合计百分比
    private float yl_h=0;//医疗交强险合计
    private float sc_h=0;//伤残交强险合计
    private float cc_h=0;//财产交强险合计

    private float yl_other=0;//医疗其它交强险合计
    private float sc_other=0;//伤残其它交强险合计
    private float cc_other=0;//财产其它交强险合计



    private float age_final=0;//折算后的年龄

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculators_activity);
        init();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                choice();
                    Intent intent = new Intent(CalculatorsActivity.this, ResultActivity.class);
                    intent.putExtra("result_c", get_C());
                    intent.putExtra("result_d", get_D());
                    intent.putExtra("result_e", get_E());
                    intent.putExtra("result_f", get_F());
                    intent.putExtra("result_g", get_G());
                    intent.putExtra("result_h", get_H());
                    intent.putExtra("result_j", get_J());
                    intent.putExtra("result_k", get_K());
                    intent.putExtra("result_l", get_L());
                    intent.putExtra("result_m", get_M());
                    intent.putExtra("result_o", get_O());
                    intent.putExtra("result_p", get_P());
                    intent.putExtra("result_q", get_Q());
                    intent.putExtra("result_r", get_R());
                    intent.putExtra("result_s", get_S());
                    startActivity(intent);
                    break;
        }

    }

    private void init() {
        btn= (TextView) findViewById(R.id.btn);
        btn.setOnClickListener(this);
        domicile= (Spinner) findViewById(R.id.h);//户籍所在地
        state= (Spinner) findViewById(R.id.f);//伤者状态
        age= (EditText) findViewById(R.id.i);//伤者年龄
        one_disability=(EditText) findViewById(R.id.j);//一级伤残几处
        two_disability= (EditText) findViewById(R.id.k);//二级伤残几处
        three_disability= (EditText) findViewById(R.id.l);//三级伤残几处
        four_disability= (EditText) findViewById(R.id.m);//四级伤残几处
        five_disability= (EditText) findViewById(R.id.mn);//五级伤残几处
        six_disability= (EditText) findViewById(R.id.n);//六级伤残几处
        seven_disability= (EditText) findViewById(R.id.o);//七级伤残几处
        eight_disability= (EditText) findViewById(R.id.p);//八级伤残几处
        nine_disability= (EditText) findViewById(R.id.q);//九级伤残几处
        ten_disability= (EditText) findViewById(R.id.r);//十级伤残几处
        dispensed= (EditText) findViewById(R.id.uu);//医疗费
        hospitalized= (EditText) findViewById(R.id.v);//住院总时间
        delay= (EditText) findViewById(R.id.w);//误工期
        month_money= (EditText) findViewById(R.id.x);//月收入损失
        bus_money= (EditText) findViewById(R.id.y);//交通费
        dependents_life= (EditText) findViewById(R.id.bb);//被抚养人生活费
        disaster_money= (EditText) findViewById(R.id.cc);//精神损失费
        property_money= (EditText) findViewById(R.id.dd);//财产损失费
        blamed= (Spinner) findViewById(R.id.ee);//事故责任
        disability= (LinearLayout) findViewById(R.id.disability);
        death= (LinearLayout) findViewById(R.id.death);
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (state.getSelectedItem().equals("普通")) {
                    disability.setVisibility(View.GONE);
                    death.setVisibility(View.GONE);
                } else if (state.getSelectedItem().equals("残疾")) {
                    disability.setVisibility(View.VISIBLE);
                    death.setVisibility(View.VISIBLE);
                } else if (state.getSelectedItem().equals("死亡")) {
                    disability.setVisibility(View.GONE);
                    death.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void choice() {
        if (bus_money.getText().toString().equals("")){
            bus_money.setText("0");
        }
        if (month_money.getText().toString().equals("")){
            month_money.setText("0");
        }
        if (dispensed.getText().toString().equals("")){
            dispensed.setText("0");
        }
        if (dependents_life.getText().toString().equals("")){
            dependents_life.setText("0");
        }
        if (age.getText().toString().equals("")){
            age.setText("0");
        }
        if (property_money.getText().toString().equals("")){
            property_money.setText("0");
        }
        if (disaster_money.getText().toString().equals("")){
            disaster_money.setText("0");
        }
        if (hospitalized.getText().toString().equals("")){
            hospitalized.setText("0");
        }
        if (delay.getText().toString().equals("")){
            delay.setText("0");
        }
        if (one_disability.getText().toString().equals("")){
            one_disability.setText("0");
        }
        if (two_disability.getText().toString().equals("")){
            two_disability.setText("0");
        }
        if (three_disability.getText().toString().equals("")){
            three_disability.setText("0");
        }
        if (four_disability.getText().toString().equals("")){
            four_disability.setText("0");
        }
        if (five_disability.getText().toString().equals("")){
            five_disability.setText("0");
        }
        if (six_disability.getText().toString().equals("")){
            six_disability.setText("0");
        }
        if (seven_disability.getText().toString().equals("")){
            seven_disability.setText("0");
        }
        if (eight_disability.getText().toString().equals("")){
            eight_disability.setText("0");
        }
        if (nine_disability.getText().toString().equals("")){
            nine_disability.setText("0");
        }
        if (ten_disability.getText().toString().equals("")){
            ten_disability.setText("0");
        }
    }


    //医疗费
    private float get_C(){
        C_sum= Float.parseFloat(dispensed.getText().toString());
        return C_sum;
    }
    //住院伙食补助
    private float get_D(){
        D_sum= Float.parseFloat(hospitalized.getText().toString())*50;
        return D_sum;
    }
    //营养费
    private float get_E(){
        E_sum= (Float.parseFloat(delay.getText().toString())/2)*50;
        return E_sum;
    }
    //误工费
    private float get_F(){
        F_sum= ( (((Float.parseFloat(delay.getText().toString())/30)*Float.parseFloat(month_money.getText().toString()))));
        return F_sum;
    }
    //陪护理费
    private float get_G(){
        G_sum= (((Float.parseFloat(delay.getText().toString())/2)*120));
        return G_sum;
    }
    //交通费
    private float get_H(){
        H_sum= (Float.parseFloat(bus_money.getText().toString()));
        return H_sum;
    }

    //残疾赔偿金
    private float get_J() {
        if (state.getSelectedItem().equals("残疾")) {
            if (Float.parseFloat(age.getText().toString())>75){
                age_final=5;
            }else if (80-Float.parseFloat(age.getText().toString())>=20){
                age_final=20;
            }else{
                age_final=80-Float.parseFloat(age.getText().toString());
            }
            /**
             * 算伤残合计
             * */
            if(Float.parseFloat(one_disability.getText().toString())>0&&Float.parseFloat(one_disability.getText().toString())>=1){
                z_c= (float) (1*1+(Float.parseFloat(one_disability.getText().toString())-1)*0.1*1+Float.parseFloat(two_disability.getText().toString())*0.1*0.9+Float.parseFloat(three_disability.getText().toString())*0.1*0.8+Float.parseFloat(four_disability.getText().toString())*0.1*0.7+Float.parseFloat(five_disability.getText().toString())*0.1*0.6+Float.parseFloat(six_disability.getText().toString())*0.1*0.5+Float.parseFloat(seven_disability.getText().toString())*0.1*0.4+Float.parseFloat(eight_disability.getText().toString())*0.1*0.3+Float.parseFloat(nine_disability.getText().toString())*0.1*0.2+Float.parseFloat(ten_disability.getText().toString())*0.1*0.1);
            }else if (Float.parseFloat(two_disability.getText().toString())>=1){
                z_c= (float) (0.9*1+(Float.parseFloat(two_disability.getText().toString())-1)*0.1*0.9+Float.parseFloat(three_disability.getText().toString())*0.1*0.8+Float.parseFloat(four_disability.getText().toString())*0.1*0.7+Float.parseFloat(five_disability.getText().toString())*0.1*0.6+Float.parseFloat(six_disability.getText().toString())*0.1*0.5+Float.parseFloat(seven_disability.getText().toString())*0.1*0.4+Float.parseFloat(eight_disability.getText().toString())*0.1*0.3+Float.parseFloat(nine_disability.getText().toString())*0.1*0.2+Float.parseFloat(ten_disability.getText().toString())*0.1*0.1);
                if (z_c>=1*1){
                    z_c=1*1;
                }
            }else if (Float.parseFloat(three_disability.getText().toString())>=1){
                z_c= (float) (0.8*1+(Float.parseFloat(three_disability.getText().toString())-1)*0.1*0.8+Float.parseFloat(four_disability.getText().toString())*0.1*0.7+Float.parseFloat(five_disability.getText().toString())*0.1*0.6+Float.parseFloat(six_disability.getText().toString())*0.1*0.5+Float.parseFloat(seven_disability.getText().toString())*0.1*0.4+Float.parseFloat(eight_disability.getText().toString())*0.1*0.3+Float.parseFloat(nine_disability.getText().toString())*0.1*0.2+Float.parseFloat(ten_disability.getText().toString())*0.1*0.1);
                if (z_c>=0.9*1){
                    z_c=(float)0.9*1;
                }
            }else if(Float.parseFloat(four_disability.getText().toString())>=1){
                z_c= (float) (0.7*1+(Float.parseFloat(four_disability.getText().toString())-1)*0.1*0.7+Float.parseFloat(five_disability.getText().toString())*0.1*0.6+Float.parseFloat(six_disability.getText().toString())*0.1*0.5+Float.parseFloat(seven_disability.getText().toString())*0.1*0.4+Float.parseFloat(eight_disability.getText().toString())*0.1*0.3+Float.parseFloat(nine_disability.getText().toString())*0.1*0.2+Float.parseFloat(ten_disability.getText().toString())*0.1*0.1);
                if (z_c>=0.8*1){
                    z_c=(float)0.8*1;
                }
            }else if(Float.parseFloat(five_disability.getText().toString())>=1){
                z_c= (float) (0.6*1+(Float.parseFloat(five_disability.getText().toString())-1)*0.1*0.6+Float.parseFloat(six_disability.getText().toString())*0.1*0.5+Float.parseFloat(seven_disability.getText().toString())*0.1*0.4+Float.parseFloat(eight_disability.getText().toString())*0.1*0.3+Float.parseFloat(nine_disability.getText().toString())*0.1*0.2+Float.parseFloat(ten_disability.getText().toString())*0.1*0.1);
                if (z_c>=0.7*1){
                    z_c=(float)0.7*1;
                }
            }else if (Float.parseFloat(six_disability.getText().toString())>=1){
                z_c= (float) (0.5*1+(Float.parseFloat(six_disability.getText().toString())-1)*0.1*0.5+Float.parseFloat(seven_disability.getText().toString())*0.1*0.4+Float.parseFloat(eight_disability.getText().toString())*0.1*0.3+Float.parseFloat(nine_disability.getText().toString())*0.1*0.2+Float.parseFloat(ten_disability.getText().toString())*0.1*0.1);
                if (z_c>=0.6*1){
                    z_c=(float)0.6*1;
                }
            }else if(Float.parseFloat(seven_disability.getText().toString())>=1){
                z_c= (float) (0.4*1+(Float.parseFloat(seven_disability.getText().toString())-1)*0.1*0.4+Float.parseFloat(eight_disability.getText().toString())*0.1*0.3+Float.parseFloat(nine_disability.getText().toString())*0.1*0.2+Float.parseFloat(ten_disability.getText().toString())*0.1*0.1);
                if (z_c>=0.5*1){
                    z_c=(float)0.5*1;
                }
            }else if(Float.parseFloat(eight_disability.getText().toString())>=1){
                z_c= (float) (0.3*1+(Float.parseFloat(eight_disability.getText().toString())-1)*0.1*0.3+Float.parseFloat(nine_disability.getText().toString())*0.1*0.2+Float.parseFloat(ten_disability.getText().toString())*0.1*0.1);
                if (z_c>=0.4*1){
                    z_c=(float)0.4*1;
                }
            }else if(Float.parseFloat(nine_disability.getText().toString())>=1){
                z_c= (float) (0.2*1+(Float.parseFloat(nine_disability.getText().toString())-1)*0.1*0.2+Float.parseFloat(ten_disability.getText().toString())*0.1*0.1);
                if (z_c>=0.3*1){
                    z_c=(float)0.3*1;
                }
            }else if(Float.parseFloat(ten_disability.getText().toString())>=1){
                z_c= (float) (0.1*1+(Float.parseFloat(ten_disability.getText().toString())-1)*0.1*0.1);
                if (z_c>=0.2*1){
                    z_c=(float)0.2*1;
                }
            }

            if (z_c>=1){
                z_c=1;
            }

            if (domicile.getSelectedItem().equals("城镇")){
                J_sum = (float)37173*age_final*z_c;
            }else if(domicile.getSelectedItem().equals("农村")){
                J_sum =(float) 16257*age_final*z_c;
            }
        } else {
            J_sum = 0;
        }
        return J_sum;
    }
    //丧葬费
    private float get_K(){
        if (state.getSelectedItem().equals("死亡")){
            K_sum= (float) (61783/2.0);
        }else{
            K_sum=0;
        }

        return K_sum;

    }

    //死亡赔偿金
    private float get_L(){

        if (Float.parseFloat(age.getText().toString()) > 75) {
            age_final = 5;
        } else if (80 - Float.parseFloat(age.getText().toString()) >= 20) {
            age_final = 20;
        } else {
            age_final = 80 - Float.parseFloat(age.getText().toString());
        }

        if (state.getSelectedItem().equals("死亡")){
            if (domicile.getSelectedItem().equals("城镇")){
                L_sum= (float)37173*age_final;
            }else if(domicile.getSelectedItem().equals("农村")){
                L_sum= (float)16257*age_final;
            }
        }else{
            L_sum=0;
        }
        return L_sum;
    }

    //被扶养费
    private float get_M(){
        M_sum= Float.parseFloat((dependents_life.getText().toString()));
        return M_sum;
    }

    //精神损失费
    private float get_O(){
        O_sum= Float.parseFloat(disaster_money.getText().toString());
        return O_sum;

    }
    //财产损失费
    private float get_P(){
        P_sum= Float.parseFloat(property_money.getText().toString());
        return P_sum;
    }
    //预估损失合计
    private float get_Q(){
        Q_sum= C_sum+D_sum+E_sum+F_sum+G_sum+H_sum+J_sum+K_sum+L_sum+M_sum+O_sum+P_sum;
        return Q_sum;
    }
    //交强险合计
    private float get_R(){
        float a=0;
        float b=0;
        float c=0;
        if (blamed.getSelectedItem().equals("无责")){
            a=1;
            b=11;
            c=1;
        }else {
            a=10;
            b=110;
            c=20;
        }
        if (((C_sum+D_sum+E_sum)*a)/(1*10+0*1)>a*1000){
            yl_h=a*1000;
        }else{
            yl_h=  (((C_sum+D_sum+E_sum)*a)/(1*10+0*1));
        }

        if (((F_sum+G_sum+H_sum+J_sum+K_sum+L_sum+M_sum+O_sum)*b)/(1*110+0*11)>b*1000){
            sc_h=b*1000;
        }else{
            sc_h=  (((F_sum+G_sum+H_sum+J_sum+K_sum+L_sum+M_sum+O_sum)*b)/(1*110+0*11));
        }

        if ((Float.parseFloat(property_money.getText().toString())*c)/(1*1+0*0)>c*100){
            cc_h=c*100;
        }else{
            cc_h=  ((Float.parseFloat(property_money.getText().toString())*c)/(1*1+0*0));
        }

        R_sum= yl_h+sc_h+cc_h;
        return R_sum;
    }
    //商业险合计
    private float get_S(){
        double d=0.0;
        float a=0;
        float b=0;
        float c=0;
        float other_a=0;
        float other_b=0;
        float other_c=0;
        float blamed_a=0;
        float blamed_b=0;
        float blamed_c=0;
        float sum=0;
        float sum1=0;
        float sum2=0;
        float sum3=0;
        if (blamed.getSelectedItem().equals("无责")){
            a=1;
            b=11;
            c=1;
        }else {
            a=10;
            b=110;
            c=20;
        }
        if (blamed.getSelectedItem().equals("全责")||blamed.getSelectedItem().equals("责任无法认定")){
            d=1;
        }else if (blamed.getSelectedItem().equals("主责")){
            d=0.8;
        }else if (blamed.getSelectedItem().equals("同责")){
            d=0.7;
        }else if (blamed.getSelectedItem().equals("次责")){
            d=0.4;
        }else if (blamed.getSelectedItem().equals("无责")){
            d=0;
        }

        if (blamed.getSelectedItem().equals("无责")){
            blamed_a= (1*10+0*1)-1;
            blamed_b=(1*110+0*11)-11;
            blamed_c=(1*20+0*1)-1;
        }else {
            blamed_a= (1*10+0*1)-10;
            blamed_b=(1*110+0*11)-110;
            blamed_c=(1*20+0*1)-20;
        }

        if (((C_sum+D_sum+E_sum)*blamed_a)/(1*10+0*1)>blamed_a*1000){
            yl_other= blamed_a*1000;
        }else{
            yl_other=  (((C_sum+D_sum+E_sum)*blamed_a)/(1*10+0*1));
        }

        if (((F_sum+G_sum+H_sum+J_sum+K_sum+L_sum+M_sum+O_sum)*blamed_b)/(1*110+0*11)>blamed_b*1000){
            sc_other=blamed_b*1000;
        }else{
            sc_other=  (((C_sum+D_sum+E_sum)*blamed_b)/(1*110+0*11));
        }

        if ((Float.parseFloat(property_money.getText().toString())*blamed_c)/(1*1+0*0)>blamed_c*100){
            cc_other=blamed_c*100;
        }else{
            cc_other=  ((Float.parseFloat(property_money.getText().toString())*blamed_c)/(1*1+0*0));
        }

        if (((C_sum+D_sum+E_sum)*a)/(1*10+0*1)>a*1000){
            yl_h=a*1000;
        }else{
            yl_h=  (((C_sum+D_sum+E_sum)*a)/(1*10+0*1));
        }

        if (((F_sum+G_sum+H_sum+J_sum+K_sum+L_sum+M_sum+O_sum)*b)/(1*110+0*11)>b*1000){
            sc_h=b*1000;
        }else{
            sc_h= (((F_sum+G_sum+H_sum+J_sum+K_sum+L_sum+M_sum+O_sum)*b)/(1*110+0*11));
        }

        if ((Float.parseFloat(property_money.getText().toString())*c)/(1*1+0*0)>c*100){
            cc_h=c*100;
        }else{
            cc_h= ((Float.parseFloat(property_money.getText().toString())*c)/(1*1+0*0));
        }

        if ((C_sum+D_sum+E_sum)-yl_h-yl_other>0){
            sum1=(C_sum+D_sum+E_sum)-yl_h-yl_other;
        }else{
            sum1=0;
        }

        if ((F_sum+G_sum+H_sum+J_sum+K_sum+L_sum+M_sum+O_sum)-sc_h-sc_other>0){
            sum2=(F_sum+G_sum+H_sum+J_sum+K_sum+L_sum+M_sum+O_sum)-sc_h-sc_other;
        }else{
            sum2=0;
        }

        if (Float.parseFloat(property_money.getText().toString())-cc_h-cc_other>0){
            sum3=Float.parseFloat(property_money.getText().toString())-cc_h-cc_other;
        }else{
            sum3=0;
        }

        sum= sum1+sum2+sum3;

        if ((d*sum)>100*10000){
            S_sum=  100*10000;
        }else{
            S_sum= (float) (d*sum);
        }
        return S_sum;
    }


}

