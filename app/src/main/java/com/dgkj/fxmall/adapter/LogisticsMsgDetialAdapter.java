package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.text.SpannableString;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.LogisticsBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Android004 on 2017/3/31.
 */

public class LogisticsMsgDetialAdapter extends CommonAdapter<LogisticsBean> {
    private StringBuffer sb = new StringBuffer();

    public LogisticsMsgDetialAdapter(Context context, int layoutId, List<LogisticsBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, LogisticsBean logisticsBean, int position) {
        String address = logisticsBean.getCurrentAddress();
        String person = logisticsBean.getPerson();
        String phone = logisticsBean.getPhone();

        holder.setText(R.id.tv_logistics_current_address, address);
            holder.setText(R.id.tv_logistics_arrive_time,logisticsBean.getArriveTime());
    }
}
