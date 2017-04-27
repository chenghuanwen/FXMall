package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.OrderBean;
import com.dgkj.fxmall.bean.SuperOrderBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.view.myView.XLHRatingBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.attr.order;

public class PublishCommentActivity extends BaseActivity {

    @BindView(R.id.activity_publish_comment)
    LinearLayout activityPublishComment;
    @BindView(R.id.ll_comments_container)
    LinearLayout llCommentsContainer;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_publish_comment)
    Button btnPublishComment;
    private View headerview;
    private SuperOrderBean superOrder;
    private List<OrderBean> subOrders;
    private List<View> viewList = new ArrayList<>();
    private int[] productIds;
    private int ratPrice, ratQuality, ratDescribe;
    private OkHttpClient client = new OkHttpClient.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_comment);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initHeaderView();

        superOrder = (SuperOrderBean) getIntent().getSerializableExtra("order");
        subOrders = superOrder.getSubOrders();
        productIds = new int[subOrders.size()];
        setData();

    }

    @Override
    public View getContentView() {
        return activityPublishComment;
    }

    private void setData() {
        for (int i = 0; i < subOrders.size(); i++) {
            OrderBean order = subOrders.get(i);
            View view = getLayoutInflater().inflate(R.layout.layout_publish_comment_common, llCommentsContainer, false);
            TextView tvColor = (TextView) view.findViewById(R.id.tv_car_goods_color);
            TextView tvPrice = (TextView) view.findViewById(R.id.tv_car_goods_price);
            TextView tvCuont = (TextView) view.findViewById(R.id.tv_car_goods_count);
            TextView tvIntroduce = (TextView) view.findViewById(R.id.tv_car_goods_introduce);
            TextView tvStoreName = (TextView) view.findViewById(R.id.tv_comment_storename);
            ImageView ivCarGoods = (ImageView) view.findViewById(R.id.iv_car_goods);
            tvStoreName.setText(order.getStoreName());
            tvColor.setText(order.getColor());
            tvCuont.setText("x" + order.getCount());
            tvIntroduce.setText(order.getIntroduce());
            tvPrice.setText("¥" + order.getSinglePrice());
            Glide.with(this).load(order.getUrl()).into(ivCarGoods);
            llCommentsContainer.addView(view);
            viewList.add(view);
            productIds[i] = order.getProductId();
        }

    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "发表评论");
    }

    @OnClick(R.id.btn_publish_comment)
    public void pubish() {

        for (int i = 0; i < viewList.size(); i++) {
            View view = viewList.get(i);
            XLHRatingBar rbDecribeStar = (XLHRatingBar) view.findViewById(R.id.rb_decribe_star);
            XLHRatingBar rbPriceStar = (XLHRatingBar) view.findViewById(R.id.rb_price_star);
            XLHRatingBar rbQualityStar = (XLHRatingBar) view.findViewById(R.id.rb_quality_star);
            rbDecribeStar.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
                @Override
                public void onChange(int countSelected) {
                    ratDescribe = countSelected;
                }
            });
            rbPriceStar.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
                @Override
                public void onChange(int countSelected) {
                    ratPrice = countSelected;
                }
            });
            rbQualityStar.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
                @Override
                public void onChange(int countSelected) {
                    ratQuality = countSelected;
                }
            });
            EditText etCommentContent = (EditText) view.findViewById(R.id.et_comment_content);

            String content = etCommentContent.getText().toString();
            publishComment(content,ratPrice,ratDescribe,ratQuality,productIds[i]);
        }

    }


    /**
     * 发表评论
     *
     * @param content
     * @param ratPrice
     * @param ratDescribe
     * @param ratQuality
     */
    private void publishComment(String content, int ratPrice, int ratDescribe, int ratQuality, final int id) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("user.token", sp.get("token"))
                .add("conent", content)
                .add("commodity.id", id + "")//TODO 待评价商品ID
                .add("qualityScore", ratQuality + "")
                .add("describeScore", ratDescribe + "")
                .add("transportScore", ratPrice + "");
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(FXConst.PUBLISH_COMMENT_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(PublishCommentActivity.this, "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(id != productIds[productIds.length-1]){return;}
                if (response.body().string().contains("1000")) {
                    toastInUI(PublishCommentActivity.this, "评论成功");
                } else {
                    toastInUI(PublishCommentActivity.this, "评论失败");
                }
            }
        });
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
