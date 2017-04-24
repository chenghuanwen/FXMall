package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.SuperClassifyBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by Android004 on 2017/4/17.
 */

public class StoreSuperClassifyAdapter extends CommonAdapter<SuperClassifyBean> {
    private Handler handler;
    public StoreSuperClassifyAdapter(Context context, int layoutId, List<SuperClassifyBean> datas,Handler handler) {
        super(context, layoutId, datas);
        this.handler = handler;
    }

    @Override
    protected void convert(ViewHolder viewHolder, final SuperClassifyBean item, final int position) {
                viewHolder.setText(R.id.tv_classify,item.getType());

                CheckBox cb = viewHolder.getView(R.id.tv_classify);


                if(item.isSelected()){
                    cb.setChecked(true);
                }else {
                    cb.setChecked(false);
                }


                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                          //  item.setSelected(true);
                            Message msg = Message.obtain();
                            msg.arg1 = position;
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }else {
                            item.setSelected(false);
                        }
                    }
                });

    }
}
