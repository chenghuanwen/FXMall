package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.view.myView.PasswordInputView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePhoneByPasswordActivity extends BaseActivity {
    private View headerview;

    @BindView(R.id.piv_pay_pass)
    PasswordInputView pivPayPass;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone_by_password);
        ButterKnife.bind(this);
        initHeaderView();
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "更换手机");
    }

    @OnClick(R.id.btn_confirm)
    public void confirm() {
       // jumpTo(BindNewphoneActivity.class, true);
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
