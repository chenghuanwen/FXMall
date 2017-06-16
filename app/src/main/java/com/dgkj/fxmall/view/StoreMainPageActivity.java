package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.MainProductDisplayAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnSearchProductsFinishedListener;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.view.myView.ShareInvitaCodeDialog;
import com.dgkj.fxmall.view.myView.ShareStoreInfoDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;

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
    @BindView(R.id.et_search_content)
    EditText etSearchContent;

    private StoreBean store;
    private List<MainProductBean> goods;
    private MainProductDisplayAdapter adapter;
    private String orderBy="createTime";
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private FXMallControl control = new FXMallControl();
    private int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_main_page);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initTablayout();
        setDatas();
        search();
        refresh();
    }

    private void refresh() {
        LogUtil.i("TAG","获取商店主页商品==========storeId=="+store.getId());
            control.getSearchProducts(this, null, orderBy,null,null,null,null,index, 20, store.getId(), client, new OnSearchProductsFinishedListener() {
                @Override
                public void onSearchProductsFinished(final List<MainProductBean> mainProducts) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addAll(mainProducts,true);
                        }
                    });
                }
            });
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
        // 全局获取购物车和消息数量
        if(MyApplication.shoppingCount == 0){
            tvCarCount.setVisibility(View.GONE);
        }else {
            tvCarCount.setVisibility(View.VISIBLE);
            tvCarCount.setText(MyApplication.shoppingCount+"");
        }

        if(MyApplication.msgCount == 0){
            tvMsgCount.setVisibility(View.GONE);
        }else {
            tvMsgCount.setVisibility(View.VISIBLE);
            tvMsgCount.setText(MyApplication.msgCount+"");
        }

        store = getIntent().getParcelableExtra("store");
        if (store == null) {
            return;
        }
        Glide.with(this).load(store.getIconUrl()).error(R.mipmap.sz_tx).into(civShangpuItem);
        tvStoreName.setText(store.getName());
        String adress = store.getAdress();
        tvStoreAddress.setText(adress.substring(0,adress.indexOf("市")+1));

        tvStoreSales.setText("销售总量：" + store.getTotalScals());
        tvStoreGoods.setText("宝贝数：" + store.getGoodsCount());
        //TODO 设置商品评分图片
       // double stars = (store.getDescribeScore() + store.getQualityScore() + store.getPriceScore()) / 3;
        double stars = store.getTotalScore();
        if (stars <= 1.0) {
            rbCommend.setImageResource(R.mipmap.dppj_dj1);
        } else if (stars > 1.0 && stars < 1.9) {
            rbCommend.setImageResource(R.mipmap.lan_yiban);
        } else if (stars >= 1.9 && stars <= 2.4) {
            rbCommend.setImageResource(R.mipmap.dppj_dj2);
        } else if (stars > 2.4 && stars < 2.9) {
            rbCommend.setImageResource(R.mipmap.lan_erban);
        } else if (stars >= 2.9 && stars <= 3.4) {
            rbCommend.setImageResource(R.mipmap.dppj_dj3);
        } else if (stars > 3.4 && stars <= 3.9) {
            rbCommend.setImageResource(R.mipmap.lan_sanban);
        } else if (stars > 3.9 && stars <= 4.4) {
            rbCommend.setImageResource(R.mipmap.dppj_dj4);
        } else if (stars > 4.4 && stars <= 4.9) {
            rbCommend.setImageResource(R.mipmap.lan_siban);
        } else if (stars > 4.9) {
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

        tvStoreName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreMainPageActivity.this, StoreMsgActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("store", store);
                intent.putExtras(bundle);
                jumpTo(intent, false);
            }
        });
    }


    /**
     * 点击键盘回车键进行搜索
     */
    public void search() {
        etSearchContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER && etSearchContent.getText().toString() != null) {
                    Intent intent = new Intent(StoreMainPageActivity.this, SearchContentActivity.class);
                    intent.putExtra("type","商品");
                    intent.putExtra("key", etSearchContent.getText().toString());
                    intent.putExtra("from","store");
                    intent.putExtra("storeId",store.getId());
                    jumpTo(intent, false);
                }
                return false;
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
        //TODO 店铺分享
        ShareStoreInfoDialog dialog;
        List<String> mainUrl = store.getMainUrls();
        if(mainUrl.size()>0){
            dialog = new ShareStoreInfoDialog(store.getName(), mainUrl.get(0));
        }else {
            dialog = new ShareStoreInfoDialog(store.getName(), store.getIconUrl());
        }
        dialog.show(getSupportFragmentManager(), "");
    }

    @OnClick(R.id.tv_product_classify)
    public void productClassify() {
        Intent intent = new Intent(this,StoreProductsClassifyActivity.class);
        intent.putExtra("storeId",store.getId());
        jumpTo(intent, false);
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
