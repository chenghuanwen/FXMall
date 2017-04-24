package com.dgkj.fxmall.view;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.listener.GetWXPayMsgListener;
import com.dgkj.fxmall.utils.AuthResult;
import com.dgkj.fxmall.utils.PayResult;
import com.dgkj.fxmall.utils.WXPayUtil;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.Map;

import static com.dgkj.fxmall.constans.FXConst.SDK_AUTH_FLAG;
import static com.dgkj.fxmall.constans.FXConst.SDK_PAY_FLAG;


public class PayActivity extends AppCompatActivity {



    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PayActivity.this,
                                "授权成功" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
    private IWXAPI api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        //微信支付

        api = MyApplication.getApi();

        Button appayBtn = (Button) findViewById(R.id.appay_btn);
        appayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
                Button payBtn = (Button) findViewById(R.id.appay_btn);
                payBtn.setEnabled(false);
                Toast.makeText(PayActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();

                    WXPayUtil.httpGet(url, new GetWXPayMsgListener() {
                        @Override
                        public void wxPayMsg(byte[] buf) {
                            try{
                            if (buf != null && buf.length > 0) {
                                String content = new String(buf);
                                Log.e("get server pay params:",content);
                                JSONObject json = new JSONObject(content);
                                if(null != json && !json.has("retcode") ){
                                    PayReq req = new PayReq();
                                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                                    req.appId			= json.getString("appid");
                                    req.partnerId		= json.getString("partnerid");
                                    req.prepayId		= json.getString("prepayid");
                                    req.nonceStr		= json.getString("noncestr");
                                    req.timeStamp		= json.getString("timestamp");
                                    req.packageValue	= json.getString("package");
                                    req.sign			= json.getString("sign");
                                    req.extData			= "app data"; // optional
                                    Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                    api.sendReq(req);
                                }else{
                                    Log.d("PAY_GET", "返回错误"+json.getString("retmsg"));
                                    Toast.makeText(PayActivity.this, "返回错误"+json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Log.d("PAY_GET", "服务器请求错误");
                                Toast.makeText(PayActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
                            }

                }catch(Exception e){
                    Log.e("PAY_GET", "异常："+e.getMessage());
                    Toast.makeText(PayActivity.this, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                        }
                    });

                payBtn.setEnabled(true);
            }
        });
        Button checkPayBtn = (Button) findViewById(R.id.check_pay_btn);
        checkPayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                Toast.makeText(PayActivity.this, String.valueOf(isPaySupported), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
