package com.dgkj.fxmall.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.OrderBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.view.ApplyRefundActivity;
import com.dgkj.fxmall.view.LogisticsDetialActivity;
import com.dgkj.fxmall.view.OrderDetialActivity;
import com.dgkj.fxmall.view.PublishCommentActivity;
import com.dgkj.fxmall.view.RefundDetialActivity;
import com.dgkj.fxmall.view.ShangpuDeliverActivity;
import com.dgkj.fxmall.view.SubmitLogisticsMsgActivity;
import com.dgkj.fxmall.view.myView.PayDialog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Android004 on 2017/3/28.
 */

public class OrderClassifyAdapter extends CommonAdapter<OrderBean> implements View.OnClickListener{
    private Context context;
    /**
     * 个人订单中心
     */
    private static final int WAIT_PAY = 0;//待付款
    private static final int WAIT_DELIVER = 1;//买家待发货
    private static final int WAIT_TAKE_GOODS = 2;//待收货
    private static final int WAIT_COMMENT = 3;//待评论
    private static final int ORDER_COMPLETE = 4;//交易完成
    private static final int APPLAY_REFUND = 5;//申请退款
    private static final int AGREE_REFUND = 6;//同意退款
    private static final int REDUND_SUCCESSED = 7;//退款成功
    /**
     * 店铺订单中心
     */
    private static final int SELLER_WAIT_DELIVER = 8;//卖家待发货
    private static final int SELLER_HAS_DELIVER = 9;//卖家已发货
    private static final int SELLER_HAS_SOLD = 10;//卖家已售出
    private static final int BUYER_APPLAY_REFUND = 11;//买家申请退货/退款
    private static final int SELLER_WAIT_TAKE_GOODS = 12;//卖家等待收货
    private static final int REFUND_COMPLETE = 13;//退款完成
    private static final int WAIT_BUYER_DELIVER = 14;//卖家待买家退货发货
    private static final int SELLER_REFUSED_REFUND = 15;//卖家拒绝退款
    private AlertDialog pw;
    private String cancleReason="";
    private FragmentActivity activity;
    private OkHttpClient client ;
    private SharedPreferencesUnit sp;
    private Handler handler;

    public OrderClassifyAdapter(Context context,List<OrderBean> datas,FragmentActivity activity) {
        super(context,-1, datas);
        this.context = context;
        this.activity = activity;
        client = new OkHttpClient.Builder().build();
        sp = SharedPreferencesUnit.getInstance(context);
        handler = new Handler(Looper.getMainLooper());
    }


