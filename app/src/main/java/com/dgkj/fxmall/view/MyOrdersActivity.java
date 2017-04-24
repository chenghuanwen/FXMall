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
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.fragment.MyOrderClassifyFragment;
import com.dgkj.fxmall.listener.OnGetMyOrderInfoFinishedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class MyOrdersActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.vp_order)
    ViewPager vpOrder;
    private View headerview;
    private ArrayList<Fragment> fragments;
    private List<OrderBean> allOrder;
    private List<OrderBean> waitPay;
    private List<OrderBean> waitDeliver;
    private List<OrderBean> waitTakeGoods;
    private List<OrderBean> waitComment;
    private HomePageFragmentAdapter adapter;
    private String[] states = new String[]{"等待买家付款","等待卖家发货","等待买家收货","等待买家评价","交易完成"};
    private String from = "";
    private int statu,isAll;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private FXMallControl control = new FXMallControl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        ButterKnife.bind(this);

        initHeaderView();
        refresh(statu,0);
      /*  initFragment();//必须在TabLayout之前初始化，否则TabLayout标题无法显示
        initTab();*/
    }

    private void refresh(int statu, int isAll) {
        control.getMyOrderInfo(this, sp.get("token"), statu, isAll, client, new OnGetMyOrderInfoFinishedListener() {
            @Override
            public void onGetMyOrderInfoFinished(List<OrderBean> orders) {
                allOrder.addAll(orders);
                for (OrderBean order : orders) {
                    switch (order.getStateNum()){
                        case 0:
                            waitPay.add(order);
                            break;
                        case 1:
                            waitDeliver.add(order);
                            break;
                        case 2:
                            waitTakeGoods.add(order);
                            break;
                        case 3:
                            waitComment.add(order);
                            break;
                    }
                }
                initFragment();//必须在TabLayout之前初始化，否则TabLayout标题无法显示
                initTab();
            }
        });
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        allOrder = new ArrayList<>();
        waitPay = new ArrayList<>();
        waitDeliver = new ArrayList<>();
        waitTakeGoods = new ArrayList<>();
        waitComment = new ArrayList<>();

        //TEST
        for (int i = 0; i < 5; i++) {
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

        waitPay.add(allOrder.get(0));
        waitDeliver.add(allOrder.get(1));
        waitTakeGoods.add(allOrder.get(2));
        waitComment.add(allOrder.get(3));

        fragments.add(new MyOrderClassifyFragment(allOrder));
        fragments.add(new MyOrderClassifyFragment(waitPay));
        fragments.add(new MyOrderClassifyFragment(waitDeliver));
        fragments.add(new MyOrderClassifyFragment(waitTakeGoods));
        fragments.add(new MyOrderClassifyFragment(waitComment));
        adapter = new HomePageFragmentAdapter(getSupportFragmentManager(),fragments);
        vpOrder.setAdapter(adapter);

        from = getIntent().getStringExtra("from");
        switch (from){
            case "all":
                vpOrder.setCurrentItem(0);
                break;
            case "pay":
                vpOrder.setCurrentItem(1);
                break;
            case "deliver":
                vpOrder.setCurrentItem(2);
                break;
            case "take":
                vpOrder.setCurrentItem(3);
                break;
            case "comment":
                vpOrder.setCurrentItem(4);
                break;
        }

        vpOrder.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void initTab() {
        tabLayout.addTab(tabLayout.newTab().setText("全部"),true);
        tabLayout.addTab(tabLayout.newTab().setText("待付款"));
        tabLayout.addTab(tabLayout.newTab().setText("待发货"));
        tabLayout.addTab(tabLayout.newTab().setText("待收货"));
        tabLayout.addTab(tabLayout.newTab().setText("待评价"));
        tabLayout.setupWithViewPager(vpOrder,true);
        tabLayout.getTabAt(0).setText("全部");
        tabLayout.getTabAt(1).setText("待付款");
        tabLayout.getTabAt(2).setText("待发货");
        tabLayout.getTabAt(3).setText("待收货");
        tabLayout.getTabAt(4).setText("待评价");
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

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "我的订单");
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
