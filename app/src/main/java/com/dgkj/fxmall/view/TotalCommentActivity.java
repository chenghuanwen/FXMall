package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TotalCommentActivity extends BaseActivity {
    @BindView(R.id.tv_total_stars)
    TextView tvTotalStars;
    @BindView(R.id.iv_total_stars)
    ImageView ivTotalStars;
    @BindView(R.id.iv_describe_star)
    ImageView ivDescribeStar;
    @BindView(R.id.tv_describe_count)
    TextView tvDescribeCount;
    @BindView(R.id.iv_price_star)
    ImageView ivPriceStar;
    @BindView(R.id.tv_price_count)
    TextView tvPriceCount;
    @BindView(R.id.iv_quality_star)
    ImageView ivQualityStar;
    @BindView(R.id.tv_quality_count)
    TextView tvQualityCount;
    @BindView(R.id.rv_all_comment)
    RecyclerView rvAllComment;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
   /* @BindView(R.id.ib_car)
    ImageButton ibCar;
    @BindView(R.id.tv_car_count)
    TextView tvCarCount;*/
    private View headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_comment);
        ButterKnife.bind(this);
        initHeaderView();
    }


    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "累计评价");
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
