package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageCenterActivity extends BaseActivity {

    @BindView(R.id.tv_msg_count1)
    TextView tvMsgCount1;
    @BindView(R.id.tv_msg_content1)
    TextView tvMsgContent1;
    @BindView(R.id.tv_msg_time1)
    TextView tvMsgTime1;
    @BindView(R.id.tv_msg_count2)
    TextView tvMsgCount2;
    @BindView(R.id.tv_msg_content2)
    TextView tvMsgContent2;
    @BindView(R.id.tv_msg_time2)
    TextView tvMsgTime2;
    @BindView(R.id.tv_msg_count3)
    TextView tvMsgCount3;
    @BindView(R.id.tv_msg_content3)
    TextView tvMsgContent3;
    @BindView(R.id.tv_msg_time3)
    TextView tvMsgTime3;
    @BindView(R.id.fl_civ)
    FrameLayout flCiv;
    @BindView(R.id.rl_logistics)
    RelativeLayout rlLogistics;
    @BindView(R.id.fl_civ2)
    FrameLayout flCiv2;
    @BindView(R.id.rl_notify)
    RelativeLayout rlNotify;
    @BindView(R.id.rl_publish_demand)
    RelativeLayout rlPublishDemand;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.activity_message_center)
    LinearLayout activityMessageCenter;

    private View headerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ButterKnife.bind(this);
        initHeaderview();
    }

    @Override
    public View getContentView() {
        return activityMessageCenter;
    }

    private void initHeaderview() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "消息");
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.rl_logistics)
    public void logistics() {
        startActivity(new Intent(this, LogisticsMsgActivity.class));
    }

    @OnClick(R.id.rl_notify)
    public void notifyMsg() {
        startActivity(new Intent(this, NotifyMsgActivity.class));
    }

    @OnClick(R.id.rl_publish_demand)
    public void publishDemand() {
        startActivity(new Intent(this, DemandMsgActivity.class));
    }
}
