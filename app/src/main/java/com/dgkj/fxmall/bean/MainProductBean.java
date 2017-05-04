package com.dgkj.fxmall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android004 on 2017/3/23.
 */

public class MainProductBean implements Serializable{
    private List<String> urls,detialUrls;
    private String introduce;
    private double price;
    private String sales;
    private String address;
    private String express;
    private double vipPrice;
    private int skuId,count,id;
    private String color;
    private int inventory,brokerage;
    private StoreBean storeBean;
    private String url;

    private int describeScore,priceScore,qualityScore,totalScore;

    public int getDescribeScore() {
        return describeScore;
    }

    public void setDescribeScore(int describeScore) {
        this.describeScore = describeScore;
    }

    public int getPriceScore() {
        return priceScore;
    }

    public void setPriceScore(int priceScore) {
        this.priceScore = priceScore;
    }

    public int getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(int qualityScore) {
        this.qualityScore = qualityScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public int getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(int brokerage) {
        this.brokerage = brokerage;
    }

    public List<String> getDetialUrls() {
        return detialUrls;
    }

    public void setDetialUrls(List<String> detialUrls) {
        this.detialUrls = detialUrls;
    }

    public StoreBean getStoreBean() {
        return storeBean;
    }

    public void setStoreBean(StoreBean storeBean) {
        this.storeBean = storeBean;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public double getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(double vipPrice) {
        this.vipPrice = vipPrice;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }
}
