package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.MainProductBean;

import java.util.List;

/**
 * Created by Android004 on 2017/4/5.
 */

public interface OnGetSubClassifyProductsFinishedListener {
    void onGetDemandDatasFinished(List<MainProductBean> products);
}
