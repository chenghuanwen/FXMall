package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingPasswordActivity extends BaseActivity {

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
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private View headerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_password);
        ButterKnife.bind(this);

        initHeaderView();
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview,"设置密码");
    }

    @OnClick(R.id.ib_back)
    public void back(){
        finish();
    }

    @OnClick(R.id.iv_clean)
    public void clean(){
        etOldPassword.setText("");
    }

    @OnClick(R.id.btn_finish)
    public void confirm(){
        String old = etOldPassword.getText().toString();
        String newp = etNewPassword.getText().toString();
        String twice = etConfirmPassword.getText().toString();
        if(TextUtils.isEmpty(old) || TextUtils.isEmpty(newp) || TextUtils.isEmpty(twice)){
            toast("输入不能为空！！");
            return;
        }
        //TODO 设置密码接口
    }
}
