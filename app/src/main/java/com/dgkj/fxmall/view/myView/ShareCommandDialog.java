package com.dgkj.fxmall.view.myView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.ColorSizeBean;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.ShoppingCarBean;
import com.dgkj.fxmall.bean.ShoppingGoodsBean;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnSelectColorSizeFinishedListener;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.view.ConfirmOrderActivity;
import com.dgkj.fxmall.view.HomePageActivity;
import com.dgkj.fxmall.view.ProductDetialActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Android004 on 2017/4/5.
 */
@SuppressLint("ValidFragment")
public class ShareCommandDialog extends PopupWindow {

    private TextView tvContent,tvPrice;
    private ImageView ivProduct;
    private OkHttpClient client;
    private SharedPreferencesUnit sp;
    private FXMallControl control;
    private BaseActivity activity;
    private View conentView;
    private ClipboardManager clipboardManager;
    private Handler handler ;
    private String productId;
    private MainProductBean product;
    private ArrayList<ColorSizeBean> colors;

    public ShareCommandDialog(BaseActivity activity, ClipboardManager clipboardManager){
        this.activity = activity;
        this.clipboardManager = clipboardManager;
        client = new OkHttpClient.Builder().build();
        sp = SharedPreferencesUnit.getInstance(activity);
        control = new FXMallControl();
        handler = new Handler(Looper.getMainLooper());
        product = new MainProductBean();
        colors = new ArrayList<>();
        init();
        getProductData(new LoadFinishedListener() {
            @Override
            public void onLoadFinished() {
                setData();
            }
        });



    }

    /**
     * 获取分享的商品详情
     */
    private void getProductData(final LoadFinishedListener listener) {
        final ClipData primaryClip = clipboardManager.getPrimaryClip();
        if(primaryClip==null){return;}
        CharSequence pate = primaryClip.getItemAt(0).getText();
        if(pate==null){return;}
        String clip = pate.toString();
        if(clip.contains("FXMall")){
            Pattern pattern = Pattern.compile("^\\d+$");
            Matcher matcher = pattern.matcher(clip);
            while (matcher.find()){
                int start = matcher.start();
                int end = matcher.end();
                productId = clip.substring(start,end+1);
            }
            FormBody body = new FormBody.Builder()
                    .add("id",productId)
                    .build();
            Request request = new Request.Builder()
                    .post(body)
                    .url(FXConst.GET_PRODUCT_DETIAL_BY_ID)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String string = response.body().string();
                    if(string.contains("1000")){
                        try {
                            JSONObject object = new JSONObject(string);
                            JSONObject jsonObject = object.getJSONObject("data");
                            product.setId(jsonObject.getInt("id"));
                            product.setDescribeScore(jsonObject.getInt("describeScore"));
                            product.setPriceScore(jsonObject.getInt("transportScore"));
                            product.setQualityScore(jsonObject.getInt("qualityScore"));
                            product.setTotalScore(jsonObject.getInt("totalScore"));
                            product.setIntroduce(jsonObject.getString("detail"));
                            product.setSales(jsonObject.getInt("sales") + "");
                            String detailUrls = jsonObject.getString("detailUrl");
                            JSONArray detailUrl = new JSONArray(detailUrls);
                            List<String> mainImages = new ArrayList<>();
                            for (int j = 0; j < detailUrl.length(); j++) {
                                mainImages.add(detailUrl.getString(j));
                            }
                            product.setDetialUrls(mainImages);
                            List<String> detialImages = new ArrayList<>();
                            String pictrue = jsonObject.getString("pictrue");
                            JSONArray pictrues = new JSONArray(pictrue);
                            for (int k = 0; k < pictrues.length(); k++) {
                                detialImages.add(pictrues.getString(k));
                            }
                            product.setUrls(detialImages);
                            product.setUrl(product.getUrls().get(0));
                            //TODO 商品对应店铺详情
                            JSONObject store = jsonObject.getJSONObject("store");
                            StoreBean storeBean = new StoreBean();
                            storeBean.setName(store.getString("storeName"));
                            String address = store.getString("address");
                            storeBean.setAdress(address);
                            storeBean.setGoodsCount(store.getInt("cnum"));
                            storeBean.setTotalScals(store.getInt("sales"));
                            storeBean.setCreateTime(store.getString("createTime"));
                            storeBean.setQualityScore(store.getDouble("qualityScore"));
                            storeBean.setDescribeScore(store.getDouble("describeScore"));
                            storeBean.setPriceScore(store.getDouble("transportScore"));
                            storeBean.setStars(store.getInt("totalScore"));
                            storeBean.setIconUrl(store.getString("logo"));
                            storeBean.setLicence(store.getString("license"));
                            storeBean.setKeeper(store.getString("storekeeper"));
                            storeBean.setId(store.getInt("id"));
                            storeBean.setRecommender("哎购商城");
                            storeBean.setMainUrls(new ArrayList<String>());
                            storeBean.setPhone(store.getString("phone"));
                            storeBean.setStars(4);

                            product.setStoreBean(storeBean);
                            product.setAddress(address.substring(0, address.indexOf("市")+1));

                            JSONArray dataset = object.getJSONArray("dataset");
                            for (int i = 0; i < dataset.length(); i++) {
                                ColorSizeBean color = new ColorSizeBean();
                                JSONObject colorbean = dataset.getJSONObject(i);
                                color.setColor(colorbean.getString("content"));
                                color.setInventory(colorbean.getInt("inventory"));
                                color.setBrokrage(colorbean.getInt("brokerage"));
                                color.setPrice(colorbean.getInt("price"));
                                color.setId(colorbean.getInt("id"));
                                colors.add(color);
                            }
                            ColorSizeBean colorSizeBean = colors.get(0);
                            product.setSkuId(colorSizeBean.getId());
                            product.setColor(colorSizeBean.getColor());
                            product.setInventory(colorSizeBean.getInventory());
                            product.setBrokerage(colorSizeBean.getBrokrage());
                            product.setPrice(colorSizeBean.getPrice());
                            product.setCount(1);
                            product.setVipPrice(colorSizeBean.getPrice()*(1-MyApplication.vipRate));
                            product.setExpress("包邮");//TODO 待定
                            listener.onLoadFinished();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

    }

    private void setData() {

        TextView tvGirl = (TextView) conentView.findViewById(R.id.tv_confirm);
        tvContent.setText(product.getIntroduce());
        tvPrice.setText("¥"+product.getPrice());
        Glide.with(activity).load(product.getUrl()).into(ivProduct);

        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 获取商品数据
                Intent intent = new Intent(activity,ProductDetialActivity.class);
                intent.putExtra("product",product);
                intent.putParcelableArrayListExtra("colors",colors);

                activity.startActivity(intent);
                dismiss();
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null,""));
            }
        });
        TextView tvBoy = (TextView) conentView.findViewById(R.id.tv_cancle);
        tvBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null,""));
            }
        });



    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.layout_check_command_dialog, null);

        tvContent = (TextView) conentView.findViewById(R.id.tv_command_content);
        tvPrice = (TextView) conentView.findViewById(R.id.tv_price);
        ivProduct = (ImageView) conentView.findViewById(R.id.iv_command_picture);


        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(final View parent) {
        if(parent==null){return;}
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showAtLocation(parent,Gravity.CENTER,0,0);
                }
            },500);
        } else {
            this.dismiss();
        }
    }


    private interface LoadFinishedListener{
        void onLoadFinished();
    }
}
