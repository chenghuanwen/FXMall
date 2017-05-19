package com.dgkj.fxmall.view.myView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.listener.InputCompletetListener;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.view.OrderDetialActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Android004 on 2017/4/5.
 */
@SuppressLint("ValidFragment")
public class PayDialogforByPlace extends DialogFragment {
    private Context context;
    private OkHttpClient client;
    private SharedPreferencesUnit sp;
    private int payMode = 3;
    private int count = 1;

    public PayDialogforByPlace(Context context, int count) {
        this.context = context;
        client = new OkHttpClient.Builder().build();
        sp = SharedPreferencesUnit.getInstance(context);
        this.count = count;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
                context.startActivity(new Intent(context, OrderDetialActivity.class));
                dialog.dismiss();
            }
        });
        tvNeedPay.setText("");
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
                       showPayDialog();
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
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("user.token",sp.get("token"))
                .add("upperLimit",count+"".trim())
                .add("payMode",payMode+"".trim());
        if(password != null){
            builder.add("user.payPassword",password);
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .post(formBody)
                .url(FXConst.BUY_PRODUCT_PLACE_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.i("TAG","支付结果==="+response.body().string());
            }
        });

    }


    /**
     * 输入平台支付密码
     * @param
     */
    private void showPayDialog(){
        View contentview = getActivity().getLayoutInflater().inflate(R.layout.layout_input_password_dialog, null);
        final AlertDialog pw = new AlertDialog.Builder(context).create();
        pw.setView(contentview);
        TextView tvCancel = (TextView) contentview.findViewById(R.id.tv_colse);
        final PasswordInputView piv = (PasswordInputView) contentview.findViewById(R.id.piv_set);
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
                toPay(password);
            }

            @Override
            public void deleteContent(boolean isDelete) {

            }
        });

        //设置触摸对话框以外区域，对话框消失
        pw.setCanceledOnTouchOutside(false);
        pw.show();
    }
}
