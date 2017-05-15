package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.ProductDetialImageAdapter;
import com.dgkj.fxmall.adapter.SearchStoreAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.MainDemandBean;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetMyRecommendStoreFinishedListener;
import com.dgkj.fxmall.view.myView.RecommendStoreDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class DemandDetialActivity extends BaseActivity {

    @BindView(R.id.rv_image_preview1)
    RecyclerView rvImagePreview1;
    @BindView(R.id.tv_demand_detial)
    TextView tvDemandDetial;
    @BindView(R.id.tv_demand_count)
    TextView tvDemandCount;
    @BindView(R.id.tv_product_address)
    TextView tvProductAddress;
    @BindView(R.id.tv_product_decribe)
    TextView tvProductDecribe;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_take_address)
    TextView tvTakeAddress;
    @BindView(R.id.rv_recommend_store)
    RecyclerView rvRecommendStore;
    @BindView(R.id.iv_demand_back)
    FrameLayout ivDemandBack;
    @BindView(R.id.tv_demand_car)
    TextView tvDemandCar;
    @BindView(R.id.tv_car_count1)
    TextView tvCarCount1;
    @BindView(R.id.tv_my_demand)
    TextView tvMyDemand;
    @BindView(R.id.tv_recommend)
    TextView tvRecommend;
    @BindView(R.id.activity_product_detial)
    LinearLayout activityProductDetial;

    private View headerview;
    private MainDemandBean demandBean;
    private ProductDetialImageAdapter adapter;
    private OkHttpClient client;
    private FXMallControl control;
    private List<StoreBean> storeList;
    private SearchStoreAdapter storeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_detial);
        ButterKnife.bind(this);
        initHeaderView();

        demandBean = getIntent().getParcelableExtra("product");
        if(MyApplication.shoppingCount == 0){
            tvCarCount1.setVisibility(View.GONE);
        }else {
            tvCarCount1.setVisibility(View.VISIBLE);
            tvCarCount1.setText(MyApplication.shoppingCount+"");
        }


        setData();
    }

    @Override
    public View getContentView() {
        return activityProductDetial;
    }

    private void setData() {
        if (demandBean == null) {
            return;
        }

        adapter = new ProductDetialImageAdapter(this, R.layout.item_product_main_image, demandBean.getUrls());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPositionWithOffset(1, 100);
        // layoutManager.setStackFromEnd(true);
        rvImagePreview1.setLayoutManager(layoutManager);
        rvImagePreview1.setAdapter(adapter);

        tvProductDecribe.setText(demandBean.getIntroduce());
        tvDemandCount.setText(demandBean.getDemand() + "件");
        tvDemandDetial.setText(demandBean.getTitel());
        tvPhone.setText(demandBean.getPhone());
        String address = demandBean.getAddress();
        int index = address.indexOf("市");
        tvProductAddress.setText(address.substring(0, index + 1));
        tvTakeAddress.setText(address);

        storeList = new ArrayList<>();
        storeAdapter = new SearchStoreAdapter(this, R.layout.item_shangpu_search_result, storeList, "search");
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        rvRecommendStore.setLayoutManager(layoutManager1);
        rvRecommendStore.setAdapter(storeAdapter);

        getRecommendStore();
    }

    /**
     * 获取该需求推荐的商铺
     */
    private void getRecommendStore() {
        client = new OkHttpClient.Builder().build();
        control = new FXMallControl();
        control.getMyRecommendStore(this, sp.get("token"), FXConst.GET_RECOMMEND_STORES_FOR_DEMAND, 1, 20, client, new OnGetMyRecommendStoreFinishedListener() {
            @Override
            public void onGetMyRecommendStoreFinished(List<StoreBean> stores) {
                storeAdapter.addAll(stores, true);
            }
        });
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "需求详情");
    }


    @OnClick(R.id.tv_demand_car)
    public void toCar() {
        jumpTo(ShoppingCarActivity.class, false);
    }

    @OnClick(R.id.tv_my_demand)
    public void myDemand() {
        jumpTo(MyDemandActivity.class, false);
    }

    @OnClick(R.id.tv_recommend)
    public void recommend() {
        //TODO 推荐店铺
        RecommendStoreDialog dialog = new RecommendStoreDialog(this, demandBean.getId());
        dialog.show(getSupportFragmentManager(), "");
    }

    @OnClick(R.id.iv_demand_back)
    public void back() {
        finish();
    }
}
