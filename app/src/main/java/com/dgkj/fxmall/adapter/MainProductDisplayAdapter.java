package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.listener.LoadMoreListener;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.view.DemandDetialActivity;
import com.dgkj.fxmall.view.ProductDetialActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android004 on 2017/3/23.
 */

public class MainProductDisplayAdapter extends CommonAdapter<MainProductBean> {
    private List<MainProductBean> datas;
    private Context context;
    private String from;//判断从需求大厅来还是从产品大厅来
    private LoadMoreListener loadMoreListener;
    public MainProductDisplayAdapter(Context context, List<MainProductBean> datas,String from) {
        super(context, -1, datas);
        this.datas = datas;
        this.context = context;
        this.from = from;
    }



    @Override
    public int getItemViewType(int position) {
        if(position==datas.size()-1){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return datas==null?0:datas.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==1){
            view = mInflater.inflate( R.layout.item_main_product,null);
            return new ViewHolder(context,view);
        }else {
          view = mInflater.inflate(R.layout.item_load_more,null);
           // view = mInflater.inflate( R.layout.item_main_product,null);
            return new ViewHolder(context,view);
        }
    }


    @Override
    protected void convert(ViewHolder viewHolder, MainProductBean mainProductBean, int position) {
        LogUtil.i("TAG","=====开始设定数据========"+datas.size()+"====viewType=="+getItemViewType(position));

            final MainProductBean product = datas.get(position);
            //viewHolder.setImageUrl(R.id.iv_main_item,product.getUrl());
            viewHolder.setText(R.id.tv_main_item_title,product.getIntroduce());
            viewHolder.setText(R.id.tv_main_item_address,product.getAddress());
            viewHolder.setText(R.id.tv_main_item_price,"¥"+product.getPrice());
            viewHolder.setText(R.id.tv_main_item_vipPrice,"会员价:¥"+product.getVipPrice());
            viewHolder.setText(R.id.tv_main_item_sale,"销量:"+product.getSales());
            ImageView view = viewHolder.getView(R.id.iv_main_item);
            Glide.with(context).load(product.getUrl()).placeholder(R.mipmap.android_quanzi).into(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if("product".equals(from)){
                        intent = new Intent(context, ProductDetialActivity.class);
                        intent.putExtra("from","main");
                    }else {
                        intent = new Intent(context, DemandDetialActivity.class);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("product",product);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
      if (getItemViewType(position)==0){
            viewHolder.setOnClickListener(R.id.tv_loadmore, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMoreListener.onLoadmore();
                }
            });
        }


    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener){
        this.loadMoreListener = loadMoreListener;
    }

}
