package com.dgkj.fxmall.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.HomePageFragmentAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.OrderBean;
import com.dgkj.fxmall.bean.SuperOrderBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.fragment.MyOrderClassifyFragment;
import com.dgkj.fxmall.listener.OnGetMyOrderInfoFinishedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyOrdersActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.vp_order)
    ViewPager vpOrder;
    @BindView(R.id.activity_my_orders)
    LinearLayout activityMyOrders;
    private View headerview;
    private ArrayList<Fragment> fragments;
    private List<SuperOrderBean> allOrder;
    private List<SuperOrderBean> waitPay;
    private List<SuperOrderBean> waitDeliver;
    private List<SuperOrderBean> waitTakeGoods;
    private List<SuperOrderBean> waitComment;
    private List<OrderBean> waitPayOrders,waitDeliverOrders,waitTakeOrders,waitCommentOrders,allOrders;
    private HomePageFragmentAdapter adapter;
    private String[] states = new String[]{"等待买家付款", "等待卖家发货", "等待买家收货", "等待买家评价", "交易完成","待确认","商家已接单","待付款(不支持发货)"};
    private String from = "";
    private int statu=0, isAll;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private FXMallControl control = new FXMallControl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        ButterKnife.bind(this);

        initHeaderView();
         refresh(statu, 0);
      //  initFragment();//必须在TabLayout之前初始化，否则TabLayout标题无法显示
       // initTab();
    }

    @Override
    public View getContentView() {
        return activityMyOrders;
    }

    private void refresh(int statu, int isAll) {
        loadProgressDialogUtil.buildProgressDialog();
        waitPayOrders = new ArrayList<>();
        waitDeliverOrders = new ArrayList<>();
        waitTakeOrders = new ArrayList<>();
        waitCommentOrders = new ArrayList<>();
        allOrders = new ArrayList<>();

        control.getMyOrderInfo(this, sp.get("token"), statu, isAll, client, new OnGetMyOrderInfoFinishedListener() {
            @Override
            public void onGetMyOrderInfoFinished(List<OrderBean> orders) {
                loadProgressDialogUtil.cancelProgressDialog();
                if(orders.size()==0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyOrdersActivity.this,"亲，你还未下过订单哦，快去购买吧！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {

                for (OrderBean order : orders) {
                    switch (order.getStateNum()) {
                        case 0:
                            waitPayOrders.add(order);
                           allOrders.add(order);
                            break;
                        case 1:
                            waitDeliverOrders.add(order);
                            allOrders.add(order);
                            break;
                        case 2:
                            waitTakeOrders.add(order);
                            allOrders.add(order);
                            break;
                        case 3:
                            waitCommentOrders.add(order);
                            allOrders.add(order);
                            break;
                    }
                }

                    allOrder.addAll(sortProductsOfOrder(allOrders));

                waitDeliver.addAll(sortProductsOfOrder(waitDeliverOrders));
                waitPay.addAll(sortProductsOfOrder(waitPayOrders));
                waitTakeGoods.addAll(sortProductsOfOrder(waitTakeOrders));
                waitComment.addAll(sortProductsOfOrder(waitCommentOrders));

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

    private void initFragment() {

        fragments.add(new MyOrderClassifyFragment(allOrder));
        fragments.add(new MyOrderClassifyFragment(waitPay));
        fragments.add(new MyOrderClassifyFragment(waitDeliver));
        fragments.add(new MyOrderClassifyFragment(waitTakeGoods));
        fragments.add(new MyOrderClassifyFragment(waitComment));
        adapter = new HomePageFragmentAdapter(getSupportFragmentManager(), fragments);
        vpOrder.setAdapter(adapter);

        from = getIntent().getStringExtra("from");
        switch (from) {
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
        tabLayout.addTab(tabLayout.newTab().setText("全部"), true);
        tabLayout.addTab(tabLayout.newTab().setText("待付款"));
        tabLayout.addTab(tabLayout.newTab().setText("待发货"));
        tabLayout.addTab(tabLayout.newTab().setText("待收货"));
        tabLayout.addTab(tabLayout.newTab().setText("待评价"));
        tabLayout.setupWithViewPager(vpOrder, true);
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

        fragments = new ArrayList<>();
        allOrder = new ArrayList<>();
        waitPay = new ArrayList<>();
        waitDeliver = new ArrayList<>();
        waitTakeGoods = new ArrayList<>();
        waitComment = new ArrayList<>();
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }


    public  List<SuperOrderBean> sortProductsOfOrder(List<OrderBean> orders) {
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


}
