package com.dgkj.fxmall.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.Position;

import butterknife.BindView;
import butterknife.OnClick;

public class NotifyMsgActivity extends BaseActivity {
    private View headerview;
    private RecyclerView rvMsg;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_msg);

        initHeaderview();
    }

    private void initHeaderview() {
        headerview = findViewById(R.id.headerview);
        rvMsg = (RecyclerView) findViewById(R.id.rv_wuliu_msg);
        setHeaderTitle(headerview,"通知消息");

    }
    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
