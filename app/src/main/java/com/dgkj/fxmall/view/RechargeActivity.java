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
import com.dgkj.fxmall.listener.OnSelectAccountFinishedListener;
import com.dgkj.fxmall.view.myView.WithdrawalAccountSelectDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.et_charge_sum)
    EditText etChargeSum;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.ll_select_recharge_type)
    LinearLayout tvSelectRechargeType;
    @BindView(R.id.tv_select_account)
    TextView tvSelectAccount;
    @BindView(R.id.activity_recharge)
    LinearLayout activityRecharge;
    private View headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        initHeaderView();
    }

    @Override
    public View getContentView() {
        return activityRecharge;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "账户充值");
    }


    @OnClick(R.id.ll_select_recharge_type)
    public void selectType() {
        WithdrawalAccountSelectDialog dialog = new WithdrawalAccountSelectDialog("付款方式");
        dialog.show(getSupportFragmentManager(), "");
        dialog.setSelectListener(new OnSelectAccountFinishedListener() {
            @Override
            public void OnSelectAccountFinished(String result) {
                tvSelectAccount.setText(result);
            }
        });
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
