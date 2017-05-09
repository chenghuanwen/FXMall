package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.ToastUtil;

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

public class SettingPasswordActivity extends BaseActivity {


    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_check_password)
    EditText etCheckPassword;
    @BindView(R.id.btn_regist)
    Button btnRegist;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    private View headerview;
    private String phone,code;
    private OkHttpClient client = new OkHttpClient.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_regist_password);
        ButterKnife.bind(this);

        initHeaderView();
        phone = getIntent().getStringExtra("phone");
        code = getIntent().getStringExtra("code");
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "设置密码");
        btnRegist.setText("确认");
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }


    @OnClick(R.id.btn_regist)
    public void confirm() {
        final String newp = etNewPassword.getText().toString();
        String twice = etCheckPassword.getText().toString();
        if (TextUtils.isEmpty(newp) || TextUtils.isEmpty(twice)) {
            ToastUtil.show(this,this,"输入不能为空！！");
            return;
        }
        if(!newp.equals(twice)){
            ToastUtil.show(this,this,"两次密码输入不一致！");
            etCheckPassword.setText("");
            return;
        }
        //TODO 设置密码接口
        FormBody body = new FormBody.Builder()
                .add("password",twice)
                .add("phone",phone)
                .add("code",code)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.USER_FORGOT_PASSWORD)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                    toastInUI(SettingPasswordActivity.this,"密码重置成功，返回登录");
                }else {
                    toastInUI(SettingPasswordActivity.this,"密码重置失败，请稍后重试");
                }
            }
        });
    }
}
