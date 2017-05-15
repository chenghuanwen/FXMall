package com.dgkj.fxmall.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android004 on 2017/3/27.
 */

public class StoreBean implements Parcelable{
    private String name,adress,iconUrl,createTime;
    private int stars,totalScals,goodsCount,id;
    private double describeScore,priceScore,qualityScore,totalScore;
    private String licence;
    private List<String> mainUrls = new ArrayList<>();
    private String keeper,phone;
    private String recommender;


    public StoreBean (){}
    public static final Parcelable.Creator<StoreBean> CREATOR = new Creator<StoreBean>() {
        @Override
        public StoreBean createFromParcel(Parcel source) {
            return new StoreBean(source);
        }

        @Override
        public StoreBean[] newArray(int size) {
            return new StoreBean[size];
        }
    };

    public StoreBean(Parcel source) {
        name = source.readString();
        adress = source.readString();
        iconUrl = source.readString();
        createTime = source.readString();
        stars = source.readInt();
        totalScals = source.readInt();
        goodsCount = source.readInt();
        id = source.readInt();
        describeScore = source.readDouble();
        priceScore = source.readDouble();
        qualityScore = source.readDouble();
        totalScore = source.readDouble();
        licence = source.readString();
        source.readStringList(mainUrls);
        keeper = source.readString();
        phone = source.readString();
        recommender = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(adress);
        dest.writeString(iconUrl);
        dest.writeString(createTime);
        dest.writeInt(stars);
        dest.writeInt(totalScals);
        dest.writeInt(goodsCount);
        dest.writeInt(id);
        dest.writeDouble(describeScore);
        dest.writeDouble(priceScore);
        dest.writeDouble(qualityScore);
        dest.writeDouble(totalScore);
        dest.writeString(licence);
        dest.writeStringList(mainUrls);
        dest.writeString(keeper);
        dest.writeString(phone);
        dest.writeString(recommender);
    }


    public static Parcelable.Creator<StoreBean> getCREATOR() {
        return CREATOR;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getKeeper() {
        return keeper;
    }

    public void setKeeper(String keeper) {
        this.keeper = keeper;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public List<String> getMainUrls() {
        return mainUrls;
    }

    public void setMainUrls(List<String> mainUrls) {
        this.mainUrls = mainUrls;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public double getDescribeScore() {
        return describeScore;
    }

    public void setDescribeScore(double describeScore) {
        this.describeScore = describeScore;
    }

    public double getPriceScore() {
        return priceScore;
    }

    public void setPriceScore(double priceScore) {
        this.priceScore = priceScore;
    }

    public double getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(double qualityScore) {
        this.qualityScore = qualityScore;
    }

    public String getRecommender() {
        return recommender;
    }

    public void setRecommender(String recommender) {
        this.recommender = recommender;
    }
}
