package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.ShoppingGoodsBean;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.view.ProductDetialActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Android004 on 2017/3/24.
 */

public class ShoppingCarSubAdapter extends CommonAdapter<ShoppingGoodsBean> {
    private Context context;
    private List<ShoppingGoodsBean> datas;
    /**
     * 通知主界面所有的选中状态
     */
    private Handler handler;
    /**
     * 向父item发送当前商铺中商品选中状态
     */
    private Handler superHandler;
    /***
     * 父item在整个列表中的位置
     */
    private int superPosition;
    public ShoppingCarSubAdapter(Context context, int layoutId, List<ShoppingGoodsBean> datas,Handler handler,Handler superHandler,int superPosition) {
        super(context, layoutId, datas);
        this.context = context;
        this.datas = datas;
        this.handler = handler;
        this.superHandler = superHandler;
        this.superPosition = superPosition;
    }

    @Override
    protected void convert(final ViewHolder holder, final ShoppingGoodsBean goods, final int position) {
       final CheckBox checkBox =  holder.getView(R.id.cb_select_single);
       ImageView iv =  holder.getView(R.id.iv_car_goods);

        if(goods.isSelected()){
            checkBox.setChecked(true);
        }else {
            checkBox.setChecked(false);
        }


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                   goods.setSelected(true);
                    Message msg = Message.obtain();
                    msg.obj = goods;
                    msg.what = 1;//统计价格增加
                    handler.sendMessage(msg);

                    //轮询当前店铺所有商品，若均已选中则通知父adapter将店铺设为全选状态
                    for (ShoppingGoodsBean data : datas) {
                        if(!data.isSelected()){
                            return;
                        }
                    }
                    Message msg1 = Message.obtain();
                    msg1.arg1 = superPosition;
                    msg1.what = 0;//父item全选
                    superHandler.sendMessage(msg1);

                }else {
                    goods.setSelected(false);
                    Message msg = Message.obtain();
                    msg.obj = goods;
                    msg.what = 2;//统计价格减少
                    handler.sendMessage(msg);

                    //轮询当前店铺所有商品，若均未选中则通知父adapter将店铺设为全选状态
                    for (ShoppingGoodsBean data : datas) {
                        if(data.isSelected()){
                            return;
                        }
                    }
                    Message msg1 = Message.obtain();
                    msg1.arg1 = superPosition;
                    msg1.what = 1;//父item全选
                    superHandler.sendMessage(msg1);
                }

            }
        });

        Glide.with(context).load(goods.getUrl()).placeholder(R.mipmap.android_quanzi).into(iv);
        holder.setText(R.id.tv_car_goods_introduce,goods.getIntroduce());
        holder.setText(R.id.tv_car_goods_color,"颜色："+goods.getColor());
      //  holder.setText(R.id.tv_car_goods_size,"尺寸："+goods.getSize());
        holder.setText(R.id.tv_car_goods_price,"¥"+goods.getVipPrice()+"");
        holder.setText(R.id.tv_car_goods_count,"x"+goods.getCount());

        holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetialActivity.class);
                intent.putExtra("from","car");

                MainProductBean product = new MainProductBean();
                product.setBrokerage(goods.getBrokerage());
                product.setId(goods.getProductId());
                product.setUrl(goods.getUrl());
                product.setInventory(goods.getInventory());
                product.setColor(goods.getColor());
                product.setAddress(goods.getAddress());
                product.setCount(goods.getCount());
                product.setDetialUrls(goods.getDetialUrls());
                product.setUrls(goods.getMainUrls());
                product.setDescribeScore(goods.getDescribeScore());
                product.setPriceScore(goods.getPriceScore());
                product.setQualityScore(goods.getQualityScore());
                product.setExpress(goods.getPostage()+"");//TODO 邮费
                product.setIntroduce(goods.getIntroduce());
                product.setSales(goods.getSales()+"");
                product.setStoreBean(goods.getStoreBean());
                product.setVipPrice(goods.getVipPrice());
                product.setTotalScore(goods.getTotalScore());
                product.setSkuId(goods.getSkuId());
                product.setPrice(goods.getPrice());
                intent.putExtra("product",product);
                context.startActivity(intent);
            }
        });

    }
}