    @Override
    protected void convert(ViewHolder holder, final OrderBean order, final int position) {
        holder.setText(R.id.tv_order_storename,order.getStoreName());
        holder.setText(R.id.tv_order_state,order.getState());
        holder.setText(R.id.tv_car_goods_introduce,order.getIntroduce());
        holder.setText(R.id.tv_car_goods_color,"颜色："+order.getColor());
        holder.setText(R.id.tv_car_goods_size,"尺寸："+order.getSize());
        holder.setText(R.id.tv_car_goods_price,"¥"+order.getSinglePrice());
        holder.setText(R.id.tv_car_goods_count,"x"+order.getCount());
        holder.setText(R.id.tv_sumPrice,"¥"+(order.getSinglePrice()*order.getCount()));
        holder.setText(R.id.tv_postage,"(含邮费¥"+order.getPostage()+")");
        ImageView iv = holder.getView(R.id.iv_car_goods);
        Glide.with(context).load(order.getUrl()).placeholder(R.mipmap.android_quanzi).into(iv);

        TextView type = holder.getView(R.id.tv_money_type);


        holder.setOnClickListener(R.id.order_comment, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetialActivity.class);
                intent.putExtra("order",order);
                context.startActivity(intent);
            }
        });


        switch (getItemViewType(position)){
            case WAIT_PAY:
                holder.setOnClickListener(R.id.btn_cancle_order, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//取消订单
                        showCancleDialog(position,"取消订单");
                    }
                });
                holder.setOnClickListener(R.id.btn_pay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//去付款
                        PayDialog dialog = new PayDialog(context);
                        dialog.show(activity.getSupportFragmentManager(),"");
                    }
                });
                break;
            case WAIT_DELIVER:
                holder.setOnClickListener(R.id.btn_apply_refund, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//申请退款
                        showCancleDialog(position,"退款");
                    }
                });
                holder.setOnClickListener(R.id.btn_notify_deliver, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//提醒发货

                    }
                });
                break;
            case WAIT_TAKE_GOODS:
                holder.setOnClickListener(R.id.btn_logistics_msg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//物流信息
                        context.startActivity(new Intent(context, LogisticsDetialActivity.class));
                    }
                });
                holder.setOnClickListener(R.id.btn_scale_return, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//申请退货
                        context.startActivity(new Intent(context, ApplyRefundActivity.class));
                    }
                });
                holder.setOnClickListener(R.id.btn_confirm_takegoods, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//确认收货

                    }
                });
                break;
            case WAIT_COMMENT:
                holder.setOnClickListener(R.id.btn_logistics_msg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//物流信息
                        context.startActivity(new Intent(context, LogisticsDetialActivity.class));
                    }
                });
                holder.setOnClickListener(R.id.btn_delete_order, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//删除订单
                        showDeleteDialog(position);
                    }
                });
                holder.setOnClickListener(R.id.btn_comment, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//评价
                        context.startActivity(new Intent(context, PublishCommentActivity.class));
                    }
                });
                break;
            case ORDER_COMPLETE:
                holder.setOnClickListener(R.id.btn_logistics_msg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//物流信息

                    }
                });
                holder.setOnClickListener(R.id.btn_delete_order, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//删除订单
                        showDeleteDialog(position);
                    }
                });
                holder.setOnClickListener(R.id.btn_comment_finish, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//已评价

                    }
                });
                break;
            case  AGREE_REFUND:
                holder.setOnClickListener(R.id.btn_submit_logistic, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, SubmitLogisticsMsgActivity.class));
                    }
                });
                break;


            //商铺订单
            case SELLER_WAIT_DELIVER:
                holder.setOnClickListener(R.id.btn_deliver, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//商铺发货
                        Intent intent = new Intent(context, ShangpuDeliverActivity.class);
                        intent.putExtra("order",order);
                        context.startActivity(intent);
                    }
                });
                break;
            case SELLER_HAS_DELIVER:
                holder.setOnClickListener(R.id.btn_logistics_msg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//查看物流信息
                        context.startActivity(new Intent(context, LogisticsDetialActivity.class));
                    }
                });
                break;
            case SELLER_HAS_SOLD:
                holder.setOnClickListener(R.id.btn_logistics_msg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//查看物流信息
                        context.startActivity(new Intent(context, LogisticsDetialActivity.class));
                    }
                });
                holder.setOnClickListener(R.id.btn_delete_order, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, LogisticsDetialActivity.class));
                    }
                });
                break;
            case BUYER_APPLAY_REFUND:
                type.setText("退款金额：");
                holder.setOnClickListener(R.id.btn_refund_msg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//查看退款信息
                        context.startActivity(new Intent(context, RefundDetialActivity.class));
                    }
                });
                holder.setOnClickListener(R.id.btn_refused_apply, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//拒绝买家的申请

                    }
                });
                holder.setOnClickListener(R.id.btn_agree_apply, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//同意买家的申请

                    }
                });
                break;
            case SELLER_WAIT_TAKE_GOODS:
                type.setText("退款金额：");
                holder.setOnClickListener(R.id.btn_refund_msg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//查看退款信息
                        context.startActivity(new Intent(context, RefundDetialActivity.class));
                    }
                });
                holder.setOnClickListener(R.id.btn_logistics_msg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//查看物流信息
                        context.startActivity(new Intent(context, LogisticsDetialActivity.class));
                    }
                });
                holder.setOnClickListener(R.id.btn_agree_refund, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//同意退款

                    }
                });
                break;
            case REFUND_COMPLETE:
                type.setText("退款金额：");
                holder.setOnClickListener(R.id.btn_refund_msg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//查看退款信息
                        context.startActivity(new Intent(context, RefundDetialActivity.class));
                    }
                });
                holder.setOnClickListener(R.id.btn_logistics_msg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//查看物流信息
                        context.startActivity(new Intent(context, LogisticsDetialActivity.class));
                    }
                });
                holder.setOnClickListener(R.id.btn_delete_order, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//删除订单
                        showDeleteDialog(position);
                    }
                });
                break;
            case WAIT_BUYER_DELIVER:
                type.setText("退款金额：");
                holder.setOnClickListener(R.id.btn_refund_msg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//查看退款信息
                        context.startActivity(new Intent(context, RefundDetialActivity.class));
                    }
                });
                holder.setOnClickListener(R.id.btn_notify_deliver, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//提醒发货

                    }
                });

                break;
            case SELLER_REFUSED_REFUND:
                type.setText("退款金额：");
                holder.setOnClickListener(R.id.btn_refund_msg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//查看退款信息
                        context.startActivity(new Intent(context, RefundDetialActivity.class));
                    }
                });
                holder.setOnClickListener(R.id.btn_delete_order, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//删除订单
                        showDeleteDialog(position);
                    }
                });

                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        String state = mDatas.get(position).getState();
        switch (state){
            //个人订单
            case "等待买家付款":
                return WAIT_PAY;
            case "等待卖家发货":
                return WAIT_DELIVER;
            case "等待买家收货":
                return WAIT_TAKE_GOODS;
            case "等待买家评价":
                return WAIT_COMMENT;
            case "交易完成":
                return ORDER_COMPLETE;
            case "等待商家处理退款申请":
                return APPLAY_REFUND;
            case "商家同意退款":
                return AGREE_REFUND;
            case "退款成功":
                return REDUND_SUCCESSED;
            case "商家拒绝退款":
                return APPLAY_REFUND;
            //店铺订单
            case "等待发货":
                return SELLER_WAIT_DELIVER;
            case "已发货":
                return SELLER_HAS_DELIVER;
            case "已完成":
                return SELLER_HAS_SOLD;
            case "买家申请退货/退款":
                return BUYER_APPLAY_REFUND;
            case "等待收货":
                return SELLER_WAIT_TAKE_GOODS;
            case "退款已完成":
                return REFUND_COMPLETE;
            case "等待买家发货":
                return WAIT_BUYER_DELIVER;
            case "已拒绝申请":
                return SELLER_REFUSED_REFUND;


        }
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null ;
        View view;
        switch (viewType){
            case WAIT_PAY:
              view = mInflater.inflate(R.layout.item_order_waitpay,parent,false);
                viewHolder = new ViewHolder(context,view);
                break;
            case WAIT_DELIVER:
               view = mInflater.inflate(R.layout.item_order_waitdeliver,parent,false);
                viewHolder = new ViewHolder(context,view);
                break;
            case WAIT_TAKE_GOODS:
               view = mInflater.inflate(R.layout.item_order_wait_takegoods,parent,false);
                viewHolder = new ViewHolder(context,view);
                break;
            case WAIT_COMMENT:
              view = mInflater.inflate(R.layout.item_order_wait_comment,parent,false);
                viewHolder = new ViewHolder(context,view);
                break;
            case ORDER_COMPLETE:
              view = mInflater.inflate(R.layout.item_order_complete,parent,false);
                viewHolder = new ViewHolder(context,view);
                break;
            case APPLAY_REFUND:
                view = mInflater.inflate(R.layout.item_order_wait_handler_refund,parent,false);
                viewHolder = new ViewHolder(context,view);
                break;
            case AGREE_REFUND:
                view = mInflater.inflate(R.layout.item_order_agree_refund,parent,false);
                viewHolder = new ViewHolder(context,view);
                break;
            case REDUND_SUCCESSED:
                view = mInflater.inflate(R.layout.item_order_refund_successed,parent,false);
                viewHolder = new ViewHolder(context,view);
                break;
            //店铺
            case SELLER_WAIT_DELIVER:
                view = mInflater.inflate(R.layout.item_store_order_wait_deliver,parent,false);
                viewHolder = new ViewHolder(context,view);
                break;
            case SELLER_HAS_DELIVER:
                view = mInflater.inflate(R.layout.item_store_order_has_deliver,parent,false);
                viewHolder = new ViewHolder(context,view);
                break;
            case SELLER_HAS_SOLD:
                view = mInflater.inflate(R.layout.item_store_order_sold,parent,false);
                viewHolder = new ViewHolder(context,view);
                break;
            case BUYER_APPLAY_REFUND:
                view = mInflater.inflate(R.layout.item_store_order_buyer_apply_refund,parent,false);
                viewHolder = new ViewHolder(context,view);
                break;
            case SELLER_WAIT_TAKE_GOODS:
                view = mInflater.inflate(R.layout.item_store_order_wait_take_goods,parent,false);
                viewHolder = new ViewHolder(context,view);
                break;
            case REFUND_COMPLETE:
                view = mInflater.inflate(R.layout.item_store_order_refund_complete,parent,false);
                viewHolder = new ViewHolder(context,view);
                break;
            case WAIT_BUYER_DELIVER:
                view = mInflater.inflate(R.layout.item_store_wait_buyer_deliver,parent,false);
                viewHolder = new ViewHolder(context,view);
                break;
            case SELLER_REFUSED_REFUND:
                view = mInflater.inflate(R.layout.item_store_refused_apply,parent,false);
                viewHolder = new ViewHolder(context,view);
                break;

        }
        return viewHolder;
    }


    private TextView tvReason1,tvReason2,tvReason3,tvReason4;
    private ImageView iv1,iv2,iv3,iv4;
    private void showCancleDialog(final int position,String type){
        View contentview = mInflater.inflate(R.layout.layout_reason_of_cancel_order, null);
        pw = new AlertDialog.Builder(mContext).create();
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
                cancelOrder(mDatas.get(position).getId(),cancleReason);
                mDatas.remove(position);
                notifyDataSetChanged();
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
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"网络异常！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"订单已取消！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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


    private void showDeleteDialog(final int position){
        View contentview = mInflater.inflate(R.layout.layout_delete_dialog, null);
       final AlertDialog pw = new AlertDialog.Builder(mContext).create();
        pw.setView(contentview);
        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_confirm);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatas.remove(position);
                notifyDataSetChanged();
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
}
