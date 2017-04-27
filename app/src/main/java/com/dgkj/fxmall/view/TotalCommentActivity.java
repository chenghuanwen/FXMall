package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.CommentAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.CommentBean;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetProductCommentListFinishListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

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
    private List<CommentBean> commentList;
    private CommentAdapter adapter;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private FXMallControl control = new FXMallControl();
    private MainProductBean product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_comment);
        ButterKnife.bind(this);
        initHeaderView();
        initData();
        refresh();
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void refresh() {
        control.getProductComments(this, product.getId(), 1, 50, client, new OnGetProductCommentListFinishListener() {
            @Override
            public void onGetProductCommentListFinished(List<CommentBean> comments) {
                adapter.addAll(comments,true);
            }
        });
    }

    private void initData() {
        product = (MainProductBean) getIntent().getSerializableExtra("product");

        int describeScore = product.getDescribeScore();
        int priceScore = product.getPriceScore();
        int qualityScore = product.getQualityScore();
        double totalScore = product.getTotalScore();
        tvDescribeCount.setText(describeScore +"");
        tvPriceCount.setText(priceScore +"");
        tvQualityCount.setText(qualityScore +"");
        tvTotalStars.setText(totalScore +"");

        setStarsPicture(totalScore,ivTotalStars);
        setStarsPicture(describeScore,ivDescribeStar);
        setStarsPicture(qualityScore,ivQualityStar);
        setStarsPicture(priceScore,ivPriceStar);


        commentList = new ArrayList<>();
        adapter = new CommentAdapter(this,R.layout.item_comment,commentList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvAllComment.setLayoutManager(layoutManager);
        rvAllComment.setAdapter(adapter);
    }

    /**
     * 设置评分图片
     */
    private void setStarsPicture(double score, ImageView iv) {
        if(score<=1.0){
            iv.setImageResource(R.mipmap.dppj_dj1);
        }else if(score>1.0 && score<1.9){
            iv.setImageResource(R.mipmap.lan_yiban);
        }else if(score>=1.9 && score<=2.4){
            iv.setImageResource(R.mipmap.dppj_dj2);
        }else if(score>2.4 && score<2.9){
            iv.setImageResource(R.mipmap.lan_erban);
        }else if(score>=2.9 && score<=3.4){
            iv.setImageResource(R.mipmap.dppj_dj3);
        }else if(score>3.4 && score<=3.9){
            iv.setImageResource(R.mipmap.lan_sanban);
        }else if(score>3.9 && score<=4.4){
            iv.setImageResource(R.mipmap.dppj_dj4);
        }else if(score>4.4 && score<=4.9){
            iv.setImageResource(R.mipmap.lan_siban);
        }else if(score>4.9){
            iv.setImageResource(R.mipmap.dppj_dj5);
        }
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
