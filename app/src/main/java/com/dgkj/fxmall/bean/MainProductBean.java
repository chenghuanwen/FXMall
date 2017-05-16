package com.dgkj.fxmall.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android004 on 2017/3/23.
 */

public class MainProductBean implements Parcelable{
    private List<String> urls= new ArrayList<>(),detialUrls=new ArrayList<>();
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
    private int describeScore,priceScore,qualityScore;
    private double totalScore;
    private boolean isDeliverable;
    public static final Parcelable.Creator<MainProductBean> CREATOR = new Creator<MainProductBean>() {
        @Override
        public MainProductBean createFromParcel(Parcel source) {
            return new MainProductBean(source);
        }

        @Override
        public MainProductBean[] newArray(int size) {
            return new MainProductBean[size];
        }
    };

    public MainProductBean(){}

    public MainProductBean(Parcel source) {
        source.readStringList(urls);
        source.readStringList(detialUrls);
        introduce = source.readString();
        price = source.readDouble();
        sales = source.readString();
        address = source.readString();
        express = source.readString();
        vipPrice = source.readDouble();
        skuId = source.readInt();
        count = source.readInt();
        id = source.readInt();
        color = source.readString();
        inventory = source.readInt();
        brokerage = source.readInt();
        storeBean = source.readParcelable(StoreBean.class.getClassLoader());
        url = source.readString();
        describeScore = source.readInt();
        priceScore = source.readInt();
        qualityScore = source.readInt();
        totalScore = source.readDouble();
        isDeliverable = source.readInt()==0?true:false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(urls);
        dest.writeStringList(detialUrls);
        dest.writeString(introduce);
        dest.writeDouble(price);
        dest.writeString(sales);
        dest.writeString(address);
        dest.writeString(express);
        dest.writeDouble(vipPrice);
        dest.writeInt(skuId);
        dest.writeInt(count);
        dest.writeInt(id);
        dest.writeString(color);
        dest.writeInt(inventory);
        dest.writeInt(brokerage);
        dest.writeParcelable(storeBean,flags);
        dest.writeString(url);
        dest.writeInt(describeScore);
        dest.writeInt(priceScore);
        dest.writeInt(qualityScore);
        dest.writeDouble(totalScore);
        dest.writeInt(isDeliverable?0:1);

    }

    public static Parcelable.Creator<MainProductBean> getCREATOR() {
        return CREATOR;
    }

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

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
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

    public boolean isDeliverable() {
        return isDeliverable;
    }

    public void setDeliverable(boolean deliverable) {
        isDeliverable = deliverable;
    }
}
