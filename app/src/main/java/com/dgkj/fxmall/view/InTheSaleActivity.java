package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class InTheSaleActivity extends BaseActivity {
    @BindView(R.id.tv_count_all)
    TextView tvCountAll;
    @BindView(R.id.ll_store_all)
    LinearLayout llStoreAll;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.rb_onSale)
    RadioButton rbOnSale;
    @BindView(R.id.rb_rest)
    RadioButton rbRest;
    @BindView(R.id.underline1)
    View underline1;
    @BindView(R.id.underline2)
    View underline2;
    @BindView(R.id.rg_product)
    RadioGroup rgProduct;
    @BindView(R.id.ll_store_product_classify)
    LinearLayout llStoreProductClassify;
    private String from;
    private View headerview;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private FXMallControl control = new FXMallControl();
    private int statu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_the_sale);
        ButterKnife.bind(this);

        initHeaderView();
        from = getIntent().getStringExtra("from");
        if ("sale".equals(from)) {
            rbOnSale.setChecked(true);
            underline1.setVisibility(View.VISIBLE);
            underline2.setVisibility(View.GONE);
            statu = 1;
        } else {
            rbRest.setChecked(true);
            underline1.setVisibility(View.GONE);
            underline2.setVisibility(View.VISIBLE);
            statu = 0;
        }
        //TODO 需要根据服务器返回的该商家的所有商品类型再来确定有多少子类布局
        refresh();
    }

    private void refresh() {
        control.getStoreProductClassify(this, sp.get("token"),statu, client, new OnGetStoreProductClassifyFinishedListener() {
            @Override
            public void onGetStoreProductClassifyFinished(List<StoreProductClassifyBean> classifyList) {
                for (int i = 0; i < classifyList.size(); i++) {
                    final StoreProductClassifyBean classify = classifyList.get(i);
                    View view = getLayoutInflater().inflate(R.layout.layout_add_store_product_classify, llStoreProductClassify);
                    llStoreProductClassify.addView(view);
                    TextView tvName = (TextView) view.findViewById(R.id.tv_product_type);
                    TextView tvCount = (TextView) view.findViewById(R.id.tv_classify_count);
                    tvName.setText(classify.getName());
                    tvCount.setText("("+classify.getCount()+"件)");
                    tvCount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goDetial(classify.getName());
                        }
                    });
                }
            }
        });
    }


    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "店铺商品");

        rgProduct.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_onSale) {
                    underline1.setVisibility(View.VISIBLE);
                    underline2.setVisibility(View.GONE);
                    from = "sale";
                } else {
                    underline1.setVisibility(View.GONE);
                    underline2.setVisibility(View.VISIBLE);
                    from = "warehouse";
                }
            }
        });
    }

    @OnClick(R.id.ll_store_all)
    public void all() {
        Intent intent = new Intent(this, ShangpuAllProductsActivity.class);
        intent.putExtra("from", from);
        startActivity(intent);
    }



    public void goDetial(String type) {
        Intent intent = new Intent(this, ShangpuAllProductsActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("from", from);
        intent.putExtra("statu",statu);
        startActivity(intent);
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
