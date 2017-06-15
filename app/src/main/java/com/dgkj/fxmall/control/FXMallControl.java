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
import com.dgkj.fxmall.listener.OnGetHomeRecommendFinishedListener;
import com.dgkj.fxmall.listener.OnGetLogisticsDetialFinishedListener;
import com.dgkj.fxmall.listener.OnGetLogisticsMsgFinishedListener;
import com.dgkj.fxmall.listener.OnGetMainRecommendStoreFinishedListener;
import com.dgkj.fxmall.listener.OnGetMyDemandDataFinishedListener;
import com.dgkj.fxmall.listener.OnGetMyOrderInfoFinishedListener;
import com.dgkj.fxmall.listener.OnGetMyRecommendStoreFinishedListener;
import com.dgkj.fxmall.listener.OnGetMyVipFinishedListener;
import com.dgkj.fxmall.listener.OnGetNotifyMsgFinishedListener;
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

    public void getDemandDatas(OnGetDemandDatasFinishedListener listener){
        FXMallModel.getDemandDatas(listener);
    }

    public void getDemandClassifyDatas(Handler handler,Context context, OkHttpClient client, OnGetDemandClassifyFinishedListener listener){
        FXMallModel.getDemandClassify(handler,context,client,listener);
    }

    public void getStoreProducts(final BaseActivity context, String token, OkHttpClient client, String orderby, int index, int size, int status, OnGetStoreProductsFinishedListener listener){
        FXMallModel.getStoreProducts(context,token,client,orderby,index,size,status,listener);
    }


    public void getMyStoreSubClassifyProducts(final BaseActivity context, int storeId, OkHttpClient client, String orderby, int index, int size, int subId, final OnGetStoreProductsFinishedListener listener){
        FXMallModel.getMyStoreSubClassifyProducts(context,storeId,client,orderby,index,size,subId,listener);
    }


    public void getStoreSubClassifyProducts(final BaseActivity context, int storeId, OkHttpClient client, String orderby, int index, int size, int subId, final OnSearchProductsFinishedListener listener){
        FXMallModel.getStoreSubClassifyProducts(context,storeId,client,orderby,index,size,subId,listener);
    }

    public void getShopingCarData(OnGetShoppingCarDataListener listener){
        FXMallModel.getShoppingCarData(listener);
    }

    public void getLogisticsMsgData(OnGetLogisticsMsgFinishedListener listener){
        FXMallModel.getLogisticsMsg(listener);
    }

    public void getLogisticsDetial(int id ,String expNo,OnGetLogisticsDetialFinishedListener listener){
        FXMallModel.getLogisticsDetial(id,expNo,listener);
    }

    public void getSubClassifyProductData(OnGetSubClassifyProductsFinishedListener listener){
        FXMallModel.getSubClassifyProductData(listener);
    }

    public void getHomePageProductsDisplay(String token,int index, int size, OkHttpClient client,OnSearchProductsFinishedListener listener) {
        FXMallModel.getHomePageProductsDisplay(token,index,size,client,listener);
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

    public void getStoreProductClassify(final BaseActivity context,int id,int statu,OkHttpClient client, OnGetStoreProductClassifyFinishedListener listener){
        FXMallModel.getStoreProductClassify(context,id,statu,client,listener);
    }

    public void add2shoppingcar(final BaseActivity context,String token,int skuId,OkHttpClient client){
        FXMallModel.add2shoppingcar(context,token,skuId,client);
    }

    public  void getTransactionRecord(final BaseActivity context,String token,int index,int size,OkHttpClient client, OnGetTransactionRecorderFinishedListener listener){
        FXMallModel.getTransactionRecord(context,token,index,size,client,listener);
    }

    public void getShoppingcarProducts(final BaseActivity context,String token,int index,int size,OkHttpClient client, OnGetShoppingcarProductsFinishedListener listener){
        FXMallModel.getShoppingcarProducts(context,token,index,size,client,listener);
    }

    public void getMyDemandData(final BaseActivity context, String token, int index, int size, OkHttpClient client,OnGetMyDemandDataFinishedListener listener){
        FXMallModel.getMyDemandData(context,token,index,size,client,listener);
    }

    public void getDemandByClassify(final BaseActivity context, int classifyId, int index, int size, OkHttpClient client, OnGetMyDemandDataFinishedListener listener){
        FXMallModel.getDemandByClassify(context,classifyId,index,size,client,listener);
    }

    public void getMyRecommendStore(final BaseActivity context, String token, String url,int index, int size, OkHttpClient client,OnGetMyRecommendStoreFinishedListener listener){
        FXMallModel.getMyRecommendStore(context,token,url,index,size,client,listener);
    }

    public void getSearchProducts(Activity context, String key, String orderBy, String token,String startPrice,String endPice,String address,int index, int size, int storeId,OkHttpClient client, OnSearchProductsFinishedListener listener){
        FXMallModel.getSearchProducts(context,key,orderBy,token,startPrice,endPice,address,index,size,storeId,client,listener);
    }

    public void getSearchStores(final BaseActivity context, String key, int index, int size, OkHttpClient client, OnGetMyRecommendStoreFinishedListener listener){
        FXMallModel.getSearchStores(context,key,index,size,client,listener);
    }

    public void getMyOrderInfo(final BaseActivity context, String token, int statu, int isAll, OkHttpClient client, OnGetMyOrderInfoFinishedListener listener){
        FXMallModel.getMyOrderInfo(context,token,statu,isAll,client,listener);
    }

    public void getStoreOrderInfo(final BaseActivity context, String token,int statu, int isAll,int index,int size, OkHttpClient client, OnGetMyOrderInfoFinishedListener listener){
        FXMallModel.getStoreOrderInfo(context,token,statu,isAll,index,size,client,listener);
    }

    public void getExpressList(final BaseActivity context, OkHttpClient client, OnGetExpressCompanyFinishedListener listener){
        FXMallModel.getAllExpressCompany(context,client,listener);
    }

    public void getProductComments(final BaseActivity context, int id, int index, int size, OkHttpClient client, OnGetProductCommentListFinishListener listener){
        FXMallModel.getProductComments(context,id,index,size,client,listener);
    }

    public void getBanners(OkHttpClient client, OnGetBannerFinishedListener listener){
        FXMallModel.getBanners(client,listener);
    }


    public void getProductCS(OkHttpClient client, int id, OnGetProductCSFinishedListener listener){
        FXMallModel.getProductCS(client,id,listener);
    }


    public void getProductMallAllSubclassify(OkHttpClient client, OnGetSubclassifyFinishedListener listener){
        FXMallModel.getProductMallAllSubclassify(client,listener);
    }

    public void getProductsOfSubclassify(int subId, String orderBy, int index, int size, OkHttpClient client,OnSearchProductsFinishedListener listener){
        FXMallModel.getProductsForSubclassify(subId,orderBy,index,size,client,listener);
    }

    public void getHomePageAllRecommender(int page, OnGetHomeRecommendFinishedListener listener){
        FXMallModel.getHomepageAllRecommend(page,listener);
    }

    public void getSearchHotwords(int index, int searchType, OnGetSearchHotWordsFinishedListener listener){
        FXMallModel.getHotWords(index,searchType,listener);
    }

    public void getStoreDetialByid(int id, OnGetStoreDetialFinishedListener listener){
        FXMallModel.getStoreDetailById(id,listener);
    }

    public void getProductDetialById(int id, OnGetProductDetialFinishedListener listener){
        FXMallModel.getProductDetialById(id,listener);
    }

    public void getMySubVipData(String url, String token, int index, int size,  OnGetMyVipFinishedListener listener){
        FXMallModel.getMySubVipData(url,token,index,size,listener);
    }

    public void getMySuperVipInfo(String token, OnGetMyVipFinishedListener listener){
        FXMallModel.getMySuperVipInfo(token,listener);
    }

    public void getNotifyMsgData(String token, OnGetNotifyMsgFinishedListener listener){
        FXMallModel.getNotifyMsgData(token,listener);
    }

    public void getAllNewProducts(Activity context,int index, int size, OkHttpClient client, OnSearchProductsFinishedListener listener) {
        FXMallModel.getAllNewProducts(context,index,size,client,listener);
    }
}
