package com.dgkj.fxmall.view.myView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.SuperOrderBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.listener.InputCompletetListener;
import com.dgkj.fxmall.listener.OnPayFinishedListener;
import com.dgkj.fxmall.utils.LoadProgressDialogUtil;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.PayResult;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.view.OrderDetialActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.dgkj.fxmall.constans.FXConst.SDK_PAY_FLAG;

/**
 * Created by Android004 on 2017/4/5.
 */
@SuppressLint("ValidFragment")
public class PayDialog extends DialogFragment {
    private Context context;
    private Activity activity;
    private OkHttpClient client;
    private SharedPreferencesUnit sp;
    private int payMode = 3;
    private boolean isSettPayword;
    private int[] orderIds;
    private LoadProgressDialogUtil loadProgressDialogUtil;
    private OnPayFinishedListener listener;
    private double paySum=0.0;
    private Handler uiHandler = new Handler(Looper.getMainLooper());
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    LogUtil.i("TAG","支付结果====="+payResult);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                default:
                    break;
            }
        };
    };


    public PayDialog(Context context,int[] orderIds,Activity activity,double paySum) {
        this.context = context;
        this.orderIds = orderIds;
        this.activity = activity;
        this.paySum = paySum;
        client = new OkHttpClient.Builder().build();
        sp = SharedPreferencesUnit.getInstance(context);
        loadProgressDialogUtil = new LoadProgressDialogUtil(context);
        LogUtil.i("TAG","下单ID==="+orderIds[0]);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        whetherHasSetPayWord();

        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity());
        final Dialog dialog = new Dialog(getActivity(), R.style.ColorAndSizeSelectDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 必须在设置布局之前调用
        dialog.setContentView(R.layout.layout_pay_dialog);
        dialog.setCanceledOnTouchOutside(true);

        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        TextView tvCancle = (TextView) dialog.findViewById(R.id.tv_cancle_pay);
        TextView tvNeedPay = (TextView) dialog.findViewById(R.id.tv_need_pay);
        RadioGroup rgPay = (RadioGroup) dialog.findViewById(R.id.rg_pay);
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // context.startActivity(new Intent(context, OrderDetialActivity.class));
                dialog.dismiss();
            }
        });
        tvNeedPay.setText("¥"+paySum);
        rgPay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.tv_zhifubao_pay:
                        payMode = 1;
                        dialog.dismiss();
                        toPay(null);
                        break;
                    case R.id.tv_weixi_pay:
                        payMode = 2;
                        dialog.dismiss();
                        toPay(null);
                        break;
                    case R.id.tv_balance_pay:
                        payMode = 3;
                        dialog.dismiss();
                        if(isSettPayword){
                            showPayDialog();
                        }else {
                            Toast.makeText(context,"您还未设置支付密码，请到“设置”中进行设置",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });


        return dialog;
    }

    /**
     * 支付订单
     */
    private void toPay(String password) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                loadProgressDialogUtil.buildProgressDialog();
            }
        });
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("user.token",sp.get("token"))
                .add("payMode",payMode+"".trim());
        for (int i = 0; i < orderIds.length; i++) {
            builder.add("ids",orderIds[i]+"".trim());
        }
        if(password != null){
            builder.add("user.payPassword",password);
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .post(formBody)
                .url(FXConst.PAY_ORDER_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadProgressDialogUtil.cancelProgressDialog();
                    }
                });

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
                            String orderInfo = object.getString("data");
                            aliPay(orderInfo);
                        }else if(payMode==3){//TODO 余额支付返回信息
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context,"支付完成",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(string.contains("1008")){
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"余额不足",Toast.LENGTH_LONG).show();
                        }
                    });
                }

               if(listener != null){
                   listener.onPayFinishedListener();
               }
            }
        });

    }



    /**
     * 输入平台支付密码
     * @param
     */
    private void showPayDialog(){
        whetherHasSetPayWord();

        View contentview = getActivity().getLayoutInflater().inflate(R.layout.layout_input_password_dialog2, null);
        final AlertDialog pw = new AlertDialog.Builder(context).create();
        pw.setView(contentview);
        TextView tvCancel = (TextView) contentview.findViewById(R.id.tv_colse);
        final PasswordInputView2 piv = (PasswordInputView2) contentview.findViewById(R.id.piv_set);
        //自动弹出软键盘
        final EditText et = (EditText) piv.findViewById(R.id.et_setpassword);
        et.setFocusable(true);
        //et.requestFocus();
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);

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
                pw.dismiss();
                imm.hideSoftInputFromWindow(et.getWindowToken(),0);

            }

            @Override
            public void deleteContent(boolean isDelete) {

            }
        });

        //设置触摸对话框以外区域，对话框消失
        pw.setCanceledOnTouchOutside(false);
        pw.show();
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
                } else if (string.contains("1003")) {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"密码错误",Toast.LENGTH_SHORT).show();
                        }
                    });

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
                PayTask alipay = new PayTask(activity);//此处获取activity对象可能失败
                int indexOf = orderInfo.indexOf("&", 0);
                String substring = orderInfo.substring(indexOf + 1, orderInfo.length());
                LogUtil.i("TAG","支付参数==========="+substring);

                Map<String, String> result = alipay.payV2(substring, true);

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


    /**
     * 检测是否已设置过支付密码
     */
    private void whetherHasSetPayWord() {
        FormBody body = new FormBody.Builder()
                .add("token", sp.get("token"))
                .build();
        Request request = new Request.Builder()
                .url(FXConst.WHETHER_IS_FIRST_SET_PAYWORD)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.i("TAG","是否设置支付密码==="+result);
                if (result.contains("1000")) {
                    isSettPayword = true;
                } else if (result.contains("109")) {
                    isSettPayword = false;
                }
            }
        });
    }

    public void setPayFinishListener(OnPayFinishedListener listener){
        this.listener = listener;
    }
}
