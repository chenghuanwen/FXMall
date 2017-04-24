package com.dgkj.fxmall;

import android.app.Application;
import android.content.Context;

import com.dgkj.fxmall.constans.FXConst;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Android004 on 2017/3/8.
 */

public class MyApplication extends Application {
    private static Context context;
    private static IWXAPI api;
    public static  boolean isLogin;
    public static List<String> selectedPictures;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        ShareSDK.initSDK(this);

        // 将该app注册到微信
        api = WXAPIFactory.createWXAPI(context, null);
        api.registerApp(FXConst.APP_ID);
    }

    public static Context getContext() {
        return context;
    }

    public static IWXAPI getApi() {
        return api;
    }
}
