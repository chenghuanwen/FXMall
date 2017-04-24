package com.dgkj.fxmall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android004 on 2017/4/19.
 */

public class SuperPostageBean implements Serializable{
    private int id;
    private List<PostageBean> posts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<PostageBean> getPosts() {
        return posts;
    }

    public void setPosts(List<PostageBean> posts) {
        this.posts = posts;
    }
}
