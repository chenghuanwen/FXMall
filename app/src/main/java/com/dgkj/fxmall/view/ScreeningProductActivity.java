package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dgkj.fxmall.MyApplication;
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
    @BindView(R.id.tv_reset1)
    TextView tvReset;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.activity_screening)
    LinearLayout activityScreening;
    @BindView(R.id.rb_time)
    RadioButton rbTime;
    @BindView(R.id.rb_near)
    RadioButton rbNear;
    @BindView(R.id.rg_rank)
    RadioGroup rgRank;
    private String orderby="",startPrice="",endPrice="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening);
        ButterKnife.bind(this);
        setListener();
    }

    private void setListener() {
        rgRank.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_time:
                        orderby = "createTime";
                        break;
                    case R.id.rb_near:
                        orderby = MyApplication.currentCity;
                        break;
                }
            }
        });
    }

    @OnClick(R.id.tv_reset1)
    public void reset(){
        etSpan1.setText("");
        etSpan2.setText("");
        rbTime.setChecked(true);
    }

    @OnClick(R.id.btn_confirm)
    public void confirm(){
        startPrice = etSpan1.getText().toString();
        endPrice = etSpan2.getText().toString();
        Intent intent = new Intent(this,SearchContentActivity.class);
        intent.putExtra("orderBy",orderby);
        intent.putExtra("start",startPrice);
        intent.putExtra("end",endPrice);
        intent.putExtra("from","screening");
        jumpTo(intent,true);
    }

    @OnClick(R.id.tv_cancle)
    public void cancel() {
        finish();
    }

    @Override
    public View getContentView() {
        return null;
    }
}
