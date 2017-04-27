package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.StoreProductAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.StoreProductBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetStoreProductsFinishedListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class ShangpuAllProductsActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_publish_product)
    TextView tvPublishProduct;
    @BindView(R.id.tv_batch_handle)
    TextView tvBatchHandle;
    @BindView(R.id.rv_product)
    RecyclerView rvProduct;
    @BindView(R.id.activity_shangpu_all_products)
    LinearLayout activityShangpuAllProducts;
    private View headerview;
    private List<StoreProductBean> products;
    private StoreProductAdapter adapter;
    private FXMallControl control;
    private String type = "新品";
    private String from = "";
    private int statu;
    private String orderBy = "createTime";
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangpu_all_products);
        ButterKnife.bind(this);
        control = new FXMallControl();
        from = getIntent().getStringExtra("from");
        statu = getIntent().getIntExtra("statu", -1);
        initHeaderView();
        refresh(orderBy, index, 10, statu);
    }

    @Override
    public View getContentView() {
        return activityShangpuAllProducts;
    }

    /**
     * @param orderBy 数据排序
     * @param index   页码
     * @param size    每页数量
     * @param statu   0：仓库中，1：销售中
     */
    private void refresh(String orderBy, int index, int size, int statu) {
        //TODO createTime(新品)、inventory（库存）、sales（销量），区分销售中和仓库中
        control.getStoreProducts(this, sp.get("token"), client, orderBy, index, size, statu, new OnGetStoreProductsFinishedListener() {
            @Override
            public void OnGetStoreProductsFinished(List<StoreProductBean> products) {
                adapter.addAll(products, true);
            }
        });
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "全部商品");

        tabLayout.addTab(tabLayout.newTab().setText("新品"), true);
        tabLayout.addTab(tabLayout.newTab().setText("销量"));
        tabLayout.addTab(tabLayout.newTab().setText("库存"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                type = tab.getText().toString();
                switch (type) {
                    case "新品":
                        orderBy = "createTime";
                        break;
                    case "销量":
                        orderBy = "sales";
                        break;
                    case "库存":
                        orderBy = "inventory";
                        break;
                }
                refresh(orderBy, index, 10, statu);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        products = new ArrayList<>();
        if ("sale".equals(from)) {
            adapter = new StoreProductAdapter(this, this, R.layout.item_store_product, products, from);
        } else {
            adapter = new StoreProductAdapter(this, this, R.layout.item_store_product2, products, from);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvProduct.setLayoutManager(layoutManager);
        rvProduct.setAdapter(adapter);
    }


    @OnClick(R.id.tv_publish_product)
    public void publishProduct() {
        startActivity(new Intent(this, PublishProductActivity.class));
    }

    @OnClick(R.id.tv_batch_handle)
    public void BatchHandler() {
        Intent intent = new Intent(this, BatchHandleActivity.class);
        intent.putExtra("data", (Serializable) products);
        if ("sale".equals(from)) {
            intent.putExtra("from", "sale");
        } else {
            intent.putExtra("from", "rest");
        }
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 161 && resultCode == 162) {
            int position = data.getIntExtra("position", 1);
            products.remove(position);
            adapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }
}
