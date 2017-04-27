package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePhoneActivity extends BaseActivity {

    @BindView(R.id.tv_bind_phone)
    TextView tvBindPhone;
    @BindView(R.id.et_check_code)
    EditText etCheckCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_change_by_paypass)
    TextView tvChangeByPaypass;
    @BindView(R.id.activity_change_phone)
    LinearLayout activityChangePhone;
    private View headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        ButterKnife.bind(this);
        initHeaderView();
        etCheckCode.setText(getIntent().getStringExtra("phone"));
    }

    @Override
    public View getContentView() {
        return activityChangePhone;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "更换手机");
    }


    @OnClick(R.id.tv_change_by_paypass)
    public void byPayPass() {
        jumpTo(ChangePhoneByPasswordActivity.class, false);
    }

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        jumpTo(BindNewphoneActivity.class, true);
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
