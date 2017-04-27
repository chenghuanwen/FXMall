package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemandMsgActivity extends BaseActivity {

    @BindView(R.id.activity_logistics_msg)
    LinearLayout activityLogisticsMsg;
    private View headerview;
    private RecyclerView rvMsg;
    @BindView(R.id.ib_back)
    ImageButton ibBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_msg);
        ButterKnife.bind(this);
        initHeaderview();
    }

    @Override
    public View getContentView() {
        return activityLogisticsMsg;
    }

    private void initHeaderview() {
        headerview = findViewById(R.id.headerview);
        rvMsg = (RecyclerView) findViewById(R.id.rv_wuliu_msg);
        setHeaderTitle(headerview, "发布需求消息");
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
