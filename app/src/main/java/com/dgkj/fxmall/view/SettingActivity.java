package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.DataCleanManager;
import com.dgkj.fxmall.utils.UpdateManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    @BindView(R.id.civ_set)
    CircleImageView civSet;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.activity_setting)
    LinearLayout activitySetting;
    @BindView(R.id.tv_invite_code)
    TextView tvInviteCode;
    @BindView(R.id.tv_product_place)
    TextView tvProductPlace;

    private View headerview;
    private String icon = "", nick = "", inviteCode = "", gender = "", realname = "", phone = "", location = "";
    private Handler handler = new Handler();
    private UpdateManager mUpdateManager;
    private OkHttpClient client  = new OkHttpClient.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initHeaderView();
        setData();
        getCacheSize();
    }

    private void setData() {
        Intent intent = getIntent();
        icon = intent.getStringExtra("icon");
        nick = intent.getStringExtra("nick");
        inviteCode = intent.getStringExtra("code");
        realname = intent.getStringExtra("realname");
        phone = intent.getStringExtra("phone");
        location = intent.getStringExtra("address");
        gender = intent.getStringExtra("gender");
        Glide.with(this).load(icon).error(R.mipmap.sz_tx).into(civSet);
        tvInviteCode.setText(inviteCode);
        try {
            String nickname = URLDecoder.decode(nick, "utf-8");
            tvUsername.setText(nickname);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View getContentView() {
        return null;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "设置");
    }


    @OnClick(R.id.tv_user_msg)
    public void userMsg() {
        Intent intent = new Intent(this, UserMsgActivity.class);
        intent.putExtra("icon", icon);
        intent.putExtra("nick", nick);
        intent.putExtra("code", inviteCode);
        intent.putExtra("gender", gender);
        intent.putExtra("realname", realname);
        intent.putExtra("phone", phone);
        intent.putExtra("address", location);
        jumpTo(intent, false);
    }


    @OnClick(R.id.tv_change_password)
    public void changePassword() {
        jumpTo(AccountSafeActivity.class, false);
    }


    @OnClick(R.id.tv_about_us)
    public void aboutus() {
        jumpTo(AboutUsActivity.class, false);
    }

    @OnClick(R.id.tv_advice_feedback)
    public void feedback() {
        jumpTo(AdviceFeedbackActivity.class, false);
    }

    @OnClick(R.id.tv_check_update)
    public void updateVersion() {

        mUpdateManager = new UpdateManager(this);
        mUpdateManager.checkUpdateInfo();


        FormBody body = new FormBody.Builder()
                .add("type","1")
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.VERSION_UPDATE_URL)
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
                        JSONObject data = object.getJSONObject("data");
                        String url = data.getString("url");
                        String version = data.getString("version");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



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


    @OnClick(R.id.tv_product_place)
    public void buyPlace(){
        jumpTo(ConfirmBuyProductPlaceActivity.class,false);
    }


    @OnClick(R.id.btn_logout)
    public void logout() {
        sp.put("login", "false");
        sp.put("token","");
        jumpTo(HomePageActivity.class, true);
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }

}
