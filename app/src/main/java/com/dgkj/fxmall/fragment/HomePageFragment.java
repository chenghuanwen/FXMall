package com.dgkj.fxmall.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.MainProductDisplayAdapter;
import com.dgkj.fxmall.adapter.ProductClassifyAdapter;
import com.dgkj.fxmall.bean.BannerBean;
import com.dgkj.fxmall.bean.ColorSizeBean;
import com.dgkj.fxmall.bean.HomePageRecommendBean;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.MainRecommendStoreBean;
import com.dgkj.fxmall.bean.ProductClassifyBean;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetBannerFinishedListener;
import com.dgkj.fxmall.listener.OnGetHomeRecommendFinishedListener;
import com.dgkj.fxmall.listener.OnGetMainRecommendStoreFinishedListener;
import com.dgkj.fxmall.listener.OnGetProductDetialFinishedListener;
import com.dgkj.fxmall.listener.OnGetSearchHotWordsFinishedListener;
import com.dgkj.fxmall.listener.OnGetStoreDetialFinishedListener;
import com.dgkj.fxmall.listener.OnSearchProductsFinishedListener;
import com.dgkj.fxmall.utils.BannerImageLoader;
import com.dgkj.fxmall.utils.LoadProgressDialogUtil;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.view.NewGoodsActivity;
import com.dgkj.fxmall.view.ProductDetialActivity;
import com.dgkj.fxmall.view.StoreMainPageActivity;
import com.dgkj.fxmall.view.myView.FullyLinearLayoutManager;
import com.dgkj.fxmall.view.myView.MyGridLayoutManager;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
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
    private List<HomePageRecommendBean> storeRecommends = new ArrayList<>();
    private List<HomePageRecommendBean> newGoodsRecommends = new ArrayList<>();
    private HomePageRecommendBean store, newGoods;
    private SharedPreferencesUnit sp ;
    private int index = 1;
    private  List<ProductClassifyBean> list = new ArrayList<>();
    private LoadProgressDialogUtil progressDialogUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = inflater.inflate(R.layout.layout_homepage_fragment, container, false);
        ButterKnife.bind(this, rootView);
        initview(rootView);

        sp = SharedPreferencesUnit.getInstance(getContext());
        progressDialogUtil = new LoadProgressDialogUtil(getContext());
        progressDialogUtil.buildProgressDialog();

        initBanner();
        //TEST
        testRecyclerview();
        //获取所有推荐
        getRecommendData();
        //获取首页展示商品
        getDisplayProducts();

        return rootView;
    }



    //获取首页展示商品
    private void getDisplayProducts() {

        if(TextUtils.isEmpty(sp.get("token"))){//未登录,展示热门搜索商品
            control.getSearchHotwords(1, 0, new OnGetSearchHotWordsFinishedListener() {
                @Override
                public void onGetSearchHotWordsFinishedListener(List<String> words) {
                    control.getSearchProducts(getActivity(), words.get(0),"createTime",null,null,null,null,index, 20, 0,okHttpClient, new OnSearchProductsFinishedListener() {
                        @Override
                        public void onSearchProductsFinished(final List<MainProductBean> products) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //TODO 写一个通用的商品实体类
                                    productDisplayAdapter.addAll(products,true);
                                    progressDialogUtil.cancelProgressDialog();
                                }
                            });
                        }
                    });
                }
            });
        }else {
            control.getHomePageProductsDisplay(sp.get("token"), index, 20, okHttpClient, new OnSearchProductsFinishedListener() {
                @Override
                public void onSearchProductsFinished(final List<MainProductBean> mainProducts) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            productDisplayAdapter.addAll(mainProducts,true);
                        }
                    });
                }
            });
        }
    }


    //获取所有推荐数据
    private void getRecommendData() {
        control.getHomePageAllRecommender(0, new OnGetHomeRecommendFinishedListener() {
            @Override
            public void onGetHomeRecommendFinishedListener(List<HomePageRecommendBean> recommendList) {
                for (HomePageRecommendBean homePageRecommendBean : recommendList) {
                    String position = homePageRecommendBean.getPosition();
                    switch (position) {
                        case "home_RStore_Left":
                            store = homePageRecommendBean;
                            break;
                        case "home_RStore_Right":
                            storeRecommends.add(homePageRecommendBean);
                            break;
                        case "home_NCommodity_Top":
                            newGoods = homePageRecommendBean;
                            break;
                        case "home_NCommodity_Down":
                            newGoodsRecommends.add(homePageRecommendBean);
                            break;
                        case "home_Category"://
                            ProductClassifyBean classifyBean = new ProductClassifyBean();
                            classifyBean.setTaxon(homePageRecommendBean.getTitel());
                            classifyBean.setUrl(homePageRecommendBean.getUrl());
                            classifyBean.setId(homePageRecommendBean.getLink());
                            list.add(classifyBean);
                            break;
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setData();
                    }
                });

            }
        });
    }

    private void setData() {
        Glide.with(getContext()).load(store.getUrl()).error(R.mipmap.android_quanzi).into(ivStore1);
        Glide.with(getContext()).load(storeRecommends.get(0).getUrl()).error(R.mipmap.android_quanzi).into(ivStore2);
        Glide.with(getContext()).load(storeRecommends.get(1).getUrl()).error(R.mipmap.android_quanzi).into(ivStore3);
        Glide.with(getContext()).load(newGoods.getUrl()).error(R.mipmap.android_quanzi).into(newGoods1);
        Glide.with(getContext()).load(newGoodsRecommends.get(0).getUrl()).error(R.mipmap.android_quanzi).into(newGoods2);
        Glide.with(getContext()).load(newGoodsRecommends.get(1).getUrl()).error(R.mipmap.android_quanzi).into(newGoods3);
        Glide.with(getContext()).load(newGoodsRecommends.get(2).getUrl()).error(R.mipmap.android_quanzi).into(newGoods4);
        Glide.with(getContext()).load(newGoodsRecommends.get(3).getUrl()).error(R.mipmap.android_quanzi).into(newGoods5);
        tvStore1Name.setText(store.getTitel());
        tvStore1Describe.setText(store.getIntroduce());
        tvStore2Name.setText(storeRecommends.get(0).getTitel());
        tvStore2Describe.setText(storeRecommends.get(0).getIntroduce());
        tvStore3Name.setText(storeRecommends.get(1).getTitel());
        tvStore3Describe.setText(storeRecommends.get(1).getIntroduce());

        classifyAdapter.addAll(list, true);

    }


    //TEST
    private void testRecyclerview() {
        List<ProductClassifyBean> list1 = new ArrayList<>();
        List<MainProductBean> list = new ArrayList<>();
        List<String> url = new ArrayList<>();
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        for (int i = 0; i < 5; i++) {
            MainProductBean productBean = new MainProductBean();
            productBean.setUrls(url);
            productBean.setDetialUrls(url);
            productBean.setPrice(35);
            productBean.setIntroduce("粉小萌正酣上线，绝对独一无二，吃货的福利");
            productBean.setSales("10000");
            productBean.setAddress("深圳");
            productBean.setExpress("包邮");
            productBean.setVipPrice(25);
            productBean.setSkuId(i + 1);
            productBean.setCount(3);
            productBean.setId(i + 2);
            productBean.setColor("绿色-M");
            productBean.setInventory(100);
            productBean.setBrokerage(20);
            StoreBean storeBean = new StoreBean();
            storeBean.setName("粉小萌");
            storeBean.setAdress("广东省深圳市龙岗区");
            storeBean.setIconUrl("http://img2015.zdface.com/20170417/06bf77be0521dc47da46f596893b0dbf.jpg");
            storeBean.setCreateTime("2017-5-2");
            storeBean.setStars(3);
            storeBean.setTotalScals(300);
            storeBean.setGoodsCount(1000);
            storeBean.setId(i + 1);
            storeBean.setDescribeScore(4.8);
            storeBean.setPriceScore(4.0);
            storeBean.setQualityScore(4.2);
            storeBean.setTotalScore(4.5);
            storeBean.setLicence("http://img2015.zdface.com/20170417/06bf77be0521dc47da46f596893b0dbf.jpg");
            storeBean.setMainUrls(url);
            storeBean.setKeeper("小成成");
            storeBean.setPhone("15641651432");
            storeBean.setRecommender("小成成");
            productBean.setStoreBean(storeBean);
            productBean.setUrl(url.get(0));
            productBean.setDescribeScore(4);
            productBean.setPriceScore(4);
            productBean.setQualityScore(4);
            productBean.setTotalScore(4);
            if(i<2){
                productBean.setDeliverable(true);
            }else {
                productBean.setDeliverable(false);
            }
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
        Glide.with(getContext()).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493974150352&di=50d4277e4cbbb74f24803b0cc967c70a&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201608%2F02%2F20160802172340_W528L.jpeg").into(newGoods1);
        Glide.with(getContext()).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493974150344&di=0ba39eb86045a759464936ad002dec0c&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201512%2F09%2F20151209225838_2G3Zc.thumb.700_0.jpeg").into(newGoods2);
        Glide.with(getContext()).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493974150340&di=6c86ce998672986123e8df590335555a&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201604%2F26%2F20160426184930_ivKdF.jpeg").into(newGoods3);
        Glide.with(getContext()).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493974150336&di=48182bee774f3d62e89100a7f73fbea2&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201607%2F23%2F20160723104550_hFtTn.thumb.700_0.jpeg").into(newGoods4);
        Glide.with(getContext()).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493974260222&di=7e54dac3ff5cfff89438fa1da97c2242&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201510%2F27%2F20151027171037_3tiQZ.jpeg").into(newGoods5);


    }

/*

    @OnClick(R.id.iv_store1)
    public void toStore1() {
        getStoreDetial(store.getLink());
    }

    @OnClick(R.id.iv_store2)
    public void toStore2() {
        getStoreDetial(storeRecommends.get(0).getLink());
    }

    @OnClick(R.id.iv_store3)
    public void toStore3() {
        getStoreDetial(storeRecommends.get(1).getLink());
    }

    @OnClick(R.id.new_goods1)
    public void toNew1(){
        getProductDetial(newGoods.getLink());
    }

    @OnClick(R.id.new_goods2)
    public void toNew2(){
        getProductDetial(newGoodsRecommends.get(0).getLink());
    }

    @OnClick(R.id.new_goods3)
    public void toNew3(){
        getProductDetial(newGoodsRecommends.get(1).getLink());
    }

    @OnClick(R.id.new_goods4)
    public void toNew4(){
        getProductDetial(newGoodsRecommends.get(2).getLink());
    }

    @OnClick(R.id.new_goods5)
    public void toNew5(){
        getProductDetial(newGoodsRecommends.get(3).getLink());
    }

*/

    //TEST
    @OnClick(R.id.rl_store)
    public void toStore(){
        StoreBean storeBean = new StoreBean();
        storeBean.setName("粉小萌");
        storeBean.setAdress("广东省深圳市龙岗区");
        storeBean.setIconUrl("http://img2015.zdface.com/20170417/06bf77be0521dc47da46f596893b0dbf.jpg");
        storeBean.setCreateTime("2017-5-2");
        storeBean.setStars(3);
        storeBean.setTotalScals(300);
        storeBean.setGoodsCount(1000);
        storeBean.setId(5);
        storeBean.setDescribeScore(4.8);
        storeBean.setPriceScore(4.0);
        storeBean.setQualityScore(4.2);
        storeBean.setTotalScore(4.5);
        storeBean.setLicence("http://img2015.zdface.com/20170417/06bf77be0521dc47da46f596893b0dbf.jpg");
        storeBean.setMainUrls(new ArrayList<String>());
        storeBean.setKeeper("小成成");
        storeBean.setPhone("15641651432");
        storeBean.setRecommender("小成成");

        Intent intent = new Intent(getContext(), StoreMainPageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("store", storeBean);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }

    //TEST
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
        productBean.setUrls(url);
        productBean.setDetialUrls(url);
        productBean.setPrice(35);
        productBean.setIntroduce("粉小萌正酣上线，绝对独一无二，吃货的福利");
        productBean.setSales("10000");
        productBean.setAddress("深圳");
        productBean.setExpress("包邮");
        productBean.setVipPrice(25);
        productBean.setSkuId(1);
        productBean.setCount(3);
        productBean.setId( 2);
        productBean.setColor("绿色-M");
        productBean.setInventory(100);
        productBean.setBrokerage(20);
        StoreBean storeBean = new StoreBean();
        storeBean.setName("粉小萌");
        storeBean.setAdress("广东省深圳市龙岗区");
        storeBean.setIconUrl("http://img2015.zdface.com/20170417/06bf77be0521dc47da46f596893b0dbf.jpg");
        storeBean.setCreateTime("2017-5-2");
        storeBean.setStars(3);
        storeBean.setTotalScals(300);
        storeBean.setGoodsCount(1000);
        storeBean.setId(5);
        storeBean.setDescribeScore(4.8);
        storeBean.setPriceScore(4.0);
        storeBean.setQualityScore(4.2);
        storeBean.setTotalScore(4.5);
        storeBean.setLicence("http://img2015.zdface.com/20170417/06bf77be0521dc47da46f596893b0dbf.jpg");
        storeBean.setMainUrls(url);
        storeBean.setKeeper("小成成");
        storeBean.setPhone("15641651432");
        storeBean.setRecommender("小成成");
        productBean.setStoreBean(storeBean);
        productBean.setUrl(url.get(0));
        productBean.setDescribeScore(4);
        productBean.setPriceScore(4);
        productBean.setQualityScore(4);
        productBean.setTotalScore(4);
        intent.putExtra("product", productBean);
        jumpTo(intent, false);
    }


    //TODO 全部新品
    @OnClick({R.id.tv_newgoods_all, R.id.ll_new_goods})
    public void allProductRecommend() {
        jumpTo(NewGoodsActivity.class, false);
    }


    //初始化banner图
    private void initBanner() {
        bannerDatas = new ArrayList<>();
      /*  control.getBanners(okHttpClient, new OnGetBannerFinishedListener() {
            @Override
            public void onGetBannerFinishedListener(final List<BannerBean> banners) {
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
                        BannerBean bannerBean = banners.get(position);
                        if (bannerBean.getType() == 0) {//商品
                            getProductDetial(bannerBean.getProductId());
                        } else {//商铺
                            getStoreDetial(bannerBean.getProductId());
                        }
                    }
                });
            }
        });*/


        //TEST
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
                    case 1:
                    case 2:
                        Intent intent = new Intent(getContext(), ProductDetialActivity.class);
                        //TODO 需将商铺推荐数据实体类集合传递下去
                        MainProductBean productBean = new MainProductBean();
                        List<String> url = new ArrayList<>();
                        url.add("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
                        url.add("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
                        url.add("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
                        url.add("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
                        url.add("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
                        productBean.setUrls(url);
                        productBean.setDetialUrls(url);
                        productBean.setPrice(35);
                        productBean.setIntroduce("粉小萌正酣上线，绝对独一无二，吃货的福利");
                        productBean.setSales("10000");
                        productBean.setAddress("深圳");
                        productBean.setExpress("包邮");
                        productBean.setVipPrice(25);
                        productBean.setSkuId(1);
                        productBean.setCount(3);
                        productBean.setId( 2);
                        productBean.setColor("绿色-M");
                        productBean.setInventory(100);
                        productBean.setBrokerage(20);
                        StoreBean storeBean = new StoreBean();
                        storeBean.setName("粉小萌");
                        storeBean.setAdress("广东省深圳市龙岗区");
                        storeBean.setIconUrl("http://img2015.zdface.com/20170417/06bf77be0521dc47da46f596893b0dbf.jpg");
                        storeBean.setCreateTime("2017-5-2");
                        storeBean.setStars(3);
                        storeBean.setTotalScals(300);
                        storeBean.setGoodsCount(1000);
                        storeBean.setId(5);
                        storeBean.setDescribeScore(4.8);
                        storeBean.setPriceScore(4.0);
                        storeBean.setQualityScore(4.2);
                        storeBean.setTotalScore(4.5);
                        storeBean.setLicence("http://img2015.zdface.com/20170417/06bf77be0521dc47da46f596893b0dbf.jpg");
                        storeBean.setMainUrls(url);
                        storeBean.setKeeper("小成成");
                        storeBean.setPhone("15641651432");
                        storeBean.setRecommender("小成成");
                        productBean.setStoreBean(storeBean);
                        productBean.setUrl(url.get(0));
                        productBean.setDescribeScore(4);
                        productBean.setPriceScore(4);
                        productBean.setQualityScore(4);
                        productBean.setTotalScore(4);
                        intent.putExtra("product", productBean);
                        jumpTo(intent, false);
                        break;
                }
            }
        });
    }

    /**
     * 根据商铺id获取商品详情
     *
     * @param productId
     */
    private void getProductDetial(int productId) {
        control.getProductDetialById(productId, new OnGetProductDetialFinishedListener() {
            @Override
            public void onGetProductDetialFinishedListener(MainProductBean product) {
                //跳转到商品详情
                Intent intent = new Intent(getContext(), ProductDetialActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("product", product);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });

    }


    /**
     * 根据商品id获取商pu详情
     *
     * @param productId
     */
    private void getStoreDetial(int productId) {
        control.getStoreDetialByid(productId, new OnGetStoreDetialFinishedListener() {
            @Override
            public void onGetStoreDetialFinishedListener(StoreBean store) {
                //TODO 跳转到商铺详情
                Intent intent = new Intent(getContext(), StoreMainPageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("store", store);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
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
