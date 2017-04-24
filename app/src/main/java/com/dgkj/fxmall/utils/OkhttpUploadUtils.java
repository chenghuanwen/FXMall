package com.dgkj.fxmall.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.dgkj.fxmall.constans.FXConst;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Android004 on 2017/4/18.
 */

public class OkhttpUploadUtils {
    private static OkhttpUploadUtils uploadUtils;
    private static OkHttpClient client;
    private static SharedPreferencesUnit sp;
    private static Context context;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private Handler handler = new Handler(Looper.getMainLooper());

    public static OkhttpUploadUtils getInstance(Context cont) {
        client = new OkHttpClient.Builder().build();
        sp = SharedPreferencesUnit.getInstance(context);
        context = cont;
        return new OkhttpUploadUtils();
    }

    /**
     * 同时上传多个文件，并附带其他键值对参数
     *
     * @param reqUrl  上传接口
     * @param params  其他键值对参数
     * @param pic_key 文件对应参数名
     * @param files   要上传的文件集合
     */
    public void sendMultipart(final String reqUrl, final Map<String, String> params, final String pic_key, final List<File> files,final String image_key ,final List<File> fileList) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        if(params != null){
            for (String key : params.keySet()) {
                builder.addFormDataPart(key,params.get(key));
            }
        }

        if(files != null){
            for (File file : files) {
                builder.addFormDataPart(pic_key,file.getName(), RequestBody.create(MEDIA_TYPE_PNG,file));
            }
        }

        if(fileList != null){
            for (File file : fileList) {
                builder.addFormDataPart(image_key,file.getName(), RequestBody.create(MEDIA_TYPE_PNG,file));
            }
        }

        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(reqUrl)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"网络异常！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"上传成功！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"网络繁忙！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}