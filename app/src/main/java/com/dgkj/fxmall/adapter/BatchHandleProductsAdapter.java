package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.ShoppingCarBean;
import com.dgkj.fxmall.bean.StoreProductBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Android004 on 2017/4/1.
 */

public class BatchHandleProductsAdapter extends CommonAdapter<StoreProductBean> {
    private Context context;
    private Handler handler;
    public BatchHandleProductsAdapter(Context context, int layoutId, List<StoreProductBean> datas,Handler handler) {
        super(context, layoutId, datas);
        this.context = context;
        this.handler = handler;
    }

    @Override
    protected void convert(final ViewHolder holder, final StoreProductBean storeProduct, int position) {
        holder.setText(R.id.tv_store_goods_introduce,storeProduct.getDescribe());
        holder.setText(R.id.tv_store_goods_introduce,"¥"+storeProduct.getPrice());
        holder.setText(R.id.tv_store_goods_introduce,"销量"+storeProduct.getSales());
        holder.setText(R.id.tv_store_goods_introduce,"库存"+storeProduct.getInventory());

        CheckBox cb = holder.getView(R.id.cb_batch_handle);

        if(storeProduct.isSelect()){
            cb.setChecked(true);
        }else {
            cb.setChecked(false);
        }

        ImageView iv = holder.getView(R.id.iv_store_goods_batch);
        Glide.with(context).load(storeProduct.getUrl()).placeholder(R.mipmap.android_quanzi).into(iv);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    storeProduct.setSelect(true);
                    Message msg = Message.obtain();
                    msg.obj = storeProduct;
                    msg.arg1 = 1;
                    handler.sendMessage(msg);
                }else {
                    storeProduct.setSelect(false);
                    Message msg = Message.obtain();
                    msg.obj = storeProduct;
                    msg.arg1 = 2;
                    handler.sendMessage(msg);
                }
               // notifyDataSetChanged();
            }
        });
    }

    /**
     * 所有商品全选
     */
    public void selectAll(){
        for (StoreProductBean mData : mDatas) {
            mData.setSelect(true);
        }
        notifyDataSetChanged();
    }

    public void cancleAll(){
        for (StoreProductBean mData : mDatas) {
            mData.setSelect(false);
        }
        notifyDataSetChanged();
    }
}
