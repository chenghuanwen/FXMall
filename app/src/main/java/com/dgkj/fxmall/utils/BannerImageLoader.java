package com.dgkj.fxmall.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Android004 on 2017/3/22.
 */

public class BannerImageLoader extends ImageLoader {
    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    public void displayImage(final Context context, Object path, final ImageView imageView) {
        final String url = (String) path;
        handler.post(new Runnable() {
            @Override
            public void run() {
                Glide.with(context).load(url).into(imageView);
            }
        });
    }
}
