package com.dgkj.fxmall.model;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.BannerBean;
import com.dgkj.fxmall.bean.CommentBean;
import com.dgkj.fxmall.bean.DemandMallClassifyBean;
import com.dgkj.fxmall.bean.ExpressCompanyBean;
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
import com.dgkj.fxmall.listener.OnGetLogisticsDetialFinishedListener;
import com.dgkj.fxmall.listener.OnGetLogisticsMsgFinishedListener;
import com.dgkj.fxmall.listener.OnGetMainRecommendStoreFinishedListener;
import com.dgkj.fxmall.listener.OnGetMyDemandDataFinishedListener;
import com.dgkj.fxmall.listener.OnGetMyOrderInfoFinishedListener;
import com.dgkj.fxmall.listener.OnGetMyRecommendStoreFinishedListener;
import com.dgkj.fxmall.listener.OnGetProductCommentListFinishListener;
import com.dgkj.fxmall.listener.OnGetShoppingCarDataListener;
import com.dgkj.fxmall.listener.OnGetShoppingcarProductsFinishedListener;
import com.dgkj.fxmall.listener.OnGetStoreProductClassifyFinishedListener;
import com.dgkj.fxmall.listener.OnGetStoreProductsFinishedListener;
import com.dgkj.fxmall.listener.OnGetStoreSuperClassifyFinishedListener;
import com.dgkj.fxmall.listener.OnGetSubClassifyProductsFinishedListener;
import com.dgkj.fxmall.listener.OnGetSubclassifyFinishedListener;
import com.dgkj.fxmall.listener.OnGetTransactionRecorderFinishedListener;
import com.dgkj.fxmall.listener.OnSearchProductsFinishedListener;
import com.dgkj.fxmall.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
                            classify.setUrl("http://img3.duitang.com/uploads/item/201505/05/20150505210633_NHRj4.jpeg");
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
                        for (int i = 0; i < dataset.length(); i++) {
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            JSONObject commodity = jsonObject.getJSONObject("commodity");
                            StoreProductBean product = new StoreProductBean();
                            product.setId(jsonObject.getInt("id"));
                            product.setBrokerage(jsonObject.getInt("brokerage"));
                            product.setPrice(commodity.getDouble("price"));
                            //TODO 根据佣金计算会员价
                            product.setVipPrice(product.getPrice()-product.getBrokerage()*0.6);
                            product.setColor(jsonObject.getString("content"));
                            product.setDescribe(commodity.getString("detail"));
                            product.setSales(commodity.getInt("sales") + "");
                            product.setInventory(commodity.getInt("inventory") + "");
                            product.setStatu(commodity.getInt("status"));
                            product.setSkuID(commodity.getInt("id"));
                            product.setTitel(commodity.getString("name"));
                            JSONArray detailUrl = commodity.getJSONArray("detailUrl");
                            List<String> mainImages = new ArrayList<>();
                            for (int j = 0; j < detailUrl.length(); j++) {
                                mainImages.add(detailUrl.getString(j));
                            }
                            product.setDetialUrls(mainImages);
                            List<String> detialImages = new ArrayList<>();
                            JSONArray pictrues = commodity.getJSONArray("pictrue");
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
            list.add(productBean);
            listener.OnGetStoreProductsFinished(list);
        }
    }


    public static void getShoppingCarData(OnGetShoppingCarDataListener listener) {
        List<ShoppingCarBean> carBeanList = new ArrayList<>();
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
                goodsList.add(goods);
            }
            carBean.setGoods(goodsList);
            if (i < 2) {
                carBean.setStoreName("粉小萌酸辣粉");
            } else {
                carBean.setStoreName("粉小萌旗舰店");
            }
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
            MainProductBean product = new MainProductBean();
            product.setIntroduce("粉小萌横空出世，买到就是赚到");
            product.setPrice(88);
            product.setAddress("广东深圳");
            product.setVipPrice(58);
            product.setSales("564");
            product.setExpress("韵达快递");
            product.setUrls(url);
            product.setUrl(url.get(0));
            list.add(product);
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
                LogUtil.i("TAG","收货地址=="+result);
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
                            //classify.setUrl(jsonObject.getString("url"));
                            //classify.setUrl("http://img5q.duitang.com/uploads/item/201504/13/20150413H2605_LYRWc.jpeg");
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
    public static void getShoppingcarProducts(final BaseActivity context, String token, int index, int size, OkHttpClient client, final OnGetShoppingcarProductsFinishedListener listener) {
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
                            goods.setCarId(jsonObject.getInt("id"));
                            goods.setColor(jsonObject.getString("content"));
                            JSONObject sku = jsonObject.getJSONObject("sku");
                            goods.setBrokerage(sku.getInt("brokerage"));
                            goods.setSkuId(sku.getInt("id"));
                            JSONObject commodity = sku.getJSONObject("commodity");
                            goods.setIntroduce(commodity.getString("detail"));
                            goods.setProductId(commodity.getInt("id"));
                            goods.setSales(commodity.getInt("sales"));
                            goods.setStatu(commodity.getInt("status"));

                            goods.setDescribeScore(commodity.getInt("describeScore"));
                            goods.setPriceScore(commodity.getInt("transportScore"));
                            goods.setQualityScore(commodity.getInt("qualityScore"));
                            goods.setTotalScore(commodity.getInt("totalScore"));

                            JSONArray pictrues = commodity.getJSONArray("pictrue");
                            List<String> mainUrls = new ArrayList<>();
                            for (int j = 0; j < pictrues.length(); j++) {
                                mainUrls.add(pictrues.getString(i));
                            }
                            goods.setMainUrls(mainUrls);
                            JSONArray detailUrls = commodity.getJSONArray("detailUrl");
                            List<String> detailImages = new ArrayList<>();
                            for (int k = 0; k < detailUrls.length(); k++) {
                                detailImages.add(detailUrls.getString(k));
                            }
                            goods.setDetialUrls(detailImages);
                            JSONObject store = commodity.getJSONObject("store");
                            goods.setStoreId(store.getInt("id"));
                            goods.setStoreName(store.getString("storeName"));
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
                            JSONArray photos = object.getJSONArray("url");
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
                            JSONArray photos = object.getJSONArray("url");
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
        FormBody body = new FormBody.Builder()
                .add("uuser.token", token)
                .add("index", index + "")
                .add("size", size + "")
                .build();
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
    public static void getSearchProducts(final BaseActivity context, String key, final int index, int size, OkHttpClient client, final OnSearchProductsFinishedListener listener) {
        final List<MainProductBean> list = new ArrayList<>();
        FormBody body = new FormBody.Builder()
                .add("name", key)
                .add("orderby", "createTime")
                .add("index", index + "")
                .add("size", size + "")
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_SEARCH_PRODUCTS)
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
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            MainProductBean product = new MainProductBean();
                            product.setBrokerage(jsonObject.getInt("brokerage"));
                            product.setPrice(jsonObject.getDouble("price"));
                            product.setSkuId(jsonObject.getInt("id"));
                            //TODO 根据佣金和价格算出会员价：
                            product.setVipPrice(product.getPrice()-product.getBrokerage()*0.6);
                            product.setColor(jsonObject.getString("content"));

                            JSONObject commodity = jsonObject.getJSONObject("commodity");
                            product.setId(commodity.getInt("id"));
                            product.setDescribeScore(commodity.getInt("describeScore"));
                            product.setPriceScore(commodity.getInt("transportScore"));
                            product.setQualityScore(commodity.getInt("qualityScore"));
                            product.setTotalScore(commodity.getInt("totalScore"));
                            product.setIntroduce(commodity.getString("detail"));
                            product.setSales(commodity.getInt("sales") + "");
                            product.setInventory(commodity.getInt("inventory"));
                            JSONArray detailUrl = commodity.getJSONArray("detailUrl");
                            List<String> mainImages = new ArrayList<>();
                            for (int j = 0; j < detailUrl.length(); j++) {
                                mainImages.add(detailUrl.getString(j));
                            }
                            product.setDetialUrls(mainImages);
                            List<String> detialImages = new ArrayList<>();
                            JSONArray pictrues = commodity.getJSONArray("pictrue");
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
                            storeBean.setIconUrl(store.getString("url"));
                            product.setStoreBean(storeBean);

                            product.setAddress(address.substring(0,address.indexOf("市")));

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
                            switch (refundState){//退款状态
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

                            JSONObject productInfo = jsonObject.getJSONObject("sku");//TODO 商品信息有待完善，字段待确定
                            orderBean.setBrokerage(productInfo.getInt("brokerage"));
                            orderBean.setSinglePrice(productInfo.getInt("price"));
                            orderBean.setColor(productInfo.getString("content"));
                            orderBean.setSkuId(productInfo.getInt("id"));
                            JSONObject commodity = productInfo.getJSONObject("commodity");
                            //TODO 根据当前定位城市算出邮费
                            //orderBean.setPostage(productInfo.getInt(""));
                            orderBean.setIntroduce(commodity.getString("detail"));
                            //orderBean.setCount(commodity.getInt("num"));
                            orderBean.setProductId(commodity.getInt("id"));
                            JSONArray pictures = commodity.getJSONArray("pictrue");
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
    /***
     * @param context
     * @param token
     * @param statu
     * @param isAll
     * @param client
     * @param listener
     */
    public static void getStoreOrderInfo(final BaseActivity context, String token, final int statu, int isAll,int index,int size,OkHttpClient client, final OnGetMyOrderInfoFinishedListener listener) {
        final List<OrderBean> list = new ArrayList<>();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("user.token", token)
                .add("status", statu + "")
                .add("index",index+"")
                .add("size",size+"");
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

                            JSONObject order = jsonObject.getJSONObject("orders");
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
                            switch (refundState){//退款状态
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

                            JSONObject productInfo = jsonObject.getJSONObject("sku");//TODO 商品信息有待完善，字段待确定
                            orderBean.setBrokerage(productInfo.getInt("brokerage"));
                            orderBean.setSinglePrice(productInfo.getInt("price"));
                            orderBean.setColor(productInfo.getString("content"));
                            orderBean.setSkuId(productInfo.getInt("id"));
                            //TODO 获取邮费
                            // orderBean.setPostage(productInfo.getInt(""));
                            JSONObject commodity = productInfo.getJSONObject("commodity");
                           // orderBean.setCount(commodity.getInt("num"));
                            orderBean.setUrl(commodity.getString("detail"));
                            orderBean.setProductId(commodity.getInt("id"));
                            JSONArray pictures = commodity.getJSONArray("pictrue");
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
                        JSONObject  object = new JSONObject(result);
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
     * @param client
     * @param listener
     */
    public static void getBanners(OkHttpClient client, final OnGetBannerFinishedListener listener){
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
                if(string.contains("1000")){
                    try {
                        JSONObject object = new JSONObject(string);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            BannerBean banner = new BannerBean();
                            banner.setType(jsonObject.getInt("status"));
                            banner.setUrl(jsonObject.getString("url"));
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
     * 获取首页店铺推荐
     * @param client
     * @param listener
     */
    public static void getMainRecommentStores(OkHttpClient client, final OnGetMainRecommendStoreFinishedListener listener){
        final List<MainRecommendStoreBean> list = new ArrayList<>();
        Request request = new Request.Builder()
                .url(FXConst.GET_MAIN_RECOMMEND_STORES_URLS)
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
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            MainRecommendStoreBean recommendStoreBean = new MainRecommendStoreBean();
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            recommendStoreBean.setAdUrl(jsonObject.getString("url"));
                            StoreBean storeBean = new StoreBean();
                            JSONObject store = jsonObject.getJSONObject("store");
                            storeBean.setIconUrl(store.getString("logo"));
                            storeBean.setAdress(store.getString("address"));
                            storeBean.setName(store.getString("storeName"));
                            storeBean.setDescribeScore(store.getDouble("describeScore"));
                            storeBean.setQualityScore(store.getDouble("qualityScore"));
                            storeBean.setPriceScore(store.getDouble("transportScore"));
                            storeBean.setTotalScore(store.getDouble("totalScore"));
                            storeBean.setTotalScals(store.getInt("sales"));
                            storeBean.setGoodsCount(store.getInt("cnum"));
                            recommendStoreBean.setStoreBean(storeBean);
                            list.add(recommendStoreBean);
                        }
                        listener.onGetMainRecommendStoreFinishedListener(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
