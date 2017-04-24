package com.dgkj.fxmall.view.myView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.ShoppingCarBean;
import com.dgkj.fxmall.bean.ShoppingGoodsBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnSelectColorSizeFinishedListener;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.view.ConfirmOrderActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Created by Android004 on 2017/4/5.
 */
@SuppressLint("ValidFragment")
public class SelectColorAndSizeDialog extends DialogFragment {
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
    public SelectColorAndSizeDialog(BaseActivity activity, int resId, ShoppingGoodsBean goods, String from, OnSelectColorSizeFinishedListener listener){
        this.resId = resId;
        this.goods = goods;
        this.from = from;
        this.activity = activity;
        this.listener = listener;
        count = goods.getCount();
        client = new OkHttpClient.Builder().build();
        sp = SharedPreferencesUnit.getInstance(getContext());
        control = new FXMallControl();
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity());
        final Dialog dialog = new Dialog(getActivity(), R.style.ColorAndSizeSelectDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 必须在设置布局之前调用
        dialog.setContentView(resId);
        dialog.setCanceledOnTouchOutside(true);

        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        final RadioButton tvCS1 = (RadioButton) dialog.findViewById(R.id.tv_cs_1);
        final RadioButton tvCS2 = (RadioButton) dialog.findViewById(R.id.tv_cs_2);
        final RadioButton tvCS3 = (RadioButton) dialog.findViewById(R.id.tv_cs_3);
        final RadioButton tvCS4 = (RadioButton) dialog.findViewById(R.id.tv_cs_4);
        TextView tvPrice = (TextView) dialog.findViewById(R.id.tv_price);
        TextView tvInventory = (TextView) dialog.findViewById(R.id.tv_inventory);
        final TextView tvColor = (TextView) dialog.findViewById(R.id.tv_color_select);
        final TextView tvCount = (TextView) dialog.findViewById(R.id.tv_edit_car_count);
        TextView tvAdd = (TextView) dialog.findViewById(R.id.tv_edit_car_add);
        TextView tvMius = (TextView) dialog.findViewById(R.id.tv_edit_car_minus);
        ImageView ivProduct = (ImageView) dialog.findViewById(R.id.iv_product_photo);
        ImageView ivCancel = (ImageView) dialog.findViewById(R.id.iv_cancel);
        RadioGroup rgCS = (RadioGroup) dialog.findViewById(R.id.rg_cs);
        Glide.with(getContext()).load(goods.getUrl()).into(ivProduct);
        tvPrice.setText("¥"+goods.getPrice());
        tvCount.setText(count+"");
        tvInventory.setText("(库存:"+goods.getInventory()+"件)");
        tvColor.setText("“"+goods.getColor()+"”");

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        rgCS.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.tv_cs_1:
                        selectColor = tvCS1.getText().toString();

                        break;
                    case R.id.tv_cs_2:
                        selectColor = tvCS2.getText().toString();
                        break;
                    case R.id.tv_cs_3:
                        selectColor = tvCS3.getText().toString();
                        break;
                    case R.id.tv_cs_4:
                        selectColor = tvCS4.getText().toString();
                        break;
                }
                tvColor.setText("“"+selectColor+"”");
            }
        });

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
            TextView tvAdd2Car = (TextView) dialog.findViewById(R.id.tv_add_to_car);
            TextView tv2Buy = (TextView) dialog.findViewById(R.id.tv_go_buy);
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
            TextView tvConfirm = (TextView) dialog.findViewById(R.id.tv_confirm);
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
                        getContext().startActivity(intent);
                        listener.selectCompelete(selectColor);
                    }else {
                        listener.selectCompelete(selectColor);
                        dialog.dismiss();
                    }
                }
            });
        }

        return dialog;
    }
}
