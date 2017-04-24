package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.LogisticsMsgBean;
import com.dgkj.fxmall.bean.NotifyMsgBean;

import java.util.List;

/**
 * Created by Android004 on 2017/4/5.
 */

public interface OnGetNotifyMsgFinishedListener {
    void OnGetLogisticsMsgFinished(List<NotifyMsgBean> list);
}
