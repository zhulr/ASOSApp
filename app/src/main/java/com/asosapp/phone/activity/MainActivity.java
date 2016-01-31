package com.asosapp.phone.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.asosapp.phone.R;
import com.asosapp.phone.fragment.BuyCardsFragment;
import com.asosapp.phone.fragment.NewsFragment;
import com.asosapp.phone.fragment.OnlineConsultFragment;
import com.asosapp.phone.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by ASOS_zhulr on 2015/11/20.
 * 主界面
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private FragmentPagerAdapter mAdapter;
    private NoScrollViewPager mViewPager;
    private List<Fragment> mDatas = new ArrayList<Fragment>();
    private View newsLayout;
    private View onlineConsultLayout;
    private View buyCardsLayout;
    private ImageView newsImageView;
    private ImageView onlineConsultImageView;
    private ImageView buyCardsImageView;
    private TextView newsTextView;
    private TextView onlineConsultTextView;
    private TextView buyCardsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDatas();
    }
    private void initView() {
        mViewPager = (NoScrollViewPager) findViewById(R.id.id_viewpager);
        newsLayout = findViewById(R.id.layout_news);
        newsLayout.setOnClickListener(this);
        onlineConsultLayout = findViewById(R.id.layout_online_consult);
        onlineConsultLayout.setOnClickListener(this);
        buyCardsLayout = findViewById(R.id.layout_buy_cards);
        buyCardsLayout.setOnClickListener(this);
        newsImageView = (ImageView) findViewById(R.id.iv_news);
        onlineConsultImageView = (ImageView) findViewById(R.id.iv_online_consult);
        buyCardsImageView = (ImageView) findViewById(R.id.iv_buy_cards);
        newsTextView = (TextView) findViewById(R.id.tv_news);
        onlineConsultTextView = (TextView) findViewById(R.id.tv_online_consult);
        buyCardsTextView = (TextView) findViewById(R.id.tv_buy_cards);
    }

    private void initDatas() {
        NewsFragment newsFragment = new NewsFragment();
        OnlineConsultFragment onlineConsultFragment = new OnlineConsultFragment();
        BuyCardsFragment buyCardsFragment = new BuyCardsFragment();
        mDatas.add(newsFragment);
        mDatas.add(onlineConsultFragment);
        mDatas.add(buyCardsFragment);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mDatas.get(position);
            }

            @Override
            public int getCount() {
                return mDatas.size();
            }
        };
        mViewPager.setNoScroll(false);//设置是否可以滑动
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetImageView();
                switch (position) {
                    case 0:
                        newsImageView.setImageResource(R.mipmap.documents_64px_color);
                        newsTextView.setTextColor(getResources().getColorStateList(R.color.white));
                        break;
                    case 1:
                        onlineConsultImageView.setImageResource(R.mipmap.chat_64px_color);
                        onlineConsultTextView.setTextColor(getResources().getColorStateList(R.color.white));
                        break;
                    case 2:
                        buyCardsImageView.setImageResource(R.mipmap.commerce_64px_color);
                        buyCardsTextView.setTextColor(getResources().getColorStateList(R.color.white));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void resetImageView() {
        newsImageView.setImageResource(R.mipmap.documents_64px_line);
        onlineConsultImageView.setImageResource(R.mipmap.chat_64px_line);
        buyCardsImageView.setImageResource(R.mipmap.commerce_64px_line);
        newsTextView.setTextColor(getResources().getColorStateList(R.color.text_color_heise_504f4f));
        onlineConsultTextView.setTextColor(getResources().getColorStateList(R.color.text_color_heise_504f4f));
        buyCardsTextView.setTextColor(getResources().getColorStateList(R.color.text_color_heise_504f4f));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_news:
                mViewPager.setCurrentItem(0,false);
                break;
            case R.id.layout_online_consult:
                mViewPager.setCurrentItem(1,false);
                break;
            case R.id.layout_buy_cards:
                mViewPager.setCurrentItem(2,false);
                break;

        }
    }
    /**返回键监听，是否退出程序*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听
            isExit.setButton("确定", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();

        }

        return false;
    }
    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());
//                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };
    /**
     *
     * fragment响应onTouch事件
     */
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
            10);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }
    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener) ;
    }
    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }
}
