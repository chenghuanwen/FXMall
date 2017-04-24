package com.dgkj.fxmall.bean;

import java.io.Serializable;

/**
 * Created by Android004 on 2017/3/31.
 */

public class CommentBean implements Serializable {
    private String icon,name,content;
    private int stars;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
