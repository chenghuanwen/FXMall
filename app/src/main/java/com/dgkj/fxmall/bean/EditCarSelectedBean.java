package com.dgkj.fxmall.bean;

/**
 * Created by Android004 on 2017/4/5.
 */

public class EditCarSelectedBean {
    private int superPosition,subPosition;

    public int getSuperPosition() {
        return superPosition;
    }

    public void setSuperPosition(int superPosition) {
        this.superPosition = superPosition;
    }

    public int getSubPosition() {
        return subPosition;
    }

    public void setSubPosition(int subPosition) {
        this.subPosition = subPosition;
    }

    @Override
    public String toString() {
        return "EditCarSelectedBean{" +
                "superPosition=" + superPosition +
                ", subPosition=" + subPosition +
                '}';
    }
}
