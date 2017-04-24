package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.MainProductDisplayAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetSubClassifyProductsFinishedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SomeProductSubClassifyActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.rv_search_content)
    RecyclerView rvSearchContent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_car)
    ImageView ivCar;
    @BindView(R.id.iv_screening)
    ImageView ivScreening;
    @BindView(R.id.iv_go_top)
    ImageView ivGoTop;
    private View headerview;
    private List<MainProductBean> productList;
    private MainProductDisplayAdapter adapter;
    private String type = "";
    private FXMallControl control = new FXMallControl();
    private List<MainProductBean> totalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goods);
        ButterKnife.bind(this);
        initHeaderView();
        initTabLayout();
        refresh();
    }

    private void refresh() {
        control.getSubClassifyProductData(new OnGetSubClassifyProductsFinishedListener() {
            @Override
            public void onGetDemandDatasFinished(List<MainProductBean> products) {
               totalData = products;
                adapter.addAll(totalData,true);
            }
        });
    }

    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("综合"), true);
        tabLayout.addTab(tabLayout.newTab().setText("销量"));
        tabLayout.addTab(tabLayout.newTab().setText("新品"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        productList = new ArrayList<>();
        adapter = new MainProductDisplayAdapter(this, R.layout.item_main_product, productList, "product");
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        rvSearchContent.setLayoutManager(layoutManager);
        rvSearchContent.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        adapter.addAll(totalData, true);
                        break;
                    case 1:
                        adapter.addAll(totalData.subList(0, 6), true);
                        break;
                    case 2:
                        adapter.addAll(totalData.subList(6, totalData.size()), true);
                        break;
                }
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
        setHeaderTitle(headerview, getIntent().getStringExtra("type"));

    }


    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }
    @OnClick(R.id.iv_car)
    public void goCar(){
        jumpTo(ShoppingCarActivity.class,true);
    }

    @OnClick(R.id.iv_screening)
    public void screening(){
        jumpTo(ScreeningProductActivity.class,true);
    }

    @OnClick(R.id.iv_go_top)
    public void goTop(){
        rvSearchContent.smoothScrollToPosition(0);
    }
}