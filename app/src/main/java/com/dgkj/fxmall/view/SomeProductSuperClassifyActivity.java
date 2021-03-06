package com.dgkj.fxmall.view;

import android.graphics.Color;
import android.os.Build;
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
import com.dgkj.fxmall.bean.SomeProductClassifyBean;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.fragment.SomeProductClassifyFragment;
import com.dgkj.fxmall.listener.OnGetMyDemandDataFinishedListener;
import com.dgkj.fxmall.listener.OnGetSubclassifyFinishedListener;
import com.dgkj.fxmall.listener.OnSearchProductsFinishedListener;
import com.dgkj.fxmall.utils.LoadProgressDialogUtil;
import com.dgkj.fxmall.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class SomeProductSuperClassifyActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.vp_product)
    ViewPager vpProduct;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_car)
    ImageView ivCar;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.activity_some_product_classify)
    LinearLayout activitySomeProductClassify;
    @BindView(R.id.iv_screening)
    ImageView ivScreening;
    @BindView(R.id.iv_go_top)
    ImageView ivGoTop;


    private String productType;
    private int superId;
    private String[] subClassify;
    private int[] subId;
    private int index = 1;
    private List<SomeProductClassifyBean> mainList = new ArrayList<>();
    private List<MainProductBean> subList = new ArrayList<>();
    private ArrayList<Fragment> fragments;
    private FXMallControl control = new FXMallControl() ;
    private OkHttpClient client = new OkHttpClient.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_some_product_classify);
        ButterKnife.bind(this);

        initTitle();
        getData(productType);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#62b1fe"));
        }
    }

    @Override
    public View getContentView() {
        return activitySomeProductClassify;
    }


    /**
     * 获取该商品类型的所有数据，以购物车实体类为原型SomeProductClassifyBean，店名为子类名称，
     * 子类数据实体类以购物车中商品实体类为原型MainProductBean
     *
     * @param productType
     */
    private void getData(String productType) {
        loadProgressDialogUtil.buildProgressDialog();

        control.getSubclassify(this, superId, client, new OnGetSubclassifyFinishedListener() {
            @Override
            public void onGetSubclassifyFinished(List<ProductClassifyBean> subList) {
                subClassify = new String[subList.size()];
                subId = new int[subList.size()];
                LogUtil.i("TAG","子分类个数==="+subList.size());
                for (int i = 0; i < subList.size(); i++) {
                    subClassify[i] = subList.get(i).getTaxon();
                    subId[i] = subList.get(i).getId();
                }
                final int[] count = {0};
                for (int j = 0; j < subClassify.length; j++) {

                    final SomeProductClassifyBean classifyBean = new SomeProductClassifyBean();
                    classifyBean.setType(subClassify[j]);
                    control.getProductsOfSubclassify(subId[j], "createTime", index, 20, client, new OnSearchProductsFinishedListener() {
                        @Override
                        public void onSearchProductsFinished(List<MainProductBean> mainProducts) {
                            count[0]++;
                            LogUtil.i("TAG","二级分类商品数量==="+mainProducts.size());
                            classifyBean.setSubList(mainProducts);
                            mainList.add(classifyBean);
                            LogUtil.i("TAG","正在获取第几个子分类的数据===="+count[0]);
                            if(count[0] == subClassify.length){
                                initTab(mainList);
                            }
                        }
                    });


                }

            }
        });




/*
        //  TEST TODO 数据获取完成后
        String[] type = new String[]{"上衣", "寸衫", "风衣", "T恤", "马甲", "丝巾", "披肩"};
        List<String> url = new ArrayList<>();
        url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492433529&di=de2494834a545a7e044f2cf696345ace&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.wmtuku.com%2Fd%2Ffile%2F2017-02-17%2F2858b31981db27e4af218207575658dc.jpg");
        url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492433529&di=de2494834a545a7e044f2cf696345ace&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.wmtuku.com%2Fd%2Ffile%2F2017-02-17%2F2858b31981db27e4af218207575658dc.jpg");
        url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492433529&di=de2494834a545a7e044f2cf696345ace&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.wmtuku.com%2Fd%2Ffile%2F2017-02-17%2F2858b31981db27e4af218207575658dc.jpg");
        url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492433529&di=de2494834a545a7e044f2cf696345ace&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.wmtuku.com%2Fd%2Ffile%2F2017-02-17%2F2858b31981db27e4af218207575658dc.jpg");
        url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492433529&di=de2494834a545a7e044f2cf696345ace&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.wmtuku.com%2Fd%2Ffile%2F2017-02-17%2F2858b31981db27e4af218207575658dc.jpg");
        url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492433529&di=de2494834a545a7e044f2cf696345ace&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.wmtuku.com%2Fd%2Ffile%2F2017-02-17%2F2858b31981db27e4af218207575658dc.jpg");
        url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492433529&di=de2494834a545a7e044f2cf696345ace&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.wmtuku.com%2Fd%2Ffile%2F2017-02-17%2F2858b31981db27e4af218207575658dc.jpg");
        for (int i = 0; i < 6; i++) {
            SomeProductClassifyBean classifyBean = new SomeProductClassifyBean();
            classifyBean.setType(type[i]);
            List<MainProductBean> list = new ArrayList<>();
            for (int j = 0; j < 15; j++) {
                MainProductBean productBean = new MainProductBean();
                productBean.setUrls(url);
                productBean.setDetialUrls(url);
                productBean.setUrl(url.get(0));
                productBean.setPrice(88);
                productBean.setExpress("韵达快递");
                productBean.setSales("465");
                productBean.setVipPrice(58);
                productBean.setAddress("广东深圳");
                productBean.setIntroduce("粉小萌，够猛，够萌，够亮，够酸，够辣，这酸辣爽");
                StoreBean storeBean = new StoreBean();
                storeBean.setStars(3);
                storeBean.setIconUrl("http://img2015.zdface.com/20170417/06bf77be0521dc47da46f596893b0dbf.jpg");
                storeBean.setName("粉小萌");
                storeBean.setQualityScore(4.2);
                storeBean.setDescribeScore(4.8);
                storeBean.setPriceScore(4.0);
                storeBean.setAdress("广东省深圳市龙岗区");
                storeBean.setGoodsCount(1000);
                storeBean.setTotalScals(300);
                storeBean.setCreateTime("2017-5-2");
                productBean.setStoreBean(storeBean);
                list.add(productBean);
            }
            classifyBean.setSubList(list);
            mainList.add(classifyBean);
        }
        initTab(mainList);*/
    }

    /**
     * 初始化导航栏，需要根据查询改类别商品分为多少种小类别，以各小类别的名称为导航栏标题
     * 如服装类分为上衣、裤子、秋装、春装等...
     */
    private void initTab(final List<SomeProductClassifyBean> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fragments = new ArrayList<>();
                for (SomeProductClassifyBean classifyBean : list) {
                    fragments.add(new SomeProductClassifyFragment(classifyBean.getSubList()));
                }
                vpProduct.setAdapter(new HomePageFragmentAdapter(getSupportFragmentManager(), fragments));
                vpProduct.setCurrentItem(0);
                vpProduct.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

                for (SomeProductClassifyBean classifyBean : list) {
                    tabLayout.addTab(tabLayout.newTab().setText(classifyBean.getType()), true);
                }
                tabLayout.setTabMode(TabLayout.MODE_FIXED);
                tabLayout.setupWithViewPager(vpProduct, true);
                for (int i = 0; i < list.size(); i++) {//与viewpager连用后续重新设置标题
                    tabLayout.getTabAt(i).setText(list.get(i).getType());
                }

                loadProgressDialogUtil.cancelProgressDialog();
            }
        });

    }

    private void initTitle() {
        productType = getIntent().getStringExtra("type");
        superId = getIntent().getIntExtra("classifyId",0);
        tvTitleCenter.setText(productType);
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
        SomeProductClassifyFragment fragment = (SomeProductClassifyFragment) fragments.get(vpProduct.getCurrentItem());
        fragment.goTop();
    }
}
