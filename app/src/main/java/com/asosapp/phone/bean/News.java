package com.asosapp.phone.bean;

import java.util.List;

/**
 * Created by ASOS_lijianfeng on 2016/1/18.
 *
 * 新闻信息
 *
 */
public class News {
    private String newsId;//新闻id
    private String newsTitle;//新闻标题
    private String newsIntro;//新闻内容


    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsIntro() {
        return newsIntro;
    }

    public void setNewsIntro(String newsIntro) {
        this.newsIntro = newsIntro;
    }

    @Override
    public String toString() {
        return "News [newsId="+newsId+",newsTitle="+newsTitle+",newsIntro="+newsIntro+"]";
    }
}
