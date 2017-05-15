package com.dgkj.fxmall.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Android004 on 2017/5/5.
 */

public class ColorSizeBean implements Parcelable{
    private String color;
    private int id,price,brokrage,inventory;
    private boolean isCheck;
    private static final Parcelable.Creator<ColorSizeBean> CREATOR = new Creator() {
        @Override
        public ColorSizeBean createFromParcel(Parcel source) {
            return new ColorSizeBean(source);
        }

        @Override
        public ColorSizeBean[] newArray(int size) {
            return new ColorSizeBean[size];
        }
    };


    public ColorSizeBean(){}


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



    public ColorSizeBean(Parcel in){
        color = in.readString();
        id = in.readInt();
        price = in.readInt();
        brokrage = in.readInt();
        inventory = in.readInt();
        isCheck = in.readByte()!=1;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(color);
            dest.writeInt(id);
            dest.writeInt(price);
            dest.writeInt(brokrage);
            dest.writeInt(inventory);
            dest.writeByte((byte) (isCheck?0:1));//写入布尔值时用可用数字进行替换，写出时将数字转为Boolean即可
    }


    //固定写法, 只用修改Creator的泛型
    public static Creator<ColorSizeBean> getCREATOR() {
        return CREATOR;
    }
}
