package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.StoreBean;

import java.util.List;

/**
 * Created by Android004 on 2017/4/21.
 */

public interface OnGetMyRecommendStoreFinishedListener {
    void onGetMyRecommendStoreFinished(List<StoreBean> stores);
}
