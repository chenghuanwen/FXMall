package com.dgkj.fxmall.view;

import android.app.AlertDialog;
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

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.BatchHandleProductsAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.StoreProductBean;
import com.dgkj.fxmall.utils.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private View headerview;
    private List<StoreProductBean> products;
    private BatchHandleProductsAdapter adapter;
    private int selectCount;
    private AlertDialog pw;
    private String from = "";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case 1://选择
                    adapter.notifyDataSetChanged();
                    break;
                case 2://取消选择
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

    private void initData() {
        products = (List<StoreProductBean>) getIntent().getSerializableExtra("data");
        adapter = new BatchHandleProductsAdapter(this, R.layout.item_store_product_batch_handle, products,handler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvBatch.setLayoutManager(layoutManager);
        rvBatch.setAdapter(adapter);

    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "批量管理");

        from = getIntent().getStringExtra("from");
        LogUtil.i("TAG","from==="+from);
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
        showDeleteDialog();
    }

    @OnClick(R.id.tv_product_online)
    public void online() {//上架

    }

    @OnClick(R.id.tv_product_delete)
    public void delete() {//删除

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

    private void showDeleteDialog() {
        View contentview = getLayoutInflater().inflate(R.layout.layout_offline_dialog, null);
        pw = new AlertDialog.Builder(BatchHandleActivity.this).create();
        pw.setView(contentview);
        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_confirm);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
