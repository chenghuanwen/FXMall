package com.dgkj.fxmall.adapter;

import android.content.Context;

import com.dgkj.fxmall.R;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by Android004 on 2017/3/13.
 */

public class DistrictAdapter extends CommonAdapter<String> {
    public DistrictAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, String item, int position) {
                viewHolder.setText(R.id.tv_province,item);
    }
}
