package com.dgkj.fxmall;

import android.app.Application;
import android.content.Context;

import com.alipay.sdk.app.EnvUtils;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.LoggerUtil;
import com.dgkj.fxmall.utils.SDUtils;
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
    public static List<String> selectedPictures;
    public static String currentProvince = "",currentCity="";
    public static int vipRate,shoppingCount,msgCount,systemMsgCount,orderMsgCount,warmMsgCount,accountMsgCount;
    public static double balance;
    public static String root;//本APP文件夹路径

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        ShareSDK.initSDK(this);

        // 将该app注册到微信,用于微信支付
     //  api = WXAPIFactory.createWXAPI(context, null);
     //   api.registerApp(FXConst.APP_ID);

        createAiGouFolder();

      //  EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);

        // 初始化Looger工具
      //  LoggerUtil.init(true);
    }

    public static Context getContext() {
        return context;
    }

    public static IWXAPI getApi() {
        return api;
    }

    /**
     * 创建哎购文件夹，将本APP所产生的文件全部放在该文件夹下
     */
    private void createAiGouFolder() {
        if(SDUtils.isSDCardEnable()){
            root = SDUtils.getSDCardPath() + "AiGou";
            SDUtils.isFolderExists(root);
        }
    }
}
