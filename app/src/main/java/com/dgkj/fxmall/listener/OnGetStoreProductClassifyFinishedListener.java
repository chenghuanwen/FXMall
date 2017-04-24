package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.StoreProductClassifyBean;

import java.util.List;

/**
 * Created by Android004 on 2017/4/19.
 */

public interface OnGetStoreProductClassifyFinishedListener {
    void onGetStoreProductClassifyFinished(List<StoreProductClassifyBean> classifyList);
}
