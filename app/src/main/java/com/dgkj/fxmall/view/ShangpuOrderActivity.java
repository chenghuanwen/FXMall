package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.HomePageFragmentAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.OrderBean;
import com.dgkj.fxmall.bean.SuperOrderBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.fragment.MyOrderClassifyFragment;
import com.dgkj.fxmall.listener.OnGetMyOrderInfoFinishedListener;
import com.dgkj.fxmall.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class ShangpuOrderActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.vp_order)
    ViewPager vpOrder;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.activity_shangpu_order)
    LinearLayout activityShangpuOrder;
    private View headerview;
    private ArrayList<Fragment> fragments;
    private List<SuperOrderBean> allOrder;
    private List<SuperOrderBean> hasDeliver;
    private List<SuperOrderBean> sold;
    private List<SuperOrderBean> refund;
    private List<SuperOrderBean> waitDeliver;
    private List<OrderBean> subHasDeliver, subSols, subRefund, subWaitDeliver;
    private HomePageFragmentAdapter adapter;
    private String[] states = new String[]{"等待发货", "已发货", "已完成", "买家申请退货/退款", "等待收货", "等待买家发货", "退款已完成", "已拒绝申请", "待接单", "已接单"};
    private String from = "";
    private int statu = 0, isAll, index = 1;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private FXMallControl control = new FXMallControl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangpu_order);
        ButterKnife.bind(this);
        initHeaderView();
        refresh(statu, 0, 1);
        //   initFragment();//必须在TabLayout之前初始化，否则TabLayout标题无法显示
        // initTab();
    }

    @Override
    public View getContentView() {
        return activityShangpuOrder;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "我的订单");

        fragments = new ArrayList<>();
        allOrder = new ArrayList<>();
        waitDeliver = new ArrayList<>();
        hasDeliver = new ArrayList<>();
        sold = new ArrayList<>();
        refund = new ArrayList<>();
        subHasDeliver = new ArrayList<>();
        subWaitDeliver = new ArrayList<>();
        subSols = new ArrayList<>();
        subRefund = new ArrayList<>();
    }


    private void initFragment() {

        fragments.add(new MyOrderClassifyFragment(waitDeliver));
        fragments.add(new MyOrderClassifyFragment(hasDeliver));
        fragments.add(new MyOrderClassifyFragment(sold));
        fragments.add(new MyOrderClassifyFragment(refund));
        adapter = new HomePageFragmentAdapter(getSupportFragmentManager(), fragments);
        vpOrder.setAdapter(adapter);

        from = getIntent().getStringExtra("from");
        switch (from) {
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
        tabLayout.addTab(tabLayout.newTab().setText("待发货"), true);
        tabLayout.addTab(tabLayout.newTab().setText("已发货"));
        tabLayout.addTab(tabLayout.newTab().setText("已售出"));
        tabLayout.addTab(tabLayout.newTab().setText("退货/退款"));
        tabLayout.setupWithViewPager(vpOrder, true);
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


    private void refresh(int statu, int isAll, int index) {
        loadProgressDialogUtil.buildProgressDialog();
        control.getStoreOrderInfo(this, sp.get("token"), statu, isAll, index,50, client, new OnGetMyOrderInfoFinishedListener() {
            @Override
            public void onGetMyOrderInfoFinished(final List<OrderBean> orders) {
                loadProgressDialogUtil.cancelProgressDialog();

                if(orders.size()==0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ShangpuOrderActivity.this,"亲，你还未下过订单哦，快去购买吧！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    allOrder.addAll(sortProductsOfOrder(orders));
                    for (OrderBean order : orders) {
                        switch (order.getStateNum()) {
                            //TODO 根据订单不同状态区分
                            case 2:
                                subHasDeliver.add(order);
                                break;
                            case 1:
                                subWaitDeliver.add(order);
                                break;
                            case 3:
                                subSols.add(order);
                                break;
                            case 4:
                                subRefund.add(order);
                                break;
                        }
                    }
                    waitDeliver.addAll(sortProductsOfOrder(subWaitDeliver));
                    LogUtil.i("TAG","商铺待发货订单数量==="+subWaitDeliver.size());
                    hasDeliver.addAll(sortProductsOfOrder(subHasDeliver));
                    sold.addAll(sortProductsOfOrder(subSols));
                    refund.addAll(sortProductsOfOrder(subRefund));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initFragment();//必须在TabLayout之前初始化，否则TabLayout标题无法显示
                            initTab();
                        }
                    });
                }
            }
        });
    }


    /**
     * 将相同订单id数据放到同一个集合
     *
     * @param orders
     * @return
     */
    public List<SuperOrderBean> sortProductsOfOrder(List<OrderBean> orders) {
        //将所有运费模板按照id进行分类，相同的id属于同一个大模板
        List<SuperOrderBean> superPostageList = new ArrayList<>();
        Map<OrderBean, List<OrderBean>> map = new HashMap<>();
        OrderBean post = new OrderBean();
        if (orders.size() > 0) {
            for (int i = 0; i < orders.size(); i++) {
                int key = orders.get(i).getId();//获取当条数据的id值
                if (post.getId() > 0) {
                    boolean b = key != post.getId();//当该id值与key值中的id值不同时，则创建新的key,保证key值唯一
                    if (b) {
                        post = new OrderBean();
                    }
                }
                post.setId(key);//为key值设置id

                //将相同id 的key所有的数据都指向一个数据集合
                List<OrderBean> posts = map.get(post);//key值变时，集合也会变
                //当第一次次集合没有初始化时，创建一个新集合，此后这相同的数据全部添加到此集合
                if (posts == null) {
                    posts = new ArrayList<>();
                }
                posts.add(orders.get(i));//将相同数据添加到集合
                map.put(post, posts);//将相同的数据放入map（不断覆盖，直至最后一次得到相同全部数据）
            }

            //TODO 集成数据:遍历map，将数据添加到listView实体类集合
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                OrderBean postageBean = (OrderBean) entry.getKey();

                SuperOrderBean superPost = new SuperOrderBean();
                int id = postageBean.getId();
                ArrayList<OrderBean> posts = (ArrayList<OrderBean>) entry.getValue();
                superPost.setId(id);
                superPost.setSubOrders(posts);
                superPostageList.add(superPost);
            }

        }
        return superPostageList;
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
