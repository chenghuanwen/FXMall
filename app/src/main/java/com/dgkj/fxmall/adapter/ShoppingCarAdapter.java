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
    private Handler handler;
    private Handler superHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0://全选某商店商品
                    int superposition = msg.arg1;
                    mDatas.get(superposition).setSelected(true);
                    notifyItemChanged(superposition);
                    break;
                case 1://全不选某商店商品
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
    protected void convert(ViewHolder holder, final ShoppingCarBean shoppingCar, int position) {
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
            checkBox.setChecked(true);
          //  checkBox.setButtonDrawable(R.mipmap.sc_zcx);
        }else {
            checkBox.setChecked(false);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    shoppingCar.setSelected(true);
                    for (ShoppingGoodsBean good : goods) {
                        good.setSelected(true);
                    }
                }else {
                    shoppingCar.setSelected(false);
                    for (ShoppingGoodsBean good : goods) {
                        good.setSelected(false);
                    }
                }
                subAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 所有商品全选
     */
    public void selectAll(){
        for (ShoppingCarBean mData : mDatas) {
            mData.setSelected(true);
        }
        notifyDataSetChanged();
    }

    public void cancleAll(){
        for (ShoppingCarBean mData : mDatas) {
            mData.setSelected(false);
        }
        notifyDataSetChanged();
    }
}
