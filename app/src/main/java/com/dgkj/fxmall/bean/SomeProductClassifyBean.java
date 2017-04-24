package com.dgkj.fxmall.bean;

import java.util.List;

/**
 * Created by Android004 on 2017/3/25.
 */

public class SomeProductClassifyBean {
    private String type;
    private List<MainProductBean> subList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MainProductBean> getSubList() {
        return subList;
    }

    public void setSubList(List<MainProductBean> subList) {
        this.subList = subList;
    }
}
