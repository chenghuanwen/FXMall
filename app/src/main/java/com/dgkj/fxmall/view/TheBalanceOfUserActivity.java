package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.Position;
import com.dgkj.fxmall.view.myView.WithdrawalAccountSelectDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TheBalanceOfUserActivity extends BaseActivity {
    @BindView(R.id.tv_balance)
    TextView tvBalance;

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    @BindView(R.id.tv_withdrawal)
    TextView tvWithdrawal;
    private View headerview;
    private String balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_balance_of_user);
        ButterKnife.bind(this);
        initHeaderView();

        balance = getIntent().getStringExtra("balance");
        tvBalance.setText(balance);
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "余额");
        setHeaderImage(headerview, -1, "交易记录", Position.RIGHT, new View.OnClickListener() {
            @Override
            public void onClick(View v) {//查看交易记录
                startActivity(new Intent(TheBalanceOfUserActivity.this, TransactionRecordActivity.class));
            }
        });
    }

    @OnClick(R.id.tv_recharge)
    public void recharge(){

        jumpTo(RechargeActivity.class,false);
    }

    @OnClick(R.id.tv_withdrawal)
    public void withdrawal(){

        jumpTo(WithdrawalActivity.class,false);
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
