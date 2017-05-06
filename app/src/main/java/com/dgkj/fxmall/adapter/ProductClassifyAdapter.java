package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.ProductClassifyBean;
import com.dgkj.fxmall.view.SearchContentActivity;
import com.dgkj.fxmall.view.SomeDemandClassifyActivity;
import com.dgkj.fxmall.view.SomeProductSubClassifyActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;


import java.util.List;

/**
 * Created by Android004 on 2017/3/23.
 */

public class ProductClassifyAdapter extends CommonAdapter<ProductClassifyBean> {
    private List<ProductClassifyBean> datas;
    private Context context;
    private String from;//区分从产品大厅或需求大厅来
    private String subClassify="";
    private int subClassifyId;
    public ProductClassifyAdapter(Context context, int layoutId, List<ProductClassifyBean> datas,String from) {
        super(context, layoutId, datas);
        this.datas = datas;
        this.context = context;
        this.from = from;
    }

    @Override
    protected void convert(final ViewHolder viewHolder, final ProductClassifyBean item, final int position) {

        viewHolder.setText(R.id.tv_item_classify_name,item.getTaxon());
        ImageView view = viewHolder.getView(R.id.iv_item_classify);
        Glide.with(context).load(item.getUrl()).placeholder(R.mipmap.android_quanzi).into(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if("product".equals(from)){
                    intent = new Intent(context,SomeProductSubClassifyActivity.class);
                }else if("classify".equals(from)){
                   ImageView iv = viewHolder.getView(R.id.iv_selected);
                    iv.setVisibility(View.VISIBLE);
                    subClassify = item.getTaxon();
                    subClassifyId = item.getId();
                    return;
                }else if("search".equals(from)){
                    intent = new Intent(context, SearchContentActivity.class);
                }else {
                    intent = new Intent(context, SomeDemandClassifyActivity.class);
                    intent.putExtra("superId",item.getId());
                }
                intent.putExtra("type",item.getTaxon());
                context.startActivity(intent);
            }
        });
    }

    public String getSubClassify(){
        return subClassify;
    }

    public int getSubClassifyId(){
        return subClassifyId;
    }
}
