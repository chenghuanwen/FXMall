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
  //  private List<ProductClassifyBean> subClassifys = new ArrayList<>();
    private List<DemandMallClassifyBean> superClassifys = new ArrayList<>();
    private List<NiceStoreBean> recommendStores = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_products_fragment, container, false);
        ButterKnife.bind(this, view);

        initClassifyData();
        initNiceStoreData();
        getRecommendData();
        initClassifyRecommendData(view);
        return view;
    }

    private void initClassifyRecommendData(View view) {
        gv = (GridView) view.findViewById(R.id.gv_classify_recommend);

        //测试
        List<ProductClassifyBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ProductClassifyBean classify = new ProductClassifyBean();
            classify.setTaxon("坚果");
            classify.setUrl("http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg");
            list.add(classify);
        }
        gridViewAdapter = new ProductClassifyGridViewAdapter(getContext(), R.layout.item_productmall_classify, list, "product");
        gv.setAdapter(gridViewAdapter);

        control.getProductMallAllSubclassify(client, new OnGetSubclassifyFinishedListener() {
            @Override
            public void onGetSubclassifyFinished(final List<ProductClassifyBean> subList) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gridViewAdapter.addAll(subList, true);
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
        //测试
        List<NiceStoreBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            NiceStoreBean store = new NiceStoreBean();
            store.setAddress("深圳市龙岗区");
            store.setIntroduce("好店粉小萌再次上传好店粉小萌再次上传好店粉小萌再次上传好店粉小萌再次上传");
            store.setName("粉小萌");
            store.setUrl("http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg");
            list.add(store);
        }
        niceStoreRecommendAdapter.addAll(list, true);

    }

    private void initClassifyData() {
        List<DemandMallClassifyBean> classifyBeens = new ArrayList<>();
        classifyBeens.add(new DemandMallClassifyBean("美食", R.mipmap.cpdt_ms));
        classifyBeens.add(new DemandMallClassifyBean("母婴", R.mipmap.cpdt_my));
        classifyBeens.add(new DemandMallClassifyBean("美妆", R.mipmap.cpdt_mz));
        classifyBeens.add(new DemandMallClassifyBean("家居", R.mipmap.cpdt_jz));
        classifyBeens.add(new DemandMallClassifyBean("箱包", R.mipmap.cpdt_xb));
        classifyBeens.add(new DemandMallClassifyBean("女装", R.mipmap.cpdt_nz));
        classifyBeens.add(new DemandMallClassifyBean("男装", R.mipmap.cpdt_nanz));
        classifyBeens.add(new DemandMallClassifyBean("电器", R.mipmap.cpdt_dq));
        classifyBeens.add(new DemandMallClassifyBean("汽车", R.mipmap.cpdt_qc));
        classifyBeens.add(new DemandMallClassifyBean("更多", R.mipmap.cpdt_gd));

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


    //TEST
    @OnClick({R.id.nice1, R.id.nice2, R.id.nice3, R.id.nice4})
    public void niceRecommend() {
        //TODO 根据推荐是是商品还是商店进行不同的跳转
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

        Intent intent = new Intent(getContext(),ProductDetialActivity.class);
        intent.putExtra("product", productBean);
        getContext().startActivity(intent);
    }

/*

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
*/

    @OnClick(R.id.nice2)
    public void nice2(){
      //  toStoreMain(youzhi.getLink());
        //TEST
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
                            liangpin = homePageRecommendBean;
                            break;
                        case "product_GCommodity":
                            youzhi = homePageRecommendBean;
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
                            superClassifys.add(classify);
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
                setData();
            }
        });
    }

    /**
     * 刷新各个列表
     */
    private void setData() {
      //  gridViewAdapter.addAll(subClassifys,true);
        niceStoreRecommendAdapter.addAll(recommendStores,true);
        classifyAdapter.addAll(superClassifys,true);
    }
}
