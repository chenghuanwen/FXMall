package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.OrderBean;

import java.util.List;

/**
 * Created by Android004 on 2017/4/24.
 */

public interface OnGetMyOrderInfoFinishedListener {
    void onGetMyOrderInfoFinished(List<OrderBean> orders);
}
