package com.asosapp.phone.bean;

import java.util.List;

/**
 * Created by ASOS_lijianfeng on 2016/1/18.
 */
public class NewsList  {
    private List<News> newsinfor;
    public List<News> getNewsinfor() {
        return newsinfor;
    }

    public void setNewsinfor(List<News> newsinfor) {
        this.newsinfor = newsinfor;
    }

    @Override
    public String toString() {
        return "NewsList[newsinfor="+newsinfor+"]";
    }
}
