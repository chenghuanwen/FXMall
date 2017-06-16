package com.dgkj.fxmall.bean;

import java.io.Serializable;

/**
 * Created by Android004 on 2017/3/28.
 */

public class OrderBean implements Serializable{
    private String storeName,url,introduce,color,size;
    private String storeUser,storePhone,storeAddress;
    private int count;
    private double singlePrice;
    private int postage;
    private String state;
    private String orderNum;
    private int id;//订单id
    private long payTime;
    private int brokerage;
    private double sumPrice;
    private boolean hasComment;//是否已评价
    private boolean hasDeliver;//商品是否已发出
    private long createTime;
    private String takeMan,takePhone,takeAddress;
    private int stateNum;
    private String buyerLeave;
    private int productId;//商品id
    private int skuId;//商品sku id
    private String express,expressCode;
    private int expressCodeId;//快递编码id
    private int refundId;//退款id
    private String agreeRefundTime;
    private int storeId;//商品所属店铺id
    private String buyer;//买家用户名
    private boolean isDeliver;//是否支持发货
    private String unDeliverMan,unDeliverPhone;


    public String getUnDeliverMan() {
        return unDeliverMan;
    }

    public void setUnDeliverMan(String unDeliverMan) {
        this.unDeliverMan = unDeliverMan;
    }

    public String getUnDeliverPhone() {
        return unDeliverPhone;
    }

    public void setUnDeliverPhone(String unDeliverPhone) {
        this.unDeliverPhone = unDeliverPhone;
    }

    public boolean isDeliver() {
        return isDeliver;
    }

    public void setDeliver(boolean deliver) {
        isDeliver = deliver;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public boolean isHasDeliver() {
        return hasDeliver;
    }
    public void setHasDeliver(boolean hasDeliver) {
        this.hasDeliver = hasDeliver;
    }

    public int getExpressCodeId() {
        return expressCodeId;
    }

    public void setExpressCodeId(int expressCodeId) {
        this.expressCodeId = expressCodeId;
    }

    public String getAgreeRefundTime() {
        return agreeRefundTime;
    }

    public void setAgreeRefundTime(String agreeRefundTime) {
        this.agreeRefundTime = agreeRefundTime;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreUser() {
        return storeUser;
    }

    public void setStoreUser(String storeUser) {
        this.storeUser = storeUser;
    }

    public int getRefundId() {
        return refundId;
    }

    public void setRefundId(int refundId) {
        this.refundId = refundId;
    }

    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getBuyerLeave() {
        return buyerLeave;
    }

    public void setBuyerLeave(String buyerLeave) {
        this.buyerLeave = buyerLeave;
    }

    public int getStateNum() {
        return stateNum;
    }

    public void setStateNum(int stateNum) {
        this.stateNum = stateNum;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public String getTakeMan() {
        return takeMan;
    }

    public void setTakeMan(String takeMan) {
        this.takeMan = takeMan;
    }

    public String getTakePhone() {
        return takePhone;
    }

    public void setTakePhone(String takePhone) {
        this.takePhone = takePhone;
    }

    public String getTakeAddress() {
        return takeAddress;
    }

    public void setTakeAddress(String takeAddress) {
        this.takeAddress = takeAddress;
    }

    public int getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(int brokerage) {
        this.brokerage = brokerage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(double sumPrice) {
        this.sumPrice = sumPrice;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    public boolean isHasComment() {
        return hasComment;
    }

    public void setHasComment(boolean hasComment) {
        this.hasComment = hasComment;
    }

    public int getPostage() {
        return postage;
    }

    public void setPostage(int postage) {
        this.postage = postage;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(double singlePrice) {
        this.singlePrice = singlePrice;
    }


}
