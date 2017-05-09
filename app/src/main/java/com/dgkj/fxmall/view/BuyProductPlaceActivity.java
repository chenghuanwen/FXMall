package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuyProductPlaceActivity extends BaseActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_buy)
    Button btnBuy;
    private View headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product_place);
        ButterKnife.bind(this);
        initHeaderView();
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "发布商品");
    }

    @OnClick(R.id.btn_buy)
    public void buyPlace(){
        jumpTo(ConfirmBuyProductPlaceActivity.class,true);
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }

    @Override
    public View getContentView() {
        return null;
    }
}
