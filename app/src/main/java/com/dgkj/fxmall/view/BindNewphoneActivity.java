package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindNewphoneActivity extends BaseActivity {

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
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.activity_bind_newphone)
    LinearLayout activityBindNewphone;

    private View headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_newphone);
        ButterKnife.bind(this);
        initHeaderView();
    }

    @Override
    public View getContentView() {
        return activityBindNewphone;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "更换手机");
    }

    @OnClick(R.id.btn_cancel)
    public void cancel() {
        finish();
    }

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        String phone = etNewPhone.getText().toString();
        String code = etCheckCode.getText().toString();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(code)) {
            toast("请输入手机号和验证码！");
            return;
        }
        Intent intent = new Intent(this, UserMsgActivity.class);
        intent.putExtra("from", "newphone");
        intent.putExtra("phone", phone);
        jumpTo(intent, true);
    }
}
