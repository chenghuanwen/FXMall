package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.MainProductBean;

import java.util.List;

/**
 * Created by Android004 on 2017/5/2.
 */

public interface OnSearchProductsFinishedListener {
    void onSearchProductsFinished(List<MainProductBean> mainProducts);
}
