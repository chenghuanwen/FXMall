package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.view.myView.FullImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android004 on 2017/3/29.
 */

public class ViewFlipperAdapter extends BaseAdapter {
    private List<String> images;
    private Context context;

    public ViewFlipperAdapter(List<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public int getCount() {
        return images==null?0:images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FullImageView imageView = new FullImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Glide.with(context).load(images.get(position)).error(R.mipmap.android_quanzi).into(imageView);
        return imageView;
    }
}
