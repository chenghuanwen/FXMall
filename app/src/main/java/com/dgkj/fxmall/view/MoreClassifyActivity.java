package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class MoreClassifyActivity extends BaseActivity {
    @BindView(R.id.rv_all_classify)
    ListView rvAllClassify;
    @BindView(R.id.rv_sub_classify)
    RecyclerView rvSubClassify;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_search_content)
    EditText tvSearchContent;
    private View headerview;
    private List<SuperClassifyBean> allClassify;
    private List<ProductClassifyBean> subClassify;
    private ProductClassifyAdapter adapter;
    private String baseClassify="";
    private FXMallControl control = new FXMallControl();
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private CommonAdapter<SuperClassifyBean> subAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_classify);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ButterKnife.bind(this);
        initHeaderview();
        initDatas();
        getSuperClassify();
    }

    /**
     * 获取一级分类
     */
    private void getSuperClassify() {
        control.getStoreSuperClassify(this, sp.get("token"), client, new OnGetStoreSuperClassifyFinishedListener() {
            @Override
            public void onGetStoreSuperClassifyFinished(List<SuperClassifyBean> classifyList) {
                subAdapter.addAll(classifyList,true);
            }
        });
    }


    private void initHeaderview() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "全部分类");
    }


    private void initDatas() {
        allClassify = new ArrayList<>();
        subClassify = new ArrayList<>();
       /* //TEST
        for (int i = 0; i < 10; i++) {
            allClassify.add("服装");
        }
*/
       /* for (int i = 0; i < 20; i++) {
            ProductClassifyBean classify = new ProductClassifyBean();
            classify.setUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490778845608&di=59152ae78c6655e37565a97559ec5bd6&imgtype=0&src=http%3A%2F%2Fgb.cri.cn%2Fmmsource%2Fimages%2F2010%2F09%2F27%2Feo100927986.jpg");
            classify.setTaxon("时尚套装");
            subClassify.add(classify);
        }*/
        subAdapter = new CommonAdapter<SuperClassifyBean>(this,R.layout.item_all_classify,allClassify) {
            @Override
            protected void convert(ViewHolder holder, final SuperClassifyBean o, int position) {
                holder.setText(R.id.tv_base_classify,o.getType());

            }
        };

        rvAllClassify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                baseClassify = subClassify.get(position).getTaxon();
                int superTypeId = allClassify.get(position).getId();
                getSubclassify(superTypeId);
            }
        });
        rvAllClassify.setAdapter(subAdapter);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,3);
        rvSubClassify.setLayoutManager(linearLayoutManager);
        adapter = new ProductClassifyAdapter(this, R.layout.item_all_subclassify, subClassify, "search");
        ItemOffsetDecoration decoration = new ItemOffsetDecoration(20);
        rvSubClassify.addItemDecoration(decoration);
        rvSubClassify.setAdapter(adapter);
    }

    /**
     * 获取二级分类
     * @param superTypeId
     */
    private void getSubclassify(int superTypeId) {
        control.getSubclassify(this, superTypeId, client, new OnGetSubclassifyFinishedListener() {
            @Override
            public void onGetSubclassifyFinished(List<ProductClassifyBean> subList) {
                adapter.addAll(subList,true);
            }
        });
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
