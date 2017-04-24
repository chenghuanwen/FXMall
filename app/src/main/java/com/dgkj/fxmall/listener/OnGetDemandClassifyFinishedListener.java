package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.DemandMallClassifyBean;
import com.dgkj.fxmall.bean.MainDemandBean;
import com.dgkj.fxmall.bean.ProductClassifyBean;

import java.util.List;

/**
 * Created by Android004 on 2017/4/5.
 */

public interface OnGetDemandClassifyFinishedListener {
    void onGetDemandClassifyFinished(List<ProductClassifyBean> calssifys);
}
