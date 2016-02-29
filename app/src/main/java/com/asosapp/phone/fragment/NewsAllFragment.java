package com.asosapp.phone.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asosapp.phone.R;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Created by Leo on 2016/2/27.
 */
public class NewsAllFragment extends Fragment{
    private static final String[] TITLE = new String[]{"医学", "法律", "保险", "赔偿", "其它"};
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_newsall,null);
        init();
        return view;
    }

    private void init() {

        // ViewPager结合Indicator
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getChildFragmentManager());
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
//                Toast.makeText(getActivity(), TITLE[arg0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    /**
     * TabPageIndicatorAdapter类
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {



        public TabPageIndicatorAdapter(FragmentManager childFragmentManager) {
            super(childFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            // 示例，新建一样的Fragment，传入String进行显示
            NewsMedicalFragment medicalFragment=new NewsMedicalFragment();
            NewsLawFragment lawFragment=new NewsLawFragment();
            NewsCompensationFragment compensationFragment=new NewsCompensationFragment();
            NewsRestFragment restFragment=new NewsRestFragment();
            NewsInsuranceFragment sinsuranceFragment=new NewsInsuranceFragment();
            switch (position){
                case 0:
                    return medicalFragment;
                case 1:
                    return lawFragment;
                case 2:
                    return sinsuranceFragment;
                case 3:
                    return compensationFragment;
                case 4:
                    return restFragment;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position % TITLE.length];
        }

        @Override
        public int getCount() {
            return TITLE.length;
        }
    }


}
