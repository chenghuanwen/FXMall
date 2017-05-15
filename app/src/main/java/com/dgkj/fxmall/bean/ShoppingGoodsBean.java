package com.dgkj.fxmall.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android004 on 2017/3/24.
 */

public class ShoppingGoodsBean implements Parcelable {
    private boolean isSelected;
    private String url;
    private String introduce;
    private String address;
    private double price;
    private int count;
    private int skuId;
    private int productId, carId;
    private int brokerage, inventory;//佣金,库存
    private List<String> mainUrls=new ArrayList<>(), detialUrls=new ArrayList<>();
    private int statu;//是否已下架
    private int sales;
    private int describeScore, priceScore, qualityScore;
    private double totalScore;
    private StoreBean storeBean;
    private String color;
    private double vipPrice, postage;

    private String storeName;

    public ShoppingGoodsBean(){}

    public static final Parcelable.Creator<ShoppingGoodsBean> CREATOR = new Creator<ShoppingGoodsBean>() {
        @Override
        public ShoppingGoodsBean createFromParcel(Parcel source) {
            return new ShoppingGoodsBean(source);
        }

        @Override
        public ShoppingGoodsBean[] newArray(int size) {
            return new ShoppingGoodsBean[size];
        }
    };

    public ShoppingGoodsBean(Parcel source) {
        isSelected = source.readInt() == 0 ? true : false;
        url = source.readString();
        introduce = source.readString();
        address = source.readString();
        price = source.readDouble();
        count = source.readInt();
        skuId = source.readInt();
        productId = source.readInt();
        carId = source.readInt();
        brokerage = source.readInt();
        inventory = source.readInt();
        source.readStringList(mainUrls);
        source.readStringList(detialUrls);
        statu = source.readInt();
        sales = source.readInt();
        describeScore = source.readInt();
        priceScore = source.readInt();
        qualityScore = source.readInt();
        totalScore = source.readDouble();
        storeBean = source.readParcelable(StoreBean.class.getClassLoader());
        color = source.readString();
        vipPrice = source.readDouble();
        postage = source.readDouble();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isSelected ? 0 : 1);
        dest.writeString(url);
        dest.writeString(introduce);
        dest.writeString(address);
        dest.writeDouble(price);
        dest.writeInt(count);
        dest.writeInt(skuId);
        dest.writeInt(productId);
        dest.writeInt(carId);
        dest.writeInt(brokerage);
        dest.writeInt(inventory);
        dest.writeStringList(mainUrls);
        dest.writeStringList(detialUrls);
        dest.writeInt(statu);
        dest.writeInt(sales);
        dest.writeInt(describeScore);
        dest.writeInt(priceScore);
        dest.writeInt(qualityScore);
        dest.writeDouble(totalScore);
        dest.writeParcelable(storeBean, flags);
        dest.writeString(color);
        dest.writeDouble(vipPrice);
        dest.writeDouble(postage);
    }


    public static Parcelable.Creator<ShoppingGoodsBean> getCREATOR() {
        return CREATOR;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(double vipPrice) {
        this.vipPrice = vipPrice;
    }

    public double getPostage() {
        return postage;
    }

    public void setPostage(double postage) {
        this.postage = postage;
    }

    public StoreBean getStoreBean() {
        return storeBean;
    }

    public void setStoreBean(StoreBean storeBean) {
        this.storeBean = storeBean;
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

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public int getCarId() {
        return carId;
    }


    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    public int getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(int brokerage) {
        this.brokerage = brokerage;
    }

    public int getProductId() {
        return productId;
    }


    public void setProductId(int productId) {
        this.productId = productId;
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

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }


    public void setCount(int count) {

        this.count = count;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
