package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.ColorSizeBean;
import com.dgkj.fxmall.view.MessageCenterActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Android004 on 2017/5/5.
 */

public class SelectColorAndSizeAdapter extends CommonAdapter<ColorSizeBean> {
    private Handler handler;
    public SelectColorAndSizeAdapter(Context context, int layoutId, List<ColorSizeBean> datas,Handler handler) {
        super(context, layoutId, datas);
        this.handler = handler;
    }

    @Override
    protected void convert(final ViewHolder holder, final ColorSizeBean colorSizeBean, int position) {
        final CheckBox tvcs = holder.getView(R.id.tv_cs_1);
        holder.setText(R.id.tv_cs_1,colorSizeBean.getColor());
        if(colorSizeBean.isCheck()){
            tvcs.setChecked(true);
        }else {
            tvcs.setChecked(false);
        }
        tvcs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    for (ColorSizeBean mData : mDatas) {
                        mData.setCheck(false);
                    }
                 colorSizeBean.setCheck(true);
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = colorSizeBean;
                    handler.sendMessage(msg);
                }
            }
        });
    }
}
