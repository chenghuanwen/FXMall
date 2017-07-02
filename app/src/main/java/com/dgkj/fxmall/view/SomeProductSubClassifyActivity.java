package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.MainProductDisplayAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.StoreProductBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.LoadMoreListener;
import com.dgkj.fxmall.listener.OnGetStoreProductsFinishedListener;
import com.dgkj.fxmall.listener.OnGetSubClassifyProductsFinishedListener;
import com.dgkj.fxmall.listener.OnSearchProductsFinishedListener;
import com.dgkj.fxmall.utils.LoadProgressDialogUtil;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.view.myView.ItemOffsetDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

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
    @BindView(R.id.activity_new_goods)
    LinearLayout activityNewGoods;
    private View headerview;
    private List<MainProductBean> productList;
    private MainProductDisplayAdapter adapter;
    private String type = "totalScore",orderBy="createTime",from="";
    private int subId,index,storeId;
    private FXMallControl control = new FXMallControl();
    private OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
    private List<MainProductBean> totalData;
    private LoadProgressDialogUtil progressDialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goods);
        ButterKnife.bind(this);
        initHeaderView();
        initTabLayout();

        progressDialogUtil = new LoadProgressDialogUtil(this);

        if("store".equals(from)){
            if(subId == -1){
                refresh(storeId);
            }else {
                refresh(orderBy,1,20,storeId,subId);
            }
            LogUtil.i("TAG","获取商铺二级分类商品============"+orderBy+"===="+storeId+"===="+subId);
        }else {
            refresh();
        }
    }

    @Override
    public View getContentView() {
        return activityNewGoods;
    }

    private void refresh() {
        progressDialogUtil.buildProgressDialog();

        control.getProductsOfSubclassify(subId, orderBy, index, 20, okHttpClient, new OnSearchProductsFinishedListener() {
            @Override
            public void onSearchProductsFinished(List<MainProductBean> mainProducts) {
                totalData = mainProducts;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(index>1 && totalData.size()<20){
                            Toast.makeText(SomeProductSubClassifyActivity.this,"已经到底啦！！",Toast.LENGTH_SHORT).show();
                        }
                        if(index>1){
                            adapter.addAll(totalData, false);
                        }else {
                            adapter.addAll(totalData, true);
                        }

                        progressDialogUtil.cancelProgressDialog();
                    }
                });
            }
        });
    }


    /**
     * 获取店铺二级分类商品
     * @param orderBy
     * @param index
     * @param size
     * @param storeId
     * @param subId
     */
    private void refresh(String orderBy, int index, int size, int storeId,int subId){
        loadProgressDialogUtil.buildProgressDialog();
        control.getStoreSubClassifyProducts(this, storeId, okHttpClient, orderBy, index, size, subId, new OnSearchProductsFinishedListener() {
            @Override
            public void onSearchProductsFinished(List<MainProductBean> mainProducts) {
                totalData = mainProducts;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addAll(totalData, true);
                        progressDialogUtil.cancelProgressDialog();
                    }
                });
            }
        });
    }


    /**
     * 获取商铺所有商品
     * @param storeId
     */
    private void refresh(int storeId) {
        loadProgressDialogUtil.buildProgressDialog();
        control.getSearchProducts(this, null, orderBy,null,null,null,null,index, 20, storeId, okHttpClient, new OnSearchProductsFinishedListener() {
            @Override
            public void onSearchProductsFinished(final List<MainProductBean> mainProducts) {
                loadProgressDialogUtil.cancelProgressDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(index>1 && mainProducts.size()<20){
                            Toast.makeText(SomeProductSubClassifyActivity.this,"已经到底啦！！",Toast.LENGTH_SHORT).show();
                        }
                        if(index>1){
                            adapter.addAll(mainProducts, false);
                        }else {
                            adapter.addAll(mainProducts, true);
                        }
                    }
                });
            }
        });
    }


    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("综合"), true);
        tabLayout.addTab(tabLayout.newTab().setText("销量"));
        tabLayout.addTab(tabLayout.newTab().setText("新品"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        productList = new ArrayList<>();
        adapter = new MainProductDisplayAdapter(this, productList, "product");
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvSearchContent.setLayoutManager(layoutManager);
        rvSearchContent.addItemDecoration(new ItemOffsetDecoration(10));
        rvSearchContent.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        orderBy = "totalScore";
                        refresh();
                        break;
                    case 1:
                        orderBy = "sales";
                        refresh();
                        break;
                    case 2:
                        orderBy = "createTime";
                        refresh();
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


        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadmore() {
                index++;
                refresh();
            }
        });

    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, getIntent().getStringExtra("type"));
        subId = getIntent().getIntExtra("subId",0);
        from = getIntent().getStringExtra("from");
        if("store".equals(from)){
            storeId = getIntent().getIntExtra("storeId",-1);
        }

        LogUtil.i("TAG","商店商品分类===storeid=="+storeId+"===="+subId);
    }


    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.iv_car)
    public void goCar() {
        jumpTo(ShoppingCarActivity.class, true);
    }

    @OnClick(R.id.iv_screening)
    public void screening() {
        jumpTo(ScreeningProductActivity.class, true);
    }

    @OnClick(R.id.iv_go_top)
    public void goTop() {
        rvSearchContent.smoothScrollToPosition(0);
    }
}
