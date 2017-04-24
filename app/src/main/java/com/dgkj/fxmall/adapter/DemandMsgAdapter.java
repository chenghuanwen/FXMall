package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.DemandMsgBean;
import com.dgkj.fxmall.bean.LogisticsMsgBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Android004 on 2017/4/5.
 */

public class DemandMsgAdapter extends CommonAdapter<DemandMsgBean> {
    public DemandMsgAdapter(Context context, int layoutId, List<DemandMsgBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, DemandMsgBean logisticsMsg, final int position) {
        holder.setText(R.id.tv_msg_goods_introduce,logisticsMsg.getContent());
        holder.setText(R.id.tv_msg_time,logisticsMsg.getTime());
        ImageView iv = holder.getView(R.id.iv_msg_goods);
        Glide.with(mContext).load(logisticsMsg.getUrl()).placeholder(R.mipmap.android_quanzi).into(iv);
        holder.setOnClickListener(R.id.iv_delete_msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.remove(position);
                notifyDataSetChanged();
                //TODO 删除服务器上该条信息
            }
        });
    }
}
