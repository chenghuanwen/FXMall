package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.OrderBean;
import com.dgkj.fxmall.constans.FXConst;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SubmitLogisticsMsgActivity extends BaseActivity {
    @BindView(R.id.tv_agress_refund_time)
    TextView tvAgressRefundTime;
    @BindView(R.id.tv_take_man)
    TextView tvTakeMan;
    @BindView(R.id.tv_take_phone)
    TextView tvTakePhone;
    @BindView(R.id.tv_take_postcode)
    TextView tvTakePostcode;
    @BindView(R.id.tv_take_address)
    TextView tvTakeAddress;
    @BindView(R.id.tv_take_express)
    TextView tvTakeExpress;
    @BindView(R.id.tv_express_number)
    EditText tvExpressNumber;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private View headerview;
    private OrderBean order;
    private OkHttpClient client = new OkHttpClient.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_logistics_msg);
        ButterKnife.bind(this);
        initHeaderView();

        order = (OrderBean) getIntent().getSerializableExtra("order");
        tvAgressRefundTime.setText(order.getAgreeRefundTime());
        tvTakeMan.setText(order.getStoreUser());
        tvTakeAddress.setText(order.getStoreAddress());
        tvTakePhone.setText(order.getStorePhone());
        tvTakeExpress.setText(order.getExpress());
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "提交物流信息");
    }

    @OnClick(R.id.btn_submit)
    public void submit(){
        String expressNumber = tvExpressNumber.getText().toString();
        if(TextUtils.isEmpty(expressNumber)){
            toast("请填写物流正确单号");
            return;
        }
        FormBody body = new FormBody.Builder()
                .add("orderCommodity.orders.user.token",sp.get("token"))
                .add("orderCommodity.sku.id",order.getSkuId()+"")
                .add("orderCommodity.orders.id",order.getId()+"")
                .add("expressCode.id",order.getExpressCodeId()+"")
                .add("waybill",expressNumber)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.USER_SUBMIT_LOGISTICS_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(SubmitLogisticsMsgActivity.this,"网络异常！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                    toastInUI(SubmitLogisticsMsgActivity.this,"提交成功！");
                    finish();
                }else {
                    toastInUI(SubmitLogisticsMsgActivity.this,"提交失败！");
                }
            }
        });
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
