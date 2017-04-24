package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.ProductDetialImageAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.ShoppingCarBean;
import com.dgkj.fxmall.bean.ShoppingGoodsBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnSelectColorSizeFinishedListener;
import com.dgkj.fxmall.view.myView.SelectColorAndSizeDialog;
import com.dgkj.fxmall.view.myView.ShareDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;

public class ProductDetialActivity extends BaseActivity {

    @BindView(R.id.rv_image_preview)
    RecyclerView rvImagePreview;
    @BindView(R.id.tv_product_detial)
    TextView tvProductDetial;
    @BindView(R.id.tv_product_price)
    TextView tvProductPrice;
    @BindView(R.id.tv_product_express)
    TextView tvProductExpress;
    @BindView(R.id.tv_product_scales)
    TextView tvProductScales;
    @BindView(R.id.tv_product_address)
    TextView tvProductAddress;
    @BindView(R.id.iv_product_back)
    ImageView ivProductBack;
    @BindView(R.id.tv_product_car)
    TextView tvProductCar;
    @BindView(R.id.tv_product_store)
    TextView tvProductStore;
    @BindView(R.id.tv_product_share)
    TextView tvProductShare;
    @BindView(R.id.tv_car_count1)
    TextView tvCarCount1;
    @BindView(R.id.activity_product_detial)
    LinearLayout activityProductDetial;
    @BindView(R.id.tv_product_addCar)
    TextView tvProductAddCar;
    @BindView(R.id.tv_product_buy)
    TextView tvProductBuy;
    @BindView(R.id.tv_select_color)
    TextView tvSelectColor;
    @BindView(R.id.tv_all_comment)
    TextView tvAllComment;
    @BindView(R.id.rv_detial_comment)
    RecyclerView rvDetialComment;
    @BindView(R.id.civ_shangpu_item)
    CircleImageView civShangpuItem;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_store_address)
    TextView tvStoreAddress;
    @BindView(R.id.tv_delete_recommend)
    TextView tvCancleRecommend;
    @BindView(R.id.rb_commend)
    ImageView rbCommend;
    @BindView(R.id.tv_total_sales)
    TextView tvTotalSales;
    @BindView(R.id.tv_total_goods)
    TextView tvTotalGoods;
    @BindView(R.id.tv_describe)
    TextView tvDescribe;
    @BindView(R.id.tv_price_reasonable)
    TextView tvPriceReasonable;
    @BindView(R.id.tv_quality_ok)
    TextView tvQualityOk;
    @BindView(R.id.tv_enter_store)
    TextView tvEnterStore;
    @BindView(R.id.rv_goods_detial)
    RecyclerView rvGoodsDetial;
    @BindView(R.id.tv_vip_price)
    TextView tvVipPrice;

    private ProductDetialImageAdapter adapter;
    private MainProductBean product;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private FXMallControl control = new FXMallControl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detial);
        ButterKnife.bind(this);
        setDatas();
    }

    private void setDatas() {
        product = (MainProductBean) getIntent().getSerializableExtra("product");
        if(product==null){return;}
        tvProductDetial.setText(product.getIntroduce());
        tvProductPrice.setText("¥" + product.getPrice());
        tvProductExpress.setText(product.getExpress());
        tvProductScales.setText("销量：" + product.getSales() + "笔");
        tvProductAddress.setText(product.getAddress());
        tvVipPrice.setText("会员价¥"+product.getVipPrice());

        adapter = new ProductDetialImageAdapter(this, R.layout.item_product_detial_image, product.getUrl());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPositionWithOffset(1, 100);
       // layoutManager.setStackFromEnd(true);
        rvImagePreview.setLayoutManager(layoutManager);
        rvImagePreview.setAdapter(adapter);
    }

    @OnClick(R.id.tv_product_share)
    public void share() {
        //TODO 仿淘口令分享
        ShareDialog shareDialog = new ShareDialog(product);
        shareDialog.show(getSupportFragmentManager(),"");

    }

    @OnClick(R.id.iv_product_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_product_car)
    public void toShoppingCar() {//去购物车
        jumpTo(ShoppingCarActivity.class, false);
    }

    @OnClick(R.id.tv_product_store)
    public void toStore() {//去商铺
        jumpTo(StoreMainPageActivity.class, false);
    }

    @OnClick(R.id.tv_product_addCar)
    public void addToCar() {//加入购物车
        control.add2shoppingcar(this,sp.get("token"),product.getSkuId(),client);
    }

    @OnClick(R.id.tv_product_buy)
    public void buyNow() {//立即购买
        ShoppingGoodsBean goodsBean = new ShoppingGoodsBean();
        goodsBean.setCount(product.getCount());
        goodsBean.setIntroduce(product.getIntroduce());
        goodsBean.setSkuId(product.getSkuId());
        goodsBean.setUrl(product.getUrl().get(0));
        goodsBean.setPrice(Double.parseDouble(product.getVipPrice()));
        goodsBean.setColor(product.getColor());
        goodsBean.setStoreName(product.getStoreName());
        goodsBean.setInventory(product.getInventory());
        SelectColorAndSizeDialog dialog = new SelectColorAndSizeDialog(this, R.layout.layout_edit_select_color_size, goodsBean, "buy", new OnSelectColorSizeFinishedListener() {
            @Override
            public void selectCompelete(String result) {
                tvSelectColor.setText("已选："+result);
            }
        });
        dialog.show(getSupportFragmentManager(),"");
    }

    @OnClick(R.id.tv_select_color)
    public void setlectCS() {
        ShoppingGoodsBean goodsBean = new ShoppingGoodsBean();
        goodsBean.setCount(product.getCount());
        goodsBean.setIntroduce(product.getIntroduce());
        goodsBean.setSkuId(product.getSkuId());
        goodsBean.setUrl(product.getUrl().get(0));
        goodsBean.setPrice(Double.parseDouble(product.getVipPrice()));
        goodsBean.setColor(product.getColor());
        goodsBean.setStoreName(product.getStoreName());
        goodsBean.setInventory(product.getInventory());
         SelectColorAndSizeDialog dialog = new SelectColorAndSizeDialog(this, R.layout.layout_car_select_color_size, goodsBean, "edit", new OnSelectColorSizeFinishedListener() {
             @Override
             public void selectCompelete(String result) {
                 tvSelectColor.setText("已选："+result);
             }
         });
         dialog.show(getSupportFragmentManager(),"");
    }

    @OnClick(R.id.tv_all_comment)
    public void allComments(){
        jumpTo(TotalCommentActivity.class,false);
    }

    @OnClick(R.id.tv_enter_store)
    public void enterStore(){
        jumpTo(StoreMainPageActivity.class,false);
    }
}
