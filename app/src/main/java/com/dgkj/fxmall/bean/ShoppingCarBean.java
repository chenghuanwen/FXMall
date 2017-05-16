package com.dgkj.fxmall.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Android004 on 2017/3/24.
 */

public class ShoppingCarBean implements Parcelable{
    private ArrayList<ShoppingGoodsBean> goods;
    private String storeName;
    private boolean isSelected;

    public ShoppingCarBean (){}
    public static final Parcelable.Creator<ShoppingCarBean> CREATOR = new Creator<ShoppingCarBean>() {
        @Override
        public ShoppingCarBean createFromParcel(Parcel source) {
            return new ShoppingCarBean(source);
        }

        @Override
        public ShoppingCarBean[] newArray(int size) {
            return new ShoppingCarBean[size];
        }
    };

    public ShoppingCarBean(Parcel source) {
        goods = source.readArrayList(ShoppingGoodsBean.class.getClassLoader());
        storeName = source.readString();
        isSelected = source.readInt()==0?true:false;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(goods);
        dest.writeString(storeName);
        dest.writeInt(isSelected?0:1);
    }

    public static Parcelable.Creator<ShoppingCarBean> getCREATOR() {
        return CREATOR;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ArrayList<ShoppingGoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<ShoppingGoodsBean> goods) {
        this.goods = goods;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
