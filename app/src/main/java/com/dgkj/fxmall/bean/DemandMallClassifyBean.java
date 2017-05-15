package com.dgkj.fxmall.bean;

/**
 * Created by Android004 on 2017/3/23.
 */

public class DemandMallClassifyBean {
    private String title,url;
    private int resId,superId;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSuperId() {
        return superId;
    }

    public void setSuperId(int superId) {
        this.superId = superId;
    }

    public DemandMallClassifyBean(String title, int resId){
    this.title = title;
    this.resId = resId;

}
    public DemandMallClassifyBean(){}
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
