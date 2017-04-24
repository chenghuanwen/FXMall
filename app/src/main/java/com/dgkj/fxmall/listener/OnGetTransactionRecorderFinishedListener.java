package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.TransactionRecordBean;

import java.util.List;

/**
 * Created by Android004 on 2017/4/20.
 */

public interface OnGetTransactionRecorderFinishedListener {
    void onGetTransactionRecorderFinished(List<TransactionRecordBean> recordList);
}
