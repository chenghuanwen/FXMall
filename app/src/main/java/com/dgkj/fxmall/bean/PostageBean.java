package com.dgkj.fxmall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android004 on 2017/4/1.
 */

public class PostageBean implements Serializable{
    private String modeName,postCount,addCount,postPay,addPay,modeSetting;
    private int id,isDefault;
    private int districtId;

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    private List<String> provinces;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public List<String> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<String> provinces) {
        this.provinces = provinces;
    }

    public String getModeSetting() {
        return modeSetting;
    }

    public void setModeSetting(String modeSetting) {
        this.modeSetting = modeSetting;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getPostCount() {
        return postCount;
    }

    public void setPostCount(String postCount) {
        this.postCount = postCount;
    }

    public String getAddCount() {
        return addCount;
    }

    public void setAddCount(String addCount) {
        this.addCount = addCount;
    }

    public String getPostPay() {
        return postPay;
    }

    public void setPostPay(String postPay) {
        this.postPay = postPay;
    }

    public String getAddPay() {
        return addPay;
    }

    public void setAddPay(String addPay) {
        this.addPay = addPay;
    }
}
