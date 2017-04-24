package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.ProductClassifyBean;

import java.util.List;

/**
 * Created by Android004 on 2017/4/18.
 */

public interface OnGetSubclassifyFinishedListener {
    void onGetSubclassifyFinished(List<ProductClassifyBean> subList);
}
