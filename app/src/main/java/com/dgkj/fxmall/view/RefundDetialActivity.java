package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.OrderBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.TimeFormatUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class RefundDetialActivity extends BaseActivity {

    @BindView(R.id.tv_refund_type)
    TextView tvRefundType;
    @BindView(R.id.tv_refund_money)
    TextView tvRefundMoney;
    @BindView(R.id.tv_refund_reason)
    TextView tvRefundReason;
    @BindView(R.id.tv_refund_describe)
    TextView tvRefundDescribe;
    @BindView(R.id.tv_refund_number)
    TextView tvRefundNumber;
    @BindView(R.id.tv_refund_time)
    TextView tvRefundTime;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.iv_evidence1)
    ImageView ivEvidence1;
    @BindView(R.id.iv_evidence2)
    ImageView ivEvidence2;
    @BindView(R.id.iv_evidence3)
    ImageView ivEvidence3;
   // @BindView(R.id.tv_refund_state)
    TextView tvRefundState;
  //  @BindView(R.id.tv_user_type)
    TextView tvUserType;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.activity_refund_detial)
    LinearLayout activityRefundDetial;
    private View headerview;
    private LinearLayout contentView;
    private int storeId, orderId;
    private String state = "", from = "",storeName="";
    private boolean hasDeliver;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private ArrayList<String> evidences = new ArrayList<>();
    private String money="",timeFormat="",reason="",number="",refundType="",explain="-";
    private int refundId;
    private OrderBean orderBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //根据退款不同状态显示不同的界面
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        orderBean = (OrderBean) getIntent().getSerializableExtra("logist");
        storeId = orderBean.getStoreId();
        orderId = orderBean.getId();
        state = orderBean.getState();

        switch (state) {
            //买家退款详情
            case "商家同意退款":

                setContentView(R.layout.activity_refund_detial_for_logistics);
                Button btnLog = (Button) findViewById(R.id.btn_refused_apply_logistics);
                tvRefundState = (TextView) findViewById(R.id.tv_refund_state);
                tvRefundState.setText("商家同意退款");
                btnLog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//提交物流信息，再次进入为查看物流信息
                        Intent intent = new Intent(RefundDetialActivity.this, SubmitLogisticsMsgActivity.class);
                        //intent.putExtra("order",superOrderBean.getSubOrders().get(0));
                        startActivity(intent);
                    }
                });
                break;
            case "退款成功":

                setContentView(R.layout.activity_refund_detial_for_successed);
                tvRefundState = (TextView) findViewById(R.id.tv_refund_state);
                tvRefundState.setText("退款成功");
                break;
            case "等待商家处理退款申请":
                //LogUtil.i("TAG","退款详情=========="+tvRefundType);

                setContentView(R.layout.activity_refund_detial_for_change);

                Button btnChange = (Button) findViewById(R.id.btn_refused_apply_change);
                tvRefundState = (TextView) findViewById(R.id.tv_refund_state);
                tvRefundState.setText("等待商家处理退款申请");
                btnChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//修改退款申请
                        Intent intent1 = new Intent(RefundDetialActivity.this,ApplyRefundActivity.class);
                        intent1.putExtra("from","refund");
                        intent1.putExtra("explain",explain);
                        intent1.putExtra("money",money);
                        intent1.putExtra("number",number);
                        intent1.putExtra("reason",reason);
                        intent1.putExtra("timeFormat",timeFormat);
                        intent1.putExtra("refundType",refundType);
                        intent1.putExtra("storeName",storeName);
                        intent1.putExtra("refundId",refundId);
                        intent1.putStringArrayListExtra("evidences",evidences);
                        startActivity(intent1);
                    }
                });
                break;
            case "商家拒绝退款":

                setContentView(R.layout.activity_refund_detial_for_change);
                tvRefundState = (TextView) findViewById(R.id.tv_refund_state);
                tvRefundState.setText("商家拒绝退款");
                Button btnChange1 = (Button) findViewById(R.id.btn_refused_apply_change);
                btnChange1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//修改退款申请
                        Intent intent1 = new Intent(RefundDetialActivity.this,ApplyRefundActivity.class);
                        intent1.putExtra("from","refund");
                        intent1.putExtra("explain",explain);
                        intent1.putExtra("money",money);
                        intent1.putExtra("number",number);
                        intent1.putExtra("reason",reason);
                        intent1.putExtra("timeFormat",timeFormat);
                        intent1.putExtra("refundType",refundType);
                        intent1.putExtra("storeName",storeName);
                        intent1.putExtra("refundId",refundId);
                        intent1.putStringArrayListExtra("evidences",evidences);
                        startActivity(intent1);
                    }
                });
                break;

            //卖家退款详情
            case "等待买家发货":

                setContentView(R.layout.activity_refund_detial_for_logistics);
                tvRefundState = (TextView) findViewById(R.id.tv_refund_state);
                tvRefundState.setText("等待买家发货");
                Button btn = (Button) findViewById(R.id.btn_refused_apply_logistics);
                btn.setText("提醒发货");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case "等待收货":

                setContentView(R.layout.activity_refund_detial_for_store);
                tvRefundState = (TextView) findViewById(R.id.tv_refund_state);
                tvRefundState.setText("等待收货");
                Button btnRefuse = (Button) findViewById(R.id.btn_refused_apply);
                Button btnAgree = (Button) findViewById(R.id.btn_agree_apply);
                btnAgree.setText("同意退款");
                btnRefuse.setText("物流信息");
                btnAgree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//同意退款
                        confirmRefund(orderBean);
                    }
                });
                btnRefuse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//物流信息
                        Intent intent = new Intent(RefundDetialActivity.this,LogisticsDetialActivity.class);
                        intent.putExtra("order",orderBean);
                        jumpTo(intent,false);
                    }
                });
                break;
            case "退款已完成":

                setContentView(R.layout.activity_refund_detial_for_successed);
                tvRefundState = (TextView) findViewById(R.id.tv_refund_state);
                tvRefundState.setText("退款已完成");
                break;
            case "买家申请退货/退款":

                setContentView(R.layout.activity_refund_detial_for_store);
                tvRefundState = (TextView) findViewById(R.id.tv_refund_state);
                tvRefundState.setText("买家申请退货/退款");
                TextView tvState = (TextView) findViewById(R.id.tv_refund_state);
                TextView tvEvidence = (TextView) findViewById(R.id.tv_evidence);
                LinearLayout tvContainer = (LinearLayout) findViewById(R.id.ll_evidence_container);
                if (hasDeliver) {
                    tvState.setText("买家申请退款!(商品已发出)");
                    tvContainer.setVisibility(View.VISIBLE);
                    tvEvidence.setVisibility(View.VISIBLE);
                } else {
                    tvState.setText("买家申请退款!(商品未发出)");
                    tvContainer.setVisibility(View.INVISIBLE);
                    tvEvidence.setVisibility(View.INVISIBLE);
                }
                Button btnRefuse1 = (Button) findViewById(R.id.btn_refused_apply);
                Button btnAgree1 = (Button) findViewById(R.id.btn_agree_apply);
                btnAgree1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//同意申请
                        agreeRefund(orderBean);
                    }
                });
                btnRefuse1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//拒绝申请
                        refusedRefund(orderBean);
                    }
                });
                break;
            case "已拒绝申请":

                setContentView(R.layout.activity_refund_detial_for_successed);
                tvRefundState = (TextView) findViewById(R.id.tv_refund_state);
                tvRefundState.setText("已拒绝申请");
                break;
        }

        tvUserType = (TextView) findViewById(R.id.tv_user_type);
        if ("store".equals(from)) {
            storeName = orderBean.getBuyer();
            hasDeliver = intent.getBooleanExtra("isDeliver", false);
            tvUserType.setText("买家名称");
        }else {
            tvUserType.setText("店铺名称");
            storeName = orderBean.getStoreName();
        }

        ButterKnife.bind(this);
        initHeaderView();
        contentView = (LinearLayout) findViewById(R.id.activity_refund_detial);
        getData();
    }


    /**
     * 获取退款详情
     */
    private void getData() {
        FormBody body = new FormBody.Builder()
                .add("store.id", storeId + "".trim())
                .add("orderCommodity.id", orderId + "".trim())
                .build();
        LogUtil.i("TAG","退款详情参数==="+storeId+"==="+orderId);
        final Request request = new Request.Builder()
                .post(body)
                .url(FXConst.GET_REFUND_DETIAL_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                LogUtil.i("TAG","退款详情========"+string);
                if (string.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(string);
                        JSONArray dataset = object.getJSONArray("dataset");
                        JSONObject jsonObject = dataset.getJSONObject(0);
                        if(jsonObject.has("evidence")){
                            String evidence = jsonObject.getString("evidence");
                            JSONArray urls = new JSONArray(evidence);
                            for (int i = 0; i < urls.length(); i++) {
                                evidences.add(urls.getString(i));
                            }
                        }
                        money = jsonObject.getString("money");
                         reason = jsonObject.getString("reason");
                         number = jsonObject.getString("number");
                        refundId = jsonObject.getInt("id");

                        if(jsonObject.has("explain")){
                            explain = jsonObject.getString("explain");
                        }
                        long time = jsonObject.getLong("time");
                        timeFormat = TimeFormatUtils.long2String(time);
                        int type = jsonObject.getInt("type");
                        if(type==1){
                            refundType = "退货退款";
                        }else {
                            refundType = "仅退款";
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvRefundDescribe.setText(explain);
                                tvRefundMoney.setText(money);
                                tvRefundNumber.setText(number);
                                tvRefundReason.setText(reason);
                                tvRefundTime.setText(timeFormat);
                                tvRefundType.setText(refundType);
                                tvUserName.setText(storeName);
                                if(evidences.size()==1){
                                    Glide.with(RefundDetialActivity.this).load(evidences.get(0)).error(R.mipmap.android_quanzi).into(ivEvidence1);
                                    ivEvidence2.setVisibility(View.INVISIBLE);
                                    ivEvidence3.setVisibility(View.INVISIBLE);
                                }else if (evidences.size()==3){
                                    Glide.with(RefundDetialActivity.this).load(evidences.get(0)).error(R.mipmap.android_quanzi).into(ivEvidence1);
                                    Glide.with(RefundDetialActivity.this).load(evidences.get(1)).error(R.mipmap.android_quanzi).into(ivEvidence2);
                                    Glide.with(RefundDetialActivity.this).load(evidences.get(2)).error(R.mipmap.android_quanzi).into(ivEvidence3);
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    /**
     * 卖家拒绝退款
     * @param orderBean
     */
    private void refusedRefund(OrderBean orderBean) {
        FormBody body = new FormBody.Builder()
                .add("orderCommodity.orders.user.token",sp.get("token"))
                .add("orderCommodity.sku.id",orderBean.getSkuId()+"")
                .add("orderCommodity.orders.id",orderBean.getId()+"")
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.STORE_REFUSED_REFUND_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(RefundDetialActivity.this,"网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                    toastInUI(RefundDetialActivity.this,"已拒绝退款申请");
                }else {
                    toastInUI(RefundDetialActivity.this,"操作失败，请稍后重试！");
                }
            }
        });
    }

    /**
     *卖家同意退款
     * @param orderBean
     */
    private void agreeRefund(OrderBean orderBean) {
        FormBody body = new FormBody.Builder()
                .add("orders.user.token",sp.get("token"))
                .add("id",orderBean.getRefundId()+"")
                .add("orders.id",orderBean.getId()+"")
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.STORE_AGREE_REFUND_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(RefundDetialActivity.this,"网络异常！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                    toastInUI(RefundDetialActivity.this,"已同意退款");
                }else {
                    toastInUI(RefundDetialActivity.this,"操作失败，请稍后重试！");
                }
            }
        });
    }


    /**
     * 卖家确认退款
     * @param orderBean
     */
    private void confirmRefund(OrderBean orderBean) {
        FormBody body = new FormBody.Builder()
                .add("orderCommodity.orders.user.token",sp.get("token"))
                .add("orderCommodity.sku.id",orderBean.getSkuId()+"")
                .add("orderCommodity.orders.id",orderBean.getId()+"")
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.STORE_CONFIRM_REFUND)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(RefundDetialActivity.this,"网络异常！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                    toastInUI(RefundDetialActivity.this,"已确认退款！");
                }else {
                    toastInUI(RefundDetialActivity.this,"操作失败，请稍后重试！");
                }
            }
        });
    }




    @Override
    public View getContentView() {
        return contentView;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "退款详情");
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
