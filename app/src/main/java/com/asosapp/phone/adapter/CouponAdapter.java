package com.asosapp.phone.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asosapp.phone.R;
import com.asosapp.phone.view.ToastView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Leo on 2016/2/26.
 */
public class CouponAdapter extends RecyclerView.Adapter<ViewHolders> {

    private LayoutInflater mInfalater;
    private Context context;
    private List<String> mDatas;
    private String[] typeDate;
    private int nowTime=0;
    private int nowDays=0;
    public CouponAdapter(Context context,List<String> mDatas) {
        this.context = context;
        this.mDatas=mDatas;
        mInfalater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInfalater.inflate(R.layout.businesse_information_detail, parent, false);
        ViewHolders viewHolder = new ViewHolders(view);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolders holder, final int position) {
        typeDate=mDatas.get(position).toString().split("-");

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        nowDays=NowDay(String.valueOf(year),String.valueOf(month),String.valueOf(day));//当天时间

        nowTime=NowDay(typeDate[0],typeDate[1],typeDate[2]);//后台获取的时间

        if (LeapYear(typeDate[0])-(nowTime+(nowDays-nowTime))>=0) {
            if (LeapYear(typeDate[0]) - (nowTime + (nowDays - nowTime)) == 0) {
                holder.spare_time.setText("已过期");
                holder.use_changecolor.setBackgroundColor(R.color.gray);
            } else if (LeapYear(typeDate[0]) - (nowTime + (nowDays - nowTime)) <= 7) {
                holder.spare_time.setText("还有" + String.valueOf(LeapYear(typeDate[0]) - (nowTime + (nowDays - nowTime)) + "天过期"));
            } else {
                holder.spare_time.setVisibility(View.INVISIBLE);
            }
        }

    }

    /**
     * 判断是否为闰年
     */

    private int LeapYear(String years) {

        int year = Integer.parseInt(years);
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            // 是闰年
            return 366;
        } else {
            return 365;
        }
    }

    /**
     *
     * 判断是今年哪一天
     * @return
     */
    private int NowDay(String years,String months,String days){
        int sum = 0,leap=0;
        int year=Integer.parseInt(years);
        int month=Integer.parseInt(months);
        int day=Integer.parseInt(days);
        switch(month)
        {
            case 1:sum=0;break;
            case 2:sum=31;break;
            case 3:sum=59;break;
            case 4:sum=90;break;
            case 5:sum=120;break;
            case 6:sum=151;break;
            case 7:sum=181;break;
            case 8:sum=212;break;
            case 9:sum=243;break;
            case 10:sum=273;break;
            case 11:sum=304;break;
            case 12:sum=334;break;
            default:break;
        }
        sum=sum+day;
        if(year%400==0||(year%4==0&&year%100!=0))
            leap=1;
        else
            leap=0;
        if(leap==1&&month>2)
            sum++;
        return sum;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}

class ViewHolders extends RecyclerView.ViewHolder {
    TextView spare_time;
    LinearLayout use_changecolor;
    LinearLayout use_click;

    public ViewHolders(View itemView) {
        super(itemView);
        spare_time= (TextView) itemView.findViewById(R.id.spare_time);
        use_changecolor= (LinearLayout) itemView.findViewById(R.id.use_after);
        use_click= (LinearLayout) itemView.findViewById(R.id.use_click);

    }

}

