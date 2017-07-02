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
import android.widget.Toast;

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
import com.dgkj.fxmall.listener.LoadMoreListener;
import com.dgkj.fxmall.listener.OnGetBannerFinishedListener;
import com.dgkj.fxmall.listener.OnGetHomeRecommendFinishedListener;
import com.dgkj.fxmall.listener.OnGetMainRecommendStoreFinishedListener;
import com.dgkj.fxmall.listener.OnGetProductDetialFinishedListener;
import com.dgkj.fxmall.listener.OnGetSearchHotWordsFinishedListener;
import com.dgkj.fxmall.listener.OnGetStoreDetialFinishedListener;
import com.dgkj.fxmall.listener.OnSearchProductsFinishedListener;
import com.dgkj.fxmall.utils.BannerImageLoader;
import com.dgkj.fxmall.utils.LoadProgressDialogUtil;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.view.NewGoodsActivity;
import com.dgkj.fxmall.view.ProductDetialActivity;
import com.dgkj.fxmall.view.StoreMainPageActivity;
import com.dgkj.fxmall.view.myView.FullyLinearLayoutManager;
import com.dgkj.fxmall.view.myView.ItemOffsetDecoration;
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
      //  testRecyclerview();
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
                    LogUtil.i("TAG","搜索热词==="+words.get(0));
                    //TODO 搜索热词无数据
                    control.getSearchProducts(getActivity(), words.get(2),"createTime",null,null,null,null,index, 20, 0,okHttpClient, new OnSearchProductsFinishedListener() {
                        @Override
                        public void onSearchProductsFinished(final List<MainProductBean> products) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //TODO 写一个通用的商品实体类
                                    progressDialogUtil.cancelProgressDialog();
                                    LogUtil.i("TAG","首页商品展示个数==="+products.size());

                                    if(products.size()==0){return;}

                                    ArrayList<MainProductBean> list = new ArrayList<>();
                                    for (int i = 0; i < products.size()-1; i++) {//同一件商品的不同型号只显示一个
                                            if(products.get(i).getId() != products.get(i+1).getId()){
                                                list.add(products.get(i));
                                            }
                                        }
                                    if(products.get(products.size()-1) != list.get(list.size()-1)){
                                        list.add(products.get(products.size()-1));
                                    }

                                    if(index>1 && mainProducts.size()<20){
                                        Toast.makeText(getContext(),"已经到底啦！！",Toast.LENGTH_SHORT).show();
                                    }
                                    if(index>1){
                                        productDisplayAdapter.addAll(list,false);
                                    }else {
                                        productDisplayAdapter.addAll(list,true);
                                    }
                                  //  productDisplayAdapter.addAll(list,true);
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
                            progressDialogUtil.cancelProgressDialog();
                            if(mainProducts.size()==0){
                                Toast.makeText(getContext(),"没有更多商品啦！！",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            ArrayList<MainProductBean> list = new ArrayList<>();
                            for (int i = 0; i < mainProducts.size()-1; i++) {//同一件商品的不同型号只显示一个
                                if(mainProducts.get(i).getId() != mainProducts.get(i+1).getId()){
                                    list.add(mainProducts.get(i));
                                }
                            }
                            if(mainProducts.get(mainProducts.size()-1) != list.get(list.size()-1)){
                                list.add(mainProducts.get(mainProducts.size()-1));
                            }

                            if(index>1 && mainProducts.size()<20){
                                Toast.makeText(getContext(),"已经到底啦！！",Toast.LENGTH_SHORT).show();
                            }
                            if(index>1){
                                productDisplayAdapter.addAll(list,false);
                            }else {
                                productDisplayAdapter.addAll(list,true);
                            }
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
        Glide.with(getContext()).load(newGoodsRecommends.get(0).getUrl()).into(newGoods2);
        Glide.with(getContext()).load(newGoodsRecommends.get(1).getUrl()).into(newGoods3);
        Glide.with(getContext()).load(newGoodsRecommends.get(2).getUrl()).into(newGoods4);
        Glide.with(getContext()).load(newGoodsRecommends.get(3).getUrl()).into(newGoods5);
        //TODO 缺少店铺信息
       /* tvStore1Name.setText(store.getTitel());
        tvStore1Describe.setText(store.getIntroduce());
        tvStore2Name.setText(storeRecommends.get(0).getTitel());
        tvStore2Describe.setText(storeRecommends.get(0).getIntroduce());
        tvStore3Name.setText(storeRecommends.get(1).getTitel());
        tvStore3Describe.setText(storeRecommends.get(1).getIntroduce());*/

        classifyAdapter.addAll(list, true);

    }



    private void initview(View rootView) {
        okHttpClient = new OkHttpClient.Builder().build();
        mainProducts = new ArrayList<>();
        classifys = new ArrayList<>();
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvRecommendGoods.setLayoutManager(linearLayoutManager);
        classifyAdapter = new ProductClassifyAdapter(getContext(),R.layout.item_product_classify, classifys, "product");
        rvRecommendGoods.setAdapter(classifyAdapter);
        //  rvHomeDisplay.setLayoutManager(new FullyLinearLayoutManager(getContext()));

        productDisplayAdapter = new MainProductDisplayAdapter(getContext(), mainProducts, "product");

        MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(getContext(), 2);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        rvHomeDisplay.setLayoutManager(gridLayoutManager);
        rvHomeDisplay.addItemDecoration(new ItemOffsetDecoration(10));
        rvHomeDisplay.setHasFixedSize(true);
        rvHomeDisplay.setAdapter(productDisplayAdapter);

        productDisplayAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadmore() {
                index++;
                getDisplayProducts();
            }
        });

    }


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





    //TODO 全部新品
    @OnClick({R.id.tv_newgoods_all, R.id.ll_new_goods})
    public void allProductRecommend() {
        jumpTo(NewGoodsActivity.class, false);
    }


    //初始化banner图
    private void initBanner() {
        bannerDatas = new ArrayList<>();
        control.getBanners(okHttpClient, new OnGetBannerFinishedListener() {
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
                            LogUtil.i("TAG","banner跳转到商品==========");
                            getProductDetial(bannerBean.getProductId());
                        } else {//商铺
                            getStoreDetial(bannerBean.getProductId());
                            LogUtil.i("TAG","banner跳转到商铺==========");
                        }
                    }
                });
            }
        });


    }

    /**
     * 根据商铺id获取商品详情
     *
     * @param productId
     */
    private void getProductDetial(int productId) {
        progressDialogUtil.buildProgressDialog();
        control.getProductDetialById(productId, new OnGetProductDetialFinishedListener() {
            @Override
            public void onGetProductDetialFinishedListener(MainProductBean product) {
                progressDialogUtil.cancelProgressDialog();
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
        progressDialogUtil.buildProgressDialog();
        control.getStoreDetialByid(productId, new OnGetStoreDetialFinishedListener() {
            @Override
            public void onGetStoreDetialFinishedListener(StoreBean store) {
                progressDialogUtil.cancelProgressDialog();
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
