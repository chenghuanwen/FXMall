package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.NiceStoreBean;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.view.StoreMainPageActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Android004 on 2017/3/23.
 */

public class NiceStoreRecommendAdapter extends CommonAdapter<NiceStoreBean> {

    private static final int TYPE_ONE = 0;
    private static final int TYPE_TWO = 1;
    private static final int TYPE_THREE = 2;

    private Context context;
    private List<NiceStoreBean> datas;
    public NiceStoreRecommendAdapter(Context context,List<NiceStoreBean> datas) {
        super(context, -1, datas);
        this.datas = datas;
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, final NiceStoreBean storeBean, int position) {
        holder.setText(R.id.tv_nicestore_name,storeBean.getName());
        holder.setText(R.id.tv_nicestore_introduce,storeBean.getIntroduce());
        holder.setText(R.id.tv_nicestore_address,storeBean.getAddress());
        ImageView view = holder.getView(R.id.iv_nicestore);
        ImageView icon = holder.getView(R.id.iv_store_icon);

        Glide.with(context).load(storeBean.getUrl()).into(view);
        Glide.with(context).load(storeBean.getLogo()).into(view);
        holder.setOnClickListener(R.id.iv_nicestore, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getStoreDetial(storeBean.getId());
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null ;
        View view;
        switch (viewType){
            case TYPE_ONE:
                view = mInflater.inflate(R.layout.item_niecstore_recommend1,parent,false);
                viewHolder = new ViewHolder(context,view);
                return viewHolder;
            case TYPE_TWO:
                view = mInflater.inflate(R.layout.item_niecstore_recommend2,parent,false);
                viewHolder = new ViewHolder(context,view);
                return viewHolder;
            case TYPE_THREE:
                view = mInflater.inflate(R.layout.item_niecstore_recommend3,parent,false);
                viewHolder = new ViewHolder(context,view);
                return viewHolder;

        }

        return super.onCreateViewHolder(parent, viewType);
    }


    @Override
    public int getItemViewType(int position) {
        if(position<3){
            switch (position){
                case 0:
                    return TYPE_ONE;
                case 1:
                    return TYPE_TWO;
                case 2:
                    return TYPE_THREE;
            }
        }else {

            switch (position%3){
                case 0:
                    return TYPE_THREE;
                case 1:
                    return TYPE_ONE;
                case 2:
                    return TYPE_TWO;
            }
        }
        return super.getItemViewType(position);
    }



    //TEST
    private void getStoreDetial(int storeId){
        StoreBean storeBean = new StoreBean();
        storeBean.setName("粉小萌");
        storeBean.setAdress("广东省深圳市龙岗区");
        storeBean.setIconUrl("http://img2015.zdface.com/20170417/06bf77be0521dc47da46f596893b0dbf.jpg");
        storeBean.setCreateTime("2017-5-2");
        storeBean.setStars(3);
        storeBean.setTotalScals(300);
        storeBean.setGoodsCount(1000);
        storeBean.setId(5);
        storeBean.setDescribeScore(4.8);
        storeBean.setPriceScore(4.0);
        storeBean.setQualityScore(4.2);
        storeBean.setTotalScore(4.5);
        storeBean.setLicence("http://img2015.zdface.com/20170417/06bf77be0521dc47da46f596893b0dbf.jpg");
        storeBean.setMainUrls(new ArrayList<String>());
        storeBean.setKeeper("小成成");
        storeBean.setPhone("15641651432");
        storeBean.setRecommender("小成成");

        Intent intent = new Intent(context, StoreMainPageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("store", storeBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
