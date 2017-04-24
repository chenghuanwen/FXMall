package com.dgkj.fxmall.utils;

import com.dgkj.fxmall.listener.GetWXPayMsgListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Android004 on 2017/3/17.
 */

public class WXPayUtil {

    public static void httpGet(final String url, final GetWXPayMsgListener listener) {

        if (url == null || url.length() == 0) {
            LogUtil.e("TAG", "httpGet, url is null");
            listener.wxPayMsg(null);
        }

        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        httpClient.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.wxPayMsg(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() != 200){
                    listener.wxPayMsg(null);
                }else {
                    listener.wxPayMsg(response.body().bytes());
                }
            }
        });

    }
}
