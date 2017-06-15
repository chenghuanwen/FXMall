package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.StoreSuperClassifyAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.SuperClassifyBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetStoreSuperClassifyFinishedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class ApplyStoreAllClassifyActivity extends BaseActivity {

    @BindView(R.id.rv_all_classify)
    GridView rvAllClassify;
    @BindView(R.id.iv_back)
    ImageButton ivBack;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.activity_apply_store_all_classify)
    LinearLayout activityApplyStoreAllClassify;
    private View headerview;
    private int selectPosition;
    private String selectType = "";
    private List<SuperClassifyBean> classifyBeanList;
    private StoreSuperClassifyAdapter adapter;
    private FXMallControl control = new FXMallControl();
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int position = msg.arg1;
                for (int i = 0; i < classifyBeanList.size(); i++) {
                    if (position != i) {
                        classifyBeanList.get(i).setSelected(false);
                    } else {
                        classifyBeanList.get(i).setSelected(true);
                        selectPosition = classifyBeanList.get(position).getId();
                        selectType = classifyBeanList.get(position).getType();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_store_all_classify);
        ButterKnife.bind(this);
        initHeaderView();
        initList();
        refresh();
    }

    @Override
    public View getContentView() {
        return activityApplyStoreAllClassify;
    }

    private void refresh() {
        control.getStoreSuperClassify(this, sp.get("token"), client, new OnGetStoreSuperClassifyFinishedListener() {
            @Override
            public void onGetStoreSuperClassifyFinished(final List<SuperClassifyBean> classifyList) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addAll(classifyList, true);
                            }
                        });
                    }
                });
            }
        });
    }

    private void initList() {
        classifyBeanList = new ArrayList<>();
        adapter = new StoreSuperClassifyAdapter(this, R.layout.item_store_super_classfiy, classifyBeanList, handler);
        rvAllClassify.setAdapter(adapter);
    }


    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "全部分类");
    }


    @OnClick(R.id.btn_confirm)
    public void confirm() {
        Intent intent = new Intent();
        intent.putExtra("id", selectPosition);
        intent.putExtra("classify", selectType);
        setResult(152, intent);
        finish();
    }

    @OnClick(R.id.iv_back)
    public void back() {

        finish();
    }
}
