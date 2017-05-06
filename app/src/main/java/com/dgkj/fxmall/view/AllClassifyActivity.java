package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.ProductClassifyAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.ProductClassifyBean;
import com.dgkj.fxmall.bean.SuperClassifyBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetStoreSuperClassifyFinishedListener;
import com.dgkj.fxmall.listener.OnGetSubclassifyFinishedListener;
import com.dgkj.fxmall.view.myView.ItemOffsetDecoration;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class AllClassifyActivity extends BaseActivity {
    @BindView(R.id.rv_all_classify)
    ListView rvAllClassify;
    @BindView(R.id.rv_sub_classify)
    RecyclerView rvSubClassify;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.activity_all_classify)
    LinearLayout activityAllClassify;
    private View headerview;
    private List<SuperClassifyBean> allClassify;
    private List<ProductClassifyBean> subClassify;
    private ProductClassifyAdapter adapter;
    private String baseClassify = "";
    private FXMallControl control = new FXMallControl();
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private CommonAdapter<SuperClassifyBean> subAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_classify);
        ButterKnife.bind(this);
        initHeaderView();
        initDatas();
        getSuperClassify();
    }

    @Override
    public View getContentView() {
        return activityAllClassify;
    }

    /**
     * 获取一级分类
     */
    private void getSuperClassify() {
        control.getStoreSuperClassify(this, sp.get("token"), client, new OnGetStoreSuperClassifyFinishedListener() {
            @Override
            public void onGetStoreSuperClassifyFinished(final List<SuperClassifyBean> classifyList) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        subAdapter.addAll(classifyList, true);
                    }
                });
            }
        });
    }

    private void initDatas() {
        allClassify = new ArrayList<>();
        subClassify = new ArrayList<>();

        subAdapter = new CommonAdapter<SuperClassifyBean>(this, R.layout.item_all_classify, allClassify) {
            @Override
            protected void convert(ViewHolder holder, final SuperClassifyBean o, int position) {
                holder.setText(R.id.tv_base_classify, o.getType());

            }
        };

        rvAllClassify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                baseClassify = allClassify.get(position).getType();
                int superTypeId = allClassify.get(position).getId();
                getSubclassify(superTypeId);
            }
        });
        rvAllClassify.setAdapter(subAdapter);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 3);
        rvSubClassify.setLayoutManager(linearLayoutManager);
        adapter = new ProductClassifyAdapter(this, R.layout.item_all_subclassify, subClassify, "classify");
        ItemOffsetDecoration decoration = new ItemOffsetDecoration(20);
        rvSubClassify.addItemDecoration(decoration);
        rvSubClassify.setAdapter(adapter);
    }


    /**
     * 获取二级分类
     *
     * @param superTypeId
     */
    private void getSubclassify(int superTypeId) {
        control.getSubclassify(this, superTypeId, client, new OnGetSubclassifyFinishedListener() {
            @Override
            public void onGetSubclassifyFinished(final List<ProductClassifyBean> subList) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addAll(subList, true);
                    }
                });
            }
        });
    }


    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "全部分类");
    }

    @OnClick(R.id.iv_back)
    public void back() {


        finish();
    }

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        String result = baseClassify + "/" + adapter.getSubClassify();
        Intent intent = new Intent();
        intent.putExtra("classify", result);
        intent.putExtra("subId", adapter.getSubClassifyId());
        setResult(122, intent);
        finish();
    }
}
