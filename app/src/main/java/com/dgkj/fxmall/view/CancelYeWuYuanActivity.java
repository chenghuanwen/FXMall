package com.dgkj.fxmall.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.listener.InputCompletetListener;
import com.dgkj.fxmall.listener.OnSelectAccountFinishedListener;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.view.myView.PasswordInputView;
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

public class CancelYeWuYuanActivity extends BaseActivity {


    @BindView(R.id.et_withdrawal_account)
    EditText etWithdrawalAccount;
    @BindView(R.id.et_withdrawal_account_name)
    EditText etWithdrawalAccountName;
    @BindView(R.id.tv_withdrawal_count)
    TextView tvWithdrawalCount;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.activity_cancel_ye_wu_yuan)
    LinearLayout activityCancelYeWuYuan;
    private View headerview;
    private OkHttpClient client = new OkHttpClient.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_ye_wu_yuan);
        ButterKnife.bind(this);
        initHeaderView();

    }


    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "申请取消业务员");
    }




    @OnClick(R.id.btn_confirm)
    public void confirm() {

     /*   String account = etWithdrawalAccount.getText().toString();
        String name = etWithdrawalAccountName.getText().toString();

        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(name)) {
            toast("请输入完整信息！");
            return;
        }*/

       showDeleteDialog(200.0);


       // jumpTo(CancelYeWuYuanFinishActivity.class, true);
    }

    @Override
    public View getContentView() {
        return null;
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }




    private void showDeleteDialog(final Double money) {
        View contentview = getLayoutInflater().inflate(R.layout.layout_withdrawabl_dialog, null);
        final AlertDialog pw = new AlertDialog.Builder(this).create();
        pw.setView(contentview);
        TextView tvCancel = (TextView) contentview.findViewById(R.id.tv_colse);
        final TextView tvMoney = (TextView) contentview.findViewById(R.id.tv_money);
        tvMoney.setText("¥" + money);
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
                final String password = piv.getEditContent();
                if (TextUtils.isEmpty(password)) {
                    return;
                }
                //TODO 检测支付密码的正确性,进行提现
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
                            //TODO 提现
                            tixian(password);
                            pw.dismiss();
                        } else if (string.contains("1003")) {
                            toastInUI(CancelYeWuYuanActivity.this, "密码错误！");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    piv.clearEditContent();
                                }
                            });

                        }
                    }
                });

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
     * 提现
     * @param password 支付密码
     */
    private void tixian(String password) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadProgressDialogUtil.buildProgressDialog();
            }
        });

        FormBody body = new FormBody.Builder()
                .add("token", sp.get("token"))
                .add("payPassword", password)
                .build();
        Request request = new Request.Builder()
                .url(FXConst.TIQU_YAJIN_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(CancelYeWuYuanActivity.this, "网络异常！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                LogUtil.i("TAG","提现结果===="+string);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadProgressDialogUtil.cancelProgressDialog();
                    }
                });
                if (string.contains("1000")) {
                    toastInUI(CancelYeWuYuanActivity.this, "提现成功！");
                    jumpTo(CancelYeWuYuanFinishActivity.class, true);
                } else if (string.contains("109")) {
                    toastInUI(CancelYeWuYuanActivity.this, "余额不足！！");
                }
            }
        });
    }



}
