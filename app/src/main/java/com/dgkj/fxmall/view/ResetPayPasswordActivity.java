package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.listener.InputCompletetListener;
import com.dgkj.fxmall.view.myView.PasswordInputView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPayPasswordActivity extends BaseActivity implements InputCompletetListener{
    @BindView(R.id.piv_old_pass)
    PasswordInputView pivOldPass;
    @BindView(R.id.piv_set_new)
    PasswordInputView pivSetNew;
    @BindView(R.id.piv_confirm)
    PasswordInputView pivConfirm;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_save)
    Button btnSave;
    private View headerview;
    private String oldPass,newPass,confirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pay_password);
        ButterKnife.bind(this);
        initHeaderView();
        pivConfirm.setInputCompletetListener(this);
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "支付密码设置");

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
