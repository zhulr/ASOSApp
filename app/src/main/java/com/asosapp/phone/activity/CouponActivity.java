package com.asosapp.phone.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asosapp.phone.R;
import com.asosapp.phone.fragment.CouponCardFragment;
import com.asosapp.phone.fragment.CouponFragment;
import com.asosapp.phone.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASOS_zhulr on 2016/2/23.
 */
public class CouponActivity extends FragmentActivity implements View.OnClickListener {
    private View myCard;
    private View myCoupon;
    private TextView titleName;
    private ImageView myCardLine;
    private ImageView myCouponLine;
    private List<Fragment> mDatas = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;
    private NoScrollViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        initView();
        initDatas();
    }

    private void initView() {
        myCard = findViewById(R.id.coupon_my_card);
        myCoupon = findViewById(R.id.coupon_my_coupon);
        titleName = (TextView) findViewById(R.id.title_name);
        myCardLine = (ImageView) findViewById(R.id.coupon_my_card_line);
        myCouponLine = (ImageView) findViewById(R.id.coupon_my_coupon_line);
        titleName.setText(R.string.mycoupon);
        myCard.setOnClickListener(this);
        myCoupon.setOnClickListener(this);
    }


    private void initDatas() {
        CouponCardFragment couponCardFragment = new CouponCardFragment();
        CouponFragment couponFragment = new CouponFragment();
        mDatas.add(couponCardFragment);
        mDatas.add(couponFragment);
        mViewPager = (NoScrollViewPager) findViewById(R.id.coupon_viewpager);
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
        mViewPager.setNoScroll(false);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTag();
                switch (position) {
                    case 0:
                        myCard.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        myCardLine.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        myCoupon.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        myCouponLine.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 改变标签背景
     * *
     */
    private void changeTag() {
        myCard.setBackgroundColor(Color.parseColor("#d3d3d3"));
        myCoupon.setBackgroundColor(Color.parseColor("#d3d3d3"));
        myCardLine.setVisibility(View.INVISIBLE);
        myCouponLine.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.coupon_my_card:
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.coupon_my_coupon:
                mViewPager.setCurrentItem(1, false);
                break;
        }
    }
}



