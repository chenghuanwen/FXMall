package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PublishCommentActivity extends BaseActivity {
    @BindView(R.id.iv_car_goods)
    ImageView ivCarGoods;
    @BindView(R.id.tv_car_goods_introduce)
    TextView tvCarGoodsIntroduce;
    @BindView(R.id.tv_car_goods_color)
    TextView tvCarGoodsColor;
    @BindView(R.id.tv_car_goods_size)
    TextView tvCarGoodsSize;
    @BindView(R.id.tv_car_goods_price)
    TextView tvCarGoodsPrice;
    @BindView(R.id.tv_car_goods_count)
    TextView tvCarGoodsCount;
    @BindView(R.id.rb_decribe_star)
    RatingBar rbDecribeStar;
    @BindView(R.id.rb_price_star)
    RatingBar rbPriceStar;
    @BindView(R.id.rb_quality_star)
    RatingBar rbQualityStar;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_publish_comment)
    Button btnPublishComment;
    private View headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_comment);
        ButterKnife.bind(this);
        initHeaderView();
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "发表评论");
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
