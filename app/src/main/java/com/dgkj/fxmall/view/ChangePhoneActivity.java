package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class ChangePhoneActivity extends BaseActivity {

    @BindView(R.id.tv_bind_phone)
    TextView tvBindPhone;
    @BindView(R.id.et_check_code)
    EditText etCheckCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_change_by_paypass)
    TextView tvChangeByPaypass;
    @BindView(R.id.activity_change_phone)
    LinearLayout activityChangePhone;
    @BindView(R.id.tv_no_paypass)
    TextView tvNoPaypass;
    private View headerview;
    private String bindPhone = "";
    private OkHttpClient client = new OkHttpClient.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        ButterKnife.bind(this);
        initHeaderView();
        bindPhone = getIntent().getStringExtra("phone");
        tvBindPhone.setText("已绑定手机号:" + bindPhone);
    }

    @Override
    public View getContentView() {
        return activityChangePhone;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "更换手机");
    }


    @OnClick(R.id.tv_change_by_paypass)
    public void byPayPass() {
        jumpTo(ChangePhoneByPasswordActivity.class, false);
    }

    @OnClick(R.id.tv_get_code)
    public void getCode() {
        sendCheckCode(bindPhone);
    }

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        //TODO 检测验证码是否正确
        String code = etCheckCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            toast("请先输入验证码");
            return;
        }
        checkMsgCode(code);
    }

    private void checkMsgCode(String code) {
        FormBody body = new FormBody.Builder()
                .add("phone", bindPhone)
                .add("code", code)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.CHECK_MESSAGE_CODE)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(ChangePhoneActivity.this, "网络错误");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    jumpTo(BindNewphoneActivity.class, true);
                } else if (result.contains("109")) {
                    etCheckCode.setText("");
                    toastInUI(ChangePhoneActivity.this, "验证码错误，请重新输入！");
                } else if (result.contains("1005")) {
                    toastInUI(ChangePhoneActivity.this, "今日验证码获取次数已用完，请明天重试！");
                }
            }
        });
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }


    /**
     * 获取短信验证码
     *
     * @param phone
     */
    private void sendCheckCode(String phone) {
        FormBody body = new FormBody.Builder()
                .add("phone", phone)
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_MESSAGE_CHECK_CODE)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(ChangePhoneActivity.this, "网络错误，请稍后重试！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    toastInUI(ChangePhoneActivity.this, "验证码已发送至你的手机，请注意查收！");
                    tvGetCode.setClickable(false);
                } else if (result.contains("109")) {
                    toastInUI(ChangePhoneActivity.this, "验证码发送失败，请稍后重试！");
                    tvGetCode.setClickable(true);
                } else {
                    toastInUI(ChangePhoneActivity.this, "今日验证码获取次数已用完，请明天重试！");
                }
            }
        });
    }

    @OnClick(R.id.tv_no_paypass)
    public void setPayPassword(){
        jumpTo(SetPayPasswordActivity.class,false);
    }
}
