package com.dgkj.fxmall.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.MainProductDisplayAdapter;
import com.dgkj.fxmall.adapter.ShoppingCarSubAdapter;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.ShoppingCarBean;
import com.dgkj.fxmall.bean.SomeProductClassifyBean;
import com.dgkj.fxmall.view.myView.EndlessRecyclerOnScrollListener;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android004 on 2017/3/25.
 */
@SuppressLint("ValidFragment")
public class SomeProductClassifyFragment extends Fragment {
    private List<MainProductBean> mainList;
    private Handler mHandler;
    private RecyclerView recyclerView;
    private MainProductDisplayAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private HeaderAndFooterWrapper<String> footerWrapper;
    private Handler handler;

    public SomeProductClassifyFragment(){

    }
    public SomeProductClassifyFragment(List<MainProductBean> datas){
        mainList = datas;
        handler = new Handler();
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
        adapter = new MainProductDisplayAdapter(getContext(),R.layout.item_main_product,mainList,"product");
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh);

        footerWrapper = new HeaderAndFooterWrapper<>(adapter);
        footerWrapper.addFootView(getActivity().getLayoutInflater().inflate(R.layout.layout_pull_load_more,null,false));

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> url = new ArrayList<>();
                        url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492433529&di=de2494834a545a7e044f2cf696345ace&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.wmtuku.com%2Fd%2Ffile%2F2017-02-17%2F2858b31981db27e4af218207575658dc.jpg");
                        url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492433529&di=de2494834a545a7e044f2cf696345ace&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.wmtuku.com%2Fd%2Ffile%2F2017-02-17%2F2858b31981db27e4af218207575658dc.jpg");
                        url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492433529&di=de2494834a545a7e044f2cf696345ace&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.wmtuku.com%2Fd%2Ffile%2F2017-02-17%2F2858b31981db27e4af218207575658dc.jpg");
                        url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492433529&di=de2494834a545a7e044f2cf696345ace&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.wmtuku.com%2Fd%2Ffile%2F2017-02-17%2F2858b31981db27e4af218207575658dc.jpg");
                        url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492433529&di=de2494834a545a7e044f2cf696345ace&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.wmtuku.com%2Fd%2Ffile%2F2017-02-17%2F2858b31981db27e4af218207575658dc.jpg");
                        url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492433529&di=de2494834a545a7e044f2cf696345ace&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.wmtuku.com%2Fd%2Ffile%2F2017-02-17%2F2858b31981db27e4af218207575658dc.jpg");
                        url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492433529&di=de2494834a545a7e044f2cf696345ace&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.wmtuku.com%2Fd%2Ffile%2F2017-02-17%2F2858b31981db27e4af218207575658dc.jpg");
                        List<MainProductBean> list = new ArrayList<>();
                        for (int j = 0; j < 5; j++) {
                            MainProductBean productBean = new MainProductBean();
                            productBean.setUrls(url);
                            productBean.setDetialUrls(url);
                            productBean.setUrl(url.get(0));
                            productBean.setPrice(88);
                            productBean.setExpress("韵达快递");
                            productBean.setSales("465");
                            productBean.setVipPrice(58);
                            productBean.setAddress("广东深圳");
                            productBean.setIntroduce("粉小萌，够猛，够萌，够亮，够酸，够辣，这酸辣爽");
                            list.add(productBean);
                        }
                        adapter.addAll(list,false);
                    }
                },2000);

            }
        });
    }
}
