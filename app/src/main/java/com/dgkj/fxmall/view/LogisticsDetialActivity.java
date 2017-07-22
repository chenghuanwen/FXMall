package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.LogisticsMsgDetialAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.LogisticsBean;
import com.dgkj.fxmall.bean.OrderBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetLogisticsDetialFinishedListener;
import com.dgkj.fxmall.view.myView.TimeLineView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogisticsDetialActivity extends BaseActivity {
    @BindView(R.id.iv_logistics)
    ImageView ivLogistics;
    @BindView(R.id.tv_logistics_man)
    TextView tvLogisticsMan;
    @BindView(R.id.tv_logistics_address)
    TextView tvLogisticsAddress;
    @BindView(R.id.tv_logistics_express)
    TextView tvLogisticsExpress;
    @BindView(R.id.tv_logistics_express_number)
    TextView tvLogisticsExpressNumber;
    @BindView(R.id.timeline)
    TimeLineView timeline;
    @BindView(R.id.rv_logistics)
    RecyclerView rvLogistics;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.activity_logistics_detial)
    LinearLayout activityLogisticsDetial;
    private View headerview;
    private List<LogisticsBean> msgList = new ArrayList<>();
    private LogisticsMsgDetialAdapter adapter;
    private FXMallControl control = new FXMallControl();
    private OrderBean orderBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_detial);
        ButterKnife.bind(this);
        initHeaderView();
        initData();
        refresh();
    }

    @Override
    public View getContentView() {
        return activityLogisticsDetial;
    }

    private void refresh() {
        control.getLogisticsDetial(orderBean.getId(),orderBean.getOrderNum(),new OnGetLogisticsDetialFinishedListener() {
            @Override
            public void OnGetLogisticsDetialFinished(final List<LogisticsBean> msgs) {

                if(msgs.size()==0){
                    toastInUI(LogisticsDetialActivity.this,"暂无物流消息！");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addAll(msgs, true);
                        timeline.setTimelineCount(msgs.size());
                    }
                });

            }
        });

    }

    private void initData() {
        orderBean = (OrderBean) getIntent().getSerializableExtra("logist");
        Glide.with(this).load(orderBean.getUrl()).error(R.mipmap.android_quanzi).into(ivLogistics);
        tvLogisticsMan.setText("收件人："+orderBean.getTakeMan());
        tvLogisticsAddress.setText("收货地址："+orderBean.getTakeAddress());
        tvLogisticsExpress.setText(orderBean.getExpress());
        tvLogisticsExpressNumber.setText(orderBean.getOrderNum());

        adapter = new LogisticsMsgDetialAdapter(this, R.layout.item_logistics_detial, msgList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvLogistics.setLayoutManager(layoutManager);
        rvLogistics.setAdapter(adapter);

    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "物流详情");
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
