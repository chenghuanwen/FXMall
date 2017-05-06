package com.dgkj.fxmall.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.MainProductDisplayAdapter;
import com.dgkj.fxmall.adapter.ProductClassifyAdapter;
import com.dgkj.fxmall.bean.BannerBean;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.MainRecommendStoreBean;
import com.dgkj.fxmall.bean.ProductClassifyBean;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetBannerFinishedListener;
import com.dgkj.fxmall.listener.OnGetMainRecommendStoreFinishedListener;
import com.dgkj.fxmall.utils.BannerImageLoader;
import com.dgkj.fxmall.view.NewGoodsActivity;
import com.dgkj.fxmall.view.ProductDetialActivity;
import com.dgkj.fxmall.view.StoreMainPageActivity;
import com.dgkj.fxmall.view.myView.FullyLinearLayoutManager;
import com.dgkj.fxmall.view.myView.MyGridLayoutManager;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomePageFragment extends Fragment {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.store1)
    RelativeLayout store1;
    @BindView(R.id.store2)
    RelativeLayout store2;
    @BindView(R.id.store3)
    RelativeLayout store3;
    @BindView(R.id.iv_recommend)
    ImageView ivRecommend;
    @BindView(R.id.tv_recommend_title)
    TextView tvRecommendTitle;
    @BindView(R.id.tv_newgoods_all)
    TextView tvNewgoodsAll;
    @BindView(R.id.new_goods1)
    ImageView newGoods1;
    @BindView(R.id.new_goods2)
    ImageView newGoods2;
    @BindView(R.id.new_goods3)
    ImageView newGoods3;
    @BindView(R.id.new_goods4)
    ImageView newGoods4;
    @BindView(R.id.new_goods5)
    ImageView newGoods5;
    @BindView(R.id.rv_recommend_goods)
    RecyclerView rvRecommendGoods;
    @BindView(R.id.rv_home_display)
    RecyclerView rvHomeDisplay;
    @BindView(R.id.content_home_page)
    LinearLayout contentHomePage;
    @BindView(R.id.rl_store)
    PercentRelativeLayout rlStore;
    @BindView(R.id.ll_today_recommend)
    LinearLayout llTodayRecommend;
    @BindView(R.id.ll_new_goods)
    RelativeLayout llNewGoods;
    @BindView(R.id.tv_store1_name)
    TextView tvStore1Name;
    @BindView(R.id.tv_store1_describe)
    TextView tvStore1Describe;
    @BindView(R.id.iv_store1)
    ImageView ivStore1;
    @BindView(R.id.tv_store2_name)
    TextView tvStore2Name;
    @BindView(R.id.tv_store2_describe)
    TextView tvStore2Describe;
    @BindView(R.id.iv_store2)
    ImageView ivStore2;
    @BindView(R.id.tv_store3_name)
    TextView tvStore3Name;
    @BindView(R.id.tv_store3_describe)
    TextView tvStore3Describe;
    @BindView(R.id.iv_store3)
    ImageView ivStore3;
    @BindView(R.id.tv_ll)
    TextView tvLl;

    private ArrayList<String> bannerDatas;
    private OkHttpClient okHttpClient;
    private List<MainProductBean> mainProducts;
    private List<ProductClassifyBean> classifys;
    private MainProductDisplayAdapter productDisplayAdapter;
    private ProductClassifyAdapter classifyAdapter;
    private FXMallControl control = new FXMallControl();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = inflater.inflate(R.layout.layout_homepage_fragment, container, false);
        ButterKnife.bind(this, rootView);
        initview(rootView);
        initBanner();
        testRecyclerview();
        return rootView;
    }

    private void testRecyclerview() {
        List<MainProductBean> list = new ArrayList<>();
        List<ProductClassifyBean> list1 = new ArrayList<>();
        List<String> url = new ArrayList<>();
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        for (int i = 0; i < 5; i++) {
            MainProductBean productBean = new MainProductBean();
            productBean.setAddress("深圳");
            productBean.setIntroduce("粉小萌正酣上线，绝对独一无二，吃货的福利");
            productBean.setPrice(35);
            productBean.setVipPrice(25);
            productBean.setSales("10000");
            productBean.setUrls(url);
            productBean.setUrl(url.get(0));
            productBean.setDetialUrls(url);
            productBean.setExpress("韵达快递");
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
        productDisplayAdapter.addAll(list, true);

        for (int i = 0; i < 8; i++) {
            ProductClassifyBean bean = new ProductClassifyBean();
            bean.setUrl("http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg");
            bean.setTaxon("衣服");
            list1.add(bean);
        }
        classifyAdapter.addAll(list1, true);

    }

    private void initview(View rootView) {
        okHttpClient = new OkHttpClient.Builder().build();
        mainProducts = new ArrayList<>();
        classifys = new ArrayList<>();
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvRecommendGoods.setLayoutManager(linearLayoutManager);
        classifyAdapter = new ProductClassifyAdapter(getContext(), R.layout.item_product_classify, classifys, "product");
        rvRecommendGoods.setAdapter(classifyAdapter);
        //  rvHomeDisplay.setLayoutManager(new FullyLinearLayoutManager(getContext()));

        productDisplayAdapter = new MainProductDisplayAdapter(getContext(), R.layout.item_main_product, mainProducts, "product");

        MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(getContext(), 2);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        rvHomeDisplay.setLayoutManager(gridLayoutManager);
        rvHomeDisplay.setHasFixedSize(true);
        rvHomeDisplay.setAdapter(productDisplayAdapter);

        //TEST
        Glide.with(getContext()).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493973689173&di=e3c6c0aac909543ffe89fb9b67f917d7&imgtype=0&src=http%3A%2F%2Fzkres2.myzaker.com%2F201703%2F58de50e11bc8e02d3400005c_640.jpg").into(ivStore1);
        Glide.with(getContext()).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493973689155&di=2a9cccc3be8dc8477b0a02cd825ab3c3&imgtype=0&src=http%3A%2F%2Fwww.hxw163.com%2Fuploadfile%2F2017%2F0325%2F20170325110600403.jpg").into(ivStore2);
        Glide.with(getContext()).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493973689152&di=2c76f93145aa678f6c8e10ba452fb0a8&imgtype=0&src=http%3A%2F%2Fzkres2.myzaker.com%2F201703%2F58de0c3ca07aec595d04543c_640.jpg").into(ivStore3);
        Glide.with(getContext()).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493974097848&di=e03d075d5270399abf3017d5f28ded3a&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170319%2F1572047f77034527ab1242ae292efc41_th.jpeg").into(ivRecommend);
        Glide.with(getContext()).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493974150352&di=50d4277e4cbbb74f24803b0cc967c70a&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201608%2F02%2F20160802172340_W528L.jpeg").into(newGoods1);
        Glide.with(getContext()).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493974150344&di=0ba39eb86045a759464936ad002dec0c&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201512%2F09%2F20151209225838_2G3Zc.thumb.700_0.jpeg").into(newGoods2);
        Glide.with(getContext()).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493974150340&di=6c86ce998672986123e8df590335555a&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201604%2F26%2F20160426184930_ivKdF.jpeg").into(newGoods3);
        Glide.with(getContext()).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493974150336&di=48182bee774f3d62e89100a7f73fbea2&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201607%2F23%2F20160723104550_hFtTn.thumb.700_0.jpeg").into(newGoods4);
        Glide.with(getContext()).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493974260222&di=7e54dac3ff5cfff89438fa1da97c2242&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201510%2F27%2F20151027171037_3tiQZ.jpeg").into(newGoods5);

       /* getBannerData();
        getStoreRecommend();
        getTodayRecommend();
        getNewgoodsShelves();
        getProductsRecommend();
        getMainProductDisplay();*/

    }


    /**
     * 获取推荐商家数据
     */
    private void getStoreRecommend() {
        okHttpClient.newCall(new Request.Builder().url(FXConst.RECOMMEND_STORE_URL).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    /**
     * 获取今日推荐数据
     */
    private void getTodayRecommend() {

    }

    /**
     * 获取新品上架数据
     */
    private void getNewgoodsShelves() {
        okHttpClient.newCall(new Request.Builder().url(FXConst.NEWGOODS_SHELVES_URL).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    /**
     * 获取商品推荐
     */
    private void getProductsRecommend() {
        okHttpClient.newCall(new Request.Builder().url(FXConst.PRODUCTS_RECOMMEND_URL).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    /**
     * 获取主页商品展示列表数据
     */
    private void getMainProductDisplay() {
        okHttpClient.newCall(new Request.Builder().url(FXConst.HOMEPAGE_PRODUCTS_URL).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }


    @OnClick(R.id.rl_store)
    public void storeRecommend() {//跳转到商铺推荐界面
        control.getMainRecommendStores(okHttpClient, new OnGetMainRecommendStoreFinishedListener() {
            @Override
            public void onGetMainRecommendStoreFinishedListener(List<MainRecommendStoreBean> recommendList) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
        Intent intent = new Intent(getContext(), StoreMainPageActivity.class);
        //TODO 需将商铺推荐数据实体类集合传递下去
        StoreBean storeBean = new StoreBean();
        storeBean.setTotalScals(100);
        storeBean.setStars(3);
        storeBean.setQualityScore(3.5);
        storeBean.setPriceScore(5.0);
        storeBean.setDescribeScore(5.3);
        storeBean.setAdress("广东深圳");
        storeBean.setCreateTime("2017-05-01");
        storeBean.setGoodsCount(23213);
        storeBean.setIconUrl("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
        intent.putExtra("store", storeBean);
        jumpTo(intent, false);

    }

    @OnClick(R.id.ll_today_recommend)
    public void todayRecommend() {
        Intent intent = new Intent(getContext(), StoreMainPageActivity.class);
        //TODO 需将商铺推荐数据实体类集合传递下去,需要判断推荐的是商品还是店铺
        StoreBean storeBean = new StoreBean();
        storeBean.setTotalScals(100);
        storeBean.setStars(3);
        storeBean.setQualityScore(3.5);
        storeBean.setPriceScore(5.0);
        storeBean.setDescribeScore(5.3);
        storeBean.setAdress("广东深圳");
        storeBean.setCreateTime("2017-05-01");
        storeBean.setGoodsCount(23213);
        storeBean.setIconUrl("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
        intent.putExtra("store", storeBean);
        jumpTo(intent, false);
    }

    @OnClick({R.id.new_goods1, R.id.new_goods2, R.id.new_goods3, R.id.new_goods4, R.id.new_goods5})
    public void newGoods() {
        Intent intent = new Intent(getContext(), ProductDetialActivity.class);
        //TODO 需将商铺推荐数据实体类集合传递下去
        MainProductBean productBean = new MainProductBean();
        List<String> url = new ArrayList<>();
        url.add("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
        url.add("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
        url.add("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
        url.add("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
        url.add("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
        productBean.setIntroduce("粉小萌来啦，这酸爽，绝了，88一箱全国包邮");
        productBean.setAddress("广东深圳");
        productBean.setVipPrice(58);
        productBean.setSales("1536");
        productBean.setExpress("韵达快递");
        productBean.setPrice(88);
        productBean.setUrls(url);
        productBean.setUrl(url.get(0));
        productBean.getStoreBean().setName("我是你的菜");
        productBean.setDetialUrls(url);
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
        intent.putExtra("product", productBean);
        jumpTo(intent, false);
    }

    @OnClick({R.id.tv_newgoods_all, R.id.ll_new_goods})
    public void allProductRecommend() {
        jumpTo(NewGoodsActivity.class, false);
    }


    private void initBanner() {
        bannerDatas = new ArrayList<>();
        control.getBanners(okHttpClient, new OnGetBannerFinishedListener() {
            @Override
            public void onGetBannerFinishedListener(List<BannerBean> banners) {
                for (BannerBean bannerBean : banners) {
                    bannerDatas.add(bannerBean.getUrl());
                }
                banner.setImageLoader(new BannerImageLoader());
                banner.setImages(bannerDatas);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        banner.start();
                    }
                });
                banner.setOnBannerListener(new OnBannerListener() {//根据不同的推荐内容跳转到不同的详情界面
                    @Override
                    public void OnBannerClick(int position) {
                        switch (position) {
                            case 0:
                                jumpTo(ProductDetialActivity.class, false);
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                        }
                    }
                });
            }
        });

        bannerDatas.add("http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg");
        bannerDatas.add("http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg");
        bannerDatas.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        banner.setImageLoader(new BannerImageLoader());
        banner.setImages(bannerDatas);
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {//根据不同的推荐内容跳转到不同的详情界面
            @Override
            public void OnBannerClick(int position) {
                switch (position) {
                    case 0:
                        jumpTo(ProductDetialActivity.class, false);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });
    }


    public void jumpTo(Class<?> clazz, boolean isFinish) {
        Intent intent = new Intent(getContext(), clazz);
        startActivity(intent);
        if (isFinish) {
            getActivity().finish();
        }
    }

    //界面跳转时，需要Intent携带参数
    public void jumpTo(Intent intent, boolean isFinish) {
        startActivity(intent);
        if (isFinish) {
            getActivity().finish();
        }
    }


}
