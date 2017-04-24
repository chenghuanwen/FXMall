package com.dgkj.fxmall.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Android004 on 2017/3/24.
 */

public class ShoppingCarBean implements Serializable{
    private ArrayList<ShoppingGoodsBean> goods;
    private String storeName;
    private boolean isSelected;


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
