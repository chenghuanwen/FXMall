package com.dgkj.fxmall.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.TakeGoodsAddressBean;
import com.dgkj.fxmall.view.AddGoodsAdressActivity;
import com.dgkj.fxmall.view.ConfirmOrderActivity;
import com.dgkj.fxmall.view.SearchActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Android004 on 2017/3/28.
 */

public class AdressManagerAdapter extends CommonAdapter<TakeGoodsAddressBean> {
    private Context context;
    private AlertDialog pw;
    private Handler handler;
    private AppCompatActivity activity;

    public AdressManagerAdapter(Context context,AppCompatActivity activity, int layoutId, List<TakeGoodsAddressBean> datas,Handler handler) {
        super(context, layoutId, datas);
        this.context = context;
        this.handler = handler;
        this.activity = activity;
    }

    @Override
    protected void convert(ViewHolder holder, final TakeGoodsAddressBean addressBean, final int position) {
        holder.setText(R.id.tv_takegoods_man,addressBean.getName());
        holder.setText(R.id.tv_takegoods_phone,addressBean.getPhone());
        holder.setText(R.id.tv_takegoods_address,addressBean.getAddress());
       CheckBox cb =  holder.getView(R.id.cb_set_default);
        if(addressBean.isDefault()){
            cb.setChecked(true);
        }else {
            cb.setChecked(false);
        }

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    for (TakeGoodsAddressBean mData : mDatas) {
                        if(mData.isDefault()){
                            mData.setDefault(false);
                        }
                    }
                    addressBean.setDefault(true);//此条设为默认时，其他条将取消默认

                    //TODO 将当前收货地址设为默认，发送到服务器
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = addressBean;
                    handler.sendMessage(message);
                }else {
                    addressBean.setDefault(false);
                    handler.sendEmptyMessage(2);
                }
            //  notifyDataSetChanged();
            }
        });


        holder.setOnClickListener(R.id.tv_address_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddGoodsAdressActivity.class);
                intent.putExtra("from","edit");
                intent.putExtra("position",position);
                intent.putExtra("address",addressBean);
                activity.startActivityForResult(intent,151);
            }
        });

        holder.setOnClickListener(R.id.tv_address_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDeleteDialog(position);
                //TODO 通知服务器删除该条地址信息
            }
        });

        holder.setOnClickListener(R.id.ll_take_address, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConfirmOrderActivity.class);
                intent.putExtra("address",addressBean);
                    activity.setResult(136,intent);
            }
        });
    }

    private void showDeleteDialog(final int position){
        View contentview = mInflater.inflate(R.layout.layout_delete_dialog, null);
        pw = new AlertDialog.Builder(context).create();
        pw.setView(contentview);
        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_confirm);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = Message.obtain();
                message.what = 3;
                message.arg1 = position;
                message.obj = mDatas.get(position);
                handler.sendMessage(message);
              /*  mDatas.remove(position);
                notifyDataSetChanged();*/
                pw.dismiss();
            }
        });
        TextView tvBoy = (TextView) contentview.findViewById(R.id.tv_cancle);
        tvBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });


        //设置触摸对话框以外区域，对话框消失
        pw.setCanceledOnTouchOutside(true);
        pw.show();
    }

}
