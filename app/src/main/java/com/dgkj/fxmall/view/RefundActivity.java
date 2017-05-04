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
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetMyOrderInfoFinishedListener;
import com.dgkj.fxmall.model.FXMallModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class RefundActivity extends BaseActivity {

    @BindView(R.id.rv_refund_order)
    RecyclerView rvRefundOrder;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.activity_refund)
    LinearLayout activityRefund;
    private View headerview;
    private List<SuperOrderBean> orderBeanList;
    private List<OrderBean> refundOrders = new ArrayList<>();
    private OrderClassifyAdapter adapter;
    private String[] states = new String[]{"等待商家处理退款申请", "商家同意退款", "退款成功", "商家拒绝退款", "商家拒绝退款"};
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private FXMallControl control = new FXMallControl();
    private int statu, isAll;

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


        control.getMyOrderInfo(this, sp.get("token"), statu, isAll, client, new OnGetMyOrderInfoFinishedListener() {
            @Override
            public void onGetMyOrderInfoFinished(List<OrderBean> orders) {
                for (OrderBean order : orders) {
                    if(order.getStateNum()==4){
                        refundOrders.add(order);
                    }
                }
                List<SuperOrderBean> superOrderBeen = sortProductsOfOrder(refundOrders);
                adapter.addAll(superOrderBeen,true);
            }
        });


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

    public  List<SuperOrderBean> sortProductsOfOrder(List<OrderBean> orders) {
        //将所有运费模板按照id进行分类，相同的id属于同一个大模板
        List<SuperOrderBean> superPostageList = new ArrayList<>();
        Map<OrderBean, List<OrderBean>> map = new HashMap<>();
        OrderBean post = new OrderBean();
        if (orders.size() > 0) {
            for (int i = 0; i < orders.size(); i++) {
                int key = orders.get(i).getId();//获取当条数据的id值
                if (post.getId() >= 0) {
                    boolean b = key == post.getId();//当该id值与key值中的id值不同时，则创建新的key,保证key值唯一
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
