package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScreeningProductActivity extends BaseActivity {

    @BindView(R.id.tv_cancle)
    TextView tvCancle;
    @BindView(R.id.tv_type_titel)
    TextView tvTypeTitel;
    @BindView(R.id.et_span1)
    EditText etSpan1;
    @BindView(R.id.et_span2)
    EditText etSpan2;
    @BindView(R.id.tv_rank)
    TextView tvRank;
    @BindView(R.id.tv_reset)
    TextView tvReset;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_cancle)
    public void cancel(){
        finish();
    }

    @Override
    public View getContentView() {
        return null;
    }
}
