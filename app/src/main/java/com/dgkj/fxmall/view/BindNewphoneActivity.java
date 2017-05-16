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
import com.dgkj.fxmall.constans.FXConst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private String newPhone = "";
    private OkHttpClient client = new OkHttpClient.Builder().build();

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

    @OnClick(R.id.tv_get_code)
    public void getCode(){
        newPhone = etNewPhone.getText().toString();
        if (TextUtils.isEmpty(newPhone)) {
            toast("请输入手机号！");
            return;
        }
        sendCheckCode(newPhone);

    }

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        String code = etCheckCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            toast("请输入验证码！");
            return;
        }
        checkMsgCode(code,newPhone);
    }

    private void checkMsgCode(String code, final String newPhone) {
        FormBody body = new FormBody.Builder()
                .add("phone",newPhone)
                .add("code",code)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.CHECK_MESSAGE_CODE)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(BindNewphoneActivity.this,"网络错误");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(result.contains("1000")){
                    setNewPhone(newPhone);
                }else if(result.contains("109")){
                    etCheckCode.setText("");
                    toastInUI(BindNewphoneActivity.this,"验证码错误，请重新输入！");
                }else if (result.contains("1005")){
                    toastInUI(BindNewphoneActivity.this,"今日验证码获取次数已用完，请明天重试！");
                }
            }
        });
    }

    /**
     * 设置新手机号
     * @param newPhone
     */
    private void setNewPhone(final String newPhone) {
        FormBody body = new FormBody.Builder()
                .add("token",sp.get("token"))
                .add("phone",newPhone)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.CHANGE_USER_PHONE)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if(string.contains("1000")){
                    toastInUI(BindNewphoneActivity.this,"已绑定新手机");
                    Intent intent = new Intent(BindNewphoneActivity.this, UserMsgActivity.class);
                    intent.putExtra("from", "newphone");
                    intent.putExtra("phone", newPhone);
                    jumpTo(intent, true);
                }else if(string.contains("1002")){
                    toastInUI(BindNewphoneActivity.this,"该手机号已被占用");
                }
            }
        });
    }


    /**
     * 获取短信验证码
     * @param phone
     */
    private void sendCheckCode(String phone) {
        FormBody body = new FormBody.Builder()
                .add("phone",phone)
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_MESSAGE_CHECK_CODE)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(BindNewphoneActivity.this,"网络错误，请稍后重试！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(result.contains("1000")){
                    toastInUI(BindNewphoneActivity.this,"验证码已发送至你的手机，请注意查收！");
                    tvGetCode.setClickable(false);
                }else if(result.contains("109")){
                    toastInUI(BindNewphoneActivity.this,"验证码发送失败，请稍后重试！");
                    tvGetCode.setClickable(true);
                }else {
                    toastInUI(BindNewphoneActivity.this,"今日验证码获取次数已用完，请明天重试！");
                }
            }
        });
    }
}
