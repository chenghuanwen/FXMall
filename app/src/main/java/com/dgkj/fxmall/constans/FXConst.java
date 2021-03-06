package com.dgkj.fxmall.constans;

/**
 * Created by Android004 on 2017/3/17.
 */

public class FXConst {
    public static final int SDK_PAY_FLAG = 1;//支付宝支付结果标志
    public static final int SDK_AUTH_FLAG = 2;//支付宝授权结果标志
    public static final String APPID = "2017061307481478";//支付宝平台申请的APPID
    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "2017061307481478";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "2017061307481478";
    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE = "";
    public static final String RSA_PRIVATE = "";

    /**
     * 微信平台APPID
     */
    public static final String APP_ID = "wx38d1a16a259cbbd8";


   //public static final String FXM_URL_PORT = "http://192.168.3.126/Distribution/";
    public static final String FXM_URL_PORT = "http://106.14.149.46/Distribution/";


    public static final String CHECK_INVITE_CODE = FXM_URL_PORT+"userAction_checkCode";
    public static final String CHECK_PHONE_IS_REGISTED = FXM_URL_PORT+"userAction_checkPhone";
    public static final String GET_MESSAGE_CHECK_CODE = FXM_URL_PORT+"messageRecorderAction_sendMessage";
    public static final String CHECK_MESSAGE_CODE = FXM_URL_PORT+"messageRecorderAction_checkCode";
    public static final String USER_REGIST_URL = FXM_URL_PORT+"userAction_register";
    public static final String USER_LOGIN_URL = FXM_URL_PORT+"userAction_login";
    public static final String CHANGE_USER_ICON = FXM_URL_PORT+"userAction_headPicture";
    public static final String GET_USER_INFO = FXM_URL_PORT+"userAction_userDetail";
    public static final String RESET_LOGIN_PASSWORD = FXM_URL_PORT+"userAction_updatePassword";
    public static final String CHANGE_USER_NICK = FXM_URL_PORT+"userAction_nickename";
    public static final String CHANGE_USER_GENDER = FXM_URL_PORT+"userAction_sex";
    public static final String CHANGE_USER_NAME = FXM_URL_PORT+"userAction_realname";
    public static final String CHANGE_USER_PHONE = FXM_URL_PORT+"userAction_phone";
    public static final String CHANGE_USER_ADDRESS = FXM_URL_PORT+"userAction_location";
    public static final String USER_FORGOT_PASSWORD = FXM_URL_PORT+"MessageRecorderAction_fogetPass";

    public static final String ADD_TAKE_ADDRESS = FXM_URL_PORT+"shoppingAddressAction_addAddress";
    public static final String GET_INVITECODE_CONFIG = FXM_URL_PORT+"systemAction_getCodeConfig";
    public static final String GET_ALL_TAKE_ADDRESS = FXM_URL_PORT+"shoppingAddressAction_getAddress";
    public static final String UPDATE_TAKE_ADDRESS = FXM_URL_PORT+"shoppingAddressAction_updateAddress";
    public static final String SET_DEFAULT_TAKE_ADDRESS = FXM_URL_PORT+"shoppingAddressAction_setDefault";
    public static final String DELETE_TAKE_ADDRESS = FXM_URL_PORT+"shoppingAddressAction_deleteAddress";
    public static final String GET_DEFAULT_TAKE_ADDRESS = FXM_URL_PORT+"shoppingAddressAction_getDefaultAddress";
    public static final String APPLY_STORE = FXM_URL_PORT+"storeAction_addStore";
    public static final String GET_APPLY_STORE_CLASSIFY = FXM_URL_PORT+"categoryAction_getCategory";
    public static final String UPLOAD_STORE_LOGO = FXM_URL_PORT+"storeAction_updateStoreLogo";
    public static final String UPLOAD_STORE_INTRODUCE = FXM_URL_PORT+"storeAction_updateIntro";
    public static final String GET_SUBCLASSIFY = FXM_URL_PORT+"subCategoryAction_categoryByParent";
    public static final String ADD_POSTAGE_MODEL = FXM_URL_PORT+"freightTemplateAction_addTemplate";
    public static final String GET_ALL_POSTAGE_MODEL = FXM_URL_PORT+"freightTemplateAction_getTemplate";
    public static final String UPDATE_POSTAGE_MODEL = FXM_URL_PORT+"freightTemplateAction_updateTemplate";
    public static final String PUBLISH_PRODUCT_URL = FXM_URL_PORT+"skuAction_addSku";
    public static final String GET_STORE_PROCUCT_ALL_CLASSIFY = FXM_URL_PORT+"commodityAction_getCommodityTypeTotal";
    public static final String GET_STORE_ALL_PROCUCT = FXM_URL_PORT+"commodityAction_getCommodityDesc";
    public static final String PRODUCT_ONLINE_OFFLINE = FXM_URL_PORT+"commodityAction_updateStatus";
    public static final String USER_RECHARGE_URL = FXM_URL_PORT+"userAction_topUp";
    public static final String GET_TRANSACTION_RECORDS = FXM_URL_PORT+"userAction_financialDetails";
    public static final String GET_STORE_SALE_COUNT = FXM_URL_PORT+"commodityAction_getStatusTotal";

