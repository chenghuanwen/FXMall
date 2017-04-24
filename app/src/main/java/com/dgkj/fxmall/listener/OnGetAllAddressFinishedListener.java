package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.TakeGoodsAddressBean;

import java.util.List;

/**
 * Created by Android004 on 2017/4/17.
 */

public interface OnGetAllAddressFinishedListener {
    void onGetAllAddressFinished(List<TakeGoodsAddressBean> addressList);
}
