package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.ViewFlipperAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.StoreBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewProgressActivity extends BaseActivity {
    @BindView(R.id.tv_to_store)
    TextView tvToStore;
    @BindView(R.id.avf_store)
    AdapterViewFlipper avfStore;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_store_classify)
    TextView tvStoreClassify;
    @BindView(R.id.tv_store_phone)
    TextView tvStorePhone;
    @BindView(R.id.tv_store_city)
    TextView tvStoreCity;
    @BindView(R.id.tv_store_address)
    TextView tvStoreAddress;
    @BindView(R.id.iv_evidence)
    ImageView ivEvidence;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_review_state)
    TextView tvReviewState;
    @BindView(R.id.tv_keeper)
    TextView tvKeeper;
    private View headerview;
    private String statu = "";
    private StoreBean storeBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        statu = getIntent().getStringExtra("statu");
        storeBean = getIntent().getParcelableExtra("store");

        //TODO 区分审核状态(等待审核、正在审核、审核通过)
        if ("wait".equals(statu)) {
            setContentView(R.layout.activity_review_progress);
        } else if ("ok".equals(statu)) {
            setContentView(R.layout.activity_review_progress_ok);
        } else {
            setContentView(R.layout.activity_review_progress);
            tvReviewState.setText("店铺资料已提交成功，审核中...");
        }

        ButterKnife.bind(this);
        initHeaderView();
        setData();
    }

    private void setData() {
        String adress = storeBean.getAdress();
        int index;
        if (adress.contains("区")) {
            index = adress.indexOf("区");
        } else {
            index = adress.indexOf("县");
        }
        tvStoreCity.setText(adress.substring(0, index + 1));
        tvStoreAddress.setText(adress.substring(index + 1, adress.length()));
        tvStoreName.setText(storeBean.getName());
        tvStorePhone.setText(storeBean.getPhone());
        tvKeeper.setText(storeBean.getKeeper());
        List<String> mainUrls = storeBean.getMainUrls();
        avfStore.setAdapter(new ViewFlipperAdapter(mainUrls, this));
        avfStore.setAutoStart(true);
        avfStore.startFlipping();
        Glide.with(this).load(storeBean.getLicence()).into(ivEvidence);
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "审核进度");
    }


    @OnClick(R.id.tv_to_store)
    public void createStore(){
        jumpTo(StoreInfoEditActivity.class,true);
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
