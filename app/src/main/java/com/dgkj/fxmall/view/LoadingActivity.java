package com.dgkj.fxmall.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.model.FXMallModel;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;

public class LoadingActivity extends BaseActivity {

    private Handler handler = new Handler();
    private SharedPreferencesUnit sp  = SharedPreferencesUnit.getInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(Color.parseColor("#1f7bbe"));
        }
        FXMallModel.getVipRate();
        String token = sp.get("token");
        if(!TextUtils.isEmpty(token)){
            FXMallModel.getShoppingCarCount(token);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              jumpTo(HomePageActivity.class,true);
            }
        },1000);
    }

    @Override
    public View getContentView() {
        return null;
    }
}
