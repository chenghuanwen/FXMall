package com.dgkj.fxmall.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplyRefundActivity extends BaseActivity {
    @BindView(R.id.tv_select_refund_reason)
    TextView tvSelectRefundReason;
    @BindView(R.id.tv_most_refund)
    TextView tvMostRefund;
    @BindView(R.id.et_refund_money)
    EditText etRefundMoney;
    @BindView(R.id.et_refund_describe)
    EditText etRefundDescribe;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_refund_type)
    TextView tvRefundType;
    @BindView(R.id.tv_select_pictures)
    TextView tvSelectPictures;
    @BindView(R.id.activity_apply_refund)
    LinearLayout activityApplyRefund;
    private View headerview;
    private PopupWindow pw;
    private int refundType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_refund);
        ButterKnife.bind(this);
        initHeaderView();
    }

    @Override
    public View getContentView() {
        return activityApplyRefund;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "申请退款");
    }

    @OnClick(R.id.tv_refund_type)
    public void refundType() {
        View contentview = getLayoutInflater().inflate(R.layout.layout_refundtype_select_window, null);
        pw = new PopupWindow(contentview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_search_goods);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvRefundType.setText("退货退款");
                refundType = 0;
                tvSelectPictures.setText("上传三张凭证");
                pw.dismiss();
            }
        });
        TextView tvBoy = (TextView) contentview.findViewById(R.id.tv_search_store);
        tvBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvRefundType.setText("仅退款");
                tvSelectPictures.setText("上传物流信息截图");
                refundType = 1;
                pw.dismiss();
            }
        });


        //设置触摸对话框以外区域，对话框消失
        pw.setFocusable(true);
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#00000000"));
        pw.setBackgroundDrawable(cd);
        pw.showAsDropDown(tvRefundType);
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
