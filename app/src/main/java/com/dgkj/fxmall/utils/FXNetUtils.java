package com.dgkj.fxmall.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



/**
 * 判断网络状态
 * Created by 成焕文 on 2017/3/7.
 */
public class FXNetUtils {

        /** 检查是否有网络 */
        public static boolean isNetworkAvailable(Context context) {
            NetworkInfo info = getNetworkInfo(context);
            if (info != null) {
                return info.isAvailable();
            }
            return false;
        }

        /** 检查是否是WIFI */
        public static boolean isWifi(Context context) {
            NetworkInfo info = getNetworkInfo(context);
            if (info != null) {
                if (info.getType() == ConnectivityManager.TYPE_WIFI)
                    return true;
            }
            return false;
        }

        /** 检查是否是移动网络 */
        public static boolean isMobile(Context context) {
            NetworkInfo info = getNetworkInfo(context);
            if (info != null) {
                if (info.getType() == ConnectivityManager.TYPE_MOBILE)
                    return true;
            }
            return false;
        }

        private static NetworkInfo getNetworkInfo(Context context) {

            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo();
        }


    /**
     * 监听网络断开与重连状态改变
     */
   public static class NetWorkReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(final Context context, Intent intent) {
            if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
                NetworkInfo netInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if(netInfo != null){
                    if(NetworkInfo.State.CONNECTED.equals(netInfo.getState()) && netInfo.isAvailable()){
                        int type = netInfo.getType();
                        if(type==ConnectivityManager.TYPE_MOBILE || type==ConnectivityManager.TYPE_WIFI){//网络已连上
                            //重新发起连接
                            LogUtil.i("TAG","网络已连接..............");


                        }
                    }else {//网络断开
                        LogUtil.i("TAG","网络断开..............");
                        context.sendBroadcast(new Intent("doupai.action.netbreak"));
                        //关闭连接

                    }
                }


            }

        }
    }




    }


