package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.listener.OnSelectAccountFinishedListener;
import com.dgkj.fxmall.view.myView.WithdrawalAccountSelectDialog;

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

public class RechargeActivity extends BaseActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.et_charge_sum)
    EditText etChargeSum;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.ll_select_recharge_type)
    LinearLayout tvSelectRechargeType;
    @BindView(R.id.tv_select_account)
    TextView tvSelectAccount;
    @BindView(R.id.activity_recharge)
    LinearLayout activityRecharge;
    @BindView(R.id.iv_type)
    ImageView ivType;
    private View headerview;
    private OkHttpClient client = new OkHttpClient.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        initHeaderView();
    }

    @Override
    public View getContentView() {
        return activityRecharge;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "账户充值");
    }


    @OnClick(R.id.ll_select_recharge_type)
    public void selectType() {
        WithdrawalAccountSelectDialog dialog = new WithdrawalAccountSelectDialog("付款方式");
        dialog.show(getSupportFragmentManager(), "");
        dialog.setSelectListener(new OnSelectAccountFinishedListener() {
            @Override
            public void OnSelectAccountFinished(String result) {
                if(result.contains("微信")){
                    ivType.setImageResource(R.mipmap.weixin);
                }else {
                    ivType.setImageResource(R.mipmap.zhifubao);
                }
                tvSelectAccount.setText(result);
            }
        });
    }

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        String money = etChargeSum.getText().toString();
        if (TextUtils.isEmpty(money)) {
            toast("请输入充值金额");
            return;
        }
        FormBody body = new FormBody.Builder()
                .add("token", sp.get("token"))
                .add("balance", money)
                .build();
        Request request = new Request.Builder()
                .url(FXConst.USER_RECHARGE_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(RechargeActivity.this, "网络异常！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body().string().contains("1000")) {
                    toastInUI(RechargeActivity.this, "充值成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            etChargeSum.setText("");
                            finish();
                        }
                    });

                }
            }
        });
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
