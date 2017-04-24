package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.MainDemandBean;

import java.util.List;

/**
 * Created by Android004 on 2017/4/5.
 */

public interface OnGetDemandDatasFinishedListener {
    void onGetDemandDatasFinished(List<MainDemandBean> demands);
}
