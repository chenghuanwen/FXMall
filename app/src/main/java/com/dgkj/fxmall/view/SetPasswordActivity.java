package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;

import org.json.JSONException;
import org.json.JSONObject;

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

public class SetPasswordActivity extends BaseActivity {


    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.et_old_password)
    EditText etOldPassword;
    @BindView(R.id.iv_clean)
    ImageView ivClean;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.btn_finish)
    Button btnFinish;

    private View headerview;
    private OkHttpClient client;
    private SharedPreferencesUnit sp ;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_password);
        ButterKnife.bind(this);
        initHeaderView();

        client = new OkHttpClient.Builder().build();
        sp = SharedPreferencesUnit.getInstance(this);
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "忘记密码");

    }

    @OnClick(R.id.btn_finish)
    public void submit() {
        String old = etOldPassword.getText().toString();
        String pass = etNewPassword.getText().toString();
        String check = etConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(pass) || TextUtils.isEmpty(check)) {
            return;
        }
        if (!pass.equals(check)) {
            Toast.makeText(this, "两次输入密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }

        FormBody body = new FormBody.Builder()
                .add("token",sp.get("token"))
                .add("password",old)
                .add("newPassword",check)
                .build();
        Request request = new Request.Builder()
                .url(FXConst.RESET_LOGIN_PASSWORD)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(SetPasswordActivity.this,"网络异常！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(result.contains("1000")){
                    try {
                        JSONObject object = new JSONObject(result);
                        String token = object.getString("token");
                        sp.put("token",token);
                        toastInUI(SetPasswordActivity.this,"重置密码成功，即将去登录！");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                jumpTo(LoginActivity.class,true);
                            }
                        },2000);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(result.contains("1003")){
                    toastInUI(SetPasswordActivity.this,"旧密码输入错误，重置失败！");
                }
            }
        });

    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
