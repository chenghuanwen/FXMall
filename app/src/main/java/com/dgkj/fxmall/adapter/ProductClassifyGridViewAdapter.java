package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.ProductClassifyBean;
import com.dgkj.fxmall.view.SomeDemandClassifyActivity;
import com.dgkj.fxmall.view.SomeProductSubClassifyActivity;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;


import java.util.List;

/**
 * Created by Android004 on 2017/3/23.
 */

public class ProductClassifyGridViewAdapter extends CommonAdapter<ProductClassifyBean> {
    private List<ProductClassifyBean> datas;
    private Context context;
    private String from;//区分从产品大厅或需求大厅来
    public ProductClassifyGridViewAdapter(Context context, int layoutId, List<ProductClassifyBean> datas, String from) {
        super(context, layoutId, datas);
        this.datas = datas;
        this.context = context;
        this.from = from;
    }

    @Override
    protected void convert(ViewHolder viewHolder, final ProductClassifyBean item, int position) {
        ProductClassifyBean classify = datas.get(position);
      //  viewHolder.setImageUrl(R.id.iv_item_classify,classify.getUrl());
        viewHolder.setText(R.id.tv_item_classify_name,classify.getTaxon());
        ImageView view = viewHolder.getView(R.id.iv_item_classify);
        Glide.with(context).load(classify.getUrl()).placeholder(R.mipmap.android_quanzi).into(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if("product".equals(from)){
                    intent = new Intent(context,SomeProductSubClassifyActivity.class);
                    intent.putExtra("from","product");
                }else {
                    intent = new Intent(context, SomeDemandClassifyActivity.class);
                }
                intent.putExtra("type",item.getTaxon());
                intent.putExtra("subId",item.getId());
                context.startActivity(intent);
            }
        });

    }
}
