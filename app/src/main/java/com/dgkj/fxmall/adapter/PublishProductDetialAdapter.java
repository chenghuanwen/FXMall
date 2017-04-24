package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Collections;
import java.util.List;

/**
 * Created by Android004 on 2017/4/7.
 */

public class PublishProductDetialAdapter extends CommonAdapter<String> {
    private Context context;

    public PublishProductDetialAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, String url, final int position) {
        ImageView iv = holder.getView(R.id.iv_product_detial);
        Glide.with(context).load(url).placeholder(R.mipmap.android_quanzi).into(iv);
        ImageView btnUp = holder.getView(R.id.btn_move_up);
        ImageView btnDown = holder.getView(R.id.btn_move_down);

        if (position == 0) {
            btnUp.setVisibility(View.GONE);
        } else {
            btnUp.setVisibility(View.VISIBLE);
        }

        if (position == mDatas.size() - 1) {
            btnDown.setVisibility(View.GONE);
        } else {
            btnDown.setVisibility(View.VISIBLE);
        }

        holder.setOnClickListener(R.id.btn_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.setOnClickListener(R.id.btn_move_up, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.swap(mDatas, position, position - 1);
                notifyDataSetChanged();

            }
        });

        holder.setOnClickListener(R.id.btn_move_down, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.swap(mDatas, position, position + 1);
                notifyDataSetChanged();
            }
        });
    }

    public List<String> getSortData(){
        return mDatas;
    }
}
