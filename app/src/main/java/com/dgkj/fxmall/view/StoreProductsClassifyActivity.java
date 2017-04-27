package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.StoreProductClassifyBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetStoreProductClassifyFinishedListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class StoreProductsClassifyActivity extends BaseActivity {
    @BindView(R.id.tv_all_products)
    TextView tvAllProducts;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.ll_store_product_classify)
    LinearLayout llStoreProductClassify;
    @BindView(R.id.activity_store_products_classify)
    LinearLayout activityStoreProductsClassify;
    private View headerview;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private FXMallControl control = new FXMallControl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_products_classify);
        ButterKnife.bind(this);
        initHeaderView();
        refresh();
    }

    @Override
    public View getContentView() {
        return activityStoreProductsClassify;
    }

    private void refresh() {
        control.getStoreProductClassify(this, sp.get("token"), 0, client, new OnGetStoreProductClassifyFinishedListener() {
            @Override
            public void onGetStoreProductClassifyFinished(List<StoreProductClassifyBean> classifyList) {
                for (int i = 0; i < classifyList.size(); i++) {
                    final StoreProductClassifyBean classify = classifyList.get(i);
                    View view = getLayoutInflater().inflate(R.layout.layout_add_store_product_classify, llStoreProductClassify);
                    llStoreProductClassify.addView(view);
                    TextView tvName = (TextView) view.findViewById(R.id.tv_product_type);
                    TextView tvCount = (TextView) view.findViewById(R.id.tv_classify_count);
                    tvName.setText(classify.getName());
                    tvCount.setText("(" + classify.getCount() + "件)");
                    tvCount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jump(classify.getName());
                        }
                    });
                }
            }
        });
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "商品分类");
    }

    @OnClick(R.id.tv_all_products)
    public void toAll() {
        jump("全部商品");
    }

   /* @OnClick(R.id.tv_product_type1)
    public void toType1(){
        jump(tvProductType1.getText().toString());
    }

    @OnClick(R.id.tv_product_type2)
    public void toType2(){
        jump(tvProductType2.getText().toString());
    }

    @OnClick(R.id.tv_product_type3)
    public void toType3(){
        jump(tvProductType3.getText().toString());
    }
*/

    private void jump(String type) {
        Intent intent = new Intent(this, SomeProductSubClassifyActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
