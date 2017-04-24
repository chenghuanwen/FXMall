package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.adapter.StoreSuperClassifyAdapter;
import com.dgkj.fxmall.bean.SuperClassifyBean;

import java.util.List;

/**
 * Created by Android004 on 2017/4/17.
 */

public interface OnGetStoreSuperClassifyFinishedListener {
    void onGetStoreSuperClassifyFinished(List<SuperClassifyBean> classifyList);
}
