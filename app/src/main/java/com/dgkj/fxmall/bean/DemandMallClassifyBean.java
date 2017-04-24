package com.dgkj.fxmall.bean;

/**
 * Created by Android004 on 2017/3/23.
 */

public class DemandMallClassifyBean {
    private String title;
    private int resId;
public DemandMallClassifyBean(String title,int resId){
    this.title = title;
    this.resId = resId;

}
    public String getTitle() {
        return title;
    }

    public int getResId() {
        return resId;
    }

    @Override
    public String toString() {
        return "DemandMallClassifyBean{" +
                "title='" + title + '\'' +
                ", resId=" + resId +
                '}';
    }
}
