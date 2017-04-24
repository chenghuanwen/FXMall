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
import com.dgkj.fxmall.bean.ShoppingGoodsBean;
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

        //当父item中只有一条数据时，该子item被选中则父item应为全选状态，反正亦然
        if(datas.size()==1 && checkBox.isChecked()){
            Message msg = Message.obtain();
            msg.arg1 = superPosition;
            msg.what = 0;//父item全选
            superHandler.sendMessage(msg);
        }else if(datas.size()==1 && !checkBox.isChecked()){
            Message msg = Message.obtain();
            msg.arg1 = superPosition;
            msg.what = 1;//父item全不选
            superHandler.sendMessage(msg);
        }



        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                   goods.setSelected(true);
                    Message msg = Message.obtain();
                    msg.arg1 = (int) goods.getPrice();
                    msg.arg2 = goods.getCount();
                    msg.what = 1;//统计价格增加
                    handler.sendMessage(msg);
                }else {
                    goods.setSelected(false);
                    Message msg = Message.obtain();
                    msg.arg1 = (int) goods.getPrice();
                    msg.arg2 = goods.getCount();
                    msg.what = 2;//统计价格减少
                    handler.sendMessage(msg);
                }
//              notifyDataSetChanged();
     //           notifyItemChanged(position);
            }
        });

        Glide.with(context).load(goods.getUrl()).placeholder(R.mipmap.android_quanzi).into(iv);
        holder.setText(R.id.tv_car_goods_introduce,goods.getIntroduce());
        holder.setText(R.id.tv_car_goods_color,"颜色："+goods.getColor());
      //  holder.setText(R.id.tv_car_goods_size,"尺寸："+goods.getSize());
        holder.setText(R.id.tv_car_goods_price,"¥"+goods.getPrice()+"");
        holder.setText(R.id.tv_car_goods_count,"x"+goods.getCount());

        holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetialActivity.class);
                intent.putExtra("from","car");
                intent.putExtra("product",goods);
                context.startActivity(intent);
            }
        });

    }
}
