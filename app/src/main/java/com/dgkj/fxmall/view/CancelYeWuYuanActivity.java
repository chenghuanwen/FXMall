package com.dgkj.fxmall.view;

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
import com.dgkj.fxmall.listener.OnSelectAccountFinishedListener;
import com.dgkj.fxmall.view.myView.WithdrawalAccountSelectDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CancelYeWuYuanActivity extends BaseActivity {

    @BindView(R.id.et_withdrawal_account)
    EditText etWithdrawalAccount;
    @BindView(R.id.tv_withdrawal_count)
    TextView tvWithdrawalCount;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.activity_cancel_ye_wu_yuan)
    LinearLayout activityCancelYeWuYuan;
    @BindView(R.id.iv_type)
    ImageView ivType;
    @BindView(R.id.tv_select_account)
    TextView tvSelectAccount;
    @BindView(R.id.tv_account_type)
    LinearLayout tvAccountType;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private View headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_ye_wu_yuan);
        ButterKnife.bind(this);
        initHeaderView();

    }


    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "申请取消业务员");
    }

    @OnClick(R.id.tv_account_type)
    public void selectAccountType() {
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
    public void confirm() {
        String type = tvSelectAccount.getText().toString();
        String account = etWithdrawalAccount.getText().toString();
        if (TextUtils.isEmpty(type)) {
            toast("您还未选择账户类型");
            return;
        }
        if (TextUtils.isEmpty(account)) {
            toast("请输入提现账户");
            return;
        }

        jumpTo(CancelYeWuYuanFinishActivity.class, true);
    }

    @Override
    public View getContentView() {
        return null;
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
