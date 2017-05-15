package com.dgkj.fxmall.model;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.BannerBean;
import com.dgkj.fxmall.bean.ColorSizeBean;
import com.dgkj.fxmall.bean.CommentBean;
import com.dgkj.fxmall.bean.DemandMallClassifyBean;
import com.dgkj.fxmall.bean.ExpressCompanyBean;
import com.dgkj.fxmall.bean.HomePageRecommendBean;
import com.dgkj.fxmall.bean.LogisticsBean;
import com.dgkj.fxmall.bean.LogisticsMsgBean;
import com.dgkj.fxmall.bean.MainDemandBean;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.MainRecommendStoreBean;
import com.dgkj.fxmall.bean.OrderBean;
import com.dgkj.fxmall.bean.PostageBean;
import com.dgkj.fxmall.bean.ProductClassifyBean;
import com.dgkj.fxmall.bean.ShoppingCarBean;
import com.dgkj.fxmall.bean.ShoppingGoodsBean;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.bean.StoreProductBean;
import com.dgkj.fxmall.bean.StoreProductClassifyBean;
import com.dgkj.fxmall.bean.SuperClassifyBean;
import com.dgkj.fxmall.bean.TakeGoodsAddressBean;
import com.dgkj.fxmall.bean.TransactionRecordBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.listener.OnGetAllAddressFinishedListener;
import com.dgkj.fxmall.listener.OnGetAllPostageModelFinishedListener;
import com.dgkj.fxmall.listener.OnGetBannerFinishedListener;
import com.dgkj.fxmall.listener.OnGetDemandClassifyFinishedListener;
import com.dgkj.fxmall.listener.OnGetDemandDatasFinishedListener;
import com.dgkj.fxmall.listener.OnGetExpressCompanyFinishedListener;
import com.dgkj.fxmall.listener.OnGetHomeRecommendFinishedListener;
import com.dgkj.fxmall.listener.OnGetLogisticsDetialFinishedListener;
import com.dgkj.fxmall.listener.OnGetLogisticsMsgFinishedListener;
import com.dgkj.fxmall.listener.OnGetMainRecommendStoreFinishedListener;
import com.dgkj.fxmall.listener.OnGetMyDemandDataFinishedListener;
import com.dgkj.fxmall.listener.OnGetMyOrderInfoFinishedListener;
import com.dgkj.fxmall.listener.OnGetMyRecommendStoreFinishedListener;
import com.dgkj.fxmall.listener.OnGetPostageFinishedListener;
import com.dgkj.fxmall.listener.OnGetProductCSFinishedListener;
import com.dgkj.fxmall.listener.OnGetProductCommentListFinishListener;
import com.dgkj.fxmall.listener.OnGetProductDetialFinishedListener;
import com.dgkj.fxmall.listener.OnGetSearchHotWordsFinishedListener;
import com.dgkj.fxmall.listener.OnGetShoppingCarDataListener;
import com.dgkj.fxmall.listener.OnGetShoppingcarProductsFinishedListener;
import com.dgkj.fxmall.listener.OnGetStoreDetialFinishedListener;
import com.dgkj.fxmall.listener.OnGetStoreProductClassifyFinishedListener;
import com.dgkj.fxmall.listener.OnGetStoreProductsFinishedListener;
import com.dgkj.fxmall.listener.OnGetStoreSuperClassifyFinishedListener;
import com.dgkj.fxmall.listener.OnGetSubClassifyProductsFinishedListener;
import com.dgkj.fxmall.listener.OnGetSubclassifyFinishedListener;
import com.dgkj.fxmall.listener.OnGetTransactionRecorderFinishedListener;
import com.dgkj.fxmall.listener.OnSearchProductsFinishedListener;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.ToastUtil;
import com.dgkj.fxmall.view.LoginActivity;
import com.dgkj.fxmall.view.ProductDetialActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * FXMall 任务层
 * Created by 成焕文 on 2017/3/17.
 */

