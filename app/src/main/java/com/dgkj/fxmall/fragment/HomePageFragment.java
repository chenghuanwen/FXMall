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

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.MainProductDisplayAdapter;
import com.dgkj.fxmall.adapter.ProductClassifyAdapter;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.ProductClassifyBean;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.BannerImageLoader;
import com.dgkj.fxmall.view.ProductDetialActivity;
import com.dgkj.fxmall.view.StoreMainPageActivity;
import com.dgkj.fxmall.view.myView.FullyLinearLayoutManager;
import com.dgkj.fxmall.view.NewGoodsActivity;
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

    private ArrayList<String> bannerDatas;
    private OkHttpClient okHttpClient;
    private List<MainProductBean> mainProducts;
    private List<ProductClassifyBean> classifys;
    private MainProductDisplayAdapter productDisplayAdapter;
    private ProductClassifyAdapter classifyAdapter;

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
            productBean.setPrice("35");
            productBean.setVipPrice("25");
            productBean.setSales("10000");
            productBean.setUrl(url);
            productBean.setExpress("韵达快递");
            list.add(productBean);
        }
        productDisplayAdapter.addAll(list,true);

        for (int i = 0; i < 8; i++) {
            ProductClassifyBean bean = new ProductClassifyBean();
            bean.setUrl("http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg");
            bean.setTaxon("衣服");
            list1.add(bean);
        }
        classifyAdapter.addAll(list1,true);

    }

    private void initview(View rootView) {
        okHttpClient = new OkHttpClient.Builder().build();
        mainProducts = new ArrayList<>();
        classifys = new ArrayList<>();
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvRecommendGoods.setLayoutManager(linearLayoutManager);
        classifyAdapter = new ProductClassifyAdapter(getContext(),R.layout.item_product_classify,classifys,"product");
        rvRecommendGoods.setAdapter(classifyAdapter);
        //  rvHomeDisplay.setLayoutManager(new FullyLinearLayoutManager(getContext()));

        productDisplayAdapter = new MainProductDisplayAdapter(getContext(),R.layout.item_main_product,mainProducts,"product");

        MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(getContext(), 2);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        rvHomeDisplay.setLayoutManager(gridLayoutManager);
        rvHomeDisplay.setHasFixedSize(true);
        rvHomeDisplay.setAdapter(productDisplayAdapter);

       /* getBannerData();
        getStoreRecommend();
        getTodayRecommend();
        getNewgoodsShelves();
        getProductsRecommend();
        getMainProductDisplay();*/

    }


    /**
     * \获取banner轮播推荐图片链接
     */
    private void getBannerData() {
        okHttpClient.newCall(new Request.Builder().url(FXConst.GET_BANNER_URL).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

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
        okHttpClient.newCall(new Request.Builder().url(FXConst.TODAY_RECOMMEND_URL).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
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
    public void storeRecommend(){//跳转到商铺推荐界面
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
        intent.putExtra("store",storeBean);
        jumpTo(intent,false);

    }

    @OnClick(R.id.ll_today_recommend)
    public void todayRecommend(){
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
        intent.putExtra("store",storeBean);
        jumpTo(intent,false);
    }

    @OnClick({R.id.new_goods1,R.id.new_goods2,R.id.new_goods3,R.id.new_goods4,R.id.new_goods5})
    public void newGoods(){
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
        productBean.setVipPrice("58");
        productBean.setSales("1536");
        productBean.setExpress("韵达快递");
        productBean.setPrice("88");
        productBean.setUrl(url);
        productBean.setStoreName("我是你的菜");
        intent.putExtra("product",productBean);
        jumpTo(intent,false);
    }

    @OnClick({R.id.tv_newgoods_all,R.id.ll_new_goods})
    public void allProductRecommend(){
        jumpTo(NewGoodsActivity.class,false);
    }


    private void initBanner() {
        bannerDatas = new ArrayList<>();
        bannerDatas.add("http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg");
        bannerDatas.add("http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg");
        bannerDatas.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        banner.setImageLoader(new BannerImageLoader());
        banner.setImages(bannerDatas);
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {//根据不同的推荐内容跳转到不同的详情界面
            @Override
            public void OnBannerClick(int position) {
                switch (position){
                    case 0:
                        jumpTo(ProductDetialActivity.class,false);
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
