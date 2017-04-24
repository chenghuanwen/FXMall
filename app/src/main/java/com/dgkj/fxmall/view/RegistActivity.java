package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class RegistActivity extends BaseActivity {
    @BindView(R.id.tv_country)
    TextView tvCountry;
    @BindView(R.id.tv_area_code)
    TextView tvAreaCode;
    @BindView(R.id.et_new_phone)
    EditText etNewPhone;
    @BindView(R.id.et_check_code)
    EditText etCheckCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    private View headerview;
    private OkHttpClient client;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        initHeaderView();
        client = new OkHttpClient.Builder().build();
        userId = getIntent().getIntExtra("userId",-1);
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "注册");
    }

    @OnClick(R.id.tv_get_code)
    public void getCode(){
        final String phone = etNewPhone.getText().toString();
        if(TextUtils.isEmpty(phone) || phone.length()<11){
            toast("请输入正确的手机号码！");
            return;
        }

        FormBody body = new FormBody.Builder()
                .add("phone",phone)
                .build();
        final Request request = new Request.Builder()
                .url(FXConst.CHECK_PHONE_IS_REGISTED)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(RegistActivity.this,"网络错误，请检查你的网络，稍后重试！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(result.contains("1000")){
                    sendCheckCode(phone);
                }else {
                    toastInUI(RegistActivity.this,"该手机号已经注册过，请直接登录！");
                }
            }
        });

    }

    /**
     * 获取短信验证码
     * @param phone
     */
    private void sendCheckCode(String phone) {
        FormBody body = new FormBody.Builder()
                .add("phone",phone)
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_MESSAGE_CHECK_CODE)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(RegistActivity.this,"网络错误，请稍后重试！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(result.contains("1000")){
                    toastInUI(RegistActivity.this,"验证码已发送至你的手机，请注意查收！");
                    tvGetCode.setClickable(false);
                }else if(result.contains("109")){
                    toastInUI(RegistActivity.this,"验证码发送失败，请稍后重试！");
                    tvGetCode.setClickable(true);
                }else {
                    toastInUI(RegistActivity.this,"今日验证码获取次数已用完，请明天重试！");
                }
            }
        });
    }

    @OnClick(R.id.btn_confirm)
    public void next(){
        //TODO 检测验证码是否正确
        String code = etCheckCode.getText().toString();
        if(TextUtils.isEmpty(code)){
            toast("请先输入验证码");
            return;
        }
        checkMsgCode(code);
    }

    private void checkMsgCode(String code) {
            FormBody body = new FormBody.Builder()
                    .add("phone",etNewPhone.getText().toString())
                    .add("code",code)
                    .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.CHECK_MESSAGE_CODE)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(RegistActivity.this,"网络错误");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(result.contains("1000")){
                    Intent intent = new Intent(RegistActivity.this,SetRegistPasswordActivity.class);
                    intent.putExtra("userId",userId);
                    intent.putExtra("phone",etNewPhone.getText().toString());
                    jumpTo(intent,true);
                }else if(result.contains("109")){
                    etCheckCode.setText("");
                    toastInUI(RegistActivity.this,"验证码错误，请重新输入！");
                }else if (result.contains("1005")){
                    toastInUI(RegistActivity.this,"今日验证码获取次数已用完，请明天重试！");
                }
            }
        });
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
