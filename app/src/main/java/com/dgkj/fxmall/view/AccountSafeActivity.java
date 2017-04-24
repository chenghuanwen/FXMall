package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountSafeActivity extends BaseActivity {
    @BindView(R.id.tv_change_login_pass)
    TextView tvChangeLoginPass;
    @BindView(R.id.tv_rest_pay_pass)
    TextView tvRestPayPass;
    private View headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safe);
        ButterKnife.bind(this);
        initHeaderView();
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "账户安全");
    }

    @OnClick(R.id.tv_change_login_pass)
    public void resetLoginPass(){
        jumpTo(SetPasswordActivity.class,false);
    }

    @OnClick(R.id.tv_rest_pay_pass)
    public void restPayPass(){
        jumpTo(SetPayPasswordActivity.class,false);
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
