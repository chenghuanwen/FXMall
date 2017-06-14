package com.dgkj.fxmall.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.listener.InputCompletetListener;
import com.dgkj.fxmall.listener.OnSelectAccountFinishedListener;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.PayResult;
import com.dgkj.fxmall.view.myView.PasswordInputView;
import com.dgkj.fxmall.view.myView.PasswordInputView2;
import com.dgkj.fxmall.view.myView.WithdrawalAccountSelectDialog;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.dgkj.fxmall.constans.FXConst.SDK_PAY_FLAG;

public class RechargeActivity extends BaseActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.et_charge_sum)
    EditText etChargeSum;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.ll_select_recharge_type)
    LinearLayout tvSelectRechargeType;
    @BindView(R.id.tv_select_account)
    TextView tvSelectAccount;
    @BindView(R.id.activity_recharge)
    LinearLayout activityRecharge;
    @BindView(R.id.iv_type)
    ImageView ivType;
    private View headerview;
    private String from = "";
    private int payMode ;
    private AlertDialog pw;
    private OkHttpClient client = new OkHttpClient.Builder().build();



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
                        Toast.makeText(RechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                default:
                    break;
            }
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        initHeaderView();
    }

    @Override
    public View getContentView() {
        return activityRecharge;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "账户充值");
        from = getIntent().getStringExtra("from");
        if("ywy".equals(from)){
            etChargeSum.setText("¥"+200.00);
            etChargeSum.setCursorVisible(false);
        }
    }


    @OnClick(R.id.ll_select_recharge_type)
    public void selectType() {
        WithdrawalAccountSelectDialog dialog;
        if("mine".equals(from)){
           dialog = new WithdrawalAccountSelectDialog("付款方式",R.layout.layout_withdrawal_selector_dialog);
        }else {
          dialog = new WithdrawalAccountSelectDialog("付款方式",R.layout.layout_withdrawal_selector_dialog2);
        }
        dialog.show(getSupportFragmentManager(), "");
        dialog.setSelectListener(new OnSelectAccountFinishedListener() {
            @Override
            public void OnSelectAccountFinished(String result) {
                if(result.contains("微信")){
                    ivType.setImageResource(R.mipmap.weixin);
                    payMode = 2;
                }else if(result.contains("支付宝")){
                    ivType.setImageResource(R.mipmap.zhifubao);
                    payMode = 1;
                }else {
                    ivType.setImageResource(R.mipmap.yezf);
                    payMode = 3;
                    //余额充值押金
                    showPayDialog();


                }
                tvSelectAccount.setText(result);
            }
        });
    }

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        String money = etChargeSum.getText().toString();
        if (TextUtils.isEmpty(money)) {
            toast("请输入充值金额");
            return;
        }


        if("mine".equals(from)){//账户充值
            //TODO 缺少支付模式 status字段待确定
            FormBody body = new FormBody.Builder()
                    .add("token", sp.get("token"))
                    .add("balance", money)
                    .add("status",payMode+"".trim())
                    .build();
            Request request = new Request.Builder()
                    .url(FXConst.USER_RECHARGE_URL)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    toastInUI(RechargeActivity.this, "网络异常！");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String string = response.body().string();
                    if (string.contains("1000")) {
                        try {
                            JSONObject object = new JSONObject(string);
                            if(payMode==2){//微信预付订单信息
                                JSONObject dataset = object.getJSONObject("dataset");//TODO 该字段待定
                                String mch_id = dataset.getString("mch_id");//商户号
                                String nonce_str = dataset.getString("nonce_str");//随机字符串
                                String sign = dataset.getString("sign");//签名
                                String prepay_id = dataset.getString("prepay_id");//预付订单id
                                weixinPay(mch_id,prepay_id,nonce_str,sign);//调起微信支付
                            }else if(payMode==1){//TODO 支付宝支付返回信息
                                String orderInfo = object.getString("orderInfo");
                                aliPay(orderInfo);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                     //   toastInUI(RechargeActivity.this, "充值成功");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                etChargeSum.setText("");
                                finish();
                            }
                        });

                    }
                }
            });
        }else {
           //TODO 支付宝、微信充值押金
            toPay(null);
        }

    }



    /**
     * 输入平台支付密码
     * @param
     */
    private void showPayDialog(){
        View contentview = getLayoutInflater().inflate(R.layout.layout_input_password_dialog2, null);
        pw = new AlertDialog.Builder(this).create();
        pw.setView(contentview);
        TextView tvCancel = (TextView) contentview.findViewById(R.id.tv_colse);
        final PasswordInputView2 piv = (PasswordInputView2) contentview.findViewById(R.id.piv_set);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });

        piv.setInputCompletetListener(new InputCompletetListener() {
            @Override
            public void inputComplete() {
                String password = piv.getEditContent();
                //TODO 检测支付密码的正确性,进行支付
                checkPayword(password);
            }

            @Override
            public void deleteContent(boolean isDelete) {

            }
        });

        //设置触摸对话框以外区域，对话框消失
        pw.setCanceledOnTouchOutside(false);
        pw.show();
    }


    /**
     * 支付订单
     */
    private void toPay(String password) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadProgressDialogUtil.buildProgressDialog();
            }
        });

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("token",sp.get("token"))
                .add("status",payMode+"".trim());
        if(password != null){
            builder.add("payPassword",password);
        }

        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .post(formBody)
                .url(FXConst.RECHARGE_YEWUYUAN_CASHPLEDGE)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                LogUtil.i("TAG","支付结果==="+ string);
                if(string.contains("1000")){
                    try {
                        JSONObject object = new JSONObject(string);
                        if(payMode==2){//微信预付订单信息
                            JSONObject dataset = object.getJSONObject("dataset");//TODO 该字段待定
                            String mch_id = dataset.getString("mch_id");//商户号
                            String nonce_str = dataset.getString("nonce_str");//随机字符串
                            String sign = dataset.getString("sign");//签名
                            String prepay_id = dataset.getString("prepay_id");//预付订单id
                            weixinPay(mch_id,prepay_id,nonce_str,sign);//调起微信支付
                        }else if(payMode==1){//TODO 支付宝支付返回信息
                            String orderInfo = object.getString("orderInfo");
                            aliPay(orderInfo);
                        }else if(payMode==3){//TODO 余额支付返回信息
                            toastInUI(RechargeActivity.this,"押金充值成功！");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadProgressDialogUtil.cancelProgressDialog();
                        }
                    });
                }
            }
        });

    }

    public void checkPayword(final String password){
        FormBody body = new FormBody.Builder()
                .add("token", sp.get("token"))
                .add("payPassword", password)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.CHECK_PAY_PASSWORD)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if (string.contains("1000")) {
                    toPay(password);
                    pw.dismiss();
                } else if (string.contains("1003")) {
                    toastInUI(RechargeActivity.this, "密码错误！");
                }
            }
        });
    }


    /**
     * 调起微信支付
     */
    private void weixinPay(String storeId,String prePayId,String random,String sign){
        PayReq request = new PayReq();
        request.appId = FXConst.APP_ID;//微信开放平台审核通过的应用APPID
        request.partnerId = storeId;//微信支付分配的商户号
        request.prepayId= prePayId;//微信返回的支付交易会话ID
        request.packageValue = "Sign=WXPay";//暂填写固定值Sign=WXPay
        request.nonceStr= random;//随机字符串，不长于32位。推荐随机数生成算法
        request.timeStamp= System.currentTimeMillis()/1000+"".trim();//时间戳(秒)
        request.sign= sign;//签名
        MyApplication.getApi().sendReq(request);
    }


    /**
     * 调起支付宝支付
     * @param orderInfo
     */
    private void aliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                LogUtil.i("TAG", "支付宝支付结果==="+result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
