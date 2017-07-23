package com.dgkj.fxmall.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;

import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.LogUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
		LogUtil.i("TAG","进入微信支付界面=========");
		// 将该app注册到微信,用于微信支付
	/*	api = WXAPIFactory.createWXAPI(this, FXConst.APP_ID);
		api.registerApp(FXConst.APP_ID);
    	api = MyApplication.getApi();
        api.handleIntent(getIntent(), this);*/
		finish();
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
      //  api.handleIntent(intent, this);
	}


	@Override
	public void onResp(BaseResp resp) {
		LogUtil.d("TAG", "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode==0){//TODO 成功，向后台查询实际的支付结果，再向用户展示
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.app_tip);
				builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
				builder.show();
			}else if(resp.errCode == -1){//支付异常
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.app_tip);
				builder.setMessage("支付失败");
				builder.show();
			}

		}
	}

	@Override
	public void onReq(BaseReq baseReq) {
		LogUtil.i("TAG","微信支付请求=="+baseReq.toString());
	}
}