    public static final String GET_STORE_DETIAL_INFO = FXM_URL_PORT+"storeAction_storeDetail";
    public static final String ADD_PRODUCT_TO_SHOPPINGCAR = FXM_URL_PORT+"shoppingCartAction_addCart";
    public static final String GET_SHOPPINGCAR_PRODUCTS = FXM_URL_PORT+"shoppingCartAction_getCart";
    public static final String CHAGNE_PRODUCT_COUNT_IN_CAR = FXM_URL_PORT+"shoppingCartAction_updateCart";
    public static final String DELETE_PRODUCT_COUNT_IN_CAR = FXM_URL_PORT+"shoppingCartAction_deleteCart";
    public static final String DELETE_SUB_POSTAGE_MODEL = FXM_URL_PORT+"freightTemplateAction_delprovince";
    public static final String DELETE_SUPER_POSTAGE_MODEL = FXM_URL_PORT+"freightTemplateAction_delTemplate";
    public static final String PUBLISH_DEMAND_URL = FXM_URL_PORT+"desiredAction_addDesired";

    public static final String GET_ALL_SUB_CALSSIFY = FXM_URL_PORT+"subCategoryAction_getCategory";

    public static final String DELETE_MY_DEMAND_URL = FXM_URL_PORT+"desiredAction_delteDesired";
    public static final String GET_MY_DEMAND_URL = FXM_URL_PORT+"desiredAction_getDesired";
    public static final String GET_DEMAND_BY_CLASSIFY = FXM_URL_PORT+"desiredAction_getDesiredByType";
    public static final String RECOMMEND_STORE_FOR_DEMAND = FXM_URL_PORT+"recommendAction_recommend";
    public static final String GET_MY_RECOMMEND_STORES = FXM_URL_PORT+"recommendAction_store";
    public static final String GET_RECOMMEND_STORES_FOR_DEMAND = FXM_URL_PORT+"recommendAction_getStoreByD";

    public static final String GET_HOT_SEARCH_KEY = FXM_URL_PORT+"keyWordAction_key";
    public static final String GET_SEARCH_PRODUCTS = FXM_URL_PORT+"commodityAction_fuzzSearch";
    public static final String GET_SEARCH_STORES = FXM_URL_PORT+"storeAction_fuzzSearch";

    public static final String SUBMIT_ORDER_URL = FXM_URL_PORT+"orderCommodityAction_addOrder";
    public static final String GET_ORDER_CALSSIFY = FXM_URL_PORT+"orderAction_orderInfo";
    public static final String CANCEL_ORDER_URL = FXM_URL_PORT+"orderAction_closeOrder";

    public static final String PAY_ORDER_URL = FXM_URL_PORT+"orderAction_pay";

    public static final String GET_EXPRESS_COMPANY_LIST = FXM_URL_PORT+"expressCodeAction_express";
    public static final String SHANGPU_DELIVER_URL = FXM_URL_PORT+"orderAction_shipments";
    public static final String CONFIRM_TAKEGOODS_URL = FXM_URL_PORT+"orderAction_ok";

    public static final String WHETHER_IS_FIRST_SET_PAYWORD = FXM_URL_PORT+"userAction_isPass";
    public static final String SET_PAYWORD_URL = FXM_URL_PORT+"userAction_paypssword";
    public static final String PUBLISH_COMMENT_URL = FXM_URL_PORT+"estimateAction_estimate";
    public static final String GET_COMMENTS_FOR_ONE_PRODUCT = FXM_URL_PORT+"estimateAction_getEstimate";

    public static final String GET_STORE_ORDER_CLASSIFY = FXM_URL_PORT+"orderAction_storeOrder";
    public static final String DELETE_ORDER_FOR_STORE = FXM_URL_PORT+"orderAction_delSO";
    public static final String DELETE_ORDER_FOR_USER = FXM_URL_PORT+"orderAction_delUO";
    public static final String USER_APPLY_REFUND_URL = FXM_URL_PORT+"refundOrderAction_refund";
    public static final String STORE_AGREE_REFUND_URL = FXM_URL_PORT+"refundOrderAction_agree";
    public static final String STORE_REFUSED_REFUND_URL = FXM_URL_PORT+"refundOrderAction_repulse";
    public static final String USER_SUBMIT_LOGISTICS_URL = FXM_URL_PORT+"refundOrderAction_wibill";
    public static final String STORE_CONFIRM_REFUND = FXM_URL_PORT+"refundOrderAction_ok";


