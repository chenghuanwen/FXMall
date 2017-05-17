package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.MyVipAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.MainDemandBean;
import com.dgkj.fxmall.bean.MyVipBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetMyVipFinishedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyVIPActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.rv_my_vip)
    RecyclerView rvMyVip;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    private View headerview;
    private List<MyVipBean> vips;
    private MyVipAdapter adapter;
    private FXMallControl control = new FXMallControl();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vip);
        ButterKnife.bind(this);
        initHeaderView();
        initTabLayout();
        init();
        refresh(FXConst.GET_MY_SUB_VIP_DATA_URL);
    }

    private void refresh(String url) {
        control.getMySubVipData(url, sp.get("token"), 1, 50, new OnGetMyVipFinishedListener() {
            @Override
            public void onGetMyVipFinishedListener(final List<MyVipBean> vipList) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addAll(vipList,true);
                    }
                });
            }
        });
    }

    private void init() {
        vips = new ArrayList<>();
        adapter = new MyVipAdapter(this,R.layout.item_my_vip,vips);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMyVip.setLayoutManager(layoutManager);
        rvMyVip.setAdapter(adapter);
    }


    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "我的会员");
    }
    @Override
    public View getContentView() {
        return null;
    }

    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("上级"));
        tabLayout.addTab(tabLayout.newTab().setText("下级"),true);
        tabLayout.addTab(tabLayout.newTab().setText("下下级"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        control.getMySuperVipInfo(sp.get("token"), new OnGetMyVipFinishedListener() {
                            @Override
                            public void onGetMyVipFinishedListener(final List<MyVipBean> vipList) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.addAll(vipList,true);
                                    }
                                });
                            }
                        });
                        break;
                    case 1:
                        refresh(FXConst.GET_MY_SUB_VIP_DATA_URL);
                        break;
                    case 2:
                        refresh(FXConst.GET_MY_SUB_AND_SUB_VIP_DATA_URL);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
