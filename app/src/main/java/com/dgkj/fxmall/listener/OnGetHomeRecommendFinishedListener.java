package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.HomePageRecommendBean;

import java.util.List;

/**
 * Created by Android004 on 2017/5/13.
 */

public interface OnGetHomeRecommendFinishedListener {
    void onGetHomeRecommendFinishedListener(List<HomePageRecommendBean> recommendList);
}
