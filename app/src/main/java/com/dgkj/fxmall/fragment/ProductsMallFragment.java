package com.dgkj.fxmall.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.DemandClassifyAdapter;
import com.dgkj.fxmall.adapter.NiceStoreRecommendAdapter;
import com.dgkj.fxmall.adapter.ProductClassifyAdapter;
import com.dgkj.fxmall.adapter.ProductClassifyGridViewAdapter;
import com.dgkj.fxmall.bean.ColorSizeBean;
import com.dgkj.fxmall.bean.DemandMallClassifyBean;
import com.dgkj.fxmall.bean.HomePageRecommendBean;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.NiceStoreBean;
import com.dgkj.fxmall.bean.ProductClassifyBean;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetHomeRecommendFinishedListener;
import com.dgkj.fxmall.listener.OnGetProductDetialFinishedListener;
import com.dgkj.fxmall.listener.OnGetStoreDetialFinishedListener;
import com.dgkj.fxmall.listener.OnGetSubclassifyFinishedListener;
import com.dgkj.fxmall.utils.LoadProgressDialogUtil;
import com.dgkj.fxmall.view.ProductDetialActivity;
import com.dgkj.fxmall.view.StoreMainPageActivity;
import com.dgkj.fxmall.view.myView.FullyLinearLayoutManager;

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

public class ProductsMallFragment extends Fragment {

    @BindView(R.id.nice1)
    RelativeLayout nice1;
    @BindView(R.id.nice2)
    RelativeLayout nice2;
    @BindView(R.id.nice3)
    RelativeLayout nice3;
    @BindView(R.id.nice4)
    RelativeLayout nice4;
    @BindView(R.id.rv_product_classify)
    RecyclerView rvProductClassify;
    @BindView(R.id.rv_nice_recommend)
    RecyclerView rvNiceRecommend;
    private GridView gv;

    private List<NiceStoreBean> niceStores;
    private NiceStoreRecommendAdapter niceStoreRecommendAdapter;
    private ProductClassifyGridViewAdapter gridViewAdapter;
    private DemandClassifyAdapter classifyAdapter;
    private FXMallControl control = new FXMallControl();
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private HomePageRecommendBean liangpin, youzhi, youxuan, xinpin;
    private List<ProductClassifyBean> subClassifys = new ArrayList<>();
    private List<DemandMallClassifyBean> superClassifys = new ArrayList<>();
    private List<NiceStoreBean> recommendStores = new ArrayList<>();
    private LoadProgressDialogUtil progressDialogUtil;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_products_fragment, container, false);
        ButterKnife.bind(this, view);

        progressDialogUtil = new LoadProgressDialogUtil(getContext());
        progressDialogUtil.buildProgressDialog();

        initClassifyData();
        initNiceStoreData();
        getRecommendData();
        initClassifyRecommendData(view);
        return view;
    }

    private void initClassifyRecommendData(View view) {
        gv = (GridView) view.findViewById(R.id.gv_classify_recommend);


        gridViewAdapter = new ProductClassifyGridViewAdapter(getContext(), R.layout.item_productmall_classify, subClassifys, "product");
        gv.setAdapter(gridViewAdapter);

        control.getProductMallAllSubclassify(client, new OnGetSubclassifyFinishedListener() {
            @Override
            public void onGetSubclassifyFinished(final List<ProductClassifyBean> subList) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gridViewAdapter.addAll(subList, true);
                        progressDialogUtil.cancelProgressDialog();
                    }
                });

            }
        });

    }

    private void initNiceStoreData() {
        niceStores = new ArrayList<>();
        niceStoreRecommendAdapter = new NiceStoreRecommendAdapter(getContext(), niceStores);
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvNiceRecommend.setLayoutManager(linearLayoutManager);
        rvNiceRecommend.setAdapter(niceStoreRecommendAdapter);

    }

    private void initClassifyData() {

        List<DemandMallClassifyBean> classifyBeens = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvProductClassify.setHasFixedSize(true);
        rvProductClassify.setLayoutManager(gridLayoutManager);
        rvProductClassify.setNestedScrollingEnabled(false);
        classifyAdapter = new DemandClassifyAdapter(getContext(), R.layout.item_demandmall_product_classify, classifyBeens);
        rvProductClassify.setAdapter(classifyAdapter);

    }





    @OnClick(R.id.nice1)
    public void nice1(){
        getProductDetial(liangpin.getLink());
    }

    @OnClick(R.id.nice3)
    public void nice3(){
        getProductDetial(youxuan.getLink());
    }
    @OnClick(R.id.nice4)
    public void nice4(){
        getProductDetial(xinpin.getLink());
    }

    @OnClick(R.id.nice2)
    public void nice2(){
      toStoreMain(youzhi.getLink());

    }


    /**
     * 根据id查询店铺详情
     * @param link
     */
    private void toStoreMain(int link) {
        control.getStoreDetialByid(link, new OnGetStoreDetialFinishedListener() {
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




    //获取所有推荐数据
    private void getRecommendData() {
        control.getHomePageAllRecommender(1, new OnGetHomeRecommendFinishedListener() {
            @Override
            public void onGetHomeRecommendFinishedListener(List<HomePageRecommendBean> recommendList) {
                for (HomePageRecommendBean homePageRecommendBean : recommendList) {
                    String position = homePageRecommendBean.getPosition();
                    switch (position) {
                        case "product_GStore":
                            youzhi = homePageRecommendBean;
                            break;
                        case "product_GCommodity":
                            liangpin = homePageRecommendBean;
                            break;
                        case "product_Fcommodity":
                            youxuan = homePageRecommendBean;
                            break;
                        case "product_NCommodity":
                            xinpin = homePageRecommendBean;
                            break;
                        case "product_Category":
                            DemandMallClassifyBean classify = new DemandMallClassifyBean();
                            classify.setTitle(homePageRecommendBean.getTitel());
                            classify.setSuperId(homePageRecommendBean.getLink());
                            classify.setUrl(homePageRecommendBean.getUrl());
                            if(superClassifys.size()<10){
                                superClassifys.add(classify);
                            }
                            break;
                        case "product_RStore":
                            NiceStoreBean storeBean = new NiceStoreBean();
                            storeBean.setName(homePageRecommendBean.getStoreName());
                            storeBean.setUrl(homePageRecommendBean.getUrl());
                            storeBean.setIntroduce(homePageRecommendBean.getIntroduce());
                            storeBean.setAddress(homePageRecommendBean.getAddress());
                            storeBean.setId(homePageRecommendBean.getLink());
                            recommendStores.add(storeBean);
                            break;
                       /* case "product_SubCategory":
                            ProductClassifyBean classifyBean = new ProductClassifyBean();
                            classifyBean.setTaxon(homePageRecommendBean.getTitel());
                            classifyBean.setId(homePageRecommendBean.getLink());
                            classifyBean.setUrl(homePageRecommendBean.getUrl());
                            subClassifys.add(classifyBean);
                            break;*/
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

    /**
     * 刷新各个列表
     */
    private void setData() {
        niceStoreRecommendAdapter.addAll(recommendStores,true);
        //最后一条为“更多”
        superClassifys.add(new DemandMallClassifyBean("更多", R.mipmap.cpdt_gd));
        classifyAdapter.addAll(superClassifys,true);
    }
}
