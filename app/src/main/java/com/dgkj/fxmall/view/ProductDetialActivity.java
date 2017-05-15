package com.dgkj.fxmall.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.CommentAdapter;
import com.dgkj.fxmall.adapter.ProductDetialImageAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.CommentBean;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.ShoppingGoodsBean;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetProductCommentListFinishListener;
import com.dgkj.fxmall.listener.OnSelectColorSizeFinishedListener;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.view.myView.SelectColorAndSizeDialog;
import com.dgkj.fxmall.view.myView.ShareDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private ProductDetialImageAdapter detialAdapter;
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

        if(MyApplication.shoppingCount == 0){
            tvCarCount1.setVisibility(View.GONE);
        }else {
            tvCarCount1.setVisibility(View.VISIBLE);
            tvCarCount1.setText(MyApplication.shoppingCount+"");
        }

    }

    @Override
    public View getContentView() {
        return activityProductDetial;
    }

    /**
     * 获取商品评论
     */
    private void getComments() {
        control.getProductComments(this, product.getId(), 1, 4, client, new OnGetProductCommentListFinishListener() {
            @Override
            public void onGetProductCommentListFinished(final List<CommentBean> comments) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        commentAdapter.addAll(comments, true);
                    }
                });

            }
        });
    }

    private void setDatas() {
        product = getIntent().getParcelableExtra("product");
        if (product == null) {
            return;
        }
        //产品信息
        tvProductDetial.setText(product.getIntroduce());
        tvProductPrice.setText("¥" + product.getPrice());
        tvProductExpress.setText("邮费:¥"+product.getExpress());
        tvProductScales.setText("销量：" + product.getSales() + "笔");
        tvProductAddress.setText(product.getAddress());
        tvVipPrice.setText("会员价:¥" + product.getVipPrice());

        //店铺信息
        StoreBean storeBean = product.getStoreBean();
        tvStoreName.setText(storeBean.getName());
        tvStoreAddress.setText(product.getAddress());
        Glide.with(this).load(storeBean.getIconUrl()).into(civShangpuItem);
        tvTotalGoods.setText(storeBean.getGoodsCount()+"");
        tvTotalSales.setText(storeBean.getTotalScals()+"");

        String describe = "描述相符 " + storeBean.getDescribeScore();
        String reasonable = "价格合理 " + storeBean.getPriceScore();
        String qualityOk = "质量满意 " + storeBean.getQualityScore();
        tvDescribe.setText(changeColor(describe));
        tvPriceReasonable.setText(changeColor(reasonable));
        tvQualityOk.setText(changeColor(qualityOk));

        //TODO 根据平分数选择不同的星星图片
        float stars = storeBean.getStars();
        if(stars<=1.0){
            rbCommend.setImageResource(R.mipmap.dppj_dj1);
        }else if(stars>1.0 && stars<1.9){
            rbCommend.setImageResource(R.mipmap.lan_yiban);
        }else if(stars>=1.9 && stars<=2.4){
            rbCommend.setImageResource(R.mipmap.dppj_dj2);
        }else if(stars>2.4 && stars<2.9){
            rbCommend.setImageResource(R.mipmap.lan_erban);
        }else if(stars>=2.9 && stars<=3.4){
            rbCommend.setImageResource(R.mipmap.dppj_dj3);
        }else if(stars>3.4 && stars<=3.9){
            rbCommend.setImageResource(R.mipmap.lan_sanban);
        }else if(stars>3.9 && stars<=4.4){
            rbCommend.setImageResource(R.mipmap.dppj_dj4);
        }else if(stars>4.4 && stars<=4.9){
            rbCommend.setImageResource(R.mipmap.lan_siban);
        }else if(stars>4.9){
            rbCommend.setImageResource(R.mipmap.dppj_dj5);
        }


        //商品图片详情
        adapter = new ProductDetialImageAdapter(this, R.layout.item_product_main_image, product.getUrls());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPositionWithOffset(1, 100);
        // layoutManager.setStackFromEnd(true);
        rvImagePreview.setLayoutManager(layoutManager);
        rvImagePreview.setAdapter(adapter);

        detialAdapter = new ProductDetialImageAdapter(this,R.layout.item_product_detial_image,product.getDetialUrls());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvGoodsDetial.setLayoutManager(manager);
        rvGoodsDetial.setAdapter(detialAdapter);


        //商品评论区
        commentList = new ArrayList<>();
        //TEST
        for (int i = 0; i < 4; i++) {
            CommentBean commentBean = new CommentBean();
            commentBean.setIcon("http://img2015.zdface.com/20161230/3e633f4d71c824931bff72fff3d241b8.jpg");
            commentBean.setContent("棒棒哒");
            commentBean.setName("鼎诰瘟神");
            commentBean.setStars(4.6f);
            commentList.add(commentBean);
        }
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
        Intent intent = new Intent(this,StoreMainPageActivity.class);
        intent.putExtra("store",product.getStoreBean());
        jumpTo(intent,false);
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
        goodsBean.setUrl(product.getUrl());
        goodsBean.setPrice(product.getVipPrice());
        goodsBean.setColor(product.getColor());
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
        goodsBean.setUrl(product.getUrls().get(0));
        goodsBean.setPrice(product.getVipPrice());
        goodsBean.setColor(product.getColor());
        goodsBean.setInventory(product.getInventory());
         SelectColorAndSizeDialog dialog = new SelectColorAndSizeDialog(this, R.layout.layout_car_select_color_size, goodsBean, "edit", new OnSelectColorSizeFinishedListener() {
             @Override
             public void selectCompelete(String result) {
                 tvSelectColor.setText("已选："+result);
             }
         });
         dialog.showPopupWindow(activityProductDetial);


    }

    @OnClick(R.id.tv_all_comment)
    public void allComments() {
        Intent intent = new Intent(this,TotalCommentActivity.class);
        intent.putExtra("product",product);
        jumpTo(intent,false);
    }

    @OnClick(R.id.tv_enter_store)
    public void enterStore() {
        Intent intent = new Intent(this,StoreMainPageActivity.class);
        intent.putExtra("store",product.getStoreBean());
        jumpTo(intent, false);
    }


    public SpannableString changeColor(String s){
        SpannableString sp = new SpannableString(s);
        Pattern pattern = Pattern.compile("\\d+\\.?\\d*");
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            // LogUtil.i("TAG","start=="+start+"end=="+end);
            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#ff4a42")),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sp;
    }
}
