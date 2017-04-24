package com.dgkj.fxmall.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.TakeGoodsAddressBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.constans.Permission;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.PermissionUtil;

import java.io.IOException;
import java.util.ArrayList;
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

public class AddGoodsAdressActivity extends BaseActivity {

    @BindView(R.id.et_takegoods_man)
    EditText etTakegoodsMan;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_current_address)
    EditText etCurrentAddress;
    @BindView(R.id.et_detial_adress)
    EditText etDetialAdress;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    @BindView(R.id.tv_location)
    TextView tvLocation;

    private View headerview;
    private String from = "";
    private TakeGoodsAddressBean addressBean;
    private static final int FX_REQUEST_PERMISSION_CODE = 100;
    private LocationClient mLocationClient;
    private BDLocationListener mListener = new MyLocationListener();
    private OkHttpClient client;
    private String url = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods_adress);
        ButterKnife.bind(this);
        initHeaderView();

        setData();

        //注册百度定位监听
        mLocationClient = new LocationClient(MyApplication.getContext());
        initLocation();
        mLocationClient.registerLocationListener(mListener);

        client = new OkHttpClient.Builder().build();
    }

    private void setData() {
        from = getIntent().getStringExtra("from");
        if("edit".equals(from)){
            addressBean = (TakeGoodsAddressBean) getIntent().getSerializableExtra("address");
            etTakegoodsMan.setText(addressBean.getName());
            etPhoneNumber.setText(addressBean.getPhone());
            String address = addressBean.getAddress();
            int index = address.indexOf("区");
            if(index<0){
                index = address.indexOf("县");
            }
            etCurrentAddress.setText(address.substring(0,index+1));
            etDetialAdress.setText(address.substring(index+1,address.length()));
        }
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview,"编辑收货地址");
    }


    @OnClick(R.id.iv_back)
    public void back(){
        finish();
    }

    @OnClick(R.id.tv_location)
    public void location(){
        checkLocationPermission();
    }

    @OnClick(R.id.btn_finish)
    public void confirm(){
        //TODO 将地址信息发送到服务器
        final String phone = etPhoneNumber.getText().toString();
        final String user = etTakegoodsMan.getText().toString();
        final String address = etCurrentAddress.getText().toString();
        final String detial = etDetialAdress.getText().toString();
        if(TextUtils.isEmpty(phone) || TextUtils.isEmpty(user) || TextUtils.isEmpty(address) || TextUtils.isEmpty(detial)){
            toast("请填写完整的收货信息！");
            return;
        }


        int indexOf = address.indexOf("省");
        if(indexOf<0){
            indexOf = address.indexOf("自治区");
        }
        String province = address.substring(0,indexOf+1);
        int index = address.indexOf("区");
        if(index<0){
            index = address.indexOf("县");
        }
        String city = address.substring(indexOf+1,index+1);
        String district = address.substring(index+1,address.length());

        FormBody body = null;

        if("edit".equals(from)){
            body = new FormBody.Builder()
                    .add("user.token",sp.get("token"))
                    .add("province",province)
                    .add("city",city)
                    .add("county",district)
                    .add("consignee",user)
                    .add("phone",phone)
                    .add("particular",detial)
                    .add("id",addressBean.getId()+"")
                    .build();
            url = FXConst.UPDATE_TAKE_ADDRESS;
        }else {
            body = new FormBody.Builder()
                    .add("user.token",sp.get("token"))
                    .add("province",province)
                    .add("city",city)
                    .add("county",district)
                    .add("consignee",user)
                    .add("phone",phone)
                    .add("particular",detial)
                    .build();
            url = FXConst.ADD_TAKE_ADDRESS;
        }



        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(AddGoodsAdressActivity.this,"网络异常！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(result.contains("1000")){
                    toastInUI(AddGoodsAdressActivity.this,"收货地址添加成功");
                    TakeGoodsAddressBean takeAddress = new TakeGoodsAddressBean();
                    takeAddress.setPhone(phone);
                    takeAddress.setName(user);
                    takeAddress.setAddress(address + detial);
                    Intent intent = new Intent();
                    intent.putExtra("address",takeAddress);
                    if("edit".equals(from)){
                        intent.putExtra("position",getIntent().getIntExtra("position",-1));
                    }
                    setResult(141,intent);
                    finish();
                }
            }
        });


    }


    private void checkLocationPermission() {
        if (!PermissionUtil.isOverMarshmallow()) {
            LogUtil.i("TAG", "6.0以下机型====");
            getCurrentLocation();
        } else if (PermissionUtil.isPermissionValid(this, Permission.PERMISSIONS_LOCATION)
                && PermissionUtil.isPermissionValid(this, Permission.PERMISSIONS_COARSE_LOCATION)
                && PermissionUtil.isPermissionValid(this, Permission.PERMISSIONS_WRITE_STORAGE)
                && PermissionUtil.isPermissionValid(this, Permission.PERMISSIONS_PHONE)) {
            getCurrentLocation();

        } else {
            requestPermissionsAndroidM();
        }
    }

    private void getCurrentLocation() {
        mLocationClient.start();

    }


    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        int span = 0;
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(span);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(false);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到(兴趣点搜索)
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);

        mLocationClient.setLocOption(option);
    }


    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("latitude : ");
            sb.append(location.getLatitude());    //获取纬度信息

            sb.append("lontitude : ");
            sb.append(location.getLongitude());    //获取经度信息


            Address address = location.getAddress();
            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                // GPS定位结果

                sb.append("addr : ");
                sb.append(location.getAddrStr());    //获取地址信息
                final String city = address.province+address.city+address.district;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        etCurrentAddress.setText(city);
                    }
                });

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                // 网络定位结果
                sb.append("addr : ");
                sb.append(location.getAddrStr());    //获取地址信息
                final String city = address.province+address.city+address.district;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        etCurrentAddress.setText(city);
                    }
                });



            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("describe : ");
                sb.append("离线定位成功，离线定位结果也是有效的" + location.getAddrStr());

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("describe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("describe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("describe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("locationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            LogUtil.i("BaiduLocationApiDem", sb.toString());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    /**
     * 申请6.0动态权限
     */
    private void requestPermissionsAndroidM() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> needPermissionRequest = new ArrayList<>();
            needPermissionRequest.add(Permission.PERMISSIONS_LOCATION);
            needPermissionRequest.add(Permission.PERMISSIONS_PHONE);
            needPermissionRequest.add(Permission.PERMISSIONS_WRITE_STORAGE);
            PermissionUtil.requestPermissions(AddGoodsAdressActivity.this, FX_REQUEST_PERMISSION_CODE, needPermissionRequest);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FX_REQUEST_PERMISSION_CODE) {
            permissionsResult(permissions, grantResults);

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 响应权限请求结果
     *
     * @param permissions  申请的权限集合
     * @param grantResults 权限授权结果集合
     */
    private void permissionsResult(String[] permissions, int[] grantResults) {
        List<String> needPermission = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    needPermission.add(permissions[i]);
                }
            }
        }

        if (needPermission.size() > 0) {
            StringBuffer permissionMsg = new StringBuffer();
            for (int i = 0; i < needPermission.size(); i++) {
                if (needPermission.get(i).equals(Permission.PERMISSIONS_LOCATION)) {
                    permissionMsg.append("," + "请求使用定位权限");
                } else if (needPermission.get(i).equals(Permission.PERMISSIONS_WRITE_STORAGE)) {
                    permissionMsg.append("," + "请求访问SD卡");
                } else if (needPermission.get(i).equals(Permission.PERMISSIONS_PHONE)) {
                    permissionMsg.append("," + "请求读取手机状态");
                }
            }
            String strMessage = "请允许使用\"" + permissionMsg.substring(1).toString() + "\"权限, 以正常使用APP的定位功能.";

            Toast.makeText(AddGoodsAdressActivity.this, strMessage, Toast.LENGTH_SHORT).show();
        } else {
            mLocationClient.start();
        }
    }



}
