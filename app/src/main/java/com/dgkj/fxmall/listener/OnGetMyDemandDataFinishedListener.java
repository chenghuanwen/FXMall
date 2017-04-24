package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.MainDemandBean;

import java.util.List;

/**
 * Created by Android004 on 2017/4/21.
 */

public interface OnGetMyDemandDataFinishedListener {
    void onGetMyDemandDataFinished(List<MainDemandBean> demandList);
}
