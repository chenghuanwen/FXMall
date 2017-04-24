package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.MainProductDisplayAdapter;
import com.dgkj.fxmall.adapter.SearchStoreAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.bean.StoreProductBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetMyRecommendStoreFinishedListener;
import com.dgkj.fxmall.listener.OnGetStoreProductsFinishedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class SearchContentActivity extends BaseActivity {

    @BindView(R.id.tv_search_title)
    TextView tvSearchTitle;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.rv_search_content)
    RecyclerView rvSearchContent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_gotop)
    ImageView ivGoTop;
    private List<MainProductBean> productList;
    private MainProductDisplayAdapter adapter;
    private List<StoreBean> storeList;
    private SearchStoreAdapter storeAdapter;
    private String searchType = "", searchTitel = "";
    private int index = 1;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private FXMallControl control = new FXMallControl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_content);
        ButterKnife.bind(this);

        initSearchCondition();
        initTabLayout();

        getSearchData();
    }

    private void getSearchData() {
        if ("商品".equals(searchType)) {
            control.getSearchProducts(this, searchType, index, 15, client, new OnGetStoreProductsFinishedListener() {
                @Override
                public void OnGetStoreProductsFinished(List<StoreProductBean> products) {
                    //TODO 写一个通用的商品实体类
                }
            });
        }else {
            control.getSearchStores(this, searchType, index, 20, client, new OnGetMyRecommendStoreFinishedListener() {
                @Override
                public void onGetMyRecommendStoreFinished(List<StoreBean> stores) {
                    storeAdapter.addAll(stores,true);
                }
            });
        }
    }

    private void initSearchCondition() {
        Intent intent = getIntent();
        searchType = intent.getStringExtra("type");
        searchTitel = intent.getStringExtra("key");
        tvSearchTitle.setText(searchTitel);
    }


    private void initTabLayout() {


        tabLayout.addTab(tabLayout.newTab().setText("综合"), true);
        tabLayout.addTab(tabLayout.newTab().setText("销量"));
        tabLayout.addTab(tabLayout.newTab().setText("新品"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        productList = new ArrayList<>();
        adapter = new MainProductDisplayAdapter(this, R.layout.item_main_product, productList, "product");

        storeList = new ArrayList<>();
        storeAdapter = new SearchStoreAdapter(this, R.layout.item_shangpu_search_result, storeList, "search");

        if ("商品".equals(searchType)) {
            GridLayoutManager layoutManager = new GridLayoutManager(this,2);
            rvSearchContent.setLayoutManager(layoutManager);
            rvSearchContent.setAdapter(adapter);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvSearchContent.setLayoutManager(layoutManager);
            rvSearchContent.setAdapter(storeAdapter);
        }


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getTotalData();
                        break;
                    case 1:
                        getSalesData();
                        break;
                    case 2:
                        getNewgoodsData();
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

    /**
     * 获取综合推荐数据
     */
    private void getTotalData() {
        List<MainProductBean> list = new ArrayList<>();
        //TODO 区分商品和商铺数据


        adapter.addAll(list, true);

    }

    /**
     * 获取销量推荐数据
     */
    private void getSalesData() {
        List<MainProductBean> list = new ArrayList<>();
        //TODO


        adapter.addAll(list, true);


    }

    /**
     * 获取新品推荐数据
     */
    private void getNewgoodsData() {
        List<MainProductBean> list = new ArrayList<>();
        //TODO


        adapter.addAll(list, true);

    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.iv_gotop)
    public void goTop(){
        rvSearchContent.smoothScrollToPosition(0);
    }

    @OnClick(R.id.tv_screening)
    public void screening(){
        jumpTo(ScreeningProductActivity.class,true);
    }
}
