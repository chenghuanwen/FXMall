package com.dgkj.fxmall.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.LogisticsMsgBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Android004 on 2017/4/5.
 */

public class LogiticsMsgAdapter extends CommonAdapter<LogisticsMsgBean> {
    private AlertDialog pw;

    public LogiticsMsgAdapter(Context context, int layoutId, List<LogisticsMsgBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, LogisticsMsgBean logisticsMsg, final int position) {
        holder.setText(R.id.tv_express,logisticsMsg.getExpressType());
        holder.setText(R.id.tv_msg_goods_introduce,logisticsMsg.getContent());
        holder.setText(R.id.tv_express_number,logisticsMsg.getExpressNum());
        holder.setText(R.id.tv_msg_time,logisticsMsg.getTime());
        ImageView iv = holder.getView(R.id.iv_msg_goods);
        Glide.with(mContext).load(logisticsMsg.getUrl()).placeholder(R.mipmap.android_quanzi).into(iv);
        holder.setOnClickListener(R.id.iv_delete_msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(position);
                //TODO 删除服务器上该条信息
            }
        });
    }

    private void showDeleteDialog(final int position){
        View contentview = mInflater.inflate(R.layout.layout_delete_dialog, null);
        pw = new AlertDialog.Builder(mContext).create();
        pw.setView(contentview);
        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_confirm);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatas.remove(position);
                notifyDataSetChanged();
                pw.dismiss();
            }
        });
        TextView tvBoy = (TextView) contentview.findViewById(R.id.tv_cancle);
        tvBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });


        //设置触摸对话框以外区域，对话框消失
        pw.setCanceledOnTouchOutside(true);
        pw.show();
    }
}
