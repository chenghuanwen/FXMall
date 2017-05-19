package com.dgkj.fxmall.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.LoadProgressDialogUtil;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.view.BuyProductPlaceActivity;
import com.dgkj.fxmall.view.HomePageActivity;
import com.dgkj.fxmall.view.InTheSaleActivity;
import com.dgkj.fxmall.view.PublishProductActivity;
import com.dgkj.fxmall.view.SetPostageActivity;
import com.dgkj.fxmall.view.ShangpuOrderActivity;
import com.dgkj.fxmall.view.StoreInfoEditActivity;
import com.dgkj.fxmall.view.StoreMainPageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShangpuFragment extends Fragment {

    @BindView(R.id.civ_shangpu)
    CircleImageView civShangpu;
    @BindView(R.id.iv_shangpu_stars)
    ImageView ivShangpuStars;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_store_introduce)
    TextView tvStoreIntroduce;
    @BindView(R.id.tv_wait_deliver_count)
    TextView tvWaitDeliverCount;
    @BindView(R.id.tv_has_deliver_count)
    TextView tvHasDeliverCount;
    @BindView(R.id.tv_sold_count)
    TextView tvSoldCount;
    @BindView(R.id.tv_refund_count)
    TextView tvRefundCount;
    @BindView(R.id.tv_all_products)
    TextView tvAllProducts;
    @BindView(R.id.tv_on_sacle_count)
    TextView tvOnSacleCount;
    @BindView(R.id.tv_store_rest_count)
    TextView tvStoreRestCount;
    @BindView(R.id.tv_order_count)
    TextView tvOrderCount;
    @BindView(R.id.tv_incom)
    TextView tvIncom;
    @BindView(R.id.textView)
    TextView tvRefund;
    @BindView(R.id.tv_publish_product)
    TextView tvPublishProduct;
    @BindView(R.id.tv_postage_setting)
    TextView tvPostageSetting;
    @BindView(R.id.tv_onsale)
    TextView tvOnsale;
    @BindView(R.id.tv_rest)
    TextView tvRest;
    @BindView(R.id.ll_wait_deliver)
    LinearLayout llWaitDeliver;
    @BindView(R.id.ll_has_deliver)
    LinearLayout llHasDeliver;
    @BindView(R.id.ll_sold)
    LinearLayout llSold;
    @BindView(R.id.ll_refund)
    LinearLayout llRefund;
    @BindView(R.id.scrollview)
    ScrollView scrollView;
    @BindView(R.id.srl_store)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.tv_jump_tip)
    TextView tvJumpTip;
    @BindView(R.id.rl_insale)
    RelativeLayout rlInsale;
    @BindView(R.id.rl_warehouse)
    RelativeLayout rlWarehouse;
    private float downX, downY = 300;
    private OkHttpClient client ;
    private SharedPreferencesUnit sp;
    private Handler handler;
    private int storeId;
    private String describe="",logo="", banner ="",name="",address="";
    private int stars,goodsCount,sales;
    private double totalScore;
    private int upperlimit;//可发布商品数量上限
    private LoadProgressDialogUtil progressDialogUtil;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_shang_pu, container, false);
        ButterKnife.bind(this, view);
        sp = SharedPreferencesUnit.getInstance(getContext());
        handler = new Handler(Looper.getMainLooper());
        client = new OkHttpClient.Builder().build();
        progressDialogUtil = new LoadProgressDialogUtil(getContext());
        progressDialogUtil.buildProgressDialog();

        initData();
        refresh2Home();
        return view;
    }

    /**
     * 初始化店铺头像评分以及各种订单数量等基础信息
     */
    private void initData() {
        FormBody body = new FormBody.Builder()
                .add("store.token",sp.get("token"))
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_STORE_DETIAL_INFO)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(!result.contains("1000")){return;}
                try {
                    JSONObject object = new JSONObject(result);
                    final JSONObject dataset = object.getJSONObject("dataset");
                    name = dataset.getString("storeName");
                    describe = dataset.getString("intro");
                    address = dataset.getString("address");
                    logo = dataset.getString("logo");
                    banner = dataset.getString("banner");
                    stars = dataset.getInt("storeGrade");
                    storeId = dataset.getInt("id");
                    goodsCount = dataset.getInt("cnum");
                    sales = dataset.getInt("sales");
                    totalScore = dataset.getDouble("totalScore");
                    upperlimit = dataset.getInt("upperlimit");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvStoreName.setText(name);
                            tvStoreIntroduce.setText(describe);
                            Glide.with(getContext()).load(logo).into(civShangpu);
                            switch (stars){
                                case 0:
                                case 1:
                                    ivShangpuStars.setImageResource(R.mipmap.dpzy_dj1);
                                    break;
                                case 2:
                                    ivShangpuStars.setImageResource(R.mipmap.dpzy_dj2);
                                    break;
                                case 3:
                                    ivShangpuStars.setImageResource(R.mipmap.dpzy_dj3);
                                    break;
                                case 4:
                                    ivShangpuStars.setImageResource(R.mipmap.dpzy_dj4);
                                    break;
                                case 5:
                                    ivShangpuStars.setImageResource(R.mipmap.dpzy_dj5);
                                    break;
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        FormBody formBody = new FormBody.Builder()
                .add("store.user.token",sp.get("token"))
                .build();
        Request request1 = new Request.Builder()
                .url(FXConst.GET_STORE_SALE_COUNT)
                .post(formBody)
                .build();
        client.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(result.contains("1000")){
                    try {
                        JSONObject object = new JSONObject(result);
                        int restCount = object.getInt("toatl");
                        int inSale = object.getInt("dataset");
                        tvOnSacleCount.setText(inSale+"件商品");
                        tvStoreRestCount.setText(restCount+"件商品");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        FormBody body1 = new FormBody.Builder()
                .add("store.user.token",sp.get("token"))
                .build();
        Request request2 = new Request.Builder()
                .post(body1)
                .url(FXConst.GET_COUNT_OF_STORE_ORDER)
                .build();
        client.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if(string.contains("1000")){
                    try {
                        JSONObject object = new JSONObject(string);
                        JSONArray dataset = object.getJSONArray("dataset");
                        final int waitdeliver = dataset.getInt(0);
                        final int hasdeliver = dataset.getInt(1);
                        final int sold = dataset.getInt(2);
                        final int refund = dataset.getInt(3);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvWaitDeliverCount.setText(waitdeliver +"");
                                tvHasDeliverCount.setText(hasdeliver +"");
                                tvSoldCount.setText(sold +"");
                                tvRefundCount.setText(refund +"");
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {

                }
            }
        });

        FormBody body2 = new FormBody.Builder()
                .add("user.token",sp.get("token"))
                .build();
        Request request3 = new Request.Builder()
                .post(body2)
                .url(FXConst.GET_MY_STORE_ORDER_COUNT)
                .build();
        client.newCall(request3).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if(string.contains("1000")){
                    try {
                        JSONObject object = new JSONObject(string);
                        final int dataset = object.getInt("dataset");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvOrderCount.setText(dataset+"");
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        FormBody body3 = new FormBody.Builder()
                .add("user.token",sp.get("token"))
                .build();
        Request request4 = new Request.Builder()
                .post(body3)
                .url(FXConst.GET_MY_STORE_REFUND_COUNT)
                .build();
        client.newCall(request4).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if(string.contains("1000")){
                    try {
                        JSONObject object = new JSONObject(string);
                        final int dataset = object.getInt("dataset");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvRefund.setText(dataset+"");
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        getSomeCount(3,tvIncom);

    }


    /**
     * 获取佣金数
     * @param time
     * @param tv
     */
    public void getSomeCount(int time, final TextView tv){
        FormBody body1 = new FormBody.Builder()
                .add("user.token",sp.get("token"))
                .add("time",time+"")
                .build();
        Request request1 = new Request.Builder()
                .post(body1)
                .url(FXConst.GET_INCOME_URL)
                .build();
        client.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if(string.contains("1000")){
                    JSONObject object = null;
                    try {
                        object = new JSONObject(string);
                        final int count = object.getInt("total");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(count+"");
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                progressDialogUtil.cancelProgressDialog();
            }
        });
    }



    @OnClick(R.id.tv_publish_product)
    public void publishProduct() {
        if(goodsCount >= upperlimit){
            Toast.makeText(getContext(),"免费展位已用完！",Toast.LENGTH_SHORT).show();
            jumpto(new Intent(getContext(),BuyProductPlaceActivity.class));
        }else {
            Intent intent = new Intent(getContext(), PublishProductActivity.class);
            jumpto(intent);
        }
    }

    @OnClick(R.id.tv_postage_setting)
    public void setPostage() {
        Intent intent = new Intent(getContext(), SetPostageActivity.class);
        jumpto(intent);
    }


    @OnClick(R.id.tv_store_name)
    public void toMainPage(){
        Intent intent = new Intent(getContext(), StoreMainPageActivity.class);
        StoreBean storeBean = new StoreBean();
        storeBean.setId(storeId);
        storeBean.setTotalScore(totalScore);
        storeBean.setGoodsCount(goodsCount);
        storeBean.setTotalScals(sales);
        storeBean.setIconUrl(logo);
        storeBean.setName(name);
        storeBean.setAdress(address);
        jumpto(intent);
    }

    @OnClick({R.id.rl_insale,R.id.tv_all_products})
    public void onSale() {
        Intent intent = new Intent(getContext(), InTheSaleActivity.class);
        intent.putExtra("from", "sale");
        jumpto(intent);
    }

    @OnClick(R.id.rl_warehouse)
    public void onRest() {
        Intent intent = new Intent(getContext(), InTheSaleActivity.class);
        intent.putExtra("from", "rest");
        jumpto(intent);
    }


    @OnClick(R.id.ll_wait_deliver)
    public void waitDeliver() {
        Intent intent = new Intent(getContext(), ShangpuOrderActivity.class);
        intent.putExtra("from", "wait");
        jumpto(intent);
    }

    @OnClick(R.id.ll_has_deliver)
    public void hasDeliver() {
        Intent intent = new Intent(getContext(), ShangpuOrderActivity.class);
        intent.putExtra("from", "ok");
        jumpto(intent);
    }

    @OnClick(R.id.ll_sold)
    public void sold() {
        Intent intent = new Intent(getContext(), ShangpuOrderActivity.class);
        intent.putExtra("from", "sold");
        jumpto(intent);
    }

    @OnClick(R.id.ll_refund)
    public void refund() {
        Intent intent = new Intent(getContext(), ShangpuOrderActivity.class);
        intent.putExtra("from", "refund");
        jumpto(intent);
    }

    @OnClick(R.id.civ_shangpu)
    public void storeDetial() {
        Intent intent = new Intent(getContext(), StoreInfoEditActivity.class);
        intent.putExtra("logo",logo);
        intent.putExtra("banner", banner);
        intent.putExtra("introduce",describe);
        jumpto(intent);
    }

    public void jumpto(Intent intent) {
        getContext().startActivity(intent);
    }


    private void refresh2Home() {
        srlRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        srlRefresh.setNestedScrollingEnabled(true);
        srlRefresh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                int top = scrollView.getTop();
                LogUtil.i("TAG", "scrollview距离顶部距离==" + top);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = x;
                        downY = y;
                        LogUtil.i("TAG", "按下时坐标== x=" + downX + "==y==" + downY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = x - downX;
                        float dy = y - downY;
                        LogUtil.i("TAG", "滑动距离 x==" + dx + "y===" + dy);
                        if (dy > 0 && Math.abs(dy) > Math.abs(dx) && dy > 300) {
                            LogUtil.i("TAG", "向下滑动======");
                           /* layoutParams.height = 200;
                            tvJumpTip.setLayoutParams(layoutParams);*/
                            tvJumpTip.setVisibility(View.VISIBLE);

                        }/* else if(dy>0 && Math.abs(dy)>Math.abs(dx) && (dy>200 && dy<300)) {
                            LogUtil.i("TAG", "向上滑动======");
                            layoutParams.height = 100;
                            tvJumpTip.setLayoutParams(layoutParams);
                            tvJumpTip.setVisibility(View.VISIBLE);
                        }*/ else {
                            tvJumpTip.setVisibility(View.GONE);
                        }
                        break;
                }
                return false;
            }
        });
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlRefresh.setRefreshing(false);
                tvJumpTip.setVisibility(View.GONE);
                Intent intent = new Intent(getContext(), HomePageActivity.class);
                intent.putExtra("from","");
                startActivity(intent);
            }
        });
    }

}
