package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.MainProductDisplayAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.view.myView.ShareDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class StoreMainPageActivity extends BaseActivity {

    @BindView(R.id.tv_car_count)
    TextView tvCarCount;
    @BindView(R.id.fl_car)
    FrameLayout flCar;
    @BindView(R.id.tv_msg_count)
    TextView tvMsgCount;
    @BindView(R.id.fl_msg)
    FrameLayout flMsg;
    @BindView(R.id.civ_shangpu_item)
    CircleImageView civShangpuItem;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_store_address)
    TextView tvStoreAddress;
    @BindView(R.id.rb_commend)
    ImageView rbCommend;
    @BindView(R.id.tv_store_sales)
    TextView tvStoreSales;
    @BindView(R.id.tv_store_goods)
    TextView tvStoreGoods;
    @BindView(R.id.tab_store_main)
    TabLayout tabStoreMain;
    @BindView(R.id.tv_store_share)
    TextView tvStoreShare;
    @BindView(R.id.rv_store_main)
    RecyclerView rvStoreMain;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_product_classify)
    TextView tvProductClassify;
    @BindView(R.id.tv_store_msg)
    TextView tvStoreMsg;

    private StoreBean store;
    private List<MainProductBean> goods;
    private MainProductDisplayAdapter adapter;
    private int carCount, msgCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_main_page);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initTablayout();
        setDatas();
    }

    private void initTablayout() {
        tabStoreMain.addTab(tabStoreMain.newTab().setText("综合"),true);
        tabStoreMain.addTab(tabStoreMain.newTab().setText("销量"));
        tabStoreMain.addTab(tabStoreMain.newTab().setText("新品"));
        tabStoreMain.setTabMode(TabLayout.MODE_FIXED);

    }

    private void setDatas() {
        //TODO 全局获取购物车和消息数量
        tvCarCount.setText(carCount + "");
        tvMsgCount.setText(msgCount + "");
        store = (StoreBean) getIntent().getSerializableExtra("store");
        if(store==null){return;}
        Glide.with(this).load(store.getIconUrl()).placeholder(R.mipmap.android_quanzi).into(civShangpuItem);
        tvStoreName.setText(store.getName());
        tvStoreAddress.setText(store.getAdress());
        //TODO 设置评分图片
        tvStoreSales.setText("销售总量" + store.getTotalScals());
        tvStoreGoods.setText("宝贝数" + store.getGoodsCount());

        goods = new ArrayList<>();
        adapter = new MainProductDisplayAdapter(this, R.layout.item_main_product, goods, "product");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvStoreMain.setLayoutManager(gridLayoutManager);
        rvStoreMain.setAdapter(adapter);

        tabStoreMain.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
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

        tvStoreName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreMainPageActivity.this, StoreMsgActivity.class);
                intent.putExtra("store", store);
                jumpTo(intent, false);
            }
        });
    }


    @OnClick(R.id.fl_car)
    public void toCar() {
        jumpTo(ShoppingCarActivity.class, false);
    }

    @OnClick(R.id.fl_msg)
    public void toMsgCenter() {
        jumpTo(MessageCenterActivity.class, false);
    }

    @OnClick(R.id.tv_store_share)
    public void share() {
        //TODO 淘口令分享
      /*  ShareDialog dialog = new ShareDialog();
        dialog.show(getSupportFragmentManager(),"");*/
    }

    @OnClick(R.id.tv_product_classify)
    public void productClassify(){
        jumpTo(StoreProductsClassifyActivity.class,false);
    }

    @OnClick(R.id.tv_store_msg)
    public void storeMsg(){
        jumpTo(StoreMsgActivity.class,false);
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}