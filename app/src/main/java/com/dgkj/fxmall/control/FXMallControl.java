package com.dgkj.fxmall.control;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.dgkj.fxmall.base.BaseActivity;
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
import com.dgkj.fxmall.listener.OnGetProductCSFinishedListener;
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
import com.dgkj.fxmall.model.FXMallModel;

import okhttp3.OkHttpClient;

/** FXMall控制层
 * Created by 成焕文 on 2017/3/17.
 */

public class FXMallControl {
    /**
     * 支付宝支付业务
     */
    public void zhiPay(Activity context,String orderInfo,Handler mHandler){
        FXMallModel.payV2(context,orderInfo,mHandler);
    }

    /**
     * 支付宝授权业务
     */
    public void zhiAuth(Activity context ,String authInfo,Handler mHandler){
        FXMallModel.authV2(context,authInfo,mHandler);
    }

    /**
     * 获取需求大厅数据
     */
    public void getDemandDatas(OnGetDemandDatasFinishedListener listener){
        FXMallModel.getDemandDatas(listener);
    }

    public void getDemandClassifyDatas(Handler handler,Context context, OkHttpClient client, OnGetDemandClassifyFinishedListener listener){
        FXMallModel.getDemandClassify(handler,context,client,listener);
    }

    public void getStoreProducts(final BaseActivity context, String token, OkHttpClient client, String orderby, int index, int size, int status, final OnGetStoreProductsFinishedListener listener){
        FXMallModel.getStoreProducts(context,token,client,orderby,index,size,status,listener);
    }

    public void getShopingCarData(OnGetShoppingCarDataListener listener){
        FXMallModel.getShoppingCarData(listener);
    }

    public void getLogisticsMsgData(OnGetLogisticsMsgFinishedListener listener){
        FXMallModel.getLogisticsMsg(listener);
    }

    public void getLogisticsDetial(OnGetLogisticsDetialFinishedListener listener){
        FXMallModel.getLogisticsDetial(listener);
    }

    public void getSubClassifyProductData(OnGetSubClassifyProductsFinishedListener listener){
        FXMallModel.getSubClassifyProductData(listener);
    }

    public void getAllTakeAddress(BaseActivity context, String token, OkHttpClient client,OnGetAllAddressFinishedListener listener){
        FXMallModel.getAllTakeAddress(context,token,client,listener);
    }

    public void getStoreSuperClassify(BaseActivity context, String token, OkHttpClient client,OnGetStoreSuperClassifyFinishedListener listener){
        FXMallModel.getStoreSuperClassify(context,token,client,listener);
    }

    public void getSubclassify(BaseActivity context,int superId,OkHttpClient client,OnGetSubclassifyFinishedListener listener){
        FXMallModel.getSubClassify(context,superId,client,listener);
    }

    public void getAllPostage(BaseActivity context,String token,OkHttpClient client, OnGetAllPostageModelFinishedListener listener){
        FXMallModel.getAllPostage(context,token,client,listener);
    }

    public void getStoreProductClassify(final BaseActivity context,String token,int statu,OkHttpClient client, final OnGetStoreProductClassifyFinishedListener listener){
        FXMallModel.getStoreProductClassify(context,token,statu,client,listener);
    }

    public void add2shoppingcar(final BaseActivity context,String token,int skuId,OkHttpClient client){
        FXMallModel.add2shoppingcar(context,token,skuId,client);
    }

    public  void getTransactionRecord(final BaseActivity context,String token,int index,int size,OkHttpClient client, final OnGetTransactionRecorderFinishedListener listener){
        FXMallModel.getTransactionRecord(context,token,index,size,client,listener);
    }

    public void getShoppingcarProducts(final BaseActivity context,String token,int index,int size,OkHttpClient client, final OnGetShoppingcarProductsFinishedListener listener){
        FXMallModel.getShoppingcarProducts(context,token,index,size,client,listener);
    }

    public void getMyDemandData(final BaseActivity context, String token, final int index, int size, OkHttpClient client, final OnGetMyDemandDataFinishedListener listener){
        FXMallModel.getMyDemandData(context,token,index,size,client,listener);
    }

    public void getDemandByClassify(final BaseActivity context, int classifyId, final int index, int size, OkHttpClient client, final OnGetMyDemandDataFinishedListener listener){
        FXMallModel.getDemandByClassify(context,classifyId,index,size,client,listener);
    }

    public void getMyRecommendStore(final BaseActivity context, String token, String url,final int index, int size, OkHttpClient client, final OnGetMyRecommendStoreFinishedListener listener){
        FXMallModel.getMyRecommendStore(context,token,url,index,size,client,listener);
    }

    public void getSearchProducts(final BaseActivity context, String key, final int index, int size, OkHttpClient client, final OnSearchProductsFinishedListener listener){
        FXMallModel.getSearchProducts(context,key,index,size,client,listener);
    }

    public void getSearchStores(final BaseActivity context, String key, final int index, int size, OkHttpClient client, final OnGetMyRecommendStoreFinishedListener listener){
        FXMallModel.getSearchStores(context,key,index,size,client,listener);
    }

    public void getMyOrderInfo(final BaseActivity context, String token, final int statu, int isAll, OkHttpClient client, final OnGetMyOrderInfoFinishedListener listener){
        FXMallModel.getMyOrderInfo(context,token,statu,isAll,client,listener);
    }

    public void getStoreOrderInfo(final BaseActivity context, String token, final int statu, int isAll,int index,int size, OkHttpClient client, final OnGetMyOrderInfoFinishedListener listener){
        FXMallModel.getStoreOrderInfo(context,token,statu,isAll,index,size,client,listener);
    }

    public void getExpressList(final BaseActivity context, OkHttpClient client, final OnGetExpressCompanyFinishedListener listener){
        FXMallModel.getAllExpressCompany(context,client,listener);
    }

    public void getProductComments(final BaseActivity context, int id, final int index, int size, OkHttpClient client, final OnGetProductCommentListFinishListener listener){
        FXMallModel.getProductComments(context,id,index,size,client,listener);
    }

    public void getBanners(OkHttpClient client, final OnGetBannerFinishedListener listener){
        FXMallModel.getBanners(client,listener);
    }

    public void getMainRecommendStores(OkHttpClient client, OnGetMainRecommendStoreFinishedListener listener){
        FXMallModel.getMainRecommentStores(client,listener);
    }
    public void getProductCS(OkHttpClient client, int id, final OnGetProductCSFinishedListener listener){
        FXMallModel.getProductCS(client,id,listener);
    }
}
