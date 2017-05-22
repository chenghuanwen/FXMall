package com.dgkj.fxmall.view;

import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.HomePageFragmentAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.Permission;
import com.dgkj.fxmall.fragment.ProductsMallFragment;
import com.dgkj.fxmall.fragment.HomePageFragment;
import com.dgkj.fxmall.fragment.JoinUsFragment;
import com.dgkj.fxmall.fragment.DemandMallFragment;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.PermissionUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;


public class HomePageActivity extends BaseActivity {

    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_search)
    FrameLayout tvSearch;
    @BindView(R.id.tv_car_count)
    TextView tvCarCount;
    @BindView(R.id.fl_car)
    FrameLayout flCar;
    @BindView(R.id.tv_msg_count)
    TextView tvMsgCount;
    @BindView(R.id.fl_msg)
    FrameLayout flMsg;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.content_home_page)
    LinearLayout contentHomePage;
    @BindView(R.id.rb_shangpu)
    AppCompatRadioButton rbShangpu;
    @BindView(R.id.rb_yewuyuan)
    AppCompatRadioButton rbYewuyuan;
    @BindView(R.id.rb_mine)
    AppCompatRadioButton rbMine;
    @BindView(R.id.vp_fragment)
    ViewPager vpFragment;
    @BindView(R.id.rg_footer)
    RadioGroup rgFooter;

    private ArrayList<Fragment> fragments;
    private static final int FX_REQUEST_PERMISSION_CODE = 100;
    private LocationClient mLocationClient;
    private BDLocationListener mListener = new MyLocationListener();
    private OkHttpClient okHttpClient;
    private ClipboardManager clipboardManager;
    private String from="sp";
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        from = getIntent().getStringExtra("from");

        initViewPager();
        initTabLayout();
        initFooter();

        //注册百度定位监听
        mLocationClient = new LocationClient(MyApplication.getContext());
        initLocation();
        mLocationClient.registerLocationListener(mListener);
        checkLocationPermission();

        okHttpClient = new OkHttpClient.Builder().build();

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        if("sp".equals(from) || "yw".equals(from)){
            vpFragment.setCurrentItem(3);
        }

        if(MyApplication.shoppingCount == 0){
            tvCarCount.setVisibility(View.GONE);
        }else {
            tvCarCount.setVisibility(View.VISIBLE);
            tvCarCount.setText(MyApplication.shoppingCount+"");
        }

        if(MyApplication.msgCount == 0){
            tvMsgCount.setVisibility(View.GONE);
        }else {
            tvMsgCount.setVisibility(View.VISIBLE);
            tvMsgCount.setText(MyApplication.msgCount+"");
        }
    }

    @Override
    public View getContentView() {
        return contentHomePage ;
    }


    private void initFooter() {
        rgFooter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_shangpu:
                       toMain("sp");
                        rbShangpu.setChecked(false);
                        break;
                    case R.id.rb_yewuyuan:
                        toMain("yw");
                        rbYewuyuan.setChecked(false);
                        break;
                    case R.id.rb_mine:
                        toMain("mine");
                        rbMine.setChecked(false);
                        break;
                }
            }
        });
    }

    private void toMain(String from){
        Intent intent = new Intent(HomePageActivity.this,MainActivity.class);
        intent.putExtra("from",from);
        jumpTo(intent,false);
    }

    /**
     * 初始化导航栏各子项界面
     */
    private void initViewPager() {
        fragments = new ArrayList<>();
        fragments.add(new HomePageFragment());
        fragments.add(new ProductsMallFragment());
        fragments.add(new DemandMallFragment());
        fragments.add(new JoinUsFragment(from));

        vpFragment.setAdapter(new HomePageFragmentAdapter(getSupportFragmentManager(), fragments));
        vpFragment.setCurrentItem(0);
        vpFragment.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }


    /**
     * 初始化导航栏
     */
    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("首页"), true);
        tabLayout.addTab(tabLayout.newTab().setText("产品大厅"));
        tabLayout.addTab(tabLayout.newTab().setText("需求大厅"));
        tabLayout.addTab(tabLayout.newTab().setText("加入我们"));
        tabLayout.setupWithViewPager(vpFragment, true);//调用此方法后，标题会被清空，需重新添加
        tabLayout.getTabAt(0).setText("首页");
        tabLayout.getTabAt(1).setText("产品大厅");
        tabLayout.getTabAt(2).setText("需求大厅");
        tabLayout.getTabAt(3).setText("加入我们");
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpFragment.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void checkLocationPermission() {
        if (!PermissionUtil.isOverMarshmallow()) {
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


            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                // GPS定位结果

                sb.append("addr : ");
                sb.append(location.getAddrStr());    //获取地址信息
                final String city = location.getAddress().city;
                MyApplication.currentProvince = location.getProvince();
                MyApplication.currentCity = city;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCity.setText(city);
                    }
                });

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                // 网络定位结果
                sb.append("addr : ");
                sb.append(location.getAddrStr());    //获取地址信息
                MyApplication.currentProvince = location.getProvince();
                final String city = location.getAddress().city;
                MyApplication.currentCity = city;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCity.setText(city);
                    }
                });

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("describe : ");
                sb.append("离线定位成功，离线定位结果也是有效的" + location.getAddrStr());
                MyApplication.currentProvince = location.getProvince();
                final String city = location.getAddress().city;
                MyApplication.currentCity = city;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCity.setText(city);
                    }
                });
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


    @OnClick(R.id.tv_search)
    public void search() {
        Intent intent = new Intent(this, SearchActivity.class);
        //TODO 需将搜索参数传递下去
        jumpTo(intent, false);
    }

    @OnClick(R.id.fl_car)
    public void shoppingCar() {
        Intent intent = new Intent(this, ShoppingCarActivity.class);
        //TODO
        jumpTo(intent, false);
    }

    @OnClick(R.id.fl_msg)
    public void msgNotify() {
        Intent intent = new Intent(this, MessageCenterActivity.class);
        //TODO
        jumpTo(intent, false);
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
            PermissionUtil.requestPermissions(HomePageActivity.this, FX_REQUEST_PERMISSION_CODE, needPermissionRequest);
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

            Toast.makeText(HomePageActivity.this, strMessage, Toast.LENGTH_SHORT).show();
        } else {
            mLocationClient.start();
        }
    }

    /**
     * 连按两次返回键退出程序
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();// 更新mExitTime
            } else {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);// 否则退出程序
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
