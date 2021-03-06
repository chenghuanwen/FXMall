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

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.listener.InputCompletetListener;
import com.dgkj.fxmall.listener.OnSelectAccountFinishedListener;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
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

public class WithdrawalActivity extends BaseActivity {

    @BindView(R.id.et_bank_account)
    EditText etBankAccount;
    @BindView(R.id.iv_type)
    ImageView ivType;
    @BindView(R.id.tv_select_account)
    TextView tvSelectAccount;
    @BindView(R.id.et_withdrawal_account)
    EditText etWithdrawalAccount;
    @BindView(R.id.et_recharge_sum)
    EditText etRechargeSum;
    @BindView(R.id.tv_availabe_balance)
    TextView tvAvailabeBalance;
    @BindView(R.id.tv_withdrawal_all)
    TextView tvWithdrawalAll;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.activity_withdrawal)
    LinearLayout activityWithdrawal;
    private View headerview;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private SharedPreferencesUnit sp = SharedPreferencesUnit.getInstance(this);
    private double restCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);
        ButterKnife.bind(this);
        initHeaderView();
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "提现");

        double rest = getIntent().getDoubleExtra("rest", -1);
        if (rest <= 200.0) {
            restCount = 0;
        } else {
            restCount = rest - 200.0;
        }
        tvAvailabeBalance.setText("可提现金额额:¥" + restCount);
    }


    @OnClick(R.id.tv_withdrawal_all)
    public void withdrawalALL() {
        String balance = tvAvailabeBalance.getText().toString();
        etRechargeSum.setText(balance.substring(balance.indexOf("¥") + 1, balance.length()));
        etRechargeSum.setSelection(etRechargeSum.length());
    }



    @OnClick(R.id.btn_confirm)
    public void withdrawabl() {
        String sum = etRechargeSum.getText().toString();
        String account = etBankAccount.getText().toString();
        String user = etWithdrawalAccount.getText().toString();
        if (TextUtils.isEmpty(sum) || TextUtils.isEmpty(account) || TextUtils.isEmpty(user)) {
            toast("请输入完整信息！");
            return;
        } else {
            Double money = Double.parseDouble(sum);
            showDeleteDialog(money, account, user);
        }
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }

    private void showDeleteDialog(final Double money, final String account, final String user) {
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
                            tixian(password, account, user, money + "".trim());
                            pw.dismiss();
                        } else if (string.contains("1003")) {
                            toastInUI(WithdrawalActivity.this, "密码错误！");
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
     * @param account  银行账户
     * @param user     开户姓名
     */
    private void tixian(String password, String account, String user, String money) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadProgressDialogUtil.buildProgressDialog();
            }
        });

        FormBody body = new FormBody.Builder()
                .add("user.token", sp.get("token"))
                .add("user.payPassword", password)
                .add("money", money)
                .add("cardNo", account)
                .add("name", user)
                .build();
        Request request = new Request.Builder()
                .url(FXConst.TIXIAN_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(WithdrawalActivity.this, "网络异常！");
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
                    toastInUI(WithdrawalActivity.this, "提现成功！");
                } else if (string.contains("109")) {
                    toastInUI(WithdrawalActivity.this, "余额不足！！");
                }
            }
        });
    }


}
