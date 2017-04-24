package com.dgkj.fxmall.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.OrderBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.TimeFormatUtils;
import com.dgkj.fxmall.view.myView.PayDialog;

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

public class OrderDetialActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.tv_order_state)
    TextView tvOrderState;
    @BindView(R.id.tv_order_take_man)
    TextView tvOrderTakeMan;
    @BindView(R.id.tv_order_take_phone)
    TextView tvOrderTakePhone;
    @BindView(R.id.tv_order_take_address)
    TextView tvOrderTakeAddress;
    @BindView(R.id.tv_order_store_name)
    TextView tvOrderStoreName;
    @BindView(R.id.iv_car_goods)
    ImageView ivCarGoods;
    @BindView(R.id.tv_car_goods_introduce)
    TextView tvCarGoodsIntroduce;
    @BindView(R.id.tv_car_goods_color)
    TextView tvCarGoodsColor;
  /*  @BindView(R.id.tv_car_goods_size)
    TextView tvCarGoodsSize;*/
    @BindView(R.id.tv_car_goods_price)
    TextView tvCarGoodsPrice;
    @BindView(R.id.tv_car_goods_count)
    TextView tvCarGoodsCount;
    @BindView(R.id.tv_order_getMoney)
    TextView tvOrderGetMoney;
  /*  @BindView(R.id.tv_buyers_feedback)
    TextView tvBuyersFeedback;*/
    @BindView(R.id.tv_order_express)
    TextView tvOrderExpress;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNum;
    @BindView(R.id.tv_order_create_time)
    TextView tvCreateTime;

    private View headerview;
    private OrderBean order;
    private TextView tvChangeTakeAddress,tvCancelOrder,tvApplyRefund,tvLogistics,tvDeleteOrder;
    private TextView tvPayTime;
    private Button btnPay,btnNotifyDeliver,btnConfirmTake,btnComment,btnCheckLogistics;
    private OkHttpClient client = new OkHttpClient.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO 根据不同的订单状态选择不同的界面，各界面内不同的控件由findviewbyid查找
        order = (OrderBean) getIntent().getSerializableExtra("order");
        int stateNum = order.getStateNum();
        switch (stateNum){
            case 0:
                setContentView(R.layout.activity_order_detial_for_wait_pay);//待付款
                tvChangeTakeAddress = (TextView) findViewById(R.id.tv_chagne_address);
                tvCancelOrder = (TextView) findViewById(R.id.tv_cancle_order);
                tvCreateTime = (TextView) findViewById(R.id.tv_order_create_time);
                tvOrderNum = (TextView) findViewById(R.id.tv_order_number);
                btnPay = (Button) findViewById(R.id.btn_pay);
                tvChangeTakeAddress.setOnClickListener(this);
                tvCancelOrder.setOnClickListener(this);
                btnPay.setOnClickListener(this);
                break;
            case 1:
                setContentView(R.layout.activity_order_detial_for_user_wait_deliver);//待发货
               // setContentView(R.layout.activity_order_detial_for_store_wait_deliver);//待发货
                tvApplyRefund = (TextView) findViewById(R.id.tv_apply_refund);
                tvPayTime = (TextView) findViewById(R.id.tv_order_pay_time);
                tvPayTime.setText("付款时间："+ TimeFormatUtils.long2String(order.getPayTime()));
                btnNotifyDeliver = (Button) findViewById(R.id.btn_notify_deliver);
                tvApplyRefund.setOnClickListener(this);
                btnNotifyDeliver.setOnClickListener(this);
                break;
            case 2:
                setContentView(R.layout.activity_order_detial_for_wait_take);//待收货
                tvLogistics = (TextView) findViewById(R.id.tv_logistics);
                tvApplyRefund = (TextView) findViewById(R.id.tv_apply_refund);
                btnConfirmTake = (Button) findViewById(R.id.btn_confirm_take);
                tvPayTime = (TextView) findViewById(R.id.tv_order_pay_time);
                tvPayTime.setText("付款时间："+ TimeFormatUtils.long2String(order.getPayTime()));
                tvLogistics.setOnClickListener(this);
                tvApplyRefund.setOnClickListener(this);
                btnConfirmTake.setOnClickListener(this);
                break;
            case 3:
                setContentView(R.layout.activity_order_detial_for_complete);//交易完成
                tvLogistics = (TextView) findViewById(R.id.tv_logistics);
                tvDeleteOrder = (TextView) findViewById(R.id.tv_delete_order);
                tvPayTime = (TextView) findViewById(R.id.tv_order_pay_time);
                tvPayTime.setText("付款时间："+ TimeFormatUtils.long2String(order.getPayTime()));
                btnComment = (Button) findViewById(R.id.btn_comment);
                tvLogistics.setOnClickListener(this);
                tvDeleteOrder.setOnClickListener(this);
                btnComment.setOnClickListener(this);
                break;
            case 4:
                setContentView(R.layout.activity_order_detial_for_wait_comment);//交易完成
                tvLogistics = (TextView) findViewById(R.id.tv_logistics);
                tvDeleteOrder = (TextView) findViewById(R.id.tv_delete_order);
                tvPayTime = (TextView) findViewById(R.id.tv_order_pay_time);
                tvPayTime.setText("付款时间："+ TimeFormatUtils.long2String(order.getPayTime()));
                btnComment = (Button) findViewById(R.id.btn_comment);
                tvLogistics.setOnClickListener(this);
                tvDeleteOrder.setOnClickListener(this);
                btnComment.setOnClickListener(this);
                break;
            case 5:
                setContentView(R.layout.activity_order_detial_for_has_deliver);//已发货
                btnCheckLogistics = (Button) findViewById(R.id.btn_logistics_msg);
                btnCheckLogistics.setOnClickListener(this);
                break;
        }
        ButterKnife.bind(this);
        initHeaderView();
        setData();
    }

    private void setData() {
        tvOrderTakeMan.setText("收货人："+order.getTakeMan());
        tvOrderTakePhone.setText(order.getTakePhone());
        tvOrderTakeAddress.setText(order.getTakeAddress());
        tvOrderStoreName.setText(order.getStoreName());
        Glide.with(this).load(order.getUrl()).into(ivCarGoods);
        tvCarGoodsIntroduce.setText(order.getIntroduce());
        tvCarGoodsColor.setText(order.getColor());
        tvCarGoodsPrice.setText("¥"+order.getSinglePrice());
        tvCarGoodsPrice.setText("x"+order.getCount());
        tvOrderGetMoney.setText("¥"+order.getSinglePrice()*order.getCount());
        tvOrderExpress.setText("¥"+order.getPostage());
        tvOrderNum.setText("订单编号:"+order.getOrderNum());
        tvCreateTime.setText("创建时间："+ TimeFormatUtils.long2String(order.getCreateTime()));

    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "订单详情");
    }


    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancle_order://取消订单
                showCancleDialog("取消订单");
                break;
            case R.id.tv_delete_order://删除订单
                //TODO 同时删除本地和服务器中数据
                break;
            case R.id.tv_logistics://物流信息
                Intent intent = new Intent(OrderDetialActivity.this,LogisticsDetialActivity.class);
                intent.putExtra("orderId",order.getId());
                jumpTo(intent,false);
                break;
            case R.id.tv_apply_refund://申请退款
                showCancleDialog("申请退款");
                break;
            case R.id.tv_chagne_address://修改收货地址
                startActivityForResult(new Intent(OrderDetialActivity.this,TakeGoodsAdressActivity.class),171);
                break;
            case R.id.btn_pay://付款
                PayDialog dialog = new PayDialog(OrderDetialActivity.this);
                dialog.show(getSupportFragmentManager(), "");
                break;
            case R.id.btn_notify_deliver://提醒发货
                Toast toast = new Toast(OrderDetialActivity.this);
                View view = getLayoutInflater().inflate(R.layout.layout_toast_dialog, null);
                TextView tvContent = (TextView) view.findViewById(R.id.tv_toast_content);
                tvContent.setText("已提醒店家发货");
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
                //TODO 服务器提醒发货
                break;
            case R.id.btn_logistics_msg://查看物流信息
                Intent intent1 = new Intent(OrderDetialActivity.this,LogisticsDetialActivity.class);
                intent1.putExtra("orderId",order.getId());
                jumpTo(intent1,false);
                break;
            case R.id.btn_confirm_take://确认收货
                showDeleteDialog();
                break;
            case R.id.btn_comment://去评论
                Intent intent2 = new Intent(OrderDetialActivity.this,PublishCommentActivity.class);
                intent2.putExtra("orderId",order.getId());
                jumpTo(intent2,false);
                break;



            //弹窗点击处理
            case R.id.tv_reason1:
                cancleReason = tvReason1.getText().toString();
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.GONE);
                iv3.setVisibility(View.GONE);
                iv4.setVisibility(View.GONE);

                break;
            case R.id.tv_reason2:
                cancleReason = tvReason2.getText().toString();
                iv1.setVisibility(View.GONE);
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.GONE);
                iv4.setVisibility(View.GONE);
                break;
            case R.id.tv_reason3:
                cancleReason = tvReason3.getText().toString();
                iv1.setVisibility(View.GONE);
                iv2.setVisibility(View.GONE);
                iv3.setVisibility(View.VISIBLE);
                iv4.setVisibility(View.GONE);
                break;
            case R.id.tv_reason4:
                cancleReason = tvReason4.getText().toString();
                iv1.setVisibility(View.GONE);
                iv2.setVisibility(View.GONE);
                iv3.setVisibility(View.GONE);
                iv4.setVisibility(View.VISIBLE);
                break;
        }
    }


    private TextView tvReason1,tvReason2,tvReason3,tvReason4;
    private ImageView iv1,iv2,iv3,iv4;
    private String cancleReason;
    private AlertDialog pw;
    private void showCancleDialog(String type){
        View contentview = getLayoutInflater().inflate(R.layout.layout_reason_of_cancel_order, null);
        pw = new AlertDialog.Builder(this).create();
        pw.setView(contentview);
        TextView tvType = (TextView) contentview.findViewById(R.id.tv_cancle_type);
        tvType.setText("请选择的"+type+"原因");
        tvReason1 = (TextView) contentview.findViewById(R.id.tv_reason1);
        tvReason4 = (TextView) contentview.findViewById(R.id.tv_reason4);
        tvReason3 = (TextView) contentview.findViewById(R.id.tv_reason3);
        tvReason2 = (TextView) contentview.findViewById(R.id.tv_reason2);
        iv1 = (ImageView) contentview.findViewById(R.id.iv_confirm1);
        iv2 = (ImageView) contentview.findViewById(R.id.iv_confirm2);
        iv3 = (ImageView) contentview.findViewById(R.id.iv_confirm3);
        iv4 = (ImageView) contentview.findViewById(R.id.iv_confirm4);
        tvReason1.setOnClickListener(this);
        tvReason2.setOnClickListener(this);
        tvReason3.setOnClickListener(this);
        tvReason4.setOnClickListener(this);


        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_confirm);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 向服务器发送取消请求
                cancelOrder(order.getId(),cancleReason);

                pw.dismiss();
            }
        });
        TextView tvBoy = (TextView) contentview.findViewById(R.id.tv_cancle);
        tvBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });


        //设置触摸对话框以外区域，对话框消失
        pw.setCanceledOnTouchOutside(true);
        pw.show();
    }


    /**
     * 取消订单
     * @param id 订单ID
     * @param cancleReason 取消理由
     */
    private void cancelOrder(int id, String cancleReason) {
        FormBody body = new FormBody.Builder()
                .add("user.token",sp.get("token"))
                .add("id",id+"")
                .add("reason",cancleReason)//TODO 取消理由字段待确定
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.CANCEL_ORDER_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               toastInUI(OrderDetialActivity.this,"网络异常！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                   toastInUI(OrderDetialActivity.this,"订单已取消");
                }
            }
        });
    }

    private void showDeleteDialog(){
        View contentview = getLayoutInflater().inflate(R.layout.layout_delete_dialog, null);
        final AlertDialog pw = new AlertDialog.Builder(this).create();
        pw.setView(contentview);
        TextView tvConfirm = (TextView) contentview.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pw.dismiss();
            }
        });
        TextView tvCancel = (TextView) contentview.findViewById(R.id.tv_cancle);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });


        //设置触摸对话框以外区域，对话框消失
        pw.setCanceledOnTouchOutside(false);
        pw.show();
    }

}
