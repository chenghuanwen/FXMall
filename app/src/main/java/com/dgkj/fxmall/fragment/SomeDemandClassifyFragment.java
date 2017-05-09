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
import com.dgkj.fxmall.adapter.MainProductDisplayAdapter;
import com.dgkj.fxmall.bean.MainDemandBean;
import com.dgkj.fxmall.view.myView.EndlessRecyclerOnScrollListener;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.List;

/**
 * Created by Android004 on 2017/3/25.
 */
@SuppressLint("ValidFragment")
public class SomeDemandClassifyFragment extends Fragment {
    private List<MainDemandBean> mainList;
    private Handler mHandler;
    private RecyclerView recyclerView;
    private MainDemandDisplayAdapter adapter;
    private HeaderAndFooterWrapper<String> footerWrapper;

    public SomeDemandClassifyFragment(){

    }
    public SomeDemandClassifyFragment(List<MainDemandBean> datas){
        mainList = datas;
      //  mHandler = handler;

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_some_productclassify_fragment,container,false);
        initData(view);
        return view;
    }

    private void initData(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_some_classify);
        adapter = new MainDemandDisplayAdapter(getContext(),R.layout.item_main_demand,mainList,"demand");
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        footerWrapper = new HeaderAndFooterWrapper<>(adapter);
        footerWrapper.addFootView(getActivity().getLayoutInflater().inflate(R.layout.layout_pull_load_more,null,false));

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {

            }
        });
    }

    public void goTop(){
        recyclerView.smoothScrollToPosition(0);
    }
}
