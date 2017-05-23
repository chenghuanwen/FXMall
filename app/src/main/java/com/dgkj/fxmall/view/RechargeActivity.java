package com.dgkj.fxmall.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.listener.InputCompletetListener;
import com.dgkj.fxmall.listener.OnSelectAccountFinishedListener;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.view.myView.PasswordInputView;
import com.dgkj.fxmall.view.myView.PasswordInputView2;
import com.dgkj.fxmall.view.myView.WithdrawalAccountSelectDialog;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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


        if("mine".equals(from)){
            FormBody body = new FormBody.Builder()
                    .add("token", sp.get("token"))
                    .add("balance", money)
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
                    if (response.body().string().contains("1000")) {
                        toastInUI(RechargeActivity.this, "充值成功");
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
                .add("status",payMode+"".trim())
                .add("payPassword",password);
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
                    toastInUI(RechargeActivity.this,"押金充值成功！");
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

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
