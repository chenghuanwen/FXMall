package com.dgkj.fxmall.wxapi;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.LogUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
		LogUtil.i("TAG","接收到注册app广播");

		msgApi.registerApp(FXConst.APP_ID);
	}
}
