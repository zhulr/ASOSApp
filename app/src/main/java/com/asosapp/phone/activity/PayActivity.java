package com.asosapp.phone.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.alipay.sdk.app.PayTask;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asosapp.phone.R;
import com.asosapp.phone.alipay.PayResult;
import com.asosapp.phone.alipay.SignUtils;
import com.asosapp.phone.bean.UserInfo;
import com.asosapp.phone.initprogram.MyApplication;
import com.asosapp.phone.utils.Const;
import com.asosapp.phone.view.ToastView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class PayActivity extends FragmentActivity {
    private static String TAG = "PayActivity";
    private String userPhone = "";
    String cardName = "安邦卡";
    String cardContent = "安邦卡付款";
    String cardPrice = "0";
    private TextView nameTV;
    private TextView contentTV;
    private TextView priceTV;
    private String URL = "";
    // 商户PID
    public static final String PARTNER = "2088121911919459";
    // 商户收款账号
    public static final String SELLER = "service@anbsos.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANFsV/+HutKED6xR\n" +
            "TMRu81GZ27he1RQOb55LywDMBVgAr6JgHu8Nr6iqsXtzAnPENQHGCtDVY6FGo1g6\n" +
            "6OT9Ik4GpsV8klPUxCqb5ZApVoD4ibLqlgH7OIo42m6Qi4K0Gd6m+9fPqFSOE1Bw\n" +
            "TjKdVXJobLouOIv/ocHfnW0N+z6bAgMBAAECgYAotesVXYlPyyRQ1y0ZMohSYJp5\n" +
            "6NV7JKNGHm3EJeUUCm4aHp1k00BGhYgi+2SKpKW5Roprtk+71kOCr1D+0saMEi8b\n" +
            "wi2jsjnRcOOpn3D7au3zPTnaKED2xzFWspq8M+un2oWdn3aMPETFXPsDh+2uKYnc\n" +
            "zqRVBb+EDrG0oUzeAQJBAPm/PV4f3bC+BQ2AoZPgQmZqIgg7ISIzxE5lv7HmRGlf\n" +
            "LahMz8/3mAvTCEHX4l7BAU427bScP0XGrm9T1VVizsECQQDWqqXAiflmEDNTnd5d\n" +
            "oMliHGM/KxEIDoSlkhAgcfymruFrFRtZEN9/GJ+Yod4M4faftyh/DaM//mfKMZLW\n" +
            "i8BbAkBdgFOhYZzGBt4tZ/MWQeeaIDINktWc0HS2RTG3kNYb/R5C3D8RRUkPZDq7\n" +
            "K+/8OZYbikUEUVr+7Jtx4pkEFrIBAkEAucsEnrpMQwvugIZ4eSy6X9IWDAwoN4Lp\n" +
            "R6w9jPdbJqDhtR6VhubVqVkUeLRQTXIdVGrf/+c7zh1Qk22Im2xlVQJBAOi37EBK\n" +
            "yPJ3/bHMHECK7GFANvN41g+/pWkiDArh0cxAjCrVZEZoPBl68jz8yC0XCFvGlpw5\n" +
            "E5nlAUsySrz6Z8M=";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        intoInfo();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//						Toast.makeText(PayActivity.this, resultStatus, Toast.LENGTH_SHORT).show();
                            Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(PayActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_main);
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserInfo", 1);
        if (!sharedPreferences.getBoolean("isLogin", false)) {
            Toast.makeText(this, "请先登录!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(PayActivity.this, LoginNewActivity.class);
            startActivity(intent);
            this.finish();
            return;
        }
        userPhone = sharedPreferences.getString("user_phone", "0");
        if (userPhone.equals("0")) {
            Toast.makeText(this, "登录信息错误，请重新登录后购买！", Toast.LENGTH_SHORT).show();
            UserInfo.instance().logOut(this);
            Intent intent = new Intent();
            intent.setClass(PayActivity.this, LoginNewActivity.class);
            startActivity(intent);
            this.finish();
            return;
        }
//        Toast.makeText(this, userPhone, Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        String value = intent.getStringExtra("type");
        if (value.equals("BOSS")) {
            URL = Const.SERVICE_URL + Const.BOSSCARD;
        } else if (value.equals("DRIVER")) {
            URL = Const.SERVICE_URL + Const.DRIVERCARD;
        }
        cardName = intent.getStringExtra("name");
        cardContent = intent.getStringExtra("content");
        cardPrice = intent.getStringExtra("totalPrices");
        nameTV = (TextView) findViewById(R.id.product_subject);
        contentTV = (TextView) findViewById(R.id.pay_content);
        priceTV = (TextView) findViewById(R.id.product_price);
        nameTV.setText(cardName);
        contentTV.setText(cardContent);
        priceTV.setText(cardPrice);
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(View v) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }
        String orderInfo = getOrderInfo(cardName, cardContent, cardPrice);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(PayActivity.this);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {
        Intent intent = new Intent(this, H5PayActivity.class);
        Bundle extras = new Bundle();
        /**
         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
         * 商户可以根据自己的需求来实现
         */
        String url = "http://m.taobao.com";
        // url可以是一号店或者美团等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
        extras.putString("url", url);
        intent.putExtras(extras);
        startActivity(intent);

    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * 写入数据库
     */
    private void intoInfo() {
        String url = URL + "?userPhone=" + userPhone;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("CODE").toString().equals("200")) {
                        Toast.makeText(PayActivity.this, "购买成功，请进入我的卡券查看！", Toast.LENGTH_LONG).show();
                    } else if (jsonObject.get("CODE").toString().equals("100")) {
                        ToastView.toast(getApplication(), jsonObject.get("MESSAGE").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof NoConnectionError) {
                    ToastView.NetError(getApplication());
                } else if (volleyError instanceof com.android.volley.TimeoutError) {
                    ToastView.NetTimeOut(getApplication());
                } else {
                    ToastView.toast(getApplication(), volleyError.toString());
                }
            }
        });
        request.setTag(TAG);
        MyApplication.getHttpQueues().add(request);
    }

}
