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

public class SubmitLogisticsMsgActivity extends BaseActivity {
    @BindView(R.id.tv_agress_refund_time)
    TextView tvAgressRefundTime;
    @BindView(R.id.tv_take_man)
    TextView tvTakeMan;
    @BindView(R.id.tv_take_phone)
    TextView tvTakePhone;
    @BindView(R.id.tv_take_postcode)
    TextView tvTakePostcode;
    @BindView(R.id.tv_take_address)
    TextView tvTakeAddress;
    @BindView(R.id.tv_take_express)
    TextView tvTakeExpress;
    @BindView(R.id.tv_express_number)
    EditText tvExpressNumber;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private View headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_logistics_msg);
        ButterKnife.bind(this);
        initHeaderView();
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "提交物流信息");
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
