package com.dgkj.fxmall.view;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.StoreBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class StoreMsgActivity extends BaseActivity {


    @BindView(R.id.civ_store_icon)
    CircleImageView civStoreIcon;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.iv_store_msg_stars)
    ImageView ivStoreMsgStars;
    @BindView(R.id.iv_store_stars)
    ImageView ivStoreStars;
    @BindView(R.id.tv_store_sales)
    TextView tvStoreSales;
    @BindView(R.id.tv_store_goods)
    TextView tvStoreGoods;
    @BindView(R.id.tv_has_checked)
    TextView tvHasChecked;
    @BindView(R.id.tv_create_time_adress)
    TextView tvCreateTimeAdress;
    @BindView(R.id.tv_tore_msg_introduce)
    TextView tvToreMsgIntroduce;
    @BindView(R.id.tv_store_describe_ok)
    TextView tvStoreDescribeOk;
    @BindView(R.id.tv_store_price_ok)
    TextView tvStorePriceOk;
    @BindView(R.id.tv_store_quality_ok)
    TextView tvStoreQualityOk;
    @BindView(R.id.ib_store_msg_back)
    ImageButton ibStoreMsgBack;
    @BindView(R.id.activity_store_msg)
    LinearLayout activityStoreMsg;
    private StoreBean store;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_msg);
        ButterKnife.bind(this);

        setData();
    }

    @Override
    public View getContentView() {
        return null;
    }


    private void setData() {
        store = (StoreBean) getIntent().getSerializableExtra("store");
        if (store == null) {
            return;
        }
        Glide.with(this).load(store.getIconUrl()).placeholder(R.mipmap.android_quanzi).into(civStoreIcon);
        //TODO 设置商品评分图片
        double stars = (store.getDescribeScore() + store.getQualityScore() + store.getPriceScore()) / 3;
        if(stars<=1.0){
            ivStoreMsgStars.setImageResource(R.mipmap.dpzy_dj1);
        }else if(stars>1.0 && stars<1.9){
            ivStoreMsgStars.setImageResource(R.mipmap.yb);
        }else if(stars>=1.9 && stars<=2.4){
            ivStoreMsgStars.setImageResource(R.mipmap.dpzy_dj2);
        }else if(stars>2.4 && stars<2.9){
            ivStoreMsgStars.setImageResource(R.mipmap.eb);
        }else if(stars>=2.9 && stars<=3.4){
            ivStoreMsgStars.setImageResource(R.mipmap.dpzy_dj3);
        }else if(stars>3.4 && stars<=3.9){
            ivStoreMsgStars.setImageResource(R.mipmap.sb);
        }else if(stars>3.9 && stars<=4.4){
            ivStoreMsgStars.setImageResource(R.mipmap.dpzy_dj4);
        }else if(stars>4.4 && stars<=4.9){
            ivStoreMsgStars.setImageResource(R.mipmap.sib);
        }else if(stars>4.9){
            ivStoreMsgStars.setImageResource(R.mipmap.dpzy_dj5);
        }

        tvStoreName.setText(store.getName());
        tvStoreSales.setText("销售总量" + store.getTotalScals());
        tvStoreGoods.setText("宝贝数" + store.getGoodsCount());
        tvCreateTimeAdress.setText(String.format("创建时间：%s I %s", store.getCreateTime(), store.getAdress()));
        tvStoreDescribeOk.setText(store.getDescribeScore()+"");
        tvStorePriceOk.setText(store.getPriceScore()+"");
        tvStoreQualityOk.setText(store.getQualityScore()+"");

        //TODO 判断是否已实名认证
        if (store.isHasRealNameCheck()) {
            tvHasChecked.setText("已通过分销商资格名认证");
        } else {
            tvHasChecked.setText("还未通过分销商城资格认证");
        }

    }

    @OnClick(R.id.ib_store_msg_back)
    public void back() {
        finish();
    }
}
