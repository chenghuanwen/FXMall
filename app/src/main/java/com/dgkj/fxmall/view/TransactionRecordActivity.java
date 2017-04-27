package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.TransacitonRecordAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.TransactionRecordBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetTransactionRecorderFinishedListener;
import com.dgkj.fxmall.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class TransactionRecordActivity extends BaseActivity {
    @BindView(R.id.cb_transaction)
    RadioButton cbTransaction;

    @BindView(R.id.cb_transfer)
    RadioButton cbTransfer;

    @BindView(R.id.rv_transaction)
    RecyclerView rvTransaction;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.rg_product)
    RadioGroup rgProduct;
    @BindView(R.id.underline1)
    View underline1;
    @BindView(R.id.underline2)
    View underline2;
    private View headerview;
    private List<TransactionRecordBean> recordBeanList;
    private TransacitonRecordAdapter adapter;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private FXMallControl control = new FXMallControl();
    private int index = 1;
    private List<TransactionRecordBean> withdrawablData;
    private List<TransactionRecordBean> transactionData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_record);
        ButterKnife.bind(this);
        initHeaderView();
        initData();
        refresh();
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void refresh() {
        control.getTransactionRecord(this, sp.get("token"), index, 20, client, new OnGetTransactionRecorderFinishedListener() {
            @Override
            public void onGetTransactionRecorderFinished(List<TransactionRecordBean> recordList) {
                for (int i = 0; i < recordList.size(); i++) {
                    TransactionRecordBean recordBean = recordList.get(i);
                    if(recordBean.getType()==3){
                        withdrawablData.add(recordBean);
                        recordList.remove(recordBean);
                    }
                }
                transactionData.addAll(recordList);
                adapter.addAll(recordList,true);
            }
        });
    }

    private void initData() {
        recordBeanList = new ArrayList<>();
        withdrawablData = new ArrayList<>();
        transactionData = new ArrayList<>();
        adapter = new TransacitonRecordAdapter(this,recordBeanList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTransaction.setLayoutManager(layoutManager);
        rvTransaction.setAdapter(adapter);
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "交易记录");

        rgProduct.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.cb_transaction){
                    underline1.setVisibility(View.VISIBLE);
                    underline2.setVisibility(View.GONE);
                    adapter.addAll(transactionData,true);
                }else {
                    underline1.setVisibility(View.GONE);
                    underline2.setVisibility(View.VISIBLE);
                    adapter.addAll(withdrawablData,true);
                }
            }
        });
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
