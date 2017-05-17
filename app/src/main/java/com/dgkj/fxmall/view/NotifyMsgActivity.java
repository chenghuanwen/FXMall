package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.NotifyMsgAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.NotifyMsgBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotifyMsgActivity extends BaseActivity {
    @BindView(R.id.activity_logistics_msg)
    LinearLayout activityLogisticsMsg;
    private View headerview;
    private RecyclerView rvMsg;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    private NotifyMsgAdapter adapter;
    private ArrayList<NotifyMsgBean> msgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_msg);
        ButterKnife.bind(this);

        initHeaderview();
        init();
        
    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMsg.setLayoutManager(layoutManager);
        msgs = (ArrayList<NotifyMsgBean>) getIntent().getSerializableExtra("msg");
        adapter = new NotifyMsgAdapter(this,msgs);
        rvMsg.setAdapter(adapter);
    }

    @Override
    public View getContentView() {
        return activityLogisticsMsg;
    }

    private void initHeaderview() {
        headerview = findViewById(R.id.headerview);
        rvMsg = (RecyclerView) findViewById(R.id.rv_wuliu_msg);
        setHeaderTitle(headerview, "通知消息");

    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
