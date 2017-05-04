package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.MainRecommendStoreBean;

import java.util.List;

/**
 * Created by Android004 on 2017/5/3.
 */

public interface OnGetMainRecommendStoreFinishedListener {
    void onGetMainRecommendStoreFinishedListener(List<MainRecommendStoreBean> recommendList);
}
