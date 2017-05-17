package com.dgkj.fxmall.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.MyVipBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Android004 on 2017/5/17.
 */

public class MyVipAdapter extends CommonAdapter<MyVipBean> {
    public MyVipAdapter(Context context, int layoutId, List<MyVipBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MyVipBean myVipBean, int position) {
        CircleImageView civ = holder.getView(R.id.civ_vip);
        Glide.with(mContext).load(myVipBean.getUrl()).error(R.mipmap.android_quanzi).into(civ);
        holder.setText(R.id.tv_vip_name,myVipBean.getNick());
        holder.setText(R.id.tv_join_time,myVipBean.getTime());
    }
}
