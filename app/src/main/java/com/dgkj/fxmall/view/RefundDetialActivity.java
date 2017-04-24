package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RefundDetialActivity extends BaseActivity {

    /*  @BindView(R.id.btn_refused_apply)
      Button btnRefusedApply;
      @BindView(R.id.btn_agree_apply)
      Button btnAgreeApply;*/
    @BindView(R.id.tv_refund_type)
    TextView tvRefundType;
    @BindView(R.id.tv_refund_money)
    TextView tvRefundMoney;
    @BindView(R.id.tv_refund_reason)
    TextView tvRefundReason;
    @BindView(R.id.tv_refund_describe)
    TextView tvRefundDescribe;
    @BindView(R.id.tv_refund_number)
    TextView tvRefundNumber;
    @BindView(R.id.tv_refund_time)
    TextView tvRefundTime;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.iv_evidence1)
    ImageView ivEvidence1;
    @BindView(R.id.iv_evidence2)
    ImageView ivEvidence2;
    @BindView(R.id.iv_evidence3)
    ImageView ivEvidence3;
    private View headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO 根据退款不同状态显示不同的界面
        setContentView(R.layout.activity_refund_detial_for_store);
        //setContentView(R.layout.activity_refund_detial_for_change);
        //setContentView(R.layout.activity_refund_detial_for_logistics);//第一次进入点击物流信息按钮填写寄件信息，后续点击显示物流进度
        ButterKnife.bind(this);
        initHeaderView();
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "退款详情");
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
