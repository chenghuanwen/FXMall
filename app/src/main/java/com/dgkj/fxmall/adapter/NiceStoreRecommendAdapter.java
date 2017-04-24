package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.NiceStoreBean;
import com.dgkj.fxmall.view.StoreMainPageActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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
    protected void convert(ViewHolder holder, NiceStoreBean storeBean, int position) {
        holder.setText(R.id.tv_nicestore_name,storeBean.getName());
        holder.setText(R.id.tv_nicestore_introduce,storeBean.getIntroduce());
        holder.setText(R.id.tv_nicestore_address,storeBean.getAddress());
        ImageView view = holder.getView(R.id.iv_nicestore);
        Glide.with(context).load(storeBean.getUrl()).placeholder(R.mipmap.android_quanzi).into(view);
        holder.setOnClickListener(R.id.iv_nicestore, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, StoreMainPageActivity.class));
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
}
