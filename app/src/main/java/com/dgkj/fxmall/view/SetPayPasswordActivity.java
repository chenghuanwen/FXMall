package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.listener.InputCompletetListener;
import com.dgkj.fxmall.utils.LogUtil;
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

public class SetPayPasswordActivity extends BaseActivity implements InputCompletetListener {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.piv_set)
    PasswordInputView pivSet;
    @BindView(R.id.piv_confirm)
    PasswordInputView pivConfirm;
    private View headerview;
    private String setContent,confirmContent;
    private OkHttpClient okHttpClient = new OkHttpClient.Builder().build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pay_password);
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


    @OnClick(R.id.btn_save)
    public void save(){
        FormBody body = new FormBody.Builder()
                .add("token",sp.get("token"))
                .add("newPassword",confirmContent)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.SET_PAYWORD_URL)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(SetPayPasswordActivity.this,"网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                    toastInUI(SetPayPasswordActivity.this,"支付密码设置成功！");
                }else {
                    toastInUI(SetPayPasswordActivity.this,"支付密码设置失败，请稍后重试！");
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
        setContent = pivSet.getEditContent();
        confirmContent = pivConfirm.getEditContent();
        LogUtil.i("TAG","两次输入密码=="+setContent+"==="+confirmContent);
        if(!setContent.equals(confirmContent)){
            Toast.makeText(SetPayPasswordActivity.this,"两次输入密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deleteContent(boolean isDelete) {

    }
}
