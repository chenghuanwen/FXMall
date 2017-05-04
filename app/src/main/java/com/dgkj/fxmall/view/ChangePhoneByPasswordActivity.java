package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.view.myView.PasswordInputView;

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

public class ChangePhoneByPasswordActivity extends BaseActivity {
    @BindView(R.id.content_change_phone_by_password)
    LinearLayout contentChangePhoneByPassword;
    private View headerview;

    @BindView(R.id.piv_pay_pass)
    PasswordInputView pivPayPass;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    private OkHttpClient client = new OkHttpClient.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone_by_password);
        ButterKnife.bind(this);
        initHeaderView();
    }

    @Override
    public View getContentView() {
        return contentChangePhoneByPassword;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "更换手机");
    }

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        String password = pivPayPass.getEditContent();
        if (TextUtils.isEmpty(password)) {
            toast("请输入支付密码");
            return;
        }
        FormBody body = new FormBody.Builder()
                .add("token", sp.get("token"))
                .add("payPassword", password)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.CHECK_PAY_PASSWORD)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if (string.contains("1000")) {
                    jumpTo(BindNewphoneActivity.class, true);
                } else if (string.contains("1003")) {
                    toastInUI(ChangePhoneByPasswordActivity.this, "密码错误！");
                }
            }
        });
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
