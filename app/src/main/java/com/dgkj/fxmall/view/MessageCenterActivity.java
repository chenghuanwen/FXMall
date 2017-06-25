package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.NotifyMsgBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetNotifyMsgFinishedListener;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.tv_msg_count4)
    TextView tvMsgCount4;
    @BindView(R.id.fl_civ4)
    FrameLayout flCiv4;
    @BindView(R.id.tv_msg_content4)
    TextView tvMsgContent4;
    @BindView(R.id.tv_msg_time4)
    TextView tvMsgTime4;
    @BindView(R.id.rl_publish_account)
    RelativeLayout rlPublishAccount;

    private View headerview;
    private ArrayList<NotifyMsgBean> systemMsg,orderMsg,warmMsg,accountMsg;
    private FXMallControl control;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ButterKnife.bind(this);
        initHeaderview();
        init();
        refresh();
    }

    private void refresh() {
        control.getNotifyMsgData(sp.get("token"), new OnGetNotifyMsgFinishedListener() {
            @Override
            public void OnGetLogisticsMsgFinished(ArrayList<NotifyMsgBean> list) {
                for (NotifyMsgBean msgBean : list) {
                    String type = msgBean.getType();
                    switch (type){
                        case "system":
                            systemMsg.add(msgBean);
                            break;
                        case "order":
                            orderMsg.add(msgBean);
                            break;
                        case "warn":
                            warmMsg.add(msgBean);
                            break;
                        case "account":
                            accountMsg.add(msgBean);
                            break;
                    }
                }
                setData();
            }
        });
    }


    private void setData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(MyApplication.orderMsgCount==0){
                    tvMsgCount1.setVisibility(View.INVISIBLE);
                    tvMsgTime1.setVisibility(View.INVISIBLE);
                    tvMsgContent1.setText("暂无消息");
                }else {
                    tvMsgCount1.setVisibility(View.VISIBLE);
                    tvMsgTime1.setVisibility(View.VISIBLE);
                    if(orderMsg.size()==0){return;}
                    tvMsgCount1.setText(MyApplication.orderMsgCount+"");
                    tvMsgContent1.setText(orderMsg.get(0).getCotent());
                    tvMsgTime1.setText(orderMsg.get(0).getTime());
                }

                if(MyApplication.systemMsgCount==0){
                    tvMsgCount2.setVisibility(View.INVISIBLE);
                    tvMsgTime2.setVisibility(View.INVISIBLE);
                    tvMsgContent2.setText("暂无消息");
                }else {
                    tvMsgCount2.setVisibility(View.VISIBLE);
                    tvMsgTime2.setVisibility(View.VISIBLE);
                    tvMsgCount2.setText(MyApplication.systemMsgCount+"");
                    tvMsgContent2.setText(systemMsg.get(0).getCotent());
                    tvMsgTime2.setText(systemMsg.get(0).getTime());
                }

                if(MyApplication.warmMsgCount==0){
                    tvMsgCount3.setVisibility(View.INVISIBLE);
                    tvMsgTime3.setVisibility(View.INVISIBLE);
                    tvMsgContent3.setText("暂无消息");
                }else {
                    tvMsgCount3.setVisibility(View.VISIBLE);
                    tvMsgTime3.setVisibility(View.VISIBLE);
                    tvMsgCount3.setText(MyApplication.warmMsgCount+"");
                    tvMsgContent3.setText(warmMsg.get(0).getCotent());
                    tvMsgTime3.setText(warmMsg.get(0).getTime());
                }

                if(MyApplication.accountMsgCount==0){
                    tvMsgCount4.setVisibility(View.INVISIBLE);
                    tvMsgTime4.setVisibility(View.INVISIBLE);
                    tvMsgContent4.setText("暂无消息");
                }else {
                    tvMsgCount4.setVisibility(View.VISIBLE);
                    tvMsgTime4.setVisibility(View.VISIBLE);
                    tvMsgCount4.setText(MyApplication.accountMsgCount+"");
                    tvMsgContent4.setText(accountMsg.get(0).getCotent());
                    tvMsgTime4.setText(accountMsg.get(0).getTime());
                }
            }
        });
    }

    private void init() {
        control = new FXMallControl();
        systemMsg = new ArrayList<>();
        orderMsg = new ArrayList<>();
        warmMsg = new ArrayList<>();
        accountMsg = new ArrayList<>();

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
    public void notifyMsg() {
       gotoMsgDetial("order",orderMsg);
    }

    @OnClick(R.id.rl_notify)
    public void notifyMsg2() {
        gotoMsgDetial("system",systemMsg);
    }

    @OnClick(R.id.rl_publish_demand)
    public void notifyMsg3() {
        gotoMsgDetial("warn",warmMsg);
    }

    @OnClick(R.id.rl_publish_account)
    public void notifyMsg4() {
        gotoMsgDetial("account",accountMsg);
    }

    public void gotoMsgDetial(String from,ArrayList<NotifyMsgBean> msgList){
        Intent intent = new Intent(this, NotifyMsgActivity.class);
        intent.putExtra("msg",msgList);
        intent.putExtra("from",from);
        startActivity(intent);
    }
}
