package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.NotifyMsgAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.NotifyMsgBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.control.FXMallControl;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotifyMsgActivity extends BaseActivity {
    @BindView(R.id.activity_logistics_msg)
    LinearLayout activityLogisticsMsg;
    private View headerview;
    private RecyclerView rvMsg;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    private NotifyMsgAdapter adapter;
    private ArrayList<NotifyMsgBean> msgs;
    private String from = "";
    private OkHttpClient client = new OkHttpClient.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_msg);
        ButterKnife.bind(this);

        initHeaderview();
        init();
        from = getIntent().getStringExtra("from");
        changeReadState();

    }

    /**
     * 改变未读状态
     */
    private void changeReadState() {
        FormBody body = new FormBody.Builder()
                .add("toUser.token",sp.get("token"))
                .add("type",from)
                .build();
        Request request = new Request.Builder()
                .url(FXConst.CHANGE_MSG_READ_STATE)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMsg.setLayoutManager(layoutManager);
        msgs = (ArrayList<NotifyMsgBean>) getIntent().getSerializableExtra("msg");
        adapter = new NotifyMsgAdapter(this,msgs);
        rvMsg.setAdapter(adapter);
    }

    @Override
    public View getContentView() {
        return activityLogisticsMsg;
    }

    private void initHeaderview() {
        headerview = findViewById(R.id.headerview);
        rvMsg = (RecyclerView) findViewById(R.id.rv_wuliu_msg);
        setHeaderTitle(headerview, "通知消息");

    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
