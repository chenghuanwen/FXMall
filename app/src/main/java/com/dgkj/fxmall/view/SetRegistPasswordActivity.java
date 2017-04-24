package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class SetRegistPasswordActivity extends BaseActivity {
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_check_password)
    EditText etCheckPassword;
   /* @BindView(R.id.btn_set_finish)
    Button btnSetFinish;*/
    @BindView(R.id.btn_regist)
    Button btnRegist;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    private View headerview;
    private OkHttpClient client;
    private int userId;
    private String phone="";
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_regist_password);
        ButterKnife.bind(this);
        initHeaderView();
        client = new OkHttpClient.Builder().build();
        userId = getIntent().getIntExtra("userId",-1);
        phone = getIntent().getStringExtra("phone");
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "注册");
    }


    @OnClick(R.id.btn_regist)
    public void regist(){
        String pass = etNewPassword.getText().toString();
        String check = etCheckPassword.getText().toString();
        if(TextUtils.isEmpty(pass) || TextUtils.isEmpty(check)){return;}
        if(!pass.equals(check)){
            Toast.makeText(this,"两次输入密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }

        FormBody body = new FormBody.Builder()
                .add("id",userId+"")
                .add("phone",phone)
                .add("password",check)
                .build();
        final Request request = new Request.Builder()
                .url(FXConst.USER_REGIST_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(SetRegistPasswordActivity.this,"网络异常，请稍后重试！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(result.contains("1000")){
                    toastInUI(SetRegistPasswordActivity.this,"恭喜您成功注册哎购会员，即将前往登录");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            jumpTo(LoginActivity.class,true);
                        }
                    },2000);
                }else if(result.contains("1002")){
                    toastInUI(SetRegistPasswordActivity.this,"用户名已存在！");
                }
            }
        });
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
