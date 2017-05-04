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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.MainProductDisplayAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.StoreBean;

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
    @BindView(R.id.activity_store_main_page)
    LinearLayout activityStoreMainPage;

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

    @Override
    public View getContentView() {
        return activityStoreMainPage;
    }

    private void initTablayout() {
        tabStoreMain.addTab(tabStoreMain.newTab().setText("综合"), true);
        tabStoreMain.addTab(tabStoreMain.newTab().setText("销量"));
        tabStoreMain.addTab(tabStoreMain.newTab().setText("新品"));
        tabStoreMain.setTabMode(TabLayout.MODE_FIXED);

    }

    private void setDatas() {
        //TODO 全局获取购物车和消息数量
        tvCarCount.setText(carCount + "");
        tvMsgCount.setText(msgCount + "");
        store = (StoreBean) getIntent().getSerializableExtra("store");
        if (store == null) {
            return;
        }
        Glide.with(this).load(store.getIconUrl()).placeholder(R.mipmap.android_quanzi).into(civShangpuItem);
        tvStoreName.setText(store.getName());
        tvStoreAddress.setText(store.getAdress());

        tvStoreSales.setText("销售总量" + store.getTotalScals());
        tvStoreGoods.setText("宝贝数" + store.getGoodsCount());
        //TODO 设置商品评分图片
        double stars = (store.getDescribeScore() + store.getQualityScore() + store.getPriceScore()) / 3;
        if(stars<=1.0){
           rbCommend.setImageResource(R.mipmap.dppj_dj1);
        }else if(stars>1.0 && stars<1.9){
            rbCommend.setImageResource(R.mipmap.lan_yiban);
        }else if(stars>=1.9 && stars<=2.4){
            rbCommend.setImageResource(R.mipmap.dppj_dj2);
        }else if(stars>2.4 && stars<2.9){
            rbCommend.setImageResource(R.mipmap.lan_erban);
        }else if(stars>=2.9 && stars<=3.4){
            rbCommend.setImageResource(R.mipmap.dppj_dj3);
        }else if(stars>3.4 && stars<=3.9){
            rbCommend.setImageResource(R.mipmap.lan_sanban);
        }else if(stars>3.9 && stars<=4.4){
            rbCommend.setImageResource(R.mipmap.dppj_dj4);
        }else if(stars>4.4 && stars<=4.9){
            rbCommend.setImageResource(R.mipmap.lan_siban);
        }else if(stars>4.9){
            rbCommend.setImageResource(R.mipmap.dppj_dj5);
        }

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
    public void productClassify() {
        jumpTo(StoreProductsClassifyActivity.class, false);
    }

    @OnClick(R.id.tv_store_msg)
    public void storeMsg() {
        jumpTo(StoreMsgActivity.class, false);
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
