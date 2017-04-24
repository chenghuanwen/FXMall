package com.dgkj.fxmall.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.ProductClassifyAdapter;
import com.dgkj.fxmall.bean.ProductClassifyBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetDemandClassifyFinishedListener;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class DemandMallFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<ProductClassifyBean> classifyList;
    private ProductClassifyAdapter adapter;
    private FXMallControl control;
    private OkHttpClient client;
    private Handler handler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_demand_fragment, container, false);
        control = new FXMallControl();
        client = new OkHttpClient.Builder().build();
        handler = new Handler(Looper.getMainLooper());
        initview(view);
        refresh();
        return view;
    }

    private void refresh() {
        List<ProductClassifyBean> list = new ArrayList<>();
        //TODO 刷新数据
        control.getDemandClassifyDatas(handler,getContext(),client,new OnGetDemandClassifyFinishedListener() {
            @Override
            public void onGetDemandClassifyFinished(final List<ProductClassifyBean> calssifys) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addAll(calssifys,true);
                    }
                });

            }
        });

    }

    private void initview(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_demand_mall);
        classifyList = new ArrayList<>();
        adapter = new ProductClassifyAdapter(getContext(),R.layout.item_product_classify,classifyList,"demand");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

    }
}
