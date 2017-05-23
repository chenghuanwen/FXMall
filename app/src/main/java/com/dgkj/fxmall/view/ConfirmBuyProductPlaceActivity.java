package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.view.myView.PayDialogforByPlace;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    @BindView(R.id.tv_singlePrice)
    TextView tvSinglePrice;
    private View headerview;
    private double singlePrice;
    private int count = 1;
    private OkHttpClient client = new OkHttpClient.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_buy_product_place);
        ButterKnife.bind(this);
        initHeaderView();
        //TODO 动态获取展位单价
        getProductPlacePrice();
        setListener();
    }

    private void getProductPlacePrice() {
        Request request = new Request.Builder()
                .url(FXConst.GET_PRODUCT_PLACE_PRICE_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if (string.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(string);
                        singlePrice = object.getDouble("total");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvSinglePrice.setText("单价:¥"+singlePrice);
                                tvSumPrice.setText("¥" + singlePrice * count);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
    public void goBuy() {
        //TODO 确定支付id
        PayDialogforByPlace dialog = new PayDialogforByPlace(this, count);
        dialog.show(getSupportFragmentManager(), "");
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
