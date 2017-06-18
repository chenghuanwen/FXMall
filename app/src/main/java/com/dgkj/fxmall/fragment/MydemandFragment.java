package com.dgkj.fxmall.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
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
import com.dgkj.fxmall.bean.MainDemandBean;
import com.dgkj.fxmall.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("ValidFragment")
public class MydemandFragment extends Fragment {
    private List<MainDemandBean> mainList;
    private List<MainDemandBean> initList;
    private Handler mHandler;
    private RecyclerView recyclerView;
    private MainDemandDisplayAdapter adapter;

    public MydemandFragment() {
    }

    public MydemandFragment(List<MainDemandBean> mainList) {
        this.mainList = mainList;
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
        initList = new ArrayList<>();
        adapter = new MainDemandDisplayAdapter(getContext(),R.layout.item_main_demand,initList,"myDemand");
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.addAll(mainList,true);
    }
}
