package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CancelYeWuYuanFinishActivity extends BaseActivity {
    @BindView(R.id.tv_account_type)
    TextView tvAccountType;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    private View headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_ye_wu_yuan_finish);
        ButterKnife.bind(this);
        initHeaderView();
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "申请取消业务员");
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
