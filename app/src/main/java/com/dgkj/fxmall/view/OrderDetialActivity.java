package com.dgkj.fxmall.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.OrderBean;
import com.dgkj.fxmall.bean.SuperOrderBean;
import com.dgkj.fxmall.bean.TakeGoodsAddressBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.listener.InputCompletetListener;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.OkhttpUploadUtils;
import com.dgkj.fxmall.utils.TimeFormatUtils;
import com.dgkj.fxmall.view.myView.PasswordInputView;
import com.dgkj.fxmall.view.myView.PayDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @BindView(R.id.tv_car_goods_price)
    TextView tvCarGoodsPrice;
    @BindView(R.id.tv_car_goods_count)
    TextView tvCarGoodsCount;
    @BindView(R.id.tv_order_getMoney)
    TextView tvOrderGetMoney;

    @BindView(R.id.tv_order_express)
    TextView tvOrderExpress;
    @BindView(R.id.iv_back)
    FrameLayout ivBack;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNum;
    @BindView(R.id.tv_order_create_time)
    TextView tvCreateTime;

    private View headerview;
    private SuperOrderBean superOrder;
    private OrderBean order;
    private TextView tvChangeTakeAddress,tvCancelOrder,tvApplyRefund,tvLogistics,tvDeleteOrder;
    private TextView tvPayTime;
    private Button btnPay,btnNotifyDeliver,btnConfirmTake,btnComment,btnCheckLogistics;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private LinearLayout contentView;
    private String from = "";
    private int sumPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO 根据不同的订单状态选择不同的界面，各界面内不同的控件由findviewbyid查找
        from = getIntent().getStringExtra("from");
        superOrder = (SuperOrderBean) getIntent().getSerializableExtra("order");
        order = superOrder.getSubOrders().get(0);
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
                addProductLayout(R.layout.item_order_detail_no_opration_common);
                break;
            case 1:
                setContentView(R.layout.activity_order_detial_for_user_wait_deliver);//待发货
              //  setContentView(R.layout.activity_order_detial_for_store_wait_deliver);//待发货
                tvPayTime = (TextView) findViewById(R.id.tv_order_pay_time);
                tvPayTime.setText("付款时间："+ TimeFormatUtils.long2String(order.getPayTime()));
                btnNotifyDeliver = (Button) findViewById(R.id.btn_notify_deliver);
                btnNotifyDeliver.setOnClickListener(this);
                addProductLayout(R.layout.item_order_detail_apply_refund_common);
                break;
            case 2:
                setContentView(R.layout.activity_order_detial_for_wait_take);//待收货
                tvLogistics = (TextView) findViewById(R.id.tv_logistics);
                btnConfirmTake = (Button) findViewById(R.id.btn_confirm_take);
                tvPayTime = (TextView) findViewById(R.id.tv_order_pay_time);
                tvPayTime.setText("付款时间："+ TimeFormatUtils.long2String(order.getPayTime()));
                tvLogistics.setOnClickListener(this);
                btnConfirmTake.setOnClickListener(this);
                addProductLayout(R.layout.item_order_detail_apply_refund_common);
                break;
            case 4:
                setContentView(R.layout.activity_order_detial_for_complete);//交易完成
                tvLogistics = (TextView) findViewById(R.id.tv_logistics);
                tvDeleteOrder = (TextView) findViewById(R.id.tv_delete_order);
                tvPayTime = (TextView) findViewById(R.id.tv_order_pay_time);
                tvPayTime.setText("付款时间："+ TimeFormatUtils.long2String(order.getPayTime()));
                tvLogistics.setOnClickListener(this);
                tvDeleteOrder.setOnClickListener(this);
                addProductLayout(R.layout.item_order_detail_no_opration_common);
                break;
            case 3:
                setContentView(R.layout.activity_order_detial_for_wait_comment);//待评价
                tvLogistics = (TextView) findViewById(R.id.tv_logistics);
                tvDeleteOrder = (TextView) findViewById(R.id.tv_delete_order);
                tvPayTime = (TextView) findViewById(R.id.tv_order_pay_time);
                btnComment = (Button) findViewById(R.id.btn_comment);
                tvPayTime.setText("付款时间："+ TimeFormatUtils.long2String(order.getPayTime()));
                tvLogistics.setOnClickListener(this);
                tvDeleteOrder.setOnClickListener(this);
                btnComment.setOnClickListener(this);
                addProductLayout(R.layout.item_order_detail_no_opration_common);
                break;
            case 5:
                setContentView(R.layout.activity_order_detial_for_has_deliver);//已发货
                btnCheckLogistics = (Button) findViewById(R.id.btn_logistics_msg);
                btnCheckLogistics.setOnClickListener(this);
                addProductLayout(R.layout.item_order_detail_no_opration_common);
                break;
        }

        contentView = (LinearLayout) findViewById(R.id.activity_order_detial);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ButterKnife.bind(this);
        initHeaderView();
        setData();
    }

    @Override
    public View getContentView() {
        return contentView;
    }

    private void setData() {
        tvOrderTakeMan.setText("收货人："+order.getTakeMan());
        tvOrderTakePhone.setText(order.getTakePhone());
        tvOrderTakeAddress.setText(order.getTakeAddress());
        tvOrderStoreName.setText(order.getStoreName());

        int postage = order.getPostage();
        if(postage == 0){
            tvOrderGetMoney.setText("¥"+sumPrice+"(包邮)");
        }else {
            tvOrderGetMoney.setText("¥"+sumPrice+"(含邮费¥"+ postage +")");
        }
        tvOrderNum.setText("订单编号:"+order.getOrderNum());
        tvCreateTime.setText("创建时间："+ TimeFormatUtils.long2String(order.getCreateTime()));

    }


    public void addProductLayout(int resId){
        //动态添加多个商品信息
        List<OrderBean> subOrders = superOrder.getSubOrders();
        LinearLayout orderContainer = (LinearLayout) findViewById(R.id.ll_order_detial_container);
            for (int i = 0; i < subOrders.size(); i++) {
                final OrderBean orderBean = subOrders.get(i);
                View view = getLayoutInflater().inflate(resId, orderContainer, false);

                TextView tvContent = (TextView) view.findViewById(R.id.tv_car_goods_introduce);
                TextView tvColor = (TextView) view.findViewById(R.id.tv_car_goods_color);
                TextView tvPrice = (TextView) view.findViewById(R.id.tv_car_goods_price);
                TextView tvGoodsCount = (TextView) view.findViewById(R.id.tv_car_goods_count);
                ImageView ivPhoto = (ImageView) view.findViewById(R.id.iv_car_goods);
                tvContent.setText(orderBean.getIntroduce());
                tvColor.setText("颜色："+orderBean.getColor());
                tvPrice.setText("¥"+orderBean.getSinglePrice());
                tvGoodsCount.setText("x"+orderBean.getCount());
                Glide.with(this).load(orderBean.getUrl()).placeholder(R.mipmap.android_quanzi).into(ivPhoto);
                if(resId == R.layout.item_order_detail_apply_refund_common){
                    tvApplyRefund = (TextView) view.findViewById(R.id.tv_apply_refund);
                    tvApplyRefund.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(orderBean.getStateNum()==1){//待发货
                                showCancleDialog("申请退款",orderBean);//TODO 需获取当前所点击的商品
                            }else {
                                Intent intent = new Intent(OrderDetialActivity.this,ApplyRefundActivity.class);
                               // intent.putExtra("reason",cancleReason);
                                intent.putExtra("from","order");
                                intent.putExtra("money",orderBean.getSinglePrice()*orderBean.getCount());
                                intent.putExtra("orderId",orderBean.getId());
                                intent.putExtra("skuId",orderBean.getSkuId());
                                jumpTo(intent,false);
                            }
                        }
                    });
                }

                sumPrice += orderBean.getSinglePrice()*orderBean.getCount();

                orderContainer.addView(view);

                //  TextView tvSumPrice = (TextView) view.findViewById(R.id.tv_sumPrice);
                //TextView tvPostage = (TextView) view.findViewById(R.id.tv_postage);
                // tvSumPrice.setText("¥"+(orderBean.getSinglePrice()*orderBean.getCount()));
               /* int postage = orderBean.getPostage();
                if(postage == 0){
                    tvPostage.setText("(包邮)");
                }else {
                    tvPostage.setText("(含邮费¥"+ postage +")");
                }*/
            }

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
                showCancleDialog("取消订单",null);
                break;
            case R.id.tv_delete_order://删除订单
                //TODO 同时删除本地和服务器中数据
                showDeleteDialog(from);
                break;
            case R.id.tv_logistics://物流信息
                Intent intent = new Intent(OrderDetialActivity.this,LogisticsDetialActivity.class);
                intent.putExtra("orderId",order.getId());
                jumpTo(intent,false);
                break;

            case R.id.tv_chagne_address://修改收货地址
                startActivityForResult(new Intent(OrderDetialActivity.this,TakeGoodsAdressActivity.class),171);
                break;
            case R.id.btn_pay://付款
                PayDialog dialog = new PayDialog(OrderDetialActivity.this,order.getId());
                dialog.show(getSupportFragmentManager(), "");
                break;
            case R.id.btn_notify_deliver://提醒发货
                //服务器提醒发货
                notifyDeliver(FXConst.NOTIFY_STORER_DELIVER_URL);
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
                intent2.putExtra("order",superOrder);
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

    /**
     * 提醒发货
     */
    private void notifyDeliver(String url) {
        FormBody body = new FormBody.Builder()
                .add("id",order.getId()+"".trim())
                .add("user.token",sp.get("token"))
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if(string.contains("1000")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(OrderDetialActivity.this,"",Toast.LENGTH_SHORT);
                            View view = getLayoutInflater().inflate(R.layout.layout_toast_dialog, null);
                            TextView tvContent = (TextView) view.findViewById(R.id.tv_toast_content);
                            tvContent.setText("已提醒发货");
                            toast.setView(view);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }else if(string.contains("1006")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(OrderDetialActivity.this,"",Toast.LENGTH_SHORT);
                            View view = getLayoutInflater().inflate(R.layout.layout_toast_dialog, null);
                            TextView tvContent = (TextView) view.findViewById(R.id.tv_toast_content);
                            tvContent.setText("今天已经提醒过啦...");
                            toast.setView(view);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            }
        });

    }


    /**
     * 弹出退款或取消原因选择框
     */
    private TextView tvReason1,tvReason2,tvReason3,tvReason4;
    private ImageView iv1,iv2,iv3,iv4;
    private String cancleReason;
    private AlertDialog pw;
    private void showCancleDialog(final String type, final OrderBean order){
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

        cancleReason = tvReason1.getText().toString();//默认理由

        tvReason1.setOnClickListener(this);
        tvReason2.setOnClickListener(this);
        tvReason3.setOnClickListener(this);
        tvReason4.setOnClickListener(this);


        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_confirm);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("申请退款".equals(type)){
                    applyRefund(cancleReason,order);

                }else {
                    //T向服务器发送取消请求
                    cancelOrder(order.getId(),cancleReason);
                }

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
     * 申请退款
     * @param cancleReason
     * @param order
     */
    private void applyRefund(String cancleReason, OrderBean order) {
        Map<String, String> paramas = new HashMap<>();
        paramas.put("orderCommodity.orders.user.token", sp.get("token"));
        paramas.put("type", 2 + "");
        paramas.put("money", order.getSumPrice() + "");
        paramas.put("reason", cancleReason);
        paramas.put("orderCommodity.orders.id", order.getId() + "");
        paramas.put("orderCommodity.sku.id", order.getSkuId() + "");

        OkhttpUploadUtils.getInstance(this).sendMultipart(FXConst.USER_APPLY_REFUND_URL, paramas, null, null, null, null,null,null);
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
                .add("closeReason",cancleReason)//TODO 取消理由字段待确定
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
        View contentview = getLayoutInflater().inflate(R.layout.layout_confirm_takegoods_dialog, null);
        final AlertDialog pw = new AlertDialog.Builder(this).create();
        pw.setView(contentview);
        TextView tvCancel = (TextView) contentview.findViewById(R.id.tv_colse);
        final PasswordInputView piv = (PasswordInputView) contentview.findViewById(R.id.piv_set);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });

        piv.setInputCompletetListener(new InputCompletetListener() {
            @Override
            public void inputComplete() {
                final String password = piv.getEditContent();
                //检测支付密码的正确性
                FormBody body = new FormBody.Builder()
                        .add("token",sp.get("token"))
                        .add("payPassword",password)
                        .build();
                Request request = new Request.Builder()
                        .post(body)
                        .url(FXConst.CHECK_PAY_PASSWORD)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        if(string.contains("1000")){
                            confirmTakeGoods(order.getId(),password);
                        }else if(string.contains("1003")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OrderDetialActivity.this,"密码错误！",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

            }

            @Override
            public void deleteContent(boolean isDelete) {

            }
        });

        //设置触摸对话框以外区域，对话框消失
        pw.setCanceledOnTouchOutside(false);
        pw.show();
    }


    /**
     * 确认收货
     * @param id 订单id
     * @param payWord 支付密码
     */
    private void confirmTakeGoods(int id,String payWord) {
        FormBody body = new FormBody.Builder()
                .add("user.token",sp.get("token"))
                .add("id",id+"")
                .add("user.payPassword",payWord)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.CONFIRM_TAKEGOODS_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(OrderDetialActivity.this,"网络异常！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(result.contains("1000")){
                        toastInUI(OrderDetialActivity.this,"收货成功");
                    }else if(result.contains("109")){
                        toastInUI(OrderDetialActivity.this,"非法操作");
                    }
            }
        });
    }


    /**
     * 删除订单弹窗
     * @param from
     */
    private void showDeleteDialog(final String from){
        View contentview = getLayoutInflater().inflate(R.layout.layout_delete_dialog, null);
        final AlertDialog pw = new AlertDialog.Builder(this).create();
        pw.setView(contentview);
        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_confirm);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //向服务器发送删除请求
                postDeleteInfo2Server(superOrder.getId(),from);
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
     * 发送删除请求到服务器
     * @param id
     * @param from
     */
    private void postDeleteInfo2Server(int id, String from) {
        String url = "";
        if("user".equals(from)){
            url = FXConst.DELETE_ORDER_FOR_USER;
        }else if("store".equals(from)){
            url = FXConst.DELETE_ORDER_FOR_STORE;
        }
        FormBody body = new FormBody.Builder()
                .add("user.token",sp.get("token"))
                .add("id",id+"")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(OrderDetialActivity.this,"网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                    toastInUI(OrderDetialActivity.this,"删除成功");
                }else {
                    toastInUI(OrderDetialActivity.this,"删除失败");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==171 && resultCode==136){
            TakeGoodsAddressBean address = (TakeGoodsAddressBean) data.getSerializableExtra("address");
            tvOrderTakeMan.setText(address.getName());
            tvOrderTakeAddress.setText(address.getAddress());
            tvOrderTakePhone.setText(address.getPhone());
            changetTakeAddress(address.getId());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 告知服务器修改该订单的收货地址
     * @param addressId
     */
    private void changetTakeAddress(int addressId) {
        FormBody body = new FormBody.Builder()
                .add("user.token",sp.get("token"))
                .add("shoppingAddress.id",addressId+"".trim())
                .add("id",order.getId()+"".trim())
                .build();
        Request request = new Request.Builder()
                .url(FXConst.CHANGE_TAKE_ADDRESS_OF_ORDER)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if(string.contains("1000")){
                    toastInUI(OrderDetialActivity.this,"已成功修改收货地址");
                }else {
                    toastInUI(OrderDetialActivity.this,"无法修改该收货地址");
                }
            }
        });
    }
}
