package com.dgkj.fxmall.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.MainDemandDisplayAdapter;
import com.dgkj.fxmall.adapter.SearchStoreAdapter;
import com.dgkj.fxmall.bean.MainDemandBean;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("ValidFragment")
public class RecommendFragment extends Fragment {
    private List<StoreBean> storeList;
    private SearchStoreAdapter adapter;
    private RecyclerView recyclerView;

    public RecommendFragment() {
    }

    public RecommendFragment(List<StoreBean> storeList) {
        this.storeList = storeList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_my_demand_fragment, container, false);
        initData(view);
        return view;
    }

    private void initData(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_some_classify);
        adapter = new SearchStoreAdapter(getContext(),R.layout.item_shangpu_search_result,storeList,"recommend");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
