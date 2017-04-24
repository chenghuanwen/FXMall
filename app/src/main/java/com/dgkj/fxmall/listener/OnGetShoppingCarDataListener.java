package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.ShoppingCarBean;
import com.dgkj.fxmall.bean.ShoppingGoodsBean;

import java.util.List;

/**
 * Created by Android004 on 2017/4/5.
 */

public interface OnGetShoppingCarDataListener {
    void onGetShoppingCarDataFinished(List<ShoppingCarBean> carBeanList);
}
