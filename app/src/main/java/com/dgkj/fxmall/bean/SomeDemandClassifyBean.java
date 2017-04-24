package com.dgkj.fxmall.bean;

import java.util.List;

/**
 * Created by Android004 on 2017/3/25.
 */

public class SomeDemandClassifyBean {
    private String type;
    private List<MainDemandBean> subList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MainDemandBean> getSubList() {
        return subList;
    }

    public void setSubList(List<MainDemandBean> subList) {
        this.subList = subList;
    }
}
