package com.dgkj.fxmall.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.MainProductDisplayAdapter;
import com.dgkj.fxmall.adapter.OrderClassifyAdapter;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.OrderBean;

import java.util.List;

/**
 * Created by Android004 on 2017/3/25.
 */
@SuppressLint("ValidFragment")
public class MyOrderClassifyFragment extends Fragment {
    private List<OrderBean> mainList;
    private Handler mHandler;
    private RecyclerView recyclerView;
    private OrderClassifyAdapter adapter;
    public MyOrderClassifyFragment(){

    }
    public MyOrderClassifyFragment(List<OrderBean> datas){
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
        adapter = new OrderClassifyAdapter(getContext(),mainList,getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
