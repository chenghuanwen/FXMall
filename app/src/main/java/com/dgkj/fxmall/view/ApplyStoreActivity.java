package com.dgkj.fxmall.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.ViewFlipperAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.constans.Permission;
import com.dgkj.fxmall.utils.DataCleanManager;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.OkhttpUploadUtils;
import com.dgkj.fxmall.utils.PermissionUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class ApplyStoreActivity extends BaseActivity {
    @BindView(R.id.avf_apply_store)
    AdapterViewFlipper avfApplyStore;
    @BindView(R.id.tv_add_store_photos)
    TextView tvAddStorePhotos;
    @BindView(R.id.et_apply_store_name)
    EditText etApplyStoreName;
    @BindView(R.id.ll_store_classify)
    LinearLayout llDemandClassify;
    @BindView(R.id.et_current_address)
    EditText etCurrentAddress;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.et_detial_adress)
    EditText etDetialAdress;
    @BindView(R.id.et_store_user_name)
    EditText etStoreUserName;
    @BindView(R.id.et_store_user_phone)
    EditText etStoreUserPhone;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.iv_evidence)
    ImageView ivUploadEvidence;
    @BindView(R.id.tv_classify)
    TextView tvClassify;
    @BindView(R.id.btn_submit_finish)
    Button btnSubmitFinish;
    private View headerview;
    private ArrayList<String> images;
    private String classify;
    private static final int FX_REQUEST_PERMISSION_CODE = 100;
    private LocationClient mLocationClient;
    private BDLocationListener mListener = new MyLocationListener();

    private static final int TM_REQUEST_PERMISSION_CODE = 110;
    private File file;
    private Handler handler = new Handler();
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private int classifyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_store);
        ButterKnife.bind(this);
        initHeaderView();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //注册百度定位监听
        mLocationClient = new LocationClient(MyApplication.getContext());
        initLocation();
        mLocationClient.registerLocationListener(mListener);
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "申请店铺");
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_add_store_photos)
    public void photosChooser() {
        startActivityForResult(new Intent(this, PictrueChooserActivity.class), 117);
    }

    @OnClick(R.id.ll_store_classify)
    public void selectClassify() {
        startActivityForResult(new Intent(this, ApplyStoreAllClassifyActivity.class), 118);
    }

    @OnClick(R.id.tv_location)
    public void location() {
        checkLocationPermission();
    }


    @OnClick(R.id.iv_evidence)
    public void uploadEvidence() {
        selectPhoto();
    }

    @OnClick(R.id.btn_submit_finish)
    public void submit(){
        //TODO 上传申请资料
        String storeName = etApplyStoreName.getText().toString();
        String userName = etStoreUserName.getText().toString();
        String phone = etStoreUserPhone.getText().toString();
        String address = etCurrentAddress.getText().toString() + etDetialAdress.getText().toString();
        if(TextUtils.isEmpty(storeName) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)){
            toast("请填写完整信息");
            return;
        }

        if(classifyId < 0){
            toast("您还未选择店铺分类");
            return;
        }

        Map<String,String> params = new HashMap<>();
        params.put("user.token",sp.get("token"));
        params.put("storeName",storeName);
        params.put("address",address);
        params.put("storekeeper",userName);
        params.put("phone",phone);
        params.put("category.id",classifyId+"");

        if(images.size()==0){
            toast("请先添加店铺图片");
            return;
        }

        if(file==null || !file.exists()){
            toast("请添加营业执照相片");
            return;
        }

        List<File> imageList = new ArrayList<>();
        for (String image : images) {
            File file = new File(image);
            imageList.add(file);
        }

        List<File> licenses = new ArrayList<>();
        licenses.add(file);

       OkhttpUploadUtils.getInstance(this).sendMultipart(FXConst.APPLY_STORE,params,"file",imageList,"url",licenses);

       /* btnSubmitFinish.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DataCleanManager.clearAllCache(ApplyStoreActivity.this);
                btnSubmitFinish.setVisibility(View.INVISIBLE);
            }
        }, 3000);*/
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
                final String city = address.province + address.city + address.district;
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
                final String city = address.province + address.city + address.district;
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
            PermissionUtil.requestPermissions(ApplyStoreActivity.this, FX_REQUEST_PERMISSION_CODE, needPermissionRequest);
        }
    }


    /**
     * 上传凭证
     */
    private void selectPhoto() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final View iconSelect = getLayoutInflater().inflate(R.layout.layout_iconselect_dialog, null);
        TextView tvPicture = (TextView) iconSelect.findViewById(R.id.tv_icon_fromPictures);
        TextView tvCamera = (TextView) iconSelect.findViewById(R.id.tv_icon_fromCarema);
        TextView tvCancle = (TextView) iconSelect.findViewById(R.id.tv_icon_cancle);

        alertDialog.setView(iconSelect);

        tvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//从相册选择
                if (PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(ApplyStoreActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(ApplyStoreActivity.this, "请打开允许访问SD权限", Toast.LENGTH_SHORT).show();
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    PermissionUtil.requestPermissions(ApplyStoreActivity.this, TM_REQUEST_PERMISSION_CODE, permissions);
                    return;
                }
                selectPicture();
                alertDialog.dismiss();
            }
        });
        tvCamera.setOnClickListener(new View.OnClickListener() {//从拍照获取
            @Override
            public void onClick(View view) {
                if (PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(ApplyStoreActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(ApplyStoreActivity.this, "请打开允许访问SD权限", Toast.LENGTH_SHORT).show();
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    PermissionUtil.requestPermissions(ApplyStoreActivity.this, TM_REQUEST_PERMISSION_CODE, permissions);
                    return;
                }

                if (PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(ApplyStoreActivity.this, Manifest.permission.CAMERA)) {
                    Toast.makeText(ApplyStoreActivity.this, "请打开允许访问相机权限", Toast.LENGTH_SHORT).show();
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.CAMERA);
                    PermissionUtil.requestPermissions(ApplyStoreActivity.this, TM_REQUEST_PERMISSION_CODE, permissions);
                    return;
                }

               takePicture();
                alertDialog.dismiss();
            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);//设置对话框显示在屏幕下方
        alertDialog.show();
    }



    public void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //将拍照的图片保存到本地
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
        LogUtil.i("TAG","准备拍照==========");
        Uri iconUri = null;
        if(Build.VERSION.SDK_INT >= 24){
            iconUri = FileProvider.getUriForFile(this,"com.dgkj.fxmall.fileprovider",file);
            LogUtil.i("TAG","7.0uri===="+iconUri);
        }else {
            iconUri = Uri.fromFile(file);
        }
        LogUtil.i("TAG","准备拍照URI==========");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, iconUri);
        startActivityForResult(intent, 102);
    }

    public void selectPicture(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 101);
    }

    /**
     *  7.0 获取图片文件uri
     * @param context
     * @param file
     * @return
     */
    private Uri getImageContentUri(Context context, File file) {
        String filePath = file.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},MediaStore.Images.Media.DATA+"=?",new String[]{filePath},null);
        if(cursor!=null && cursor.moveToFirst()){
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri,""+id);
        }else {
            if(file.exists()){
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DATA,filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
            }
        }
        return null;
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

                if(permissions[i].equals(Manifest.permission.CAMERA) && grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    takePicture();
                }
                if(permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    if(permissions[i].equals(Manifest.permission.CAMERA) && grantResults[i]==PackageManager.PERMISSION_GRANTED){
                        takePicture();
                    }
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

            Toast.makeText(ApplyStoreActivity.this, strMessage, Toast.LENGTH_SHORT).show();
        } else {
            mLocationClient.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 117 && resultCode == 121) {
            images = data.getStringArrayListExtra("images");
            tvAddStorePhotos.setVisibility(View.GONE);
            avfApplyStore.setAdapter(new ViewFlipperAdapter(images, this));
            avfApplyStore.setAutoStart(true);
            avfApplyStore.startFlipping();
        } else if (requestCode == 118 && resultCode == 152) {
            classifyId = data.getIntExtra("id",-1);
            classify = data.getStringExtra("classify");
            tvClassify.setText(classify);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (resultCode == RESULT_OK && requestCode == 101) {//从相册获取
            Uri uri = data.getData();

            //根据uri查找图片地址
            ContentResolver resolver = getContentResolver();
            String[] pojo = {MediaStore.Images.Media.DATA};
            Cursor cursor = resolver.query(uri, pojo, null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                cursor.moveToFirst();
                String path = cursor.getString(columnIndex);
                file = new File(path);
                cursor.close();
            }
            Glide.with(this).load(uri).into(ivUploadEvidence);

        } else if (resultCode == RESULT_OK && requestCode == 102) {//拍照
            Uri uri1 = null;
            if(Build.VERSION.SDK_INT >= 24){
                uri1 = getImageContentUri(this,file);
                LogUtil.i("TAG","7.0uri1111111111===="+uri1);
            }else {
                uri1 = Uri.fromFile(file);
            }
            Glide.with(this).load(uri1).into(ivUploadEvidence);

        }



    }


}
