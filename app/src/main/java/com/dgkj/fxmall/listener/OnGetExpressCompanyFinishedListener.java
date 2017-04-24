package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.ExpressCompanyBean;

import java.util.List;

/**
 * Created by Android004 on 2017/4/24.
 */

public interface OnGetExpressCompanyFinishedListener {
    void onGetExpressCompanyFinished(List<ExpressCompanyBean> expressCompanies);
}
