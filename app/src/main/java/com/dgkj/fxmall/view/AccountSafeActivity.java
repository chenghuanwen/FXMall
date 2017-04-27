package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AccountSafeActivity extends BaseActivity {
    @BindView(R.id.tv_change_login_pass)
    TextView tvChangeLoginPass;
    @BindView(R.id.tv_rest_pay_pass)
    TextView tvRestPayPass;
    @BindView(R.id.activity_account_safe)
    LinearLayout activityAccountSafe;
    private View headerview;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private boolean isRestPayword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safe);
        ButterKnife.bind(this);
        initHeaderView();
        whetherHasSetPayWord();
    }

    @Override
    public View getContentView() {
        return activityAccountSafe;
    }

    private void whetherHasSetPayWord() {
        FormBody body = new FormBody.Builder()
                .add("token", sp.get("token"))
                .build();
        Request request = new Request.Builder()
                .url(FXConst.WHETHER_IS_FIRST_SET_PAYWORD)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    isRestPayword = true;
                } else if (result.contains("109")) {
                    isRestPayword = false;
                }
            }
        });
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "账户安全");
    }

    @OnClick(R.id.tv_change_login_pass)
    public void resetLoginPass() {
        jumpTo(SetPasswordActivity.class, false);
    }

    @OnClick(R.id.tv_rest_pay_pass)
    public void restPayPass() {
        if (isRestPayword) {
            jumpTo(ResetPayPasswordActivity.class, false);
        } else {
            jumpTo(SetPayPasswordActivity.class, false);
        }
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
