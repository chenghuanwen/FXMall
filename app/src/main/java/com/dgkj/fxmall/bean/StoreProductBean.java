package com.dgkj.fxmall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android004 on 2017/4/1.
 */

public class StoreProductBean implements Serializable{
    private String url,describe,sales,inventory,titel;
    private List<String> mainUrls,detialUrls;
    private boolean isSelect;
    private int id,statu,skuID,brokerage;
    private String color;
    private double price;
    private double vipPrice;


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(double vipPrice) {
        this.vipPrice = vipPrice;
    }

    public int getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(int brokerage) {
        this.brokerage = brokerage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public int getSkuID() {
        return skuID;
    }

    public void setSkuID(int skuID) {
        this.skuID = skuID;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getMainUrls() {
        return mainUrls;
    }

    public void setMainUrls(List<String> mainUrls) {
        this.mainUrls = mainUrls;
    }

    public List<String> getDetialUrls() {
        return detialUrls;
    }

    public void setDetialUrls(List<String> detialUrls) {
        this.detialUrls = detialUrls;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }
}
