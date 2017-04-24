package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.ShoppingCarBean;
import com.dgkj.fxmall.bean.ShoppingGoodsBean;
import com.dgkj.fxmall.bean.TakeGoodsAddressBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.view.myView.PayDialog;

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

public class ConfirmOrderActivity extends BaseActivity {
    @BindView(R.id.tv_select_address)
    TextView tvSelectAddress;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_goods_count)
    TextView tvGoodsCount;
    @BindView(R.id.tv_sum)
    TextView tvSum;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.tv_submit_order)
    TextView tvSubmitOrder;
    @BindView(R.id.tv_order_take_man)
    TextView tvOrderTakeMan;
    @BindView(R.id.tv_order_take_phone)
    TextView tvOrderTakePhone;
    @BindView(R.id.tv_order_take_address)
    TextView tvOrderTakeAddress;
    @BindView(R.id.ll_order_address)
    LinearLayout llOrderAddress;
    @BindView(R.id.ll_order_container)
    LinearLayout llOrderContainer;
    private View headerview;
    private boolean hasDefaultAddress;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private int defaultAddressId;
    private ArrayList<ShoppingCarBean> orderList;
    private List<View> addViews = new ArrayList<>();
    private double totalPrice;//所有订单总价
    private int totalCount;//所有订单总数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        initHeaderView();

        //TODO 获取默认收货地址
        getDefaultAddress();

        orderList = (ArrayList<ShoppingCarBean>) getIntent().getSerializableExtra("orders");

        initOrderLayout();

    }


    /**
     * 根据订单数量动态添加订单模板，每家店铺的所有商品为一个订单
     */
    private void initOrderLayout() {
        for (int i=0;i<orderList.size();i++) {
            ShoppingCarBean carBean = orderList.get(i);
            ArrayList<ShoppingGoodsBean> goods = carBean.getGoods();
            View view = getLayoutInflater().inflate(R.layout.layout_add_comfirm_order_model, llOrderContainer, false);//动态店铺版面
            LinearLayout subContainer = (LinearLayout) view.findViewById(R.id.ll_store_product_container);
            TextView tvStoreName = (TextView) view.findViewById(R.id.tv_order_store_name);
            TextView tvSumPay = (TextView) view.findViewById(R.id.tv_order_sumMoney);
            tvStoreName.setText(carBean.getStoreName());

            double storeSumPrice = 0;//当前店铺价格小计
            double sum = 0;//当前同种商品总价小计

            for (int j = 0; j < goods.size(); j++) {
                View subView = getLayoutInflater().inflate(R.layout.layout_add_product_for_store_in_order, subContainer, false);//当前店铺中商品动态版面
                ShoppingGoodsBean orderBean = goods.get(j);
                ImageView ivPhoto = (ImageView) subView.findViewById(R.id.iv_car_goods);
                TextView tvDescribe = (TextView) subView.findViewById(R.id.tv_car_goods_introduce);
                TextView tvColorSize = (TextView) subView.findViewById(R.id.tv_car_goods_color);
                TextView tvSinglePrice = (TextView) subView.findViewById(R.id.tv_car_goods_price);
                TextView tvCount = (TextView) subView.findViewById(R.id.tv_car_goods_count);
                Glide.with(this).load(orderBean.getUrl()).into(ivPhoto);
                tvDescribe.setText(orderBean.getIntroduce());
                tvColorSize.setText("颜色："+orderBean.getColor());
                tvCount.setText("x"+orderBean.getCount());
                tvSinglePrice.setText("¥"+orderBean.getPrice());
                sum = orderBean.getPrice() * orderBean.getCount();

                totalCount += orderBean.getCount();
                storeSumPrice += sum;

                subContainer.addView(subView);
            }

            tvSumPay.setText("¥"+ storeSumPrice);
            totalPrice += storeSumPrice;

            llOrderContainer.addView(view);
            addViews.add(view);
            //提交当前店铺订单
            submitOrderForOneStore(goods,i);
        }

        tvGoodsCount.setText("总共"+totalCount+"件商品");
        tvTotalMoney.setText("¥"+totalPrice);

    }


    /**
     * 获取默认收货地址
     */
    private void getDefaultAddress() {
        FormBody body = new FormBody.Builder()
                .add("token", sp.get("token"))
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_DEFAULT_TAKE_ADDRESS)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(ConfirmOrderActivity.this, "网络异常！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        final JSONObject address = object.getJSONObject("dataset");
                        final String phone = address.getString("phone");
                        final String consignee = address.getString("consignee");
                        final String detial = address.getString("province") + address.getString("city") + address.getString("county") + address.getString("particular");
                        defaultAddressId = address.getInt("id");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvSelectAddress.setVisibility(View.GONE);
                                llOrderAddress.setVisibility(View.VISIBLE);
                                tvOrderTakeMan.setText(consignee);
                                tvOrderTakePhone.setText(phone);
                                tvOrderTakeAddress.setText(detial);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvSelectAddress.setVisibility(View.VISIBLE);
                            llOrderAddress.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "确认订单");
    }


    @OnClick({R.id.tv_select_address, R.id.ll_order_address})
    public void selectAddress() {
        startActivityForResult(new Intent(this, TakeGoodsAdressActivity.class), 135);
    }

    @OnClick(R.id.tv_submit_order)
    public void submitOrder() {

        //订单提交成功，进行付款
        PayDialog dialog = new PayDialog(ConfirmOrderActivity.this);
        dialog.show(getSupportFragmentManager(), "");
    }

    /**
     * 提交某家店铺中的订单
     */
    public void submitOrderForOneStore(List<ShoppingGoodsBean> goods,int position){
        int[] skuIds = new int[goods.size()];
        int[] nums = new int[goods.size()];
        for (int i = 0; i < goods.size(); i++) {
            ShoppingGoodsBean orderBean = goods.get(i);
            skuIds[i] = orderBean.getSkuId();
            nums[i] = orderBean.getCount();
        }

        //先向服务器提交订单
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("orders.user.token",sp.get("token"))
                .add("orders.shoppingAddress.id",defaultAddressId+"");
        for (int skuId : skuIds) {
            builder.add("ids",skuId+"");
        }
        for (int num : nums) {
            builder.add("nums",num+"");
        }

        View view = addViews.get(position);
        EditText etLeave = (EditText) view.findViewById(R.id.et_say_to_seller);
        builder.add("order.leave",etLeave.getText().toString());


        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(FXConst.SUBMIT_ORDER_URL)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.i("TAG","订单提交结果==="+result);
                if(result.contains("1000")){

                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 135 && resultCode == 136) {
            tvSelectAddress.setVisibility(View.GONE);
            llOrderAddress.setVisibility(View.VISIBLE);
            TakeGoodsAddressBean address = (TakeGoodsAddressBean) data.getSerializableExtra("address");
            tvOrderTakeMan.setText(address.getName());
            tvOrderTakeAddress.setText(address.getAddress());
            tvOrderTakePhone.setText(address.getPhone());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }
}