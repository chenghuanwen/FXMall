package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.LogiticsMsgAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.LogisticsMsgBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetLogisticsMsgFinishedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogisticsMsgActivity extends BaseActivity {
    @BindView(R.id.activity_logistics_msg)
    LinearLayout activityLogisticsMsg;
    private View headerview;
    private RecyclerView rvMsg;
    private List<LogisticsMsgBean> msgs;
    private LogiticsMsgAdapter adapter;
    private FXMallControl control = new FXMallControl();
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_msg);
        ButterKnife.bind(this);
        initHeaderview();
        initData();
        refresh();
    }

    @Override
    public View getContentView() {
        return activityLogisticsMsg;
    }

    private void initData() {
        msgs = new ArrayList<>();
        adapter = new LogiticsMsgAdapter(this, R.layout.item_wuliu_msg, msgs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMsg.setLayoutManager(layoutManager);
        rvMsg.setAdapter(adapter);
    }

    private void refresh() {
        control.getLogisticsMsgData(new OnGetLogisticsMsgFinishedListener() {
            @Override
            public void OnGetLogisticsMsgFinished(List<LogisticsMsgBean> list) {
                adapter.addAll(list, true);
            }
        });
    }

    private void initHeaderview() {
        headerview = findViewById(R.id.headerview);
        rvMsg = (RecyclerView) findViewById(R.id.rv_wuliu_msg);
        setHeaderTitle(headerview, "订单物流消息");
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
