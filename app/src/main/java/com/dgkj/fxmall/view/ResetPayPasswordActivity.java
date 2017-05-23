package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.listener.InputCompletetListener;
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

public class ResetPayPasswordActivity extends BaseActivity implements InputCompletetListener{
    @BindView(R.id.piv_old_pass)
    PasswordInputView pivOldPass;
    @BindView(R.id.piv_set_new)
    PasswordInputView pivSetNew;
    @BindView(R.id.piv_confirm)
    PasswordInputView pivConfirm;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_submit)
    Button btnSave;
    private View headerview;
    private String oldPass,newPass,confirmPass;
    private OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pay_password);
        ButterKnife.bind(this);
        initHeaderView();
        pivConfirm.setInputCompletetListener(this);
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "支付密码设置");

    }


    @OnClick(R.id.btn_submit)
    public void save(){
        FormBody body = new FormBody.Builder()
                .add("token",sp.get("token"))
                .add("newPassword",confirmPass)
                .add("payPassword",oldPass)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.SET_PAYWORD_URL)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(ResetPayPasswordActivity.this,"网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                    toastInUI(ResetPayPasswordActivity.this,"支付密码重置成功！");
                    finish();
                }else {
                    toastInUI(ResetPayPasswordActivity.this,"支付密码重置失败，请稍后重试！");
                }
            }
        });
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }

    @Override
    public void inputComplete() {
        oldPass = pivOldPass.getEditContent();
        newPass = pivSetNew.getEditContent();
        confirmPass = pivConfirm.getEditContent();
        if(!newPass.equals(confirmPass)){
            Toast.makeText(ResetPayPasswordActivity.this,"两次输入密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }

    }

    @Override
    public void deleteContent(boolean isDelete) {

    }
}
