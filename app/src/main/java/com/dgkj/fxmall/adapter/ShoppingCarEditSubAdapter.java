package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.ShoppingGoodsBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Android004 on 2017/3/24.
 */

public class ShoppingCarEditSubAdapter extends CommonAdapter<ShoppingGoodsBean> {
    private Context context;
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
    private List<ShoppingGoodsBean> datas;

    public ShoppingCarEditSubAdapter(Context context, int layoutId, List<ShoppingGoodsBean> datas,Handler handler,Handler superHandler,int superPosition) {
        super(context, layoutId, datas);
        this.context = context;
        this.handler = handler;
        this.datas = datas;
        this.superHandler = superHandler;
        this.superPosition = superPosition;
    }

    @Override
    protected void convert(final ViewHolder holder, final ShoppingGoodsBean goods, final int position) {
        final CheckBox checkBox =  holder.getView(R.id.cb_edit_select_single);
        ImageView iv =  holder.getView(R.id.iv_edit_car_goods);
        final int[] count = {goods.getCount()};
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
                    msg.what = 1;//选中当前
                    handler.sendMessage(msg);

                    //通知父item该条被选中
                    Message supMsg = Message.obtain();
                    supMsg.arg1 = superPosition;
                    supMsg.arg2 = position;
                    supMsg.what = 1;
                    superHandler.sendMessage(supMsg);


                    //轮询当前店铺所有商品，若均已选中则通知父adapter将店铺设为全选状态
                    for (ShoppingGoodsBean data : datas) {
                        if(!data.isSelected()){
                            return;
                        }
                    }
                    Message msg1 = Message.obtain();
                    msg1.arg1 = superPosition;
                    msg1.what = 3;//父item全选
                    superHandler.sendMessage(msg1);
                }else {
                    goods.setSelected(false);
                    Message msg = Message.obtain();
                    msg.obj = goods;
                    msg.what = 2;//取消选中
                    handler.sendMessage(msg);

                    //通知父item该条被选中
                    Message supMsg = Message.obtain();
                    supMsg.arg1 = superPosition;
                    supMsg.arg2 = position;
                    supMsg.what = 2;
                    superHandler.sendMessage(supMsg);


                    //轮询当前店铺所有商品，若均未选中则通知父adapter将店铺设为全选状态
                    for (ShoppingGoodsBean data : datas) {
                        if(data.isSelected()){
                            return;
                        }
                    }
                    Message msg1 = Message.obtain();
                    msg1.arg1 = superPosition;
                    msg1.what = 4;//父item全选
                    superHandler.sendMessage(msg1);
                }

            }
        });

        Glide.with(context).load(goods.getUrl()).placeholder(R.mipmap.android_quanzi).into(iv);
        holder.setText(R.id.tv_edit_car_goods_color,"颜色："+goods.getColor());
       // holder.setText(R.id.tv_edit_car_goods_size,"尺寸："+goods.getSize());
        holder.setText(R.id.tv_edit_car_count, count[0] +"");

        holder.setOnClickListener(R.id.tv_edit_car_add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0] += 1;
                holder.setText(R.id.tv_edit_car_count, count[0] +"");
                goods.setCount(count[0]);
                Message msg = Message.obtain();
                msg.arg1 = superPosition;
                msg.arg2 = position;
                msg.obj = goods;
                msg.what = 3;//数量改变
                handler.sendMessage(msg);
            }
        });

        holder.setOnClickListener(R.id.tv_edit_car_minus, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count[0] >0){
                    count[0] -= 1;
                }
                holder.setText(R.id.tv_edit_car_count, count[0] +"");
                goods.setCount(count[0]);
                Message msg = Message.obtain();
                msg.arg1 = superPosition;
                msg.arg2 = position;
                msg.obj = goods;
                msg.what = 3;//数量改变
                handler.sendMessage(msg);
            }
        });

        holder.setOnClickListener(R.id.tv_edit_car_goods_color, new View.OnClickListener() {//选择颜色尺寸
            @Override
            public void onClick(View v) {
                Message message = Message.obtain();
                message.what = 4;
                message.arg1=position;
                message.obj = goods;
                handler.sendMessage(message);
            }
        });

    }
}
