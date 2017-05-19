package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WriteAddressOfTakeBySelfActivity extends BaseActivity {
    @BindView(R.id.et_takegoods_man)
    EditText etTakegoodsMan;
    @BindView(R.id.et_takegoods_phone)
    EditText etTakegoodsPhone;
    @BindView(R.id.iv_back)
    FrameLayout ivBack;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    private View headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_address_of_take_by_self);
        ButterKnife.bind(this);
        initHeaderView();
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "确认订单");
    }

    @Override
    public View getContentView() {
        return null;
    }


    @OnClick(R.id.btn_finish)
    public void confirm(){
        String man = etTakegoodsMan.getText().toString();
        String phone = etTakegoodsPhone.getText().toString();
        if(TextUtils.isEmpty(man) || TextUtils.isEmpty(phone)){
            Toast.makeText(this,"请先填写完整信息",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intetn = new Intent();
        intetn.putExtra("man",man);
        intetn.putExtra("phone",phone);
        setResult(187,intetn);
        finish();
    }


    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }
}
