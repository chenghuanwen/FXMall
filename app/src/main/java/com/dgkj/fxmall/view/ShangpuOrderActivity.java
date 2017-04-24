package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.HomePageFragmentAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.OrderBean;
import com.dgkj.fxmall.fragment.MyOrderClassifyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShangpuOrderActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.vp_order)
    ViewPager vpOrder;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    private View headerview;
    private ArrayList<Fragment> fragments;
    private List<OrderBean> hasDeliver;
    private List<OrderBean> sold;
    private List<OrderBean> refund;
    private List<OrderBean> allOrder;
    private List<OrderBean> waitDeliver;
    private HomePageFragmentAdapter adapter;
    private String[] states = new String[]{"等待发货","已发货","已完成","买家申请退货/退款","等待收货","等待买家发货","退款已完成","已拒绝申请"};
    private String from = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangpu_order);
        ButterKnife.bind(this);
        initHeaderView();
        initFragment();//必须在TabLayout之前初始化，否则TabLayout标题无法显示
        initTab();
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "我的订单");
    }


    private void initFragment() {
        fragments = new ArrayList<>();
        allOrder = new ArrayList<>();
        waitDeliver = new ArrayList<>();
        hasDeliver = new ArrayList<>();
        sold = new ArrayList<>();
        refund = new ArrayList<>();

        //TEST
        for (int i = 0; i < 8; i++) {
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
            allOrder.add(order);
        }

        waitDeliver.add(allOrder.get(0));
        hasDeliver.add(allOrder.get(1));
        sold.add(allOrder.get(2));
        refund.add(allOrder.get(3));
        refund.add(allOrder.get(4));
        refund.add(allOrder.get(5));
        refund.add(allOrder.get(6));
        refund.add(allOrder.get(7));

        fragments.add(new MyOrderClassifyFragment(waitDeliver));
        fragments.add(new MyOrderClassifyFragment(hasDeliver));
        fragments.add(new MyOrderClassifyFragment(sold));
        fragments.add(new MyOrderClassifyFragment(refund));
        adapter = new HomePageFragmentAdapter(getSupportFragmentManager(),fragments);
        vpOrder.setAdapter(adapter);

        from = getIntent().getStringExtra("from");
        switch (from){
            case "wait":
                vpOrder.setCurrentItem(0);
                break;
            case "ok":
                vpOrder.setCurrentItem(1);
                break;
            case "sold":
                vpOrder.setCurrentItem(2);
                break;
            case "refund":
                vpOrder.setCurrentItem(3);
                break;
        }

        vpOrder.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void initTab() {
        tabLayout.addTab(tabLayout.newTab().setText("待发货"),true);
        tabLayout.addTab(tabLayout.newTab().setText("已发货"));
        tabLayout.addTab(tabLayout.newTab().setText("已售出"));
        tabLayout.addTab(tabLayout.newTab().setText("退货/退款"));
        tabLayout.setupWithViewPager(vpOrder,true);
        tabLayout.getTabAt(0).setText("待发货");
        tabLayout.getTabAt(1).setText("已发货");
        tabLayout.getTabAt(2).setText("已售出");
        tabLayout.getTabAt(3).setText("退货/退款");
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                vpOrder.setCurrentItem(position);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