    public static final String CHECK_PAY_PASSWORD = FXM_URL_PORT+"userAction_checkPayPass";
    public static final String GET_BANNER_URL= FXM_URL_PORT+"slideShowAction_getSlide";
    public static final String GET_POSTAGE_OF_SOME_DISTRICT = FXM_URL_PORT+"commodityAction_freight";
    public static final String GET_COUNT_OF_STORE_ORDER = FXM_URL_PORT+"orderAction_getSToatal";
    public static final String DELETE_THE_RECOMMEND_STORE = FXM_URL_PORT+"recommendAction_delStore";
    public static final String GET_VIP_PRICE_URL = FXM_URL_PORT+"commodityAction_discount";


    public static final String GET_INCOME_URL = FXM_URL_PORT+"financialDetailsAction_income";
    public static final String GET_MY_VIP_COUNT_URL = FXM_URL_PORT+"userSubAction_userCount";
    public static final String GET_PRODUCT_CS_CLASSIFY = FXM_URL_PORT+"skuAction_getSku";
    public static final String GET_MY_STORE_ORDER_COUNT = FXM_URL_PORT+"orderAction_getSCount";
    public static final String GET_MY_STORE_REFUND_COUNT = FXM_URL_PORT+"refundOrderAction_refundSum";
    public static final String SEARCH_ONE_STORE_PRODUCT = FXM_URL_PORT+"commodityAction_fuzzStore";
    public static final String CHANGE_TAKE_ADDRESS_OF_ORDER = FXM_URL_PORT+"orderAction_upSAupSA";
    public static final String GET_PRODUCTS_OF_SUBCALSSIFY = FXM_URL_PORT+"commodityAction_subCategory";
    public static final String GET_SHOPPINGCAR_COUNT = FXM_URL_PORT+"shoppingCartAction_getCartCount";
    public static final String GET_PRODUCT_DETIAL_BY_ID = FXM_URL_PORT+"commodityAction_detail";
    public static final String UPLOAD_STORE_BANANER_URL = FXM_URL_PORT+"storeAction_banana";
    public static final String GET_REFUND_DETIAL_URL = FXM_URL_PORT+"refundOrderAction_detail";

    public static final String GET_HOME_RECOMMEND_URL = FXM_URL_PORT+"advertisingAction_Advertising";
    public static final String GET_HOME_MainProducts_URL = FXM_URL_PORT+"commodityAction_search";
    public static final String GET_STORE_DETIAL_BY_ID = FXM_URL_PORT+"storeAction_detail";
    public static final String STORE_DELETE_PRODUCTS_URL = FXM_URL_PORT+"commodityAction_delete";

    public static final String GET_ALL_UNREAD_MSG_COUNT = FXM_URL_PORT+"messageAction_count";
    public static final String QUERY_LOGISTICS_URL = FXM_URL_PORT+"expressCodeAction_info";
    public static final String CHANGE_REFUND_MSG_URL = FXM_URL_PORT+"refundOrderAction_updateRefund";
    public static final String NOTIFY_STORER_DELIVER_URL = FXM_URL_PORT+"orderAction_promt";
    public static final String NOTIFY_BUYER_DELIVER_URL = FXM_URL_PORT+"orderAction_buyPromt";
    public static final String CHANGE_MSG_READ_STATE = FXM_URL_PORT+"messageAction_status";
    public static final String GET_CENTER_MSG_URL = FXM_URL_PORT+"messageAction_page";
    public static final String GET_MY_SUB_VIP_DATA_URL = FXM_URL_PORT+"userAction_sub";
    public static final String GET_MY_SUB_AND_SUB_VIP_DATA_URL = FXM_URL_PORT+"userAction_subu";
    public static final String GET_MY_SUPER_VIP_DATA_URL = FXM_URL_PORT+"userAction_up";

    public static final String NOTIFY_TAKE_GOODS_FOR_UNDELIVER = FXM_URL_PORT+"orderAction_delivery";
    public static final String BUY_PRODUCT_PLACE_URL = FXM_URL_PORT+"storeAction_buyUpper";
    public static final String DELETE_NOTIFY_MSG_URL = FXM_URL_PORT+"messageAction_delete";
    public static final String GET_HOME_PAGE_ALL_NEWGOODS = FXM_URL_PORT+"commodityAction_news";
    public static final String GET_PRODUCT_PLACE_PRICE_URL = FXM_URL_PORT+"systemAction_showPrice";

    public static final String GET_STORE_SUBCLASSIFY_PRODUCTS = FXM_URL_PORT+"commodityAction_subCategory";
    public static final String RECHARGE_YEWUYUAN_CASHPLEDGE = FXM_URL_PORT+"userAction_cashPledge";
    public static final String TIXIAN_URL = FXM_URL_PORT+"withdrawalsAction_cash";
    public static final String VERSION_UPDATE_URL = FXM_URL_PORT+"editionAction_renew";
    public static final String TIQU_YAJIN_URL = FXM_URL_PORT+"userAction_extract";







}
