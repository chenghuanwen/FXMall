package com.dgkj.fxmall.adapter;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Android004 on 2017/3/25.
 */

public class ProductDetialImageAdapter extends CommonAdapter<String> {
    private Context context;
    public ProductDetialImageAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        Glide.with(context).load(s).placeholder(R.mipmap.android_quanzi).into((ImageView) holder.getView(R.id.iv_prouct_detial));
    }
}
