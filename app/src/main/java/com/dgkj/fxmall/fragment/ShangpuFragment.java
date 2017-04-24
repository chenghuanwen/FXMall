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
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.view.HomePageActivity;
import com.dgkj.fxmall.view.InTheSaleActivity;
import com.dgkj.fxmall.view.PublishProductActivity;
import com.dgkj.fxmall.view.SetPostageActivity;
import com.dgkj.fxmall.view.ShangpuOrderActivity;
import com.dgkj.fxmall.view.StoreInfoEditActivity;

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
    @BindView(R.id.tv_refund)
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_shang_pu, container, false);
        ButterKnife.bind(this, view);
        sp = SharedPreferencesUnit.getInstance(getContext());
        handler = new Handler(Looper.getMainLooper());
        client = new OkHttpClient.Builder().build();
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
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"网络异常！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(!result.contains("1000")){return;}
                try {
                    JSONObject object = new JSONObject(result);
                    final JSONObject dataset = object.getJSONObject("dataset");
                    final String name = dataset.getString("storeName");
                    final String describe = dataset.getString("intro");
                    final String icon = dataset.getString("logo");
                    final int stars = dataset.getInt("storeGrade");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvStoreName.setText(name);
                            tvStoreIntroduce.setText(describe);
                            Glide.with(getContext()).load(icon).into(civShangpu);
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"网络异常！",Toast.LENGTH_SHORT).show();
                    }
                });
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


    }

    @OnClick(R.id.tv_publish_product)
    public void publishProduct() {
        Intent intent = new Intent(getContext(), PublishProductActivity.class);
        jumpto(intent);
    }

    @OnClick(R.id.tv_postage_setting)
    public void setPostage() {
        Intent intent = new Intent(getContext(), SetPostageActivity.class);
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
        StoreBean storeBean = new StoreBean();
        storeBean.setTotalScals(100);
        storeBean.setStars(3);
        storeBean.setQualityScore(3.5);
        storeBean.setPriceScore(5.0);
        storeBean.setDescribeScore(5.3);
        storeBean.setAdress("广东深圳");
        storeBean.setCreateTime("2017-05-01");
        storeBean.setGoodsCount(23213);
        storeBean.setIconUrl("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
        intent.putExtra("store", storeBean);
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
                startActivity(new Intent(getContext(), HomePageActivity.class));
            }
        });
    }

}
