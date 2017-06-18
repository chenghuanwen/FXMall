package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.ShoppingCarBean;
import com.dgkj.fxmall.bean.ShoppingGoodsBean;
import com.dgkj.fxmall.view.myView.FullyLinearLayoutManager;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android004 on 2017/3/24.
 */

public class ShoppingCarAdapter extends CommonAdapter<ShoppingCarBean> {
    private Context context;
    private ShoppingCarSubAdapter subAdapter;
    private boolean isSuperCheck;//当全选状态时，由于单个子项取消了，此时改变全选状态，但不应触发全不选事件
    private Handler handler;
    private Handler superHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1://某个商店的某个商品被选中
                  int superPosition = msg.arg1;
                    int subPosition = msg.arg2;
                    mDatas.get(superPosition).getGoods().get(subPosition).setSelected(true);
                    break;
                case 2://某个商店的某个商品取消
                    int superPosition1 = msg.arg1;
                    int subPosition1 = msg.arg2;
                    mDatas.get(superPosition1).getGoods().get(subPosition1).setSelected(false);
                    //更改全选状态
                    mDatas.get(superPosition1).setSelected(false);
                    notifyItemChanged(superPosition1);
                    isSuperCheck = true;
                    break;
                case 3://全选某商店商品
                    int superposition = msg.arg1;
                    mDatas.get(superposition).setSelected(true);
                    notifyItemChanged(superposition);
                    break;
                case 4://全不选某商店商品
                    int superposition1 = msg.arg1;
                    mDatas.get(superposition1).setSelected(false);
                    notifyItemChanged(superposition1);
                    break;
            }
        }
    };
    public ShoppingCarAdapter(Context context, int layoutId, List<ShoppingCarBean> datas,Handler handler) {
        super(context, layoutId, datas);
        this.context = context;
        this.handler = handler;
    }

    @Override
    protected void convert(ViewHolder holder, final ShoppingCarBean shoppingCar, final int position) {
       final CheckBox checkBox =  holder.getView(R.id.cb_store_all);
        checkBox.setText(shoppingCar.getStoreName());
        RecyclerView rv = holder.getView(R.id.rv_sub_shoppingcar);

        final ArrayList<ShoppingGoodsBean> goods = shoppingCar.getGoods();
        subAdapter = new ShoppingCarSubAdapter(context,R.layout.item_sub_shoppingcar, goods,handler,superHandler,position);
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(context);
        linearLayoutManager.setAutoMeasureEnabled(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        rv.setNestedScrollingEnabled(false);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(subAdapter);

        if(shoppingCar.isSelected()){
           // checkBox.setChecked(true);
            checkBox.setButtonDrawable(R.mipmap.yixuan);
        }else {
            checkBox.setChecked(false);
            checkBox.setButtonDrawable(R.mipmap.weixuan);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    shoppingCar.setSelected(true);
                    for (ShoppingGoodsBean good : goods) {
                        if(!good.isSelected()){
                            good.setSelected(true);

                            Message msg = Message.obtain();
                            msg.obj = good;
                            msg.what = 1;//统计价格增加
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    if(!isSuperCheck){
                        shoppingCar.setSelected(false);
                        for (ShoppingGoodsBean good : goods) {
                            if(good.isSelected()){
                                good.setSelected(false);

                                Message msg = Message.obtain();
                                msg.obj = good;
                                msg.what = 2;//统计价格减少
                                handler.sendMessage(msg);
                            }
                        }

                    }
                    isSuperCheck = false;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    /**
     * 所有商品全选
     */
    public void selectAll(){
        for (ShoppingCarBean mData : mDatas) {
            mData.setSelected(true);
            ArrayList<ShoppingGoodsBean> goods = mData.getGoods();
            for (ShoppingGoodsBean good : goods) {
                if(!good.isSelected()){
                    good.setSelected(true);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void cancleAll(){
        for (ShoppingCarBean mData : mDatas) {
            mData.setSelected(false);
            ArrayList<ShoppingGoodsBean> goods = mData.getGoods();
            for (ShoppingGoodsBean good : goods) {
                if(good.isSelected()){
                    good.setSelected(false);
                }
            }
        }
        notifyDataSetChanged();
    }
}
