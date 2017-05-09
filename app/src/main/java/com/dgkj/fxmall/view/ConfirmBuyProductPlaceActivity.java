package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.view.myView.PayDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmBuyProductPlaceActivity extends BaseActivity {

    @BindView(R.id.tv_edit_car_minus)
    TextView tvEditCarMinus;
    @BindView(R.id.tv_edit_car_count)
    TextView tvEditCarCount;
    @BindView(R.id.tv_edit_car_add)
    TextView tvEditCarAdd;
    @BindView(R.id.iv_back)
    FrameLayout ivBack;
    @BindView(R.id.tv_sum_price)
    TextView tvSumPrice;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private View headerview;
    private float singlePrice;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_buy_product_place);
        ButterKnife.bind(this);
        initHeaderView();
        //TODO 动态获取展位单价
        setListener();
    }

    private void setListener() {

        tvEditCarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count += 1;
                tvEditCarCount.setText(count + "");
                tvSumPrice.setText("¥" + singlePrice * count);
            }
        });
        tvEditCarMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count -= 1;
                tvEditCarCount.setText(count + "");
                tvSumPrice.setText("¥" + singlePrice * count);
            }
        });
    }


    @OnClick(R.id.btn_submit)
    public void goBuy(){
        //TODO 确定支付id
        PayDialog dialog = new PayDialog(this,0);
        dialog.show(getSupportFragmentManager(),"");
    }


    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "购买展位");
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    public View getContentView() {
        return null;
    }
}
