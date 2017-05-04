package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vip);
        ButterKnife.bind(this);
        initHeaderView();
        initTabLayout();
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
        tabLayout.addTab(tabLayout.newTab().setText("上级"), true);
        tabLayout.addTab(tabLayout.newTab().setText("下级"));
        tabLayout.addTab(tabLayout.newTab().setText("下下级"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
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
