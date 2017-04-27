package com.dgkj.fxmall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android004 on 2017/4/26.
 */

public class SuperOrderBean implements Serializable{
    private int id;
    private List<OrderBean> subOrders;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<OrderBean> getSubOrders() {
        return subOrders;
    }

    public void setSubOrders(List<OrderBean> subOrders) {
        this.subOrders = subOrders;
    }
}
