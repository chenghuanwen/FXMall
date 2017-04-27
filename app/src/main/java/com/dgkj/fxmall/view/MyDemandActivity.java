package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.MainDemandBean;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.fragment.MydemandFragment;
import com.dgkj.fxmall.fragment.RecommendFragment;
import com.dgkj.fxmall.listener.OnGetMyDemandDataFinishedListener;
import com.dgkj.fxmall.listener.OnGetMyRecommendStoreFinishedListener;
import com.dgkj.fxmall.view.myView.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class MyDemandActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.rb_mydemand)
    AppCompatRadioButton rbMydemand;
    @BindView(R.id.rb_recommend)
    AppCompatRadioButton rbRecommend;
    @BindView(R.id.rb_publish_demand)
    AppCompatRadioButton rbPublishDemand;
    @BindView(R.id.rg_top)
    RadioGroup rgTop;
    @BindView(R.id.fl_top_background)
    FrameLayout flTopBackground;
    @BindView(R.id.rl_mydemand)
    RelativeLayout rvMydemand;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.myScrollView)
    ObservableScrollView myScrollView;
    @BindView(R.id.activity_my_demand)
    LinearLayout activityMyDemand;
    private View headerview;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MydemandFragment mydemandFragment;
    private RecommendFragment recommendFragment;
    private List<StoreBean> storeList;
    private List<MainDemandBean> demandList;
    private Handler handler = new Handler();
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private FXMallControl control = new FXMallControl();
    private int demandIndex = 1;
    private int storeIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_demand);
        ButterKnife.bind(this);
        initHeaderView();

        fragmentManager = getSupportFragmentManager();

        getData();
        setListener();
        showMyDemand();
        handlerScroll();
    }

    private void getData() {
        //TODO 获取数据
        storeList = new ArrayList<>();
        demandList = new ArrayList<>();
        //TEST
        for (int i = 0; i < 10; i++) {
            List<String> url = new ArrayList<>();
            url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490872429670&di=b95cc27dc9781e9510de7753a6709f39&imgtype=0&src=http%3A%2F%2Fp.nanrenwo.net%2Fuploads%2Fallimg%2F170223%2F8450-1F223100J2.jpg");
            MainDemandBean demand = new MainDemandBean();
            demand.setAddress("广东深圳");
            demand.setDemand(100);
            demand.setIntroduce("粉小萌酸辣粉，没有比这更好吃的了....");
            demand.setUrls(url);
            demandList.add(demand);
        }

        control.getMyDemandData(this, sp.get("token"), demandIndex, 10, client, new OnGetMyDemandDataFinishedListener() {
            @Override
            public void onGetMyDemandDataFinished(List<MainDemandBean> demands) {
                demandList.addAll(demands);
            }
        });


        //TEST
        for (int i = 0; i < 10; i++) {
            StoreBean store = new StoreBean();
            store.setAdress("广东深圳");
            store.setDescribeScore(5.00);
            store.setGoodsCount(134);
            store.setIconUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490872429670&di=b95cc27dc9781e9510de7753a6709f39&imgtype=0&src=http%3A%2F%2Fp.nanrenwo.net%2Fuploads%2Fallimg%2F170223%2F8450-1F223100J2.jpg");
            store.setName("粉小萌");
            store.setPriceScore(4.9);
            store.setQualityScore(5.0);
            store.setStars(5);
            store.setTotalScals(100000);
            storeList.add(store);
        }

        control.getMyRecommendStore(this, sp.get("token"), FXConst.GET_MY_RECOMMEND_STORES, storeIndex, 20, client, new OnGetMyRecommendStoreFinishedListener() {
            @Override
            public void onGetMyRecommendStoreFinished(List<StoreBean> stores) {
                storeList.addAll(stores);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        tabLayout.getTabAt(0).select();
    }

    @Override
    public View getContentView() {
        return activityMyDemand;
    }

    /***
     * 处理scrollview滑动
     */
    private void handlerScroll() {
        myScrollView.setFocusable(false);
        myScrollView.setFocusableInTouchMode(true);
        myScrollView.setOnScollChangedListener(new ObservableScrollView.OnScollChangedListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                int[] location = new int[2];
                flTopBackground.getLocationOnScreen(location);//获取该布局在屏幕中的位置
                int distance = location[1];
                if (distance < 40) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            flTopBackground.setVisibility(View.GONE);
                            tabLayout.setVisibility(View.VISIBLE);
                            if (rbMydemand.isChecked()) {
                                tabLayout.getTabAt(0).select();
                            } else {
                                tabLayout.getTabAt(1).select();
                            }
                        }
                    }, 500);

                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            flTopBackground.setVisibility(View.VISIBLE);
                            tabLayout.setVisibility(View.GONE);
                        }
                    }, 500);

                }
            }
        });

    }

    private void setListener() {
        rgTop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_mydemand:
                        showMyDemand();
                        break;
                    case R.id.rb_recommend:
                        showRecommend();
                        break;
                    case R.id.rb_publish_demand:
                        jumpTo(PublishDemandActivity.class, false);
                        break;
                }
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("我的需求"), true);
        tabLayout.addTab(tabLayout.newTab().setText("推荐的店铺"));
        tabLayout.addTab(tabLayout.newTab().setText("发布需求"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        showMyDemand();
                        break;
                    case 1:
                        showRecommend();
                        break;
                    case 2:
                        jumpTo(PublishDemandActivity.class, false);
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

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "我的需求");
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }


    private void showMyDemand() {
        fragmentTransaction = fragmentManager.beginTransaction();
        if (mydemandFragment == null) {
            mydemandFragment = new MydemandFragment(demandList);
            fragmentTransaction.add(R.id.rl_mydemand, mydemandFragment);
        }
        fragmentTransaction.commit();
        hideFragment(fragmentTransaction);
        fragmentTransaction.show(mydemandFragment);
    }


    private void showRecommend() {
        fragmentTransaction = fragmentManager.beginTransaction();
        if (recommendFragment == null) {
            recommendFragment = new RecommendFragment(storeList);
            fragmentTransaction.add(R.id.rl_mydemand, recommendFragment);
        }
        fragmentTransaction.commit();
        hideFragment(fragmentTransaction);
        fragmentTransaction.show(recommendFragment);
    }


    /**
     * 隐藏所有Fragment
     *
     * @param fragmentTransaction
     */
    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (mydemandFragment != null) {
            fragmentTransaction.hide(mydemandFragment);
        }
        if (recommendFragment != null) {
            fragmentTransaction.hide(recommendFragment);
        }

    }
}
