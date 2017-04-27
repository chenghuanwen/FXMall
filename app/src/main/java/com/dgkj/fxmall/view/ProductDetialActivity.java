package com.dgkj.fxmall.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.CommentAdapter;
import com.dgkj.fxmall.adapter.ProductDetialImageAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.CommentBean;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.ShoppingGoodsBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetProductCommentListFinishListener;
import com.dgkj.fxmall.listener.OnSelectColorSizeFinishedListener;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.view.myView.SelectColorAndSizeDialog;
import com.dgkj.fxmall.view.myView.ShareDialog;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.activity_product_detial)
    LinearLayout activityProductDetial;

    private ProductDetialImageAdapter adapter;
    private MainProductBean product;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private FXMallControl control = new FXMallControl();
    private List<CommentBean> commentList;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detial);
        ButterKnife.bind(this);
        setDatas();
        getComments();
    }

    @Override
    public View getContentView() {
        LogUtil.i("TAG","获取contentview=======");
        return activityProductDetial;
    }

    /**
     * 获取商品评论
     */
    private void getComments() {
        control.getProductComments(this, product.getId(), 1, 4, client, new OnGetProductCommentListFinishListener() {
            @Override
            public void onGetProductCommentListFinished(List<CommentBean> comments) {
                commentAdapter.addAll(comments, true);
            }
        });
    }

    private void setDatas() {
        product = (MainProductBean) getIntent().getSerializableExtra("product");
        if (product == null) {
            return;
        }
        tvProductDetial.setText(product.getIntroduce());
        tvProductPrice.setText("¥" + product.getPrice());
        tvProductExpress.setText(product.getExpress());
        tvProductScales.setText("销量：" + product.getSales() + "笔");
        tvProductAddress.setText(product.getAddress());
        tvVipPrice.setText("会员价¥" + product.getVipPrice());

        adapter = new ProductDetialImageAdapter(this, R.layout.item_product_detial_image, product.getUrl());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPositionWithOffset(1, 100);
        // layoutManager.setStackFromEnd(true);
        rvImagePreview.setLayoutManager(layoutManager);
        rvImagePreview.setAdapter(adapter);

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, R.layout.item_comment, commentList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvDetialComment.setLayoutManager(linearLayoutManager);
        rvDetialComment.setAdapter(commentAdapter);
    }

    @OnClick(R.id.tv_product_share)
    public void share() {
        //TODO 仿淘口令分享
        ShareDialog shareDialog = new ShareDialog(product);
        shareDialog.show(getSupportFragmentManager(), "");

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
        control.add2shoppingcar(this, sp.get("token"), product.getSkuId(), client);
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
                tvSelectColor.setText("已选：" + result);
            }
        });
        dialog.showPopupWindow(activityProductDetial);
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
         dialog.showPopupWindow(activityProductDetial);


       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.layout_edit_select_color_size, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = display.getWidth(); //设置宽度
        lp.height = display.getHeight();
        window.setAttributes(lp);
        alertDialog.show();*/


       /* View contentview = getLayoutInflater().inflate(R.layout.layout_car_select_color_size, null);
        PopupWindow pw = new PopupWindow(contentview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //设置触摸对话框以外区域，对话框消失
        pw.setFocusable(true);
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#00000000"));
        pw.setBackgroundDrawable(cd);
        //pw.showAsDropDown(tvSearchType);
        pw.showAtLocation(activityProductDetial, Gravity.BOTTOM, 0, 0);*/
    }

    @OnClick(R.id.tv_all_comment)
    public void allComments() {
        Intent intent = new Intent(this,TotalCommentActivity.class);
        intent.putExtra("product",product);
        jumpTo(intent,false);
    }

    @OnClick(R.id.tv_enter_store)
    public void enterStore() {
        jumpTo(StoreMainPageActivity.class, false);
    }
}
