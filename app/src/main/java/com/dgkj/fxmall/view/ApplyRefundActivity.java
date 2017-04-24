package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplyRefundActivity extends BaseActivity {
    @BindView(R.id.tv_select_refund_reason)
    TextView tvSelectRefundReason;
    @BindView(R.id.tv_most_refund)
    TextView tvMostRefund;
    @BindView(R.id.et_refund_money)
    EditText etRefundMoney;
    @BindView(R.id.et_refund_describe)
    EditText etRefundDescribe;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private View headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_refund);
        ButterKnife.bind(this);
        initHeaderView();
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "申请退款");
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
