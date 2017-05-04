package com.dgkj.fxmall.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.LogUtil;
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

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_new_phone)
    EditText etNewPhone;
    @BindView(R.id.et_check_code)
    EditText etCheckCode;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.tv_regist)
    TextView tvRegist;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.activity_login)
    LinearLayout activityLogin;
    private View headerview;
    private OkHttpClient client;
    private AlertDialog pw;
    private SharedPreferencesUnit sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initHeaderView();

        client = new OkHttpClient.Builder().build();
        sp = SharedPreferencesUnit.getInstance(this);
    }

    @Override
    public View getContentView() {
        return activityLogin;
    }


    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "登录");
    }


    @OnClick(R.id.tv_regist)
    public void regist() {
        //先检测是否需要弹出邀请码弹窗
        Request request = new Request.Builder().url(FXConst.GET_INVITECODE_CONFIG).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(LoginActivity.this, "网络错误，请稍后重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if (string.contains("1000") && string.contains("true")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showInviteDialog();
                        }
                    });
                } else if (string.contains("1000") && string.contains("false")) {
                    Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                    // intent.putExtra("id",userId);
                    startActivity(intent);
                }

            }
        });

    }

    @OnClick(R.id.tv_forget_password)
    public void forgetPassword() {
        startActivity(new Intent(this, ForgetPasswordActivity.class));
    }

    @OnClick(R.id.btn_login)
    public void login() {
        //TODO 登录
        String urer = etNewPhone.getText().toString();
        String pass = etCheckCode.getText().toString();
        if (TextUtils.isEmpty(urer) || TextUtils.isEmpty(pass)) {
            toast("请输入正确的用户名和密码！");
            return;
        }
        MyApplication.isLogin = true;
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("from", "mine");
        jumpTo(intent, true);
      /*  FormBody body = new FormBody.Builder()
                .add("phone", urer)
                .add("password", pass)
                .build();
        Request request = new Request.Builder()
                .url(FXConst.USER_LOGIN_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(LoginActivity.this, "网络异常，请稍后重试！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                if (result.contains("1000")) {
                    toastInUI(LoginActivity.this, "登录成功");
                    try {
                        JSONObject object = new JSONObject(result);
                        String token = object.getString("token");
                        sp.put("token", token);
                        sp.put("login","true");
                        LogUtil.i("TAG", "token====" + sp.get("token"));
                        MyApplication.isLogin = true;
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("from", "mine");
                        jumpTo(intent, true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (result.contains("109")) {
                    toastInUI(LoginActivity.this, "用户名或密码错误，请重新输入");
                }
            }
        });*/
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }

    private void showInviteDialog() {
        View contentview = getLayoutInflater().inflate(R.layout.layout_invite_code_dialog, null);
        pw = new AlertDialog.Builder(LoginActivity.this).create();
        pw.setView(contentview);
        final EditText etCode = (EditText) contentview.findViewById(R.id.et_invite_code);
        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_confirm);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = etCode.getText().toString();
                //TODO 判断邀请码的格式是否正确
                checkInviteCode(code);
            }
        });
        TextView tvBoy = (TextView) contentview.findViewById(R.id.tv_cancle);
        tvBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });


        //设置触摸对话框以外区域，对话框消失
        pw.setCanceledOnTouchOutside(false);
        pw.show();
    }

    private void checkInviteCode(String code) {
        FormBody body = new FormBody.Builder()
                .add("invitationCode", code)
                .build();
        Request request = new Request.Builder()
                .url(FXConst.CHECK_INVITE_CODE)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(LoginActivity.this, "网络错误，请稍后重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject object = new JSONObject(result);
                    int code = object.getInt("code");
                    if (code == 1000) {
                        String userId = object.getString("user");
                        Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                        intent.putExtra("id", userId);
                        startActivity(intent);
                        pw.dismiss();
                    } else {
                        toastInUI(LoginActivity.this, "邀请码输入错误，请重新输入！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
