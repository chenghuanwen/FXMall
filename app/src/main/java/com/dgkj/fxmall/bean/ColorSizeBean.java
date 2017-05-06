package com.dgkj.fxmall.bean;

/**
 * Created by Android004 on 2017/5/5.
 */

public class ColorSizeBean {
    private String color,url;
    private int id,price,brokrage,inventory;
    private boolean isCheck;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrokrage() {
        return brokrage;
    }

    public void setBrokrage(int brokrage) {
        this.brokrage = brokrage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
}
