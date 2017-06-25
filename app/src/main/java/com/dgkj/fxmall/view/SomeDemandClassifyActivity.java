package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.HomePageFragmentAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.MainDemandBean;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.ProductClassifyBean;
import com.dgkj.fxmall.bean.SomeDemandClassifyBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.fragment.SomeDemandClassifyFragment;
import com.dgkj.fxmall.listener.OnGetMyDemandDataFinishedListener;
import com.dgkj.fxmall.listener.OnGetSubclassifyFinishedListener;
import com.dgkj.fxmall.utils.LoadProgressDialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class SomeDemandClassifyActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.vp_product)
    ViewPager vpProduct;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_mine)
    ImageView ivMine;
    @BindView(R.id.iv_car)
    ImageView ivCar;
    @BindView(R.id.iv_top)
    ImageView ivTop;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.activity_some_product_classify)
    LinearLayout activitySomeProductClassify;


    private String productType;
    private List<SomeDemandClassifyBean> mainList = new ArrayList<>();
    private List<MainProductBean> subList = new ArrayList<>();
    private ArrayList<Fragment> fragments;
    private int superId;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private FXMallControl control = new FXMallControl();
    private String[] subClassify;
    private int[] subId;
    private int index = 1;
    private LoadProgressDialogUtil progressDialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_some_demand_classify);
        ButterKnife.bind(this);

        superId = getIntent().getIntExtra("superId", -1);
        progressDialogUtil = new LoadProgressDialogUtil(this);

        initTitle();
        getData();

    }

    @Override
    public View getContentView() {
        return activitySomeProductClassify;
    }


    /**
     * 获取该商品类型的所有数据，以购物车实体类为原型SomeProductClassifyBean，店名为子类名称，
     * 子类数据实体类以购物车中商品实体类为原型MainProductBean
     */
    private void getData() {
        //根据一级分类获取对应的二级分类
        final int[] index = {0};
        progressDialogUtil.buildProgressDialog();
        control.getSubclassify(this, superId, client, new OnGetSubclassifyFinishedListener() {
            @Override
            public void onGetSubclassifyFinished(List<ProductClassifyBean> subList) {
                subClassify = new String[subList.size()];
                subId = new int[subList.size()];
                for (int i = 0; i < subList.size(); i++) {
                    subClassify[i] = subList.get(i).getTaxon();
                    subId[i] = subList.get(i).getId();
                }
                for (int j = 0; j < subClassify.length; j++) {
                    index[0] = j;
                    final SomeDemandClassifyBean classifyBean = new SomeDemandClassifyBean();
                    classifyBean.setType(subClassify[j]);
                    control.getDemandByClassify(SomeDemandClassifyActivity.this, subId[j], index[0], 20, client, new OnGetMyDemandDataFinishedListener() {
                        @Override
                        public void onGetMyDemandDataFinished(List<MainDemandBean> demandList) {
                            classifyBean.setSubList(demandList);
                            mainList.add(classifyBean);
                            if(index[0]==subClassify.length-1){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initTab(mainList);
                                    }
                                });
                            }
                        }
                    });

                }


            }
        });

    }

    /**
     * 初始化导航栏，需要根据查询改类别商品分为多少种小类别，以各小类别的名称为导航栏标题
     * 如服装类分为上衣、裤子、秋装、春装等...
     */
    private void initTab(List<SomeDemandClassifyBean> list) {
        fragments = new ArrayList<>();
        for (SomeDemandClassifyBean classifyBean : list) {
            fragments.add(new SomeDemandClassifyFragment(classifyBean.getSubList()));
        }
        vpProduct.setAdapter(new HomePageFragmentAdapter(getSupportFragmentManager(), fragments));
        vpProduct.setCurrentItem(0);
        vpProduct.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        for (SomeDemandClassifyBean classifyBean : list) {
            tabLayout.addTab(tabLayout.newTab().setText(classifyBean.getType()), true);
        }
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(vpProduct, true);
        for (int i = 0; i < list.size(); i++) {//与viewpager连用后续重新设置标题
            tabLayout.getTabAt(i).setText(list.get(i).getType());
        }

        progressDialogUtil.cancelProgressDialog();
    }

    private void initTitle() {
        productType = getIntent().getStringExtra("type");
        tvTitleCenter.setText(productType);
    }


    @OnClick(R.id.iv_top)
    public void gotop() {
        SomeDemandClassifyFragment fragment = (SomeDemandClassifyFragment) fragments.get(vpProduct.getCurrentItem());
        fragment.goTop();
    }


    @OnClick(R.id.iv_car)
    public void screening() {
        startActivityForResult(new Intent(this, ScreeningProductActivity.class), 137);
    }


    @OnClick(R.id.iv_mine)
    public void publish() {
        jumpTo(PublishDemandActivity.class, true);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }
}
