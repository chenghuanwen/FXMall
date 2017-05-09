package com.dgkj.fxmall.view.myView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.SelectColorAndSizeAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.ColorSizeBean;
import com.dgkj.fxmall.bean.ShoppingCarBean;
import com.dgkj.fxmall.bean.ShoppingGoodsBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetProductCSFinishedListener;
import com.dgkj.fxmall.listener.OnSelectColorSizeFinishedListener;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.view.ConfirmOrderActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;

/**
 * Created by Android004 on 2017/4/5.
 */
@SuppressLint("ValidFragment")
public class SelectColorAndSizeDialog extends PopupWindow {
    private int resId;
    private ShoppingGoodsBean goods;
    private int count;
    private String from;
    private OkHttpClient client;
    private SharedPreferencesUnit sp;
    private FXMallControl control;
    private BaseActivity activity;
    private String selectColor = "";
    private OnSelectColorSizeFinishedListener listener;
    private View conentView;
    private RecyclerView rvcs;
    private TextView tvPrice,tvInventory,tvColor,tvCount,tvAdd,tvMius;
    private ImageView ivProduct,ivCancel;
    private SelectColorAndSizeAdapter adapter;
    private List<ColorSizeBean> colorSizeList;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                adapter.notifyDataSetChanged();
                ColorSizeBean bean = (ColorSizeBean) msg.obj;
                tvColor.setText(bean.getColor());
                tvInventory.setText(bean.getInventory());
                Glide.with(activity).load(goods.getUrl());
                selectColor = bean.getColor();
            }
            super.handleMessage(msg);
        }
    };
    public SelectColorAndSizeDialog(BaseActivity activity, int resId, ShoppingGoodsBean goods, String from, OnSelectColorSizeFinishedListener listener){
        this.resId = resId;
        this.goods = goods;
        this.from = from;
        this.activity = activity;
        this.listener = listener;
        count = goods.getCount();
        client = new OkHttpClient.Builder().build();
        sp = SharedPreferencesUnit.getInstance(activity);
        control = new FXMallControl();
        init();
        initColor();
        getColorAndSize();
        setData();


    }

    private void initColor() {
        colorSizeList = new ArrayList<>();
        adapter = new SelectColorAndSizeAdapter(activity,R.layout.item_product_color_size,colorSizeList,handler);
        GridLayoutManager layoutManager = new GridLayoutManager(activity,4);
        rvcs.setLayoutManager(layoutManager);
        rvcs.setAdapter(adapter);
    }

    /**
     * 获取该商品不同的颜色和尺寸
     */
    private void getColorAndSize() {
       control.getProductCS(client, goods.getProductId(), new OnGetProductCSFinishedListener() {
           @Override
           public void onGetProductCSFinishedListener(List<ColorSizeBean> sizes) {
               for (ColorSizeBean size : sizes) {
                   if(goods.getColor().equals(size.getColor())){
                       size.setCheck(true);
                   }
               }
              adapter.addAll(sizes,true);
           }
       });
    }

    private void setData() {
        selectColor = goods.getColor();
        Glide.with(activity).load(goods.getUrl()).into(ivProduct);
        tvPrice.setText("¥"+goods.getPrice());
        tvCount.setText(count+"");
        tvInventory.setText("(库存:"+goods.getInventory()+"件)");
        tvColor.setText("“"+goods.getColor()+"”");

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        tvColor.setText("“"+selectColor+"”");

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count += 1;
                tvCount.setText(count+"");
            }
        });
        tvMius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count -= 1;
                tvCount.setText(count+"");
            }
        });

        if(resId==R.layout.layout_car_select_color_size){
            TextView tvAdd2Car = (TextView) conentView.findViewById(R.id.tv_add_to_car);
            TextView tv2Buy = (TextView) conentView.findViewById(R.id.tv_go_buy);
            tvAdd2Car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 加入购物车
                    control.add2shoppingcar(activity,sp.get("token"),goods.getSkuId(),client);
                    dismiss();
                }
            });
            tv2Buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 立即购买
                    ArrayList<ShoppingCarBean> orders = new ArrayList<>();
                    ShoppingCarBean carBean = new ShoppingCarBean();
                    ArrayList<ShoppingGoodsBean> list = new ArrayList<>();
                    goods.setCount(count);
                    goods.setColor(selectColor);
                    list.add(goods);
                    carBean.setStoreName(goods.getStoreName());
                    carBean.setGoods(list);
                    orders.add(carBean);
                    Intent intent = new Intent(activity,ConfirmOrderActivity.class);
                    intent.putExtra("orders",orders);
                    activity.startActivity(intent);
                }
            });
        }else {
            TextView tvConfirm = (TextView) conentView.findViewById(R.id.tv_confirm);
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 确认修改
                    if("buy".equals(from)){
                        ArrayList<ShoppingCarBean> orders = new ArrayList<>();
                        ShoppingCarBean carBean = new ShoppingCarBean();
                        ArrayList<ShoppingGoodsBean> list = new ArrayList<>();
                        goods.setCount(count);
                        goods.setColor(selectColor);
                        list.add(goods);
                        carBean.setStoreName(goods.getStoreName());
                        carBean.setGoods(list);
                        orders.add(carBean);
                        Intent intent = new Intent(activity,ConfirmOrderActivity.class);
                        intent.putExtra("orders",orders);
                        activity.startActivity(intent);
                        listener.selectCompelete(selectColor);
                    }else {
                        listener.selectCompelete(selectColor);
                        dismiss();
                    }
                }
            });
        }

    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(resId, null);

        rvcs = (RecyclerView) conentView.findViewById(R.id.rv_cs);
        tvPrice = (TextView) conentView.findViewById(R.id.tv_price);
        tvInventory = (TextView) conentView.findViewById(R.id.tv_inventory);
        tvColor = (TextView) conentView.findViewById(R.id.tv_color_select);
        tvCount = (TextView) conentView.findViewById(R.id.tv_edit_car_count);
        tvAdd = (TextView) conentView.findViewById(R.id.tv_edit_car_add);
        tvMius = (TextView) conentView.findViewById(R.id.tv_edit_car_minus);
        ivProduct = (ImageView) conentView.findViewById(R.id.iv_product_photo);
        ivCancel = (ImageView) conentView.findViewById(R.id.iv_cancel);

        int h = activity.getWindowManager().getDefaultDisplay().getHeight();
        int w = activity.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAtLocation(parent,Gravity.BOTTOM,0,0);
        } else {
            this.dismiss();
        }
    }
}
