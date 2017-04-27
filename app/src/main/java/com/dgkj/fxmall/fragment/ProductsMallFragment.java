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

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.DemandClassifyAdapter;
import com.dgkj.fxmall.adapter.NiceStoreRecommendAdapter;
import com.dgkj.fxmall.adapter.ProductClassifyAdapter;
import com.dgkj.fxmall.adapter.ProductClassifyGridViewAdapter;
import com.dgkj.fxmall.bean.DemandMallClassifyBean;
import com.dgkj.fxmall.bean.NiceStoreBean;
import com.dgkj.fxmall.bean.ProductClassifyBean;
import com.dgkj.fxmall.view.StoreMainPageActivity;
import com.dgkj.fxmall.view.myView.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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


    private List<NiceStoreBean> niceStores;
    private NiceStoreRecommendAdapter niceStoreRecommendAdapter;
    private List<ProductClassifyBean> classifyBeans;
    private ProductClassifyAdapter classifyAdapter;
    private GridView gv;
    private ProductClassifyGridViewAdapter gridViewAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_products_fragment, container, false);
        ButterKnife.bind(this, view);
        initClassifyData();
        initNiceStoreData();
     //  initClassifyRecommendData();
        initClassifyRecommendData(view);
        return view;
    }

    private void initClassifyRecommendData(View view) {
        gv = (GridView) view.findViewById(R.id.gv_classify_recommend);
      //  classifyBeans = new ArrayList<>();

        //测试
        List<ProductClassifyBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ProductClassifyBean classify = new ProductClassifyBean();
            classify.setTaxon("坚果");
            classify.setUrl("http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg");
            list.add(classify);
        }
        gridViewAdapter = new ProductClassifyGridViewAdapter(getContext(),R.layout.item_product_classify,list,"product");
        gv.setAdapter(gridViewAdapter);
        //   gv.addAll(list,true);
    }

    private void initNiceStoreData() {
        niceStores = new ArrayList<>();
        niceStoreRecommendAdapter = new NiceStoreRecommendAdapter(getContext(),niceStores);
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
        niceStoreRecommendAdapter.addAll(list,true);

    }

    private void initClassifyData() {
        List<DemandMallClassifyBean> classifyBeens = new ArrayList<>();
        classifyBeens.add(new DemandMallClassifyBean("美食",R.mipmap.cpdt_ms));
        classifyBeens.add(new DemandMallClassifyBean("母婴",R.mipmap.cpdt_my));
        classifyBeens.add(new DemandMallClassifyBean("美妆",R.mipmap.cpdt_mz));
        classifyBeens.add(new DemandMallClassifyBean("家居",R.mipmap.cpdt_jz));
        classifyBeens.add(new DemandMallClassifyBean("箱包",R.mipmap.cpdt_xb));
        classifyBeens.add(new DemandMallClassifyBean("女装",R.mipmap.cpdt_nz));
        classifyBeens.add(new DemandMallClassifyBean("男装",R.mipmap.cpdt_nanz));
        classifyBeens.add(new DemandMallClassifyBean("电器",R.mipmap.cpdt_dq));
        classifyBeens.add(new DemandMallClassifyBean("汽车",R.mipmap.cpdt_qc));
        classifyBeens.add(new DemandMallClassifyBean("更多",R.mipmap.cpdt_gd));

       GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),5);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvProductClassify.setHasFixedSize(true);
        rvProductClassify.setLayoutManager(gridLayoutManager);
        rvProductClassify.setNestedScrollingEnabled(false);
        DemandClassifyAdapter classifyAdapter = new DemandClassifyAdapter(getContext(), R.layout.item_demandmall_product_classify, classifyBeens);
        rvProductClassify.setAdapter(classifyAdapter);

    }

    @OnClick({R.id.nice1,R.id.nice2,R.id.nice3,R.id.nice4})
    public void niceRecommend(){
        //TODO 根据推荐是是商品还是商店进行不同的跳转
        startActivity(new Intent(getContext(),StoreMainPageActivity.class));
    }
}
