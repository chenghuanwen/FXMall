package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.StoreProductBean;

import java.util.List;

/**
 * Created by Android004 on 2017/4/5.
 */

public interface OnGetStoreProductsFinishedListener {
    void OnGetStoreProductsFinished(List<StoreProductBean> products);
}
