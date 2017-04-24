package com.dgkj.fxmall.bean;

/**
 * Created by Android004 on 2017/3/23.
 */

public class ProductClassifyBean {
    private String url;
    private String taxon;
    private int id;
    private int supId;

    public int getSupId() {
        return supId;
    }

    public void setSupId(int supId) {
        this.supId = supId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTaxon() {
        return taxon;
    }

    public void setTaxon(String taxon) {
        this.taxon = taxon;
    }
}
