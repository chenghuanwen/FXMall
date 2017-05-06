package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.model.FXMallModel;

public class LoadingActivity extends BaseActivity {

    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        FXMallModel.getVipRate();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              jumpTo(HomePageActivity.class,true);
            }
        },2000);
    }

    @Override
    public View getContentView() {
        return null;
    }
}
