package com.dgkj.fxmall.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.ViewFlipperAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.StoreProductBean;
import com.dgkj.fxmall.constans.FXConst;

import java.io.IOException;
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

public class MyStoreProductDetialActivity extends BaseActivity {
    @BindView(R.id.avf_products)
    AdapterViewFlipper avfProducts;
    @BindView(R.id.et_product_title)
    EditText etProductTitle;
    @BindView(R.id.et_product_destribe)
    EditText etProductDestribe;
    @BindView(R.id.et_product_type)
    EditText etProductType;
    @BindView(R.id.et_product_price)
    EditText etProductPrice;
    @BindView(R.id.et_product_save_count)
    EditText etProductSaveCount;
    @BindView(R.id.et_demand_count)
    EditText etDemandCount;
    @BindView(R.id.ll_add_type_container)
    LinearLayout llAddTypeContainer;
    @BindView(R.id.tv_add_type)
    TextView tvAddType;
    @BindView(R.id.tv_product_detial)
    TextView tvProductDetial;
    @BindView(R.id.tv_product_classify)
    TextView tvProductClassify;
    @BindView(R.id.cb_deliver)
    CheckBox cbDeliver;
    @BindView(R.id.tv_postage_model)
    TextView tvPostageModel;
    @BindView(R.id.rv_postage_select)
    ListView rvPostageSelect;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btn_on_off)
    Button btnOnOff;
    private View headerview;
    private String from = "";
    private int position;//商品在列表中的位置
    private int statu;//判断是出售中的还是仓库中的
    private StoreProductBean product;
    private AlertDialog pw;
    private OkHttpClient client = new OkHttpClient.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store_product_detial);
        ButterKnife.bind(this);
        initHeaderView();
        disableEdit();
        setData();
    }

    private void disableEdit() {//禁用edittext编辑功能
        etProductTitle.setKeyListener(null);
        etDemandCount.setKeyListener(null);
        etProductDestribe.setKeyListener(null);
        etProductPrice.setKeyListener(null);
        etProductSaveCount.setKeyListener(null);
        etProductType.setKeyListener(null);
    }

    private void setData() {
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        position = intent.getIntExtra("position",-1);

        if("sale".equals(from)){
            btnOnOff.setText("下架");
            statu = 1;
        }else {
            btnOnOff.setText("上架");
            statu = 0;
        }

        product = (StoreProductBean) intent.getSerializableExtra("product");

        List<String> mainUrls = product.getMainUrls();
        avfProducts.setAdapter(new ViewFlipperAdapter(mainUrls, this));
        avfProducts.setAutoStart(true);
        avfProducts.startFlipping();

        etProductTitle.setText(product.getTitel());
        etProductDestribe.setText(product.getDescribe());
        etProductPrice.setText(product.getPrice());
        etProductType.setText(product.getColor());
        etProductSaveCount.setText(product.getInventory());
        etDemandCount.setText(product.getBrokerage());

    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "商品详情");

    }

    @OnClick(R.id.btn_delete)
    public void delete(){
        showDeleteDialog("你确定要删除此商品吗？");
    }

    @OnClick(R.id.btn_on_off)
    public void onOrOffLine(){
        if("sale".equals(from)){
            showDeleteDialog("你确定下架此商品吗？");
        }else {
            showDeleteDialog("你确定上架此商品吗？");
        }
    }


    @OnClick(R.id.ib_back)
    public void back() {
        MyApplication.selectedPictures = null;
        finish();
    }


    private void showDeleteDialog(final String titel) {
        View contentview = getLayoutInflater().inflate(R.layout.layout_delete_dialog, null);
        pw = new AlertDialog.Builder(this).create();
        pw.setView(contentview);
        TextView ttTitel = (TextView) contentview.findViewById(R.id.tv_dialog_titel);
        ttTitel.setText(titel);
        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_confirm);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titel.contains("删除")) {
                    //TODO 通知服务器删除
                    Intent intent = new Intent();
                    intent.putExtra("position",position);
                    setResult(162,intent);
                    pw.dismiss();
                    finish();
                } else {
                    online(product.getId(),statu);
                }

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
     * @param id    商品ID
     */
    private void online(int id, int statu) {
        FormBody body = new FormBody.Builder()
                .add("store.user.token", sp.get("token"))
                .add("ids", id + "")
                .add("status", statu + "")
                .build();
        final Request request = new Request.Builder()
                .post(body)
                .url(FXConst.PRODUCT_ONLINE_OFFLINE)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               toastInUI(MyStoreProductDetialActivity.this,"网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body().string().contains("1000")) {
                    toastInUI(MyStoreProductDetialActivity.this,"操作成功");
                } else {
                    toastInUI(MyStoreProductDetialActivity.this,"操作失败");
                }
            }
        });
    }

}