public class FXMallModel {
    /**
     * 支付宝支付业务
     *
     * @param context   支付activity对象
     * @param orderInfo 订单信息（由后台签名后返回）
     * @param mHandler  处理支付结果消息
     */
    public static void payV2(final Activity context, final String orderInfo, final Handler mHandler) {
        if (TextUtils.isEmpty(FXConst.APPID) || (TextUtils.isEmpty(FXConst.RSA2_PRIVATE) && TextUtils.isEmpty(FXConst.RSA_PRIVATE))) {
            new AlertDialog.Builder(context).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            context.finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
      /*  boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;*/

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = FXConst.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * 支付宝授权业务
     *
     * @param context  支付activity对象
     * @param authInfo 授权信息（由后台签名后返回）
     * @param mHandler
     */
    public static void authV2(final Activity context, final String authInfo, final Handler mHandler) {
        if (TextUtils.isEmpty(FXConst.PID) || TextUtils.isEmpty(FXConst.APPID)
                || (TextUtils.isEmpty(FXConst.RSA2_PRIVATE) && TextUtils.isEmpty(FXConst.RSA_PRIVATE))
                || TextUtils.isEmpty(FXConst.TARGET_ID)) {
            new AlertDialog.Builder(context).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
      /*  boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;*/
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(context);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = FXConst.SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }


    private static OkHttpClient client;

    static {
        client = new OkHttpClient.Builder().build();
    }


    /**
     * 获取需求大厅数据
     */
    public static void getDemandDatas(OnGetDemandDatasFinishedListener listener) {
        List<MainDemandBean> demands = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        urls.add("http://img.mp.itc.cn/upload/20170315/43a7cb3a994a41d3ac63806b0a983d88_th.jpeg");
        urls.add("http://www.86kx.com/uploads/allimg/170220/2310_170220115607_1.jpg");
        urls.add("http://img.mp.itc.cn/upload/20170315/43a7cb3a994a41d3ac63806b0a983d88_th.jpeg");
        urls.add("http://www.86kx.com/uploads/allimg/170220/2310_170220115607_1.jpg");
        //Test
        for (int i = 0; i < 20; i++) {
            MainDemandBean demandBean = new MainDemandBean();
            demandBean.setUrls(urls);
            demandBean.setUrl("http://cdn.duitang.com/uploads/item/201510/04/20151004162013_Shev3.jpeg");
            demandBean.setIntroduce("粉小萌牌酸辣粉，爱他就给他最好吃的粉小萌牌酸辣粉，爱他就给他最好吃的粉小萌牌酸辣粉，爱他就给他最好吃的粉小萌牌酸辣粉，爱他就给他最好吃的");
            demandBean.setDemand(200);
            demandBean.setAddress("广东深圳");
            demands.add(demandBean);
        }
        listener.onGetDemandDatasFinished(demands);
    }

    ;

    /**
     * 获取所有需求分类
     *
     * @param listener
     */
    public static void getDemandClassify(final Handler handler, final Context context, OkHttpClient client, final OnGetDemandClassifyFinishedListener listener) {

        final List<ProductClassifyBean> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ProductClassifyBean classifyBean = new ProductClassifyBean();
            classifyBean.setTaxon("食品");
            classifyBean.setUrl("http://img3.duitang.com/uploads/item/201505/05/20150505210633_NHRj4.jpeg");
            list.add(classifyBean);
        }
        listener.onGetDemandClassifyFinished(list);

        Request request = new Request.Builder()
                .url(FXConst.GET_APPLY_STORE_CLASSIFY)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            ProductClassifyBean classify = new ProductClassifyBean();
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            classify.setTaxon(jsonObject.getString("name"));
                            classify.setUrl(jsonObject.getString("url"));
                            //TODO TEST
                            classify.setUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494337489197&di=ccbe296f381cf9b3fb7ef8ba5ac453ee&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160223%2F9-1602230Z405-50.jpg");
                            classify.setId(jsonObject.getInt("id"));
                            list.add(classify);
                        }
                        listener.onGetDemandClassifyFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    /**
     * 获取商铺中商品
     *
     * @param status   分类类型，新品、销量、库存
     * @param listener
     */
    public static void getStoreProducts(final BaseActivity context, String token, OkHttpClient client, String orderby, int index, int size, int status, final OnGetStoreProductsFinishedListener listener) {
        final List<StoreProductBean> list = new ArrayList<>();
        FormBody body = new FormBody.Builder()
                .add("store.user.token", token)
                .add("orderby", orderby)
                .add("index", index + "")
                .add("size", size + "")
                .add("status", status + "")
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_STORE_ALL_PROCUCT)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.toastInUI(context.getApplicationContext(), "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        if (dataset.length() == 0) {
                            return;
                        }
                        for (int i = 0; i < dataset.length(); i++) {
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            JSONObject commodity = jsonObject.getJSONObject("commodity");
                            StoreProductBean product = new StoreProductBean();
                            product.setId(jsonObject.getInt("id"));
                            product.setBrokerage(jsonObject.getInt("brokerage"));
                            product.setPrice(commodity.getDouble("price"));
                            //TODO 根据佣金计算会员价
                            product.setVipPrice(product.getPrice() - product.getBrokerage() * MyApplication.vipRate);
                            product.setColor(jsonObject.getString("content"));
                            product.setDescribe(commodity.getString("detail"));
                            product.setSales(commodity.getInt("sales") + "");
                            product.setInventory(commodity.getInt("inventory") + "");
                            product.setStatu(commodity.getInt("status"));
                            product.setSkuID(commodity.getInt("id"));
                            product.setTitel(commodity.getString("name"));
                            String urls = commodity.getString("detailUrl");
                            JSONArray detailUrl = new JSONArray(urls);

                            List<String> mainImages = new ArrayList<>();
                            for (int j = 0; j < detailUrl.length(); j++) {
                                mainImages.add(detailUrl.getString(j));
                            }
                            product.setDetialUrls(mainImages);
                            List<String> detialImages = new ArrayList<>();
                            String pictrue = commodity.getString("pictrue");
                            JSONArray pictrues = new JSONArray(pictrue);
                            for (int k = 0; k < pictrues.length(); k++) {
                                detialImages.add(pictrues.getString(k));
                            }
                            product.setMainUrls(detialImages);
                            product.setUrl(product.getMainUrls().get(0));
                            list.add(product);
                        }
                        listener.OnGetStoreProductsFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        for (int i = 0; i < 20; i++) {
            StoreProductBean productBean = new StoreProductBean();
            productBean.setUrl("http://www.edu-hb.com/UpLoad/NewsImg/news/201644/2016031919442230.jpg");
            productBean.setDescribe("粉小萌，高颜值，绝佳味道，美味持久，久到离谱");
            productBean.setInventory("库存" + 23525);
            productBean.setPrice(25);
            productBean.setSales("销量" + 566665465);
            productBean.setBrokerage(20);
            list.add(productBean);
            listener.OnGetStoreProductsFinished(list);
        }
    }


    public static void getShoppingCarData(OnGetShoppingCarDataListener listener) {
        List<ShoppingCarBean> carBeanList = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        //TEST
        urls.add("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
        urls.add("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
        urls.add("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
        urls.add("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
        urls.add("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
        for (int i = 0; i < 5; i++) {
            ShoppingCarBean carBean = new ShoppingCarBean();
            ArrayList<ShoppingGoodsBean> goodsList = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ShoppingGoodsBean goods = new ShoppingGoodsBean();
                goods.setCount(2);
                goods.setIntroduce("粉小萌横空出世，买到就是赚到");
                goods.setUrl("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
                goods.setColor("红色-M码");
                goods.setPrice(11);
                goods.setStoreName("粉小萌");
                goods.setSelected(false);
                goods.setAddress("广东深圳");
                goods.setVipPrice(20);
                goods.setBrokerage(20);
                goods.setMainUrls(urls);
                goods.setDetialUrls(urls);
                goods.setCarId(i + 1);
                goods.setDescribeScore(4);
                goods.setPriceScore(5);
                goods.setQualityScore(4);
                goods.setTotalScore(5);
                goods.setInventory(1200);
                goods.setPostage(0);
                goods.setProductId(j + 1);
                goods.setSkuId(j + 1);
                goods.setStatu(0);

                StoreBean storeBean = new StoreBean();
                storeBean.setId(i);
                storeBean.setAdress("广东深圳");
                storeBean.setIconUrl("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
                storeBean.setGoodsCount(1000);
                storeBean.setLicence("http://pic1.win4000.com/wallpaper/2/576bae0dcf028.jpg");
                storeBean.setPhone("2415241524");
                storeBean.setStars(4);
                storeBean.setTotalScore(4.9);
                storeBean.setDescribeScore(4.4);
                storeBean.setPriceScore(4.9);
                storeBean.setQualityScore(5.0);
                storeBean.setTotalScals(122);
                storeBean.setKeeper("小成成");
                storeBean.setMainUrls(new ArrayList<String>());
                storeBean.setRecommender("哎购");
                storeBean.setCreateTime("2017-02-25");
                storeBean.setPhone("123121232");

                if (i < 2) {
                    storeBean.setName("粉小萌酸辣粉");
                    goods.setStoreName("粉小萌酸辣粉");
                } else {
                    storeBean.setName("粉小萌旗舰店");
                    goods.setStoreName("粉小萌旗舰店");
                }
                goods.setStoreBean(storeBean);
                goodsList.add(goods);
            }
            carBean.setGoods(goodsList);
            carBean.setStoreName(goodsList.get(0).getStoreName());
            carBean.setSelected(false);
            carBeanList.add(carBean);
        }
        listener.onGetShoppingCarDataFinished(carBeanList);
    }


    /**
     * 获取物流提醒信息
     */
    public static void getLogisticsMsg(OnGetLogisticsMsgFinishedListener listener) {
        List<LogisticsMsgBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LogisticsMsgBean msg = new LogisticsMsgBean();
            msg.setContent("粉小萌哈哈哈啊啊哈哈啊 啊哈啊啊哈啊啊啊   就爱看风景");
            msg.setExpressNum("订单编号：1654165415412");
            msg.setExpressType("顺丰快递");
            msg.setTime("2017年3月1日");
            msg.setUrl("http://pic1.win4000.com/wallpaper/e/57957d575664b.jpg");
            list.add(msg);
        }
        listener.OnGetLogisticsMsgFinished(list);
    }

    /**
     * 获取物流详情
     *
     * @param listener
     */
    public static void getLogisticsDetial(OnGetLogisticsDetialFinishedListener listener) {
        List<LogisticsBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LogisticsBean msg = new LogisticsBean();
            msg.setArriveTime("2017-4-6 16:16:16");
            msg.setCurrentAddress("广东省深圳市龙岗区布吉街道官坑北六巷八号一楼");
            list.add(msg);
        }
        listener.OnGetLogisticsDetialFinished(list);
    }

    /**
     * 获取商品子分类数据
     *
     * @param listener
     */
    public static void getSubClassifyProductData(OnGetSubClassifyProductsFinishedListener listener) {
        List<MainProductBean> list = new ArrayList<>();
        List<String> url = new ArrayList<>();
        url.add("http://n.sinaimg.cn/fashion/crawl/20170215/mbPG-fyamkqa6260128.jpg");
        url.add("http://n.sinaimg.cn/fashion/crawl/20170215/mbPG-fyamkqa6260128.jpg");
        url.add("http://n.sinaimg.cn/fashion/crawl/20170215/mbPG-fyamkqa6260128.jpg");
        url.add("http://n.sinaimg.cn/fashion/crawl/20170215/mbPG-fyamkqa6260128.jpg");
        url.add("http://n.sinaimg.cn/fashion/crawl/20170215/mbPG-fyamkqa6260128.jpg");
        for (int i = 0; i < 20; i++) {
            MainProductBean productBean = new MainProductBean();
            productBean.setUrls(url);
            productBean.setDetialUrls(url);
            productBean.setPrice(35);
            productBean.setIntroduce("粉小萌正酣上线，绝对独一无二，吃货的福利");
            productBean.setSales("10000");
            productBean.setAddress("深圳");
            productBean.setExpress("韵达快递");
            productBean.setVipPrice(25);
            productBean.setSkuId(i + 1);
            productBean.setCount(3);
            productBean.setId(i + 2);
            productBean.setColor("绿色-M");
            productBean.setInventory(100);
            productBean.setBrokerage(20);
            StoreBean storeBean = new StoreBean();
            storeBean.setName("粉小萌");
            storeBean.setAdress("广东省深圳市龙岗区");
            storeBean.setIconUrl("http://img2015.zdface.com/20170417/06bf77be0521dc47da46f596893b0dbf.jpg");
            storeBean.setCreateTime("2017-5-2");
            storeBean.setStars(3);
            storeBean.setTotalScals(300);
            storeBean.setGoodsCount(1000);
            storeBean.setId(i + 1);
            storeBean.setDescribeScore(4.8);
            storeBean.setPriceScore(4.0);
            storeBean.setQualityScore(4.2);
            storeBean.setTotalScore(4.5);
            storeBean.setLicence("http://img2015.zdface.com/20170417/06bf77be0521dc47da46f596893b0dbf.jpg");
            storeBean.setMainUrls(url);
            storeBean.setKeeper("小成成");
            storeBean.setPhone("15641651432");
            storeBean.setRecommender("小成成");
            productBean.setStoreBean(storeBean);
            productBean.setUrl(url.get(0));
            productBean.setDescribeScore(4);
            productBean.setPriceScore(4);
            productBean.setQualityScore(4);
            productBean.setTotalScore(4);
            list.add(productBean);
        }

        listener.onGetDemandDatasFinished(list);
    }

    /**
     * 获取用户所有收货地址
     */
    public static void getAllTakeAddress(final BaseActivity context, String token, OkHttpClient client, final OnGetAllAddressFinishedListener listener) {
        FormBody body = new FormBody.Builder()
                .add("user.token", token)
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_ALL_TAKE_ADDRESS)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.toastInUI(context.getApplicationContext(), "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.i("TAG", "收货地址==" + result);
                if (result.contains("1000")) {
                    List<TakeGoodsAddressBean> list = new ArrayList<>();
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            TakeGoodsAddressBean address = new TakeGoodsAddressBean();
                            JSONObject bean = dataset.getJSONObject(i);
                            int defaultOption = bean.getInt("defaultOption");
                            if (defaultOption == 0) {
                                address.setDefault(false);
                            } else {
                                address.setDefault(true);
                            }
                            address.setName(bean.getString("consignee"));
                            address.setPhone(bean.getString("phone"));
                            address.setAddress(bean.getString("province") + bean.getString("city") + bean.getString("county") + bean.getString("particular"));
                            address.setId(bean.getInt("id"));
                            list.add(address);
                        }

                        listener.onGetAllAddressFinished(list);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 获取商品分类的一级分类
     *
     * @param context
     * @param token
     * @param client
     * @param listener
     */
    public static void getStoreSuperClassify(final BaseActivity context, String token, OkHttpClient client, final OnGetStoreSuperClassifyFinishedListener listener) {
        Request request = new Request.Builder()
                .url(FXConst.GET_APPLY_STORE_CLASSIFY)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.toastInUI(context.getApplicationContext(), "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        List<SuperClassifyBean> list = new ArrayList<>();
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            SuperClassifyBean classify = new SuperClassifyBean();
                            classify.setId(jsonObject.getInt("id"));
                            classify.setType(jsonObject.getString("name"));
                            classify.setUrl(jsonObject.getString("url"));
                            //TODO TEST
                            classify.setUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494336714665&di=705c967663120df375c839353e1cdfe5&imgtype=0&src=http%3A%2F%2Fimg5.caijing.com.cn%2F2014%2F0902%2F1409631031356.jpg");
                            list.add(classify);
                        }
                        listener.onGetStoreSuperClassifyFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }


    /**
     * 获取一级分类下对应的二级分类
     *
     * @param context
     * @param superId
     * @param client
     * @param listener
     */
    public static void getSubClassify(final BaseActivity context, int superId, OkHttpClient client, final OnGetSubclassifyFinishedListener listener) {
        FormBody body = new FormBody.Builder()
                .add("category.id", superId + "")
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.GET_SUBCLASSIFY)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.toastInUI(context.getApplicationContext(), "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        List<ProductClassifyBean> list = new ArrayList<>();
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            ProductClassifyBean sub = new ProductClassifyBean();
                            JSONObject classify = dataset.getJSONObject(i);
                            sub.setTaxon(classify.getString("name"));
                            sub.setId(classify.getInt("id"));
                            sub.setUrl(classify.getString("url"));
                            //TODO TEST
                            sub.setUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494336714691&di=dd1185709830e5de82c5ff6167eac096&imgtype=0&src=http%3A%2F%2Fimg02.tooopen.com%2Fimages%2F20150303%2Ftooopen_sy_81400266133.jpg");
                            list.add(sub);
                        }
                        listener.onGetSubclassifyFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 获取商铺所有运费模板
     *
     * @param context
     * @param token
     * @param client
     * @param listener
     */
    public static void getAllPostage(final BaseActivity context, String token, OkHttpClient client, final OnGetAllPostageModelFinishedListener listener) {
        final List<PostageBean> list = new ArrayList<>();
        FormBody body = new FormBody.Builder()
                .add("store.user.token", token)
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_ALL_POSTAGE_MODEL)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.toastInUI(context.getApplicationContext(), "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            PostageBean postage = new PostageBean();
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            postage.setPostPay(jsonObject.getInt("cost") + "");
                            postage.setPostCount(jsonObject.getInt("nums") + "");
                            postage.setAddPay(jsonObject.getInt("increaseCost") + "");
                            postage.setAddCount(jsonObject.getInt("increaseNum") + "");
                            postage.setIsDefault(jsonObject.getInt("tacitly"));
                            JSONObject model = jsonObject.getJSONObject("freightTemplate");
                            postage.setModeName(model.getString("name"));
                            postage.setId(model.getInt("id"));
                            postage.setDistrictId(jsonObject.getInt("id"));
                            if (postage.getId() == 1) {
                                StringBuffer sb = new StringBuffer();
                                sb.append("默认模板：").append(postage.getPostCount() + "件内").append(postage.getPostPay() + "元，每增加").append(postage.getAddCount() + "件，增加").append(postage.getAddPay() + "元");
                                postage.setModeSetting(sb.toString());
                            }
                            if (postage.getIsDefault() == 0) {
                                List<String> provinces = new ArrayList<>();
                                JSONArray district = jsonObject.getJSONArray("provinces");
                                for (int j = 0; j < district.length(); j++) {
                                    provinces.add(district.getString(j));
                                }
                                postage.setProvinces(provinces);
                            }
                            list.add(postage);
                        }
                        listener.onGetAllPostageModelFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    context.toastInUI(context.getApplicationContext(), "网络繁忙");
                }
            }
        });

    }


    /**
     * 获取商铺商铺分类及对应数量
     *
     * @param context
     * @param token
     * @param client
     * @param listener
     */
    public static void getStoreProductClassify(final BaseActivity context, String token, int statu, OkHttpClient client, final OnGetStoreProductClassifyFinishedListener listener) {
        final List<StoreProductClassifyBean> list = new ArrayList<>();
        FormBody body = new FormBody.Builder()
                .add("store.user.token", token)
                .add("status", statu + "")
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_STORE_PROCUCT_ALL_CLASSIFY)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.toastInUI(context.getApplicationContext(), "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.i("TAG", "商品分类========" + result);
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            StoreProductClassifyBean classify = new StoreProductClassifyBean();
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            classify.setId(jsonObject.getInt("id"));
                            classify.setCount(jsonObject.getInt("cnum"));
                            classify.setName(jsonObject.getString("name"));
                            list.add(classify);
                        }
                        listener.onGetStoreProductClassifyFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 商品加入到购物车
     *
     * @param context
     * @param token
     * @param skuId
     * @param client
     */
    public static void add2shoppingcar(final BaseActivity context, String token, int skuId, OkHttpClient client) {
        if (TextUtils.isEmpty(token)) {//未登录
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }
        FormBody formBody = new FormBody.Builder()
                .add("user.token", token)
                .add("sku.id", skuId + "")
                .build();
        Request request = new Request.Builder()
                .url(FXConst.ADD_PRODUCT_TO_SHOPPINGCAR)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.toastInUI(context.getApplicationContext(), "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body().string().contains("1000")) {
                    context.toastInUI(context.getApplicationContext(), "添加成功");
                } else {
                    context.toastInUI(context.getApplicationContext(), "添加失败");
                }
            }
        });

    }


    /**
     * 获取用户资金流动明显
     *
     * @param context
     * @param token
     * @param index
     * @param size
     * @param client
     * @param listener
     */
    public static void getTransactionRecord(final BaseActivity context, String token, int index, int size, OkHttpClient client, final OnGetTransactionRecorderFinishedListener listener) {
        final List<TransactionRecordBean> list = new ArrayList<>();
        FormBody body = new FormBody.Builder()
                .add("token", token)
                .add("index", index + "")
                .add("size", size + "")
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_TRANSACTION_RECORDS)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.toastInUI(context.getApplicationContext(), "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            TransactionRecordBean record = new TransactionRecordBean();
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            record.setTime(jsonObject.getString("time"));
                            record.setMoney(jsonObject.getDouble("money") + "");
                            record.setType(jsonObject.getInt("type"));
                            list.add(record);
                        }
                        listener.onGetTransactionRecorderFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 获取购物车中商品
     *
     * @param context
     * @param token
     * @param index
     * @param size
     * @param client
     * @param listener
     */
    public static void getShoppingcarProducts(final BaseActivity context, String token, final int index, final int size, OkHttpClient client, final OnGetShoppingcarProductsFinishedListener listener) {
        final List<ShoppingGoodsBean> list = new ArrayList<>();
        FormBody body = new FormBody.Builder()
                .add("token", token)
                .add("index", index + "")
                .add("size", size + "")
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_SHOPPINGCAR_PRODUCTS)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.toastInUI(context.getApplicationContext(), "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            ShoppingGoodsBean goods = new ShoppingGoodsBean();
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            goods.setCount(jsonObject.getInt("num"));
                            goods.setPrice(jsonObject.getDouble("price"));
                            goods.setVipPrice(goods.getPrice() * (1 - MyApplication.vipRate));
                            goods.setCarId(jsonObject.getInt("id"));
                            goods.setColor(jsonObject.getString("content"));
                            JSONObject sku = jsonObject.getJSONObject("sku");
                            goods.setBrokerage(sku.getInt("brokerage"));
                            goods.setSkuId(sku.getInt("id"));
                            goods.setInventory(sku.getInt("inventory"));

                            JSONObject commodity = sku.getJSONObject("commodity");
                            goods.setIntroduce(commodity.getString("detail"));
                            goods.setProductId(commodity.getInt("id"));
                            goods.setSales(commodity.getInt("sales"));
                            goods.setStatu(commodity.getInt("status"));
                            goods.setPostage(0);//TODO 待确认
                            goods.setDescribeScore(commodity.getInt("describeScore"));
                            goods.setPriceScore(commodity.getInt("transportScore"));
                            goods.setQualityScore(commodity.getInt("qualityScore"));
                            goods.setTotalScore(commodity.getInt("totalScore"));

                            String pictrue = commodity.getString("pictrue");
                            JSONArray pictrues = new JSONArray(pictrue);
                            List<String> mainUrls = new ArrayList<>();
                            for (int j = 0; j < pictrues.length(); j++) {
                                mainUrls.add(pictrues.getString(i));
                            }
                            goods.setMainUrls(mainUrls);
                            goods.setUrl(goods.getMainUrls().get(0));
                            String urls = commodity.getString("detailUrl");
                            JSONArray detailUrls = new JSONArray(urls);
                            List<String> detailImages = new ArrayList<>();
                            for (int k = 0; k < detailUrls.length(); k++) {
                                detailImages.add(detailUrls.getString(k));
                            }
                            goods.setDetialUrls(detailImages);
                            goods.setSelected(false);

                            JSONObject store = commodity.getJSONObject("store");
                            StoreBean storeBean = new StoreBean();
                            storeBean.setName(store.getString("storeName"));
                            storeBean.setCreateTime(store.getString("createTime"));
                            goods.setStoreName(storeBean.getName());
                            storeBean.setId(store.getInt("id"));
                            storeBean.setIconUrl(store.getString("logo"));//TODO 缺少
                            storeBean.setStars(3);
                            storeBean.setQualityScore(store.getDouble("qualityScore"));
                            storeBean.setDescribeScore(store.getDouble("describeScore"));
                            storeBean.setPriceScore(store.getDouble("transportScore"));
                            storeBean.setGoodsCount(store.getInt("cnum"));
                            storeBean.setTotalScals(store.getInt("sales"));
                            String address = store.getString("address");
                            storeBean.setAdress(address);//TODO 缺少
                            storeBean.setMainUrls(new ArrayList<String>());
                            storeBean.setTotalScore(store.getDouble("totalScore"));
                            storeBean.setRecommender("哎购商城");
                            storeBean.setPhone(store.getString("phone"));
                            storeBean.setKeeper(store.getString("storeKeeper"));
                            storeBean.setLicence(store.getString(""));
                            int indexOf = address.indexOf("市");
                            goods.setAddress(address.substring(0, indexOf + 1));
                            goods.setStoreBean(storeBean);
                            list.add(goods);
                        }
                        listener.onGetShoppingcarProductsFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    /**
     * 获取我的需求数据
     *
     * @param context
     * @param token
     * @param index
     * @param size
     * @param client
     * @param listener
     */
    public static void getMyDemandData(final BaseActivity context, String token, final int index, int size, OkHttpClient client, final OnGetMyDemandDataFinishedListener listener) {
        final List<MainDemandBean> list = new ArrayList<>();
        FormBody body = new FormBody.Builder()
                .add("user.token", token)
                .add("index", index + "")
                .add("size", size + "")
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.GET_MY_DEMAND_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.toastInUI(context.getApplicationContext(), "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            MainDemandBean demandBean = new MainDemandBean();
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            demandBean.setAddress(jsonObject.getString("address"));
                            demandBean.setIntroduce(jsonObject.getString("detail"));
                            demandBean.setTitel(jsonObject.getString("title"));
                            demandBean.setPhone(jsonObject.getString("phone"));
                            demandBean.setId(jsonObject.getInt("id"));
                            demandBean.setDemand(jsonObject.getInt("num"));
                            String photo = object.getString("url");
                            JSONArray photos = new JSONArray(photo);
                            List<String> urls = new ArrayList<>();
                            for (int j = 0; j < photos.length(); j++) {
                                urls.add(photos.getString(j));
                            }
                            demandBean.setUrls(urls);
                            demandBean.setUrl(urls.get(0));
                            list.add(demandBean);
                        }
                        listener.onGetMyDemandDataFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /***
     * 根据分类获取对应类型的需求
     *
     * @param context
     * @param classifyId
     * @param index
     * @param size
     * @param client
     * @param listener
     */
    public static void getDemandByClassify(final BaseActivity context, int classifyId, final int index, int size, OkHttpClient client, final OnGetMyDemandDataFinishedListener listener) {
        final List<MainDemandBean> list = new ArrayList<>();
        FormBody body = new FormBody.Builder()
                .add("subCategory.id", classifyId + "")
                .add("index", index + "")
                .add("size", size + "")
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.GET_DEMAND_BY_CLASSIFY)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.toastInUI(context.getApplicationContext(), "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            MainDemandBean demandBean = new MainDemandBean();
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            demandBean.setAddress(jsonObject.getString("address"));
                            demandBean.setIntroduce(jsonObject.getString("detail"));
                            demandBean.setTitel(jsonObject.getString("title"));
                            demandBean.setPhone(jsonObject.getString("phone"));
                            demandBean.setId(jsonObject.getInt("id"));
                            demandBean.setDemand(jsonObject.getInt("num"));
                            String photo = object.getString("url");
                            JSONArray photos = new JSONArray(photo);
                            List<String> urls = new ArrayList<>();
                            for (int j = 0; j < photos.length(); j++) {
                                urls.add(photos.getString(j));
                            }
                            demandBean.setUrls(urls);
                            demandBean.setUrl(urls.get(0));
                            list.add(demandBean);
                        }
                        listener.onGetMyDemandDataFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 获取我推荐的店铺
     *
     * @param context
     * @param token
     * @param index
     * @param size
     * @param client
     * @param listener
     */
    public static void getMyRecommendStore(final BaseActivity context, String token, String url, final int index, int size, OkHttpClient client, final OnGetMyRecommendStoreFinishedListener listener) {
        final List<StoreBean> list = new ArrayList<>();
        //TEST
        for (int i = 0; i < 10; i++) {
            StoreBean store = new StoreBean();
            store.setAdress("广东深圳");
            store.setDescribeScore(5.00);
            store.setGoodsCount(134);
            store.setIconUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490872429670&di=b95cc27dc9781e9510de7753a6709f39&imgtype=0&src=http%3A%2F%2Fp.nanrenwo.net%2Fuploads%2Fallimg%2F170223%2F8450-1F223100J2.jpg");
            store.setName("粉小萌");
            store.setPriceScore(4.9);
            store.setQualityScore(5.0);
            store.setStars(5);
            store.setTotalScals(100000);
            store.setTotalScore(4.9);
            store.setCreateTime("2017-5-15");
            store.setId(i + 2);
            store.setRecommender("哎购商城");
            store.setKeeper("小成成");
            store.setLicence("");
            store.setMainUrls(new ArrayList<String>());
            store.setPhone("01665135123");
            list.add(store);
            listener.onGetMyRecommendStoreFinished(list);
        }


        FormBody.Builder builder = new FormBody.Builder()
                .add("index", index + "")
                .add("size", size + "");
        if (url.equals(FXConst.GET_MY_RECOMMEND_STORES)) {
            builder.add("uuser.token", token);
        } else {
            builder.add("desired.id", token);
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.toastInUI(context.getApplicationContext(), "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            StoreBean storeBean = new StoreBean();
                            storeBean.setGoodsCount(jsonObject.getInt("cnum"));
                            storeBean.setId(jsonObject.getInt("id"));
                            storeBean.setIconUrl(jsonObject.getString("logo"));
                            storeBean.setAdress(jsonObject.getString("address"));
                            storeBean.setDescribeScore(jsonObject.getDouble("describeScore"));
                            storeBean.setPriceScore(jsonObject.getDouble("transportScore"));
                            storeBean.setQualityScore(jsonObject.getDouble("qualityScore"));
                            storeBean.setName(jsonObject.getString("storeName"));
                            storeBean.setStars(jsonObject.getInt("totalScore"));
                            storeBean.setTotalScals(jsonObject.getInt("sales"));
                            storeBean.setTotalScore(jsonObject.getDouble("totalScore"));
                            storeBean.setPhone(jsonObject.getString("phone"));
                            storeBean.setMainUrls(new ArrayList<String>());
                            storeBean.setLicence(jsonObject.getString("license"));
                            storeBean.setKeeper(jsonObject.getString("storekeeper"));
                            storeBean.setRecommender(jsonObject.getString("recommender"));//TODO 待定
                            storeBean.setCreateTime(jsonObject.getString("createTime"));
                            list.add(storeBean);
                        }
                        listener.onGetMyRecommendStoreFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 获取商品搜索数据
     *
     * @param context
     * @param key      热词
     * @param index
     * @param size
     * @param client
     * @param listener
     */
    public static void getSearchProducts(final Activity context, String key, String orderBy, final int index, int size, int storeId, OkHttpClient client, final OnSearchProductsFinishedListener listener) {
        //TEST
        List<MainProductBean> list1 = new ArrayList<>();
        List<String> url = new ArrayList<>();
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        url.add("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg");
        for (int i = 0; i < 5; i++) {
            MainProductBean productBean = new MainProductBean();
            productBean.setUrls(url);
            productBean.setDetialUrls(url);
            productBean.setPrice(35);
            productBean.setIntroduce("粉小萌正酣上线，绝对独一无二，吃货的福利");
            productBean.setSales("10000");
            productBean.setAddress("深圳");
            productBean.setExpress("包邮");
            productBean.setVipPrice(25);
            productBean.setSkuId(i + 1);
            productBean.setCount(3);
            productBean.setId(i + 2);
            productBean.setColor("绿色-M");
            productBean.setInventory(100);
            productBean.setBrokerage(20);
            StoreBean storeBean = new StoreBean();
            storeBean.setName("粉小萌");
            storeBean.setAdress("广东省深圳市龙岗区");
            storeBean.setIconUrl("http://img2015.zdface.com/20170417/06bf77be0521dc47da46f596893b0dbf.jpg");
            storeBean.setCreateTime("2017-5-2");
            storeBean.setStars(3);
            storeBean.setTotalScals(300);
            storeBean.setGoodsCount(1000);
            storeBean.setId(i + 1);
            storeBean.setDescribeScore(4.8);
            storeBean.setPriceScore(4.0);
            storeBean.setQualityScore(4.2);
            storeBean.setTotalScore(4.5);
            storeBean.setLicence("http://img2015.zdface.com/20170417/06bf77be0521dc47da46f596893b0dbf.jpg");
            storeBean.setMainUrls(url);
            storeBean.setKeeper("小成成");
            storeBean.setPhone("15641651432");
            storeBean.setRecommender("小成成");
            productBean.setStoreBean(storeBean);
            productBean.setUrl(url.get(0));
            productBean.setDescribeScore(4);
            productBean.setPriceScore(4);
            productBean.setQualityScore(4);
            productBean.setTotalScore(4);
            list1.add(productBean);
        }
        listener.onSearchProductsFinished(list1);


        final List<MainProductBean> list = new ArrayList<>();
        FormBody.Builder builder = new FormBody.Builder()
                .add("orderby", orderBy)
                .add("index", index + "")
                .add("size", size + "");
        if (key != null) {
            builder.add("name", key);
        }

        if (storeId != 0) {
            builder.add("store.id", storeId + "");
        }
        FormBody body = builder.build();
        Request.Builder post = new Request.Builder()
                .post(body);
        if (storeId == 0) {
            post.url(FXConst.GET_SEARCH_PRODUCTS);
        } else {
            post.url(FXConst.SEARCH_ONE_STORE_PRODUCT);
        }
        Request request = post.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show(context, context, "网络异常");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            MainProductBean product = new MainProductBean();
                            product.setBrokerage(jsonObject.getInt("brokerage"));
                            product.setPrice(jsonObject.getDouble("price"));
                            product.setSkuId(jsonObject.getInt("id"));
                            product.setInventory(jsonObject.getInt("inventory"));
                            //TODO 根据佣金和价格算出会员价：
                            product.setVipPrice(product.getPrice() - product.getBrokerage() * MyApplication.vipRate);
                            product.setColor(jsonObject.getString("content"));

                            JSONObject commodity = jsonObject.getJSONObject("commodity");
                            product.setId(commodity.getInt("id"));
                            product.setCount(1);
                            product.setExpress("包邮");//TODO 待定
                            product.setDescribeScore(commodity.getInt("describeScore"));
                            product.setPriceScore(commodity.getInt("transportScore"));
                            product.setQualityScore(commodity.getInt("qualityScore"));
                            product.setTotalScore(commodity.getInt("totalScore"));
                            product.setIntroduce(commodity.getString("detail"));
                            product.setSales(commodity.getInt("sales") + "");
                            String detailUrls = commodity.getString("detailUrl");
                            JSONArray detailUrl = new JSONArray(detailUrls);
                            List<String> mainImages = new ArrayList<>();
                            for (int j = 0; j < detailUrl.length(); j++) {
                                mainImages.add(detailUrl.getString(j));
                            }
                            product.setDetialUrls(mainImages);
                            List<String> detialImages = new ArrayList<>();
                            String pictrue = commodity.getString("pictrue");
                            JSONArray pictrues = new JSONArray(pictrue);
                            for (int k = 0; k < pictrues.length(); k++) {
                                detialImages.add(pictrues.getString(k));
                            }
                            product.setUrls(detialImages);
                            product.setUrl(product.getUrls().get(0));

                            //TODO 商品对应店铺详情
                            JSONObject store = commodity.getJSONObject("store");
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
                            storeBean.setTotalScore(store.getDouble("totalScore"));
                            storeBean.setIconUrl(store.getString("logo"));
                            storeBean.setLicence(store.getString("license"));
                            storeBean.setPhone(store.getString("phone"));
                            storeBean.setMainUrls(new ArrayList<String>());
                            storeBean.setId(store.getInt("id"));
                            storeBean.setKeeper(store.getString("storekeeper"));
                            storeBean.setRecommender("哎购商城");

                            product.setStoreBean(storeBean);

                            product.setAddress(address.substring(0, address.indexOf("市")));

                            list.add(product);
                        }

                        listener.onSearchProductsFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 获取商店搜索数据
     *
     * @param context
     * @param key
     * @param index
     * @param size
     * @param client
     * @param listener
     */
    public static void getSearchStores(final BaseActivity context, String key, final int index, int size, OkHttpClient client, final OnGetMyRecommendStoreFinishedListener listener) {
        final List<StoreBean> list = new ArrayList<>();
        //TEST
        //TEST
        for (int i = 0; i < 10; i++) {
            StoreBean store = new StoreBean();
            store.setAdress("广东深圳");
            store.setDescribeScore(5.00);
            store.setGoodsCount(134);
            store.setIconUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490872429670&di=b95cc27dc9781e9510de7753a6709f39&imgtype=0&src=http%3A%2F%2Fp.nanrenwo.net%2Fuploads%2Fallimg%2F170223%2F8450-1F223100J2.jpg");
            store.setName("粉小萌");
            store.setPriceScore(4.9);
            store.setQualityScore(5.0);
            store.setStars(5);
            store.setTotalScals(100000);
            store.setTotalScore(4.9);
            store.setCreateTime("2017-5-15");
            store.setId(i + 2);
            store.setRecommender("哎购商城");
            store.setKeeper("小成成");
            store.setLicence("");
            store.setMainUrls(new ArrayList<String>());
            store.setPhone("01665135123");
            list.add(store);
            listener.onGetMyRecommendStoreFinished(list);
        }


        FormBody body = new FormBody.Builder()
                .add("storeName", key)
                .add("index", index + "")
                .add("size", size + "")
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.GET_SEARCH_STORES)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.toastInUI(context.getApplicationContext(), "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            StoreBean storeBean = new StoreBean();
                            storeBean.setName(jsonObject.getString("storeName"));
                            String address = jsonObject.getString("address");
                            storeBean.setAdress(address);
                            storeBean.setGoodsCount(jsonObject.getInt("cnum"));
                            storeBean.setTotalScals(jsonObject.getInt("sales"));
                            storeBean.setCreateTime(jsonObject.getString("createTime"));
                            storeBean.setQualityScore(jsonObject.getDouble("qualityScore"));
                            storeBean.setDescribeScore(jsonObject.getDouble("describeScore"));
                            storeBean.setPriceScore(jsonObject.getDouble("transportScore"));
                            storeBean.setStars(jsonObject.getInt("totalScore"));
                            storeBean.setTotalScore(jsonObject.getDouble("totalScore"));
                            storeBean.setIconUrl(jsonObject.getString("logo"));
                            storeBean.setLicence(jsonObject.getString("license"));
                            storeBean.setPhone(jsonObject.getString("phone"));
                            storeBean.setMainUrls(new ArrayList<String>());
                            storeBean.setId(jsonObject.getInt("id"));
                            storeBean.setKeeper(jsonObject.getString("storekeeper"));
                            storeBean.setRecommender("哎购商城");
                            list.add(storeBean);
                        }
                        listener.onGetMyRecommendStoreFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /***
     * @param context
     * @param token
     * @param statu
     * @param isAll
     * @param client
     * @param listener
     */
    public static void getMyOrderInfo(final BaseActivity context, String token, final int statu, int isAll, OkHttpClient client, final OnGetMyOrderInfoFinishedListener listener) {
        final List<OrderBean> list = new ArrayList<>();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("user.token", token)
                .add("status", statu + "");
        if (isAll == 0) {
            builder.add("isAll", isAll + "");
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.GET_ORDER_CALSSIFY)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.toastInUI(context.getApplicationContext(), "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            OrderBean orderBean = new OrderBean();
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            orderBean.setCount(jsonObject.getInt("num"));
                            orderBean.setBuyer(jsonObject.getString("data"));

                            JSONObject order = jsonObject.getJSONObject("orders");
                            orderBean.setId(order.getInt("id"));
                            orderBean.setOrderNum(order.getString("orderNo"));
                            orderBean.setSumPrice(order.getDouble("paySum"));
                            orderBean.setCreateTime(order.getLong("createTime"));
                            orderBean.setPayTime(order.getLong("payTime"));
                            int status = order.getInt("status");
                            orderBean.setStateNum(status);
                            switch (status) {//TODO 对退款退货订单还需更细分类，待补充
                                //买家
                                case 0:
                                    orderBean.setState("等待买家付款");
                                    break;
                                case 1:
                                    orderBean.setState("等待卖家发货");
                                    break;
                                case 2:
                                    orderBean.setState("等待买家收货");
                                    break;
                                case 3:
                                    orderBean.setState("等待买家评价");
                                    break;
                                case 4:
                                    orderBean.setState("退货/退款");
                                    break;
                            }

                            int refundState = jsonObject.getInt("status");
                            switch (refundState) {//退款状态
                                case 1:
                                    orderBean.setState("商家同意退款");
                                    break;
                                case 3:
                                    orderBean.setState("退款成功");
                                    break;
                                case 5:
                                    orderBean.setState("等待商家处理退款申请");
                                    break;
                                case 6:
                                    orderBean.setState("商家拒绝退款");
                                    break;
                            }

                            JSONObject expressCode = order.getJSONObject("expressCode");
                            orderBean.setExpress(expressCode.getString("name"));
                            orderBean.setExpressCode(expressCode.getString("code"));
                            orderBean.setExpressCodeId(expressCode.getInt("id"));

                            JSONObject takeInfo = order.getJSONObject("shoppingAddress");
                            orderBean.setTakeMan(takeInfo.getString("consignee"));
                            orderBean.setTakePhone(takeInfo.getString("phone"));
                            StringBuffer sb = new StringBuffer();
                            sb.append(takeInfo.getString("province")).append(takeInfo.getString("city")).append(takeInfo.getString("county")).append(takeInfo.getString("particular"));
                            orderBean.setTakeAddress(sb.toString());

                            JSONObject store = order.getJSONObject("store");
                            orderBean.setStoreName(store.getString("storeName"));
                            orderBean.setStoreUser(store.getString("storekeeper"));
                            orderBean.setStorePhone(store.getString("phone"));
                            orderBean.setStoreAddress(store.getString("address"));
                            orderBean.setStoreId(store.getInt("id"));

                            JSONObject productInfo = jsonObject.getJSONObject("sku");//TODO 商品信息有待完善，字段待确定
                            orderBean.setBrokerage(productInfo.getInt("brokerage"));
                            orderBean.setSinglePrice(productInfo.getInt("price"));
                            orderBean.setColor(productInfo.getString("content"));
                            orderBean.setSkuId(productInfo.getInt("id"));
                            JSONObject commodity = productInfo.getJSONObject("commodity");
                            orderBean.setIntroduce(commodity.getString("detail"));
                            orderBean.setProductId(commodity.getInt("id"));
                            String picture = commodity.getString("pictrue");
                            JSONArray pictures = new JSONArray(picture);
                            orderBean.setUrl(pictures.getString(0));
                            //TODO 根据当前定位城市算出邮费
                            orderBean.setPostage(productInfo.getInt("postage"));
                            list.add(orderBean);
                        }
                        listener.onGetMyOrderInfoFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /***
     * @param context
     * @param token
     * @param statu
     * @param isAll
     * @param client
     * @param listener
     */
    public static void getStoreOrderInfo(final BaseActivity context, String token, final int statu, int isAll, int index, int size, OkHttpClient client, final OnGetMyOrderInfoFinishedListener listener) {
        final List<OrderBean> list = new ArrayList<>();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("user.token", token)
                .add("status", statu + "")
                .add("index", index + "")
                .add("size", size + "");
        if (isAll == 0) {
            builder.add("isAll", isAll + "");
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.GET_STORE_ORDER_CLASSIFY)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.toastInUI(context.getApplicationContext(), "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            OrderBean orderBean = new OrderBean();
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            orderBean.setCount(jsonObject.getInt("num"));
                            orderBean.setBuyer(jsonObject.getString("data"));

                            JSONObject order = jsonObject.getJSONObject("orders");
                            if (order.has("waybill")) {
                                orderBean.setHasDeliver(true);
                            } else {
                                orderBean.setHasDeliver(false);
                            }
                            orderBean.setId(order.getInt("id"));
                            orderBean.setOrderNum(order.getString("orderNo"));
                            orderBean.setSumPrice(order.getDouble("paySum"));
                            orderBean.setCreateTime(order.getLong("createTime"));
                            orderBean.setPayTime(order.getLong("payTime"));
                            int status = order.getInt("status");
                            orderBean.setStateNum(status);
                            switch (status) {//TODO 对退款退货订单还需更细分类，待补充
                                //卖家
                                case 0:
                                    orderBean.setState("等待买家付款");
                                    break;
                                case 1:
                                    orderBean.setState("等待发货");
                                    break;
                                case 2:
                                    orderBean.setState("等待买家收货");
                                    break;
                                case 3:
                                    orderBean.setState("等待买家评价");
                                    break;

                            }

                            int refundState = jsonObject.getInt("status");
                            switch (refundState) {//退款状态
                                case 1:
                                    orderBean.setState("等待买家发货");
                                    break;
                                case 2:
                                    orderBean.setState("等待收货");
                                    break;
                                case 3:
                                    orderBean.setState("退款已完成");
                                    break;
                                case 5:
                                    orderBean.setState("买家申请退货/退款");
                                    break;
                                case 6:
                                    orderBean.setState("已拒绝申请");
                                    break;
                            }

                            JSONObject expressCode = order.getJSONObject("expressCode");
                            orderBean.setExpress(expressCode.getString("name"));
                            orderBean.setExpressCode(expressCode.getString("code"));
                            orderBean.setExpressCodeId(expressCode.getInt("id"));

                            JSONObject takeInfo = order.getJSONObject("shoppingAddress");
                            orderBean.setTakeMan(takeInfo.getString("consignee"));
                            orderBean.setTakePhone(takeInfo.getString("phone"));
                            StringBuffer sb = new StringBuffer();
                            sb.append(takeInfo.getString("province")).append(takeInfo.getString("city")).append(takeInfo.getString("county")).append(takeInfo.getString("particular"));
                            orderBean.setTakeAddress(sb.toString());

                            JSONObject store = order.getJSONObject("store");
                            orderBean.setStoreName(store.getString("storeName"));
                            orderBean.setStoreId(store.getInt("id"));

                            JSONObject productInfo = jsonObject.getJSONObject("sku");//
                            orderBean.setBrokerage(productInfo.getInt("brokerage"));
                            orderBean.setSinglePrice(productInfo.getInt("price"));
                            orderBean.setColor(productInfo.getString("content"));
                            orderBean.setSkuId(productInfo.getInt("id"));
                            //TODO 获取邮费
                            orderBean.setPostage(productInfo.getInt("postage"));
                            JSONObject commodity = productInfo.getJSONObject("commodity");
                            orderBean.setUrl(commodity.getString("detail"));
                            orderBean.setProductId(commodity.getInt("id"));
                            String picture = commodity.getString("pictrue");
                            JSONArray pictures = new JSONArray(picture);
                            orderBean.setUrl(pictures.getString(0));
                            list.add(orderBean);
                        }
                        listener.onGetMyOrderInfoFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 获取所有快递公司信息
     *
     * @param context
     * @param client
     * @param listener
     */
    public static void getAllExpressCompany(final BaseActivity context, OkHttpClient client, final OnGetExpressCompanyFinishedListener listener) {
        final List<ExpressCompanyBean> list = new ArrayList<>();
        Request request = new Request.Builder()
                .url(FXConst.GET_EXPRESS_COMPANY_LIST)
                .build();
        client.newCall(request).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String result = response.body().string();
                                                if (result.contains("1000")) {
                                                    try {
                                                        JSONObject object = new JSONObject(result);
                                                        JSONArray dataset = object.getJSONArray("dataset");
                                                        for (int i = 0; i < dataset.length(); i++) {
                                                            ExpressCompanyBean express = new ExpressCompanyBean();
                                                            JSONObject jsonObject = dataset.getJSONObject(i);
                                                            express.setId(jsonObject.getInt("id"));
                                                            express.setCode(jsonObject.getString("code"));
                                                            express.setName(jsonObject.getString("name"));
                                                            list.add(express);
                                                        }
                                                        listener.onGetExpressCompanyFinished(list);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }
                                        }

        );
    }

    /**
     * 获取指定商品评论列表
     *
     * @param context
     * @param id       商品id
     * @param index
     * @param size
     * @param client
     * @param listener
     */
    public static void getProductComments(final BaseActivity context, int id, final int index, int size, OkHttpClient client, final OnGetProductCommentListFinishListener listener) {
        final List<CommentBean> list = new ArrayList<>();
        FormBody body = new FormBody.Builder()
                .add("commodity.id", id + "")
                .add("index", index + "")
                .add("size", size + "")
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.GET_COMMENTS_FOR_ONE_PRODUCT)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            CommentBean comment = new CommentBean();
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            comment.setContent(jsonObject.getString("content"));
                            int describeScore = jsonObject.getInt("describeScore");
                            int qualityScore = jsonObject.getInt("qualityScore");
                            int transportScore = jsonObject.getInt("transportScore");
                            float stars = (describeScore + qualityScore + transportScore) / 3f;
                            comment.setStars(stars);
                            JSONObject user = jsonObject.getJSONObject("user");
                            comment.setName(user.getString("nickname"));
                            comment.setIcon(user.getString("headPortrait"));
                            list.add(comment);
                        }
                        listener.onGetProductCommentListFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 获取banner图
     *
     * @param client
     * @param listener
     */
    public static void getBanners(OkHttpClient client, final OnGetBannerFinishedListener listener) {
        final List<BannerBean> banners = new ArrayList<>();
        Request request = new Request.Builder()
                .url(FXConst.GET_BANNER_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if (string.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(string);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            BannerBean banner = new BannerBean();
                            banner.setType(jsonObject.getInt("status"));
                            banner.setUrl(jsonObject.getString("url"));
                            banner.setProductId(jsonObject.getInt("link"));
                            banners.add(banner);
                        }
                        listener.onGetBannerFinishedListener(banners);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 获取某个商品的不同颜色尺寸
     *
     * @param client
     * @param id
     * @param listener
     */
    public static void getProductCS(OkHttpClient client, int id, final OnGetProductCSFinishedListener listener) {
        final List<ColorSizeBean> list = new ArrayList<>();
        //TEST
        for (int i = 0; i < 5; i++) {
            ColorSizeBean colorSizeBean = new ColorSizeBean();
            colorSizeBean.setId(i + 1);
            colorSizeBean.setPrice(88);
            colorSizeBean.setBrokrage(30);
            colorSizeBean.setInventory(100);
            colorSizeBean.setCheck(false);
            switch (i) {
                case 0:
                    colorSizeBean.setColor("绿色-M");
                    break;
                case 1:
                    colorSizeBean.setColor("绿色-L");
                    break;
                case 2:
                    colorSizeBean.setColor("灰色-M");
                    break;
                case 3:
                    colorSizeBean.setColor("蓝色-M");
                    break;
                case 4:
                    colorSizeBean.setColor("红色-M");
                    break;

            }
            list.add(colorSizeBean);
        }
        listener.onGetProductCSFinishedListener(list);


        FormBody body = new FormBody.Builder()
                .add("commodity.id", id + "".trim())
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.GET_PRODUCT_CS_CLASSIFY)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if (string.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(string);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            ColorSizeBean cs = new ColorSizeBean();
                            cs.setId(jsonObject.getInt("id"));
                            cs.setBrokrage(jsonObject.getInt("brokerage"));
                            cs.setInventory(jsonObject.getInt("inventory"));
                            cs.setPrice(jsonObject.getInt("price"));
                            cs.setColor(jsonObject.getString("content"));
                            list.add(cs);
                        }
                        listener.onGetProductCSFinishedListener(list);
                    } catch (JSONException e) {


                    }
                }
            }
        });
    }


    /**
     * 获取各二级分类下商品列表
     *
     * @param subId    二级分类id
     * @param orderBy
     * @param index
     * @param size
     * @param client
     * @param listener
     */
    public static void getProductsForSubclassify(int subId, String orderBy, final int index, int size, OkHttpClient client, final OnSearchProductsFinishedListener listener) {
        final List<MainProductBean> list = new ArrayList<>();
        FormBody body = new FormBody.Builder()
                .add("index", index + "".trim())
                .add("size", size + "".trim())
                .add("orderby", orderBy)
                .add("subCastegory.id", subId + "".trim())
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.GET_PRODUCTS_OF_SUBCALSSIFY)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            MainProductBean product = new MainProductBean();
                            product.setBrokerage(jsonObject.getInt("brokerage"));
                            product.setPrice(jsonObject.getDouble("price"));
                            product.setSkuId(jsonObject.getInt("id"));
                            //TODO 根据佣金和价格算出会员价：
                            product.setVipPrice(product.getPrice() - product.getBrokerage() * MyApplication.vipRate);
                            product.setColor(jsonObject.getString("content"));
                            product.setCount(1);
                            product.setExpress("包邮");//如何确定邮费

                            JSONObject commodity = jsonObject.getJSONObject("commodity");
                            product.setId(commodity.getInt("id"));
                            product.setDescribeScore(commodity.getInt("describeScore"));
                            product.setPriceScore(commodity.getInt("transportScore"));
                            product.setQualityScore(commodity.getInt("qualityScore"));
                            product.setTotalScore(commodity.getInt("totalScore"));
                            product.setIntroduce(commodity.getString("detail"));
                            product.setSales(commodity.getInt("sales") + "");
                            product.setInventory(commodity.getInt("inventory"));
                            String detailUrls = commodity.getString("detailUrl");
                            JSONArray detailUrl = new JSONArray(detailUrls);
                            List<String> mainImages = new ArrayList<>();
                            for (int j = 0; j < detailUrl.length(); j++) {
                                mainImages.add(detailUrl.getString(j));
                            }
                            product.setDetialUrls(mainImages);
                            List<String> detialImages = new ArrayList<>();
                            String pictrue = commodity.getString("pictrue");
                            JSONArray pictrues = new JSONArray(pictrue);
                            for (int k = 0; k < pictrues.length(); k++) {
                                detialImages.add(pictrues.getString(k));
                            }
                            product.setUrls(detialImages);
                            product.setUrl(product.getUrls().get(0));
                            //TODO 商品对应店铺详情
                            JSONObject store = commodity.getJSONObject("store");
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
                            storeBean.setTotalScore(store.getDouble("totalScore"));
                            storeBean.setStars(store.getInt("totalScore"));
                            storeBean.setIconUrl(store.getString("logo"));
                            storeBean.setId(store.getInt("id"));
                            storeBean.setRecommender("哎购商城");
                            storeBean.setPhone(store.getString("phone"));
                            storeBean.setKeeper(store.getString("storekeeper"));
                            storeBean.setLicence(store.getString("license"));
                            storeBean.setMainUrls(new ArrayList<String>());
                            product.setStoreBean(storeBean);

                            product.setAddress(address.substring(0, address.indexOf("市")));

                            list.add(product);
                        }

                        listener.onSearchProductsFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    /**
     * 获取首页商品展示列表
     *
     * @param token
     * @param index
     * @param size
     * @param client
     * @param listener
     */
    public static void getHomePageProductsDisplay(String token, final int index, int size, OkHttpClient client, final OnSearchProductsFinishedListener listener) {
        final List<MainProductBean> list = new ArrayList<>();
        FormBody body = new FormBody.Builder()
                .add("index", index + "".trim())
                .add("size", size + "".trim())
                .add("token", token)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.GET_HOME_MainProducts_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            MainProductBean product = new MainProductBean();
                            product.setBrokerage(jsonObject.getInt("brokerage"));
                            product.setPrice(jsonObject.getDouble("price"));
                            product.setSkuId(jsonObject.getInt("id"));
                            //TODO 根据佣金和价格算出会员价：
                            product.setVipPrice(product.getPrice() - product.getBrokerage() * MyApplication.vipRate);
                            product.setColor(jsonObject.getString("content"));
                            product.setCount(1);
                            product.setExpress("包邮");//如何确定邮费

                            JSONObject commodity = jsonObject.getJSONObject("commodity");
                            product.setId(commodity.getInt("id"));
                            product.setDescribeScore(commodity.getInt("describeScore"));
                            product.setPriceScore(commodity.getInt("transportScore"));
                            product.setQualityScore(commodity.getInt("qualityScore"));
                            product.setTotalScore(commodity.getInt("totalScore"));
                            product.setIntroduce(commodity.getString("detail"));
                            product.setSales(commodity.getInt("sales") + "");
                            product.setInventory(commodity.getInt("inventory"));
                            String detailUrls = commodity.getString("detailUrl");
                            JSONArray detailUrl = new JSONArray(detailUrls);
                            List<String> mainImages = new ArrayList<>();
                            for (int j = 0; j < detailUrl.length(); j++) {
                                mainImages.add(detailUrl.getString(j));
                            }
                            product.setDetialUrls(mainImages);
                            List<String> detialImages = new ArrayList<>();
                            String pictrue = commodity.getString("pictrue");
                            JSONArray pictrues = new JSONArray(pictrue);
                            for (int k = 0; k < pictrues.length(); k++) {
                                detialImages.add(pictrues.getString(k));
                            }
                            product.setUrls(detialImages);
                            product.setUrl(product.getUrls().get(0));
                            //TODO 商品对应店铺详情
                            JSONObject store = commodity.getJSONObject("store");
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
                            storeBean.setTotalScore(store.getDouble("totalScore"));
                            storeBean.setStars(store.getInt("totalScore"));
                            storeBean.setIconUrl(store.getString("logo"));
                            storeBean.setId(store.getInt("id"));
                            storeBean.setRecommender("哎购商城");
                            storeBean.setPhone(store.getString("phone"));
                            storeBean.setKeeper(store.getString("storekeeper"));
                            storeBean.setLicence(store.getString("license"));
                            storeBean.setMainUrls(new ArrayList<String>());
                            product.setStoreBean(storeBean);

                            product.setAddress(address.substring(0, address.indexOf("市")));

                            list.add(product);
                        }

                        listener.onSearchProductsFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    /**
     * 获取产品大厅中所有二级分类
     *
     * @param client
     * @param listener
     */
    public static void getProductMallAllSubclassify(OkHttpClient client, final OnGetSubclassifyFinishedListener listener) {
        final List<ProductClassifyBean> list = new ArrayList<>();
        Request request = new Request.Builder()
                .url(FXConst.GET_ALL_SUB_CALSSIFY)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if (string.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(string);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            ProductClassifyBean classifyBean = new ProductClassifyBean();
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            classifyBean.setId(jsonObject.getInt("id"));
                            classifyBean.setTaxon(jsonObject.getString("name"));
                            classifyBean.setUrl(jsonObject.getString("url"));
                            //TODO TEST
                            classifyBean.setUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494931644&di=cb0c2d26f89d25f7d4a60791cc7170e4&imgtype=jpg&er=1&src=http%3A%2F%2Feasyread.ph.126.net%2FCoWpYcImMA_a85z6H-s5Gg%3D%3D%2F7916552989221381682.jpg");
                            list.add(classifyBean);
                        }
                        listener.onGetSubclassifyFinished(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 获取首页和产品大厅中所有推荐内容
     *
     * @param page 0为首页 1为产品大厅
     */
    public static void getHomepageAllRecommend(int page, final OnGetHomeRecommendFinishedListener listener) {
        final List<HomePageRecommendBean> list = new ArrayList<>();
        FormBody body = new FormBody.Builder()
                .add("page", page + "".trim())
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_HOME_RECOMMEND_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if (string.contains("1000")) {
                    try {
                        JSONObject jsonObject = new JSONObject(string);
                        JSONArray dataset = jsonObject.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            JSONObject object = dataset.getJSONObject(i);
                            HomePageRecommendBean recommend = new HomePageRecommendBean();
                            recommend.setLink(object.getInt("link"));
                            recommend.setUrl(object.getString("url"));
                            recommend.setPage(object.getInt("page"));
                            recommend.setPosition(object.getString("position"));
                            recommend.setType(object.getInt("type"));
                            JSONObject detail = object.getJSONObject("detail");
                            recommend.setTitel(detail.getString("name"));
                            if (recommend.getType() == 1) {//商铺
                                recommend.setStoreName(detail.getString("storeName"));
                                recommend.setLogo(detail.getString("logo"));
                                recommend.setIntroduce(detail.getString("intro"));
                                recommend.setAddress(detail.getString("address"));
                            }
                            list.add(recommend);
                        }
                        listener.onGetHomeRecommendFinishedListener(list);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }


    /**
     * 获取搜索热词
     *
     * @param index
     * @param searchType
     */
    public static void getHotWords(int index, int searchType, final OnGetSearchHotWordsFinishedListener listener) {
        final List<String> words = new ArrayList<>();
        //Test
        words.add("粉小萌");
        words.add("三只松鼠");
        words.add("江小白");
        words.add("好吃点");
        words.add("达利园");
        words.add("奥利奥");
        words.add("卫龙");
        words.add("亲嘴烧");
        listener.onGetSearchHotWordsFinishedListener(words);


        FormBody body = new FormBody.Builder()
                .add("type", searchType + "")
                .add("index", index + "")
                .add("size", 10 + "")
                .build();
        final Request request = new Request.Builder()
                .post(body)
                .url(FXConst.GET_HOT_SEARCH_KEY)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            words.add(jsonObject.getString("word"));
                        }
                        listener.onGetSearchHotWordsFinishedListener(words);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 通过店铺id 查找店铺详情
     * @param id
     * @param listener
     */
    public static void getStoreDetailById(int id, final OnGetStoreDetialFinishedListener listener){
        FormBody body = new FormBody.Builder()
                .add("id",id+"".trim())
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_STORE_DETIAL_BY_ID)
                .post(body)
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
                        JSONObject jsonObject = new JSONObject(string);
                        StoreBean storeBean = new StoreBean();
                        storeBean.setName(jsonObject.getString("storeName"));
                        String address = jsonObject.getString("address");
                        storeBean.setAdress(address);
                        storeBean.setGoodsCount(jsonObject.getInt("cnum"));
                        storeBean.setTotalScals(jsonObject.getInt("sales"));
                        storeBean.setCreateTime(jsonObject.getString("createTime"));
                        storeBean.setQualityScore(jsonObject.getDouble("qualityScore"));
                        storeBean.setDescribeScore(jsonObject.getDouble("describeScore"));
                        storeBean.setPriceScore(jsonObject.getDouble("transportScore"));
                        storeBean.setStars(jsonObject.getInt("totalScore"));
                        storeBean.setTotalScore(jsonObject.getDouble("totalScore"));
                        storeBean.setIconUrl(jsonObject.getString("logo"));
                        storeBean.setLicence(jsonObject.getString("license"));
                        storeBean.setPhone(jsonObject.getString("phone"));
                        //jsonObject.getJSONArray("storePictrue");
                        storeBean.setMainUrls(new ArrayList<String>());
                        storeBean.setId(jsonObject.getInt("id"));
                        storeBean.setKeeper(jsonObject.getString("storekeeper"));
                        storeBean.setRecommender("哎购商城");
                        listener.onGetStoreDetialFinishedListener(storeBean);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 通过id 查找商品详情
     * @param id
     * @param listener
     */
    public static void getProductDetialById(int id, final OnGetProductDetialFinishedListener listener){

        FormBody body = new FormBody.Builder()
                .add("id", id + "")
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
                if (string.contains("1000")) {
                    MainProductBean product = new MainProductBean();
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
                        product.setStoreBean(storeBean);
                        product.setAddress(address.substring(0, address.indexOf("市") + 1));

                        JSONArray dataset = object.getJSONArray("dataset");
                        List<ColorSizeBean> colors = new ArrayList<>();
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
                        product.setVipPrice(colorSizeBean.getPrice() * (1 - MyApplication.vipRate));
                        product.setExpress("包邮");//TODO 待定
                     listener.onGetProductDetialFinishedListener(product);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }




    /**
     * 计算当前地区商品的邮费
     *
     * @param productId
     */

    public static void getPostage(int productId, final OnGetPostageFinishedListener listener) {
        FormBody body = new FormBody.Builder()
                .add("id", productId + "".trim())
                .add("address", MyApplication.currentProvince)
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_POSTAGE_OF_SOME_DISTRICT)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if (string.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(string);
                        double total = object.getDouble("total");
                        listener.onGetPostageFinishedListener(total);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 获取会员折扣比例
     */
    public static void getVipRate() {
        client.newCall(new Request.Builder().url(FXConst.GET_VIP_PRICE_URL).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if (string.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(string);
                        int total = object.getInt("total");
                        MyApplication.vipRate = total;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 获取购物车中商品数量
     *
     * @param token
     */
    public static void getShoppingCarCount(String token) {
        FormBody body = new FormBody.Builder()
                .add("", token)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.GET_SHOPPINGCAR_COUNT)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if (string.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(string);
                        int total = object.getInt("total");
                        MyApplication.shoppingCount = total;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
