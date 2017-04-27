package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.OrderClassifyAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.OrderBean;
import com.dgkj.fxmall.bean.SuperOrderBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RefundActivity extends BaseActivity {

    @BindView(R.id.rv_refund_order)
    RecyclerView rvRefundOrder;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.activity_refund)
    LinearLayout activityRefund;
    private View headerview;
    private List<SuperOrderBean> orderBeanList;
    private OrderClassifyAdapter adapter;
    private String[] states = new String[]{"等待商家处理退款申请", "商家同意退款", "退款成功", "商家拒绝退款", "商家拒绝退款"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
        ButterKnife.bind(this);
        initHeaderView();
        initData();
    }

    @Override
    public View getContentView() {
        return activityRefund;
    }

    private void initData() {
        orderBeanList = new ArrayList<>();
        //TEST
        for (int i = 0; i < 5; i++) {
            List<OrderBean> list = new ArrayList<>();
            SuperOrderBean superOrderBean = new SuperOrderBean();
            for (int j = 0; j < 3; j++) {
                OrderBean order = new OrderBean();
                order.setStoreName("粉小萌酸辣粉旗舰店");
                order.setColor("蓝色");
                order.setSize("均码");
                order.setCount(i);
                order.setHasComment(true);
                order.setIntroduce("啊好多覅家乐福卡静安寺独立开发安静地离开房间阿萨德开了房");
                order.setPostage(20);
                order.setSinglePrice(56);
                order.setSumPrice(56);
                order.setState(states[i]);
                order.setUrl("http://img.12584.cn/ent/tt/201702/f50d628a6ce9a0005ee581e4e0a6a985.jpg");
                list.add(order);
            }
            superOrderBean.setId(i);
            superOrderBean.setSubOrders(list);
            orderBeanList.add(superOrderBean);
        }


        adapter = new OrderClassifyAdapter(this, orderBeanList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvRefundOrder.setLayoutManager(layoutManager);
        rvRefundOrder.setAdapter(adapter);
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "退款/售后");
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
