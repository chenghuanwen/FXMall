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
    @BindView(R.id.et_recharge_sum)
    EditText etRechargeSum;
    @BindView(R.id.tv_availabe_balance)
    TextView tvAvailabeBalance;
    @BindView(R.id.tv_withdrawal_all)
    TextView tvWithdrawalAll;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_select_account)
    TextView tvSelectAccount;
    @BindView(R.id.et_withdrawal_account)
    EditText etWithdrawalAccount;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.iv_type)
    ImageView ivType;
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

        int rest = getIntent().getIntExtra("rest", -1);
        if (rest <= 200) {
            restCount = 0;
        } else {
            restCount = rest - 200;
        }
        tvAvailabeBalance.setText("可提现金额额:¥" + restCount);
    }


    @OnClick(R.id.tv_withdrawal_all)
    public void withdrawalALL() {
        String balance = tvAvailabeBalance.getText().toString();
        etRechargeSum.setText(balance.substring(balance.indexOf("额") + 2, balance.length() - 1));
    }

    @OnClick(R.id.tv_select_account)
    public void selectAccount() {
        WithdrawalAccountSelectDialog dialog = new WithdrawalAccountSelectDialog("提现方式",R.layout.layout_withdrawal_selector_dialog);
        dialog.show(getSupportFragmentManager(), "");
        dialog.setSelectListener(new OnSelectAccountFinishedListener() {
            @Override
            public void OnSelectAccountFinished(String result) {
                if(result.contains("微信")){
                    ivType.setImageResource(R.mipmap.weixin);
                }else {
                    ivType.setImageResource(R.mipmap.zhifubao);
                }
                tvSelectAccount.setText(result);
            }
        });
    }


    @OnClick(R.id.btn_confirm)
    public void withdrawabl() {
        String sum = etRechargeSum.getText().toString();
        if (TextUtils.isEmpty(sum)) {
            toast("请输入金额");
            return;
        } else {
            Double money = Double.parseDouble(sum);
            showDeleteDialog(money);
        }
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }

    private void showDeleteDialog(Double money) {
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
                String password = piv.getEditContent();
                if(TextUtils.isEmpty(password)){return;}
                //TODO 检测支付密码的正确性,进行提现
                FormBody body = new FormBody.Builder()
                        .add("token",sp.get("token"))
                        .add("payPassword",password)
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
                        if(string.contains("1000")){
                           //TODO 提现
                        }else if(string.contains("1003")){
                           toastInUI(WithdrawalActivity.this,"密码错误！");
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
}
