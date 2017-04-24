package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdviceFeedbackActivity extends BaseActivity{

    @BindView(R.id.cb_type1)
    CheckBox cbType1;
    @BindView(R.id.cb_type2)
    CheckBox cbType2;
    @BindView(R.id.cb_type3)
    CheckBox cbType3;
    @BindView(R.id.cb_type4)
    CheckBox cbType4;
    @BindView(R.id.et_advice)
    EditText etAdvice;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    @BindView(R.id.tv_feedback_1)
    TextView tvFeedback1;
    @BindView(R.id.tv_feedback_2)
    TextView tvFeedback2;
    @BindView(R.id.tv_feedback_3)
    TextView tvFeedback3;
    @BindView(R.id.tv_feedback_4)
    TextView tvFeedback4;
    private View headerview;
    private String adviceType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice_feedback);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initHeaderView();
        setCheckListener();
    }

    private void setCheckListener() {
        cbType1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    closeAllCheck();
                    cbType1.setChecked(true);
                    adviceType = tvFeedback1.getText().toString();
                }
            }
        });
        cbType2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    closeAllCheck();
                    cbType2.setChecked(true);
                    adviceType = tvFeedback2.getText().toString();
                }
            }
        });
        cbType3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    closeAllCheck();
                    cbType3.setChecked(true);
                    adviceType = tvFeedback3.getText().toString();
                }
            }
        });
        cbType4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    closeAllCheck();
                    cbType4.setChecked(true);
                    adviceType = tvFeedback4.getText().toString();
                }
            }
        });
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "关于我们");
    }


    public void closeAllCheck(){
        cbType1.setChecked(false);
        cbType2.setChecked(false);
        cbType3.setChecked(false);
        cbType4.setChecked(false);
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }


}
