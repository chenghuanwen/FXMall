package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.utils.DataCleanManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.tv_user_msg)
    RelativeLayout tvUserMsg;
    @BindView(R.id.tv_change_password)
    TextView tvChangePassword;
    @BindView(R.id.tv_about_us)
    TextView tvAboutUs;
    @BindView(R.id.tv_advice_feedback)
    TextView tvAdviceFeedback;
    @BindView(R.id.tv_check_update)
    TextView tvCheckUpdate;
    @BindView(R.id.tv_cache_size)
    TextView tvCacheSize;
    @BindView(R.id.ll_clean_cache)
    RelativeLayout llCleanCache;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_clean_finish)
    Button tvCleanFinish;
    @BindView(R.id.tv_newst_version)
    Button tvNewstVersion;

    private View headerview;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initHeaderView();
        getCacheSize();
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "设置");
    }


    @OnClick(R.id.tv_user_msg)
    public void userMsg() {
        jumpTo(UserMsgActivity.class, false);
    }


    @OnClick(R.id.tv_change_password)
    public void changePassword() {
        jumpTo(AccountSafeActivity.class,false);
    }


    @OnClick(R.id.tv_about_us)
    public void aboutus() {
        jumpTo(AboutUsActivity.class,false);
    }

    @OnClick(R.id.tv_advice_feedback)
    public void feedback() {
        jumpTo(AdviceFeedbackActivity.class,false);
    }

    @OnClick(R.id.tv_check_update)
    public void updateVersion() {
        tvNewstVersion.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DataCleanManager.clearAllCache(SettingActivity.this);
                tvNewstVersion.setVisibility(View.INVISIBLE);
            }
        }, 3000);
    }

    @OnClick(R.id.ll_clean_cache)
    public void cleanCache() {
        tvCleanFinish.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DataCleanManager.clearAllCache(SettingActivity.this);
                tvCleanFinish.setVisibility(View.INVISIBLE);
                tvCacheSize.setText("0M");
            }
        }, 3000);
    }

    /**
     * 获取缓存大小
     */
    public void getCacheSize() {
        try {
            tvCacheSize.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }

}
