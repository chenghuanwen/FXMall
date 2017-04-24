package com.dgkj.fxmall.bean;

import java.io.Serializable;

/**
 * Created by Android004 on 2017/3/27.
 */

public class StoreBean implements Serializable{
    private String name,adress,iconUrl,createTime,recommender;
    private boolean hasRealNameCheck;
    private int stars,totalScals,goodsCount,id;
    private Double describeScore,priceScore,qualityScore;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecommender() {
        return recommender;
    }

    public void setRecommender(String recommender) {
        this.recommender = recommender;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isHasRealNameCheck() {
        return hasRealNameCheck;
    }

    public void setHasRealNameCheck(boolean hasRealNameCheck) {
        this.hasRealNameCheck = hasRealNameCheck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getTotalScals() {
        return totalScals;
    }

    public void setTotalScals(int totalScals) {
        this.totalScals = totalScals;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public Double getDescribeScore() {
        return describeScore;
    }

    public void setDescribeScore(Double describeScore) {
        this.describeScore = describeScore;
    }

    public Double getPriceScore() {
        return priceScore;
    }

    public void setPriceScore(Double priceScore) {
        this.priceScore = priceScore;
    }

    public Double getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(Double qualityScore) {
        this.qualityScore = qualityScore;
    }
}
