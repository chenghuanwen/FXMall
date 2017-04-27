package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

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
    @BindView(R.id.tv_store_IDcard)
    TextView tvStoreIDcard;
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
    private View headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO 区分审核状态(等待审核、正在审核、审核通过)
        setContentView(R.layout.activity_review_progress);
        //setContentView(R.layout.activity_review_progress_ok);
        ButterKnife.bind(this);
        initHeaderView();
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "审核进度");
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
