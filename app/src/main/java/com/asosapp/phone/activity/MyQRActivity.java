package com.asosapp.phone.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.asosapp.phone.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * Created by ASOS_zhulr on 2016/2/22.
 */
public class MyQRActivity extends BasicActivity {

    private int QR_WIDTH = 800, QR_HEIGHT = 800;
    private TextView titleName;
    private ImageView myQR;
    private TextView myCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr);
        init();
    }

    private void init() {
        titleName = (TextView) findViewById(R.id.title_name);
        myQR = (ImageView) findViewById(R.id.my_qr);
        myCode = (TextView) findViewById(R.id.my_code);
        titleName.setText(R.string.invite);
        createQRImage("http://www.baidu.com");
    }

    public void createQRImage(String url) {
        try {
            //�ж�URL�Ϸ���
            if (url == null || "".equals(url) || url.length() < 1) {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //ͼ������ת����ʹ���˾���ת��
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //�������ﰴ�ն�ά����㷨��������ɶ�ά���ͼƬ��
            //����forѭ����ͼƬ����ɨ��Ľ��
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //���ɶ�ά��ͼƬ�ĸ�ʽ��ʹ��ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            //��ʾ��һ��ImageView����
            myQR.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
