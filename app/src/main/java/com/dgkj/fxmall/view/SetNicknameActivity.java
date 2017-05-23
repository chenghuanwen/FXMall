package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SetNicknameActivity extends BaseActivity {

    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    private View headerview;
    private String from = "";
    private String nick,name;
    private OkHttpClient client;
    private SharedPreferencesUnit sp ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nickname);
        ButterKnife.bind(this);
        initHeaderview();

        client = new OkHttpClient.Builder().build();
        sp = SharedPreferencesUnit.getInstance(this);

        from = getIntent().getStringExtra("from");
        if("nick".equals(from)){
            etNickname.setText(getIntent().getStringExtra("nick"));
        }else {
            etNickname.setText(getIntent().getStringExtra("name"));
        }
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void initHeaderview() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "个人资料");
    }

    @OnClick(R.id.iv_clear)
    public void clear() {
        etNickname.setText("");
    }

    @OnClick(R.id.btn_save)
    public void save() {
        //TODO 保存昵称或真实名字到服务器
        final String nick = etNickname.getText().toString();
      /*  String encode = "";
        try {
            encode = URLEncoder.encode(nick,"utf-8").toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        if (TextUtils.isEmpty(nick)) {
            toast("还未输入任何内容！");
            return;
        }
        final Intent intent = new Intent(this, UserMsgActivity.class);
        if ("nick".equals(from)) {
            FormBody body = new FormBody.Builder()
                    .add("token",sp.get("token"))
                    .add("nickname", nick)
                    .build();
            Request request = new Request.Builder()
                    .url(FXConst.CHANGE_USER_NICK)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    toastInUI(SetNicknameActivity.this,"网络异常！");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    if(result.contains("1000")){
                        toastInUI(SetNicknameActivity.this,"昵称修改成功！");
                        intent.putExtra("nick", nick);
                        setResult(112, intent);
                        finish();
                    }else {
                        toastInUI(SetNicknameActivity.this,"昵称修改失败！");
                    }
                }
            });
        } else {
            intent.putExtra("name", nick);
            FormBody formBody = new FormBody.Builder()
                    .add("token",sp.get("token"))
                    .add("realname",nick)
                    .build();
            Request request1 = new  Request.Builder()
                    .post(formBody)
                    .url(FXConst.CHANGE_USER_NAME)
                    .build();
            client.newCall(request1).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    toastInUI(SetNicknameActivity.this,"网络异常！");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    if(result.contains("1000")){
                        toastInUI(SetNicknameActivity.this,"姓名修改成功！");
                        intent.putExtra("name", nick);
                        setResult(114, intent);
                        finish();
                    }else {
                        toastInUI(SetNicknameActivity.this,"姓名修改失败！");
                    }
                }
            });
        }

    }

    @OnClick(R.id.ib_back)
    public void back(){
        finish();
    }
}
