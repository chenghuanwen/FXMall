package com.dgkj.fxmall.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.BatchHandleProductsAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.StoreProductBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.LogUtil;

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

public class BatchHandleActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.cb_check_all)
    CheckBox cbCheckAll;
    @BindView(R.id.tv_product_online)
    TextView tvProductOnline;
    @BindView(R.id.tv_product_delete)
    TextView tvProductDelete;
    @BindView(R.id.rv_batch)
    RecyclerView rvBatch;
    @BindView(R.id.tv_product_offline)
    TextView tvProductOffline;
    @BindView(R.id.ll_from_rest)
    LinearLayout llFromRest;
    @BindView(R.id.activity_batch_handle)
    LinearLayout activityBatchHandle;
    private View headerview;
    private List<StoreProductBean> products;
    private BatchHandleProductsAdapter adapter;
    private int selectCount;
    private AlertDialog pw;
    private String from = "";
    private ArrayList<StoreProductBean> selectProducts = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1://选择
                    StoreProductBean productBean = (StoreProductBean) msg.obj;
                    selectProducts.add(productBean);
                    adapter.notifyDataSetChanged();
                    break;
                case 2://取消选择
                    StoreProductBean product = (StoreProductBean) msg.obj;
                    selectProducts.remove(product);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_handle);
        ButterKnife.bind(this);
        initHeaderView();
        initData();
        selectAll();

    }

    @Override
    public View getContentView() {
        return activityBatchHandle;
    }

    private void initData() {
        products = (List<StoreProductBean>) getIntent().getSerializableExtra("data");
        adapter = new BatchHandleProductsAdapter(this, R.layout.item_store_product_batch_handle, products, handler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvBatch.setLayoutManager(layoutManager);
        rvBatch.setAdapter(adapter);

    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "批量管理");

        from = getIntent().getStringExtra("from");

        if ("sale".equals(from)) {
            llFromRest.setVisibility(View.GONE);
            tvProductOffline.setVisibility(View.VISIBLE);
        } else {
            tvProductOffline.setVisibility(View.GONE);
            llFromRest.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.tv_product_offline)
    public void offline() {//下架
        showDeleteDialog("你确定要下架这"+selectProducts.size()+"件商品吗？");
    }

    @OnClick(R.id.tv_product_online)
    public void online() {//上架
        showDeleteDialog("你确定要上架这"+selectProducts.size()+"件商品吗？");
    }

    @OnClick(R.id.tv_product_delete)
    public void delete() {//删除
        showDeleteDialog("你确定要删除这"+selectProducts.size()+"件商品吗？");
    }

    /**
     * 全选操作
     */
    private void selectAll() {
        cbCheckAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.selectAll();
                } else {
                    adapter.cancleAll();
                }
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    private void showDeleteDialog(final String type) {
        View contentview = getLayoutInflater().inflate(R.layout.layout_offline_dialog, null);
        pw = new AlertDialog.Builder(BatchHandleActivity.this).create();
        pw.setView(contentview);
        TextView tvType = (TextView) contentview.findViewById(R.id.tv_delete_type);
        tvType.setText(type);
        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_confirm);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.contains("删除")) {
                    //通知服务器删除
                    deleteRemote();
                } else {
                    online(selectProducts.get(0).getStatu());
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
     * 上架或下架商品
     *
     * @param statu 区分是出售中的还是仓库中的
     */
    private void online(int statu) {
        FormBody.Builder builder = new FormBody.Builder()
                .add("store.user.token", sp.get("token"))
                .add("status", statu + "");
        for (StoreProductBean selectProduct : selectProducts) {
            builder.add("ids",selectProduct.getId()+"".trim());
        }
        FormBody body = builder.build();
        final Request request = new Request.Builder()
                .post(body)
                .url(FXConst.PRODUCT_ONLINE_OFFLINE)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(BatchHandleActivity.this,"网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body().string().contains("1000")) {
                    toastInUI(BatchHandleActivity.this,"操作成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (StoreProductBean selectProduct : selectProducts) {
                                products.remove(selectProduct);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    toastInUI(BatchHandleActivity.this,"操作失败");
                }
            }
        });
    }


    /**
     * 删除商品
     */
    private void deleteRemote() {
        FormBody.Builder builder = new FormBody.Builder()
                .add("token", sp.get("token"));
        for (int i = 0; i < selectProducts.size(); i++) {
            StoreProductBean productBean = selectProducts.get(i);
            builder .add("ids",productBean.getId()+"".trim());
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.STORE_DELETE_PRODUCTS_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                    toastInUI(BatchHandleActivity.this,"删除成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (StoreProductBean selectProduct : selectProducts) {
                                products.remove(selectProduct);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });

                }else {
                    toastInUI(BatchHandleActivity.this,"删除失败");
                }
            }
        });

    }

}
