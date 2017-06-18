package com.dgkj.fxmall.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.ExpressCompanyBean;
import com.dgkj.fxmall.bean.OrderBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetExpressCompanyFinishedListener;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShangpuDeliverActivity extends BaseActivity {
    @BindView(R.id.tv_deliver_number)
    TextView tvDeliverNumber;
    @BindView(R.id.tv_buyers_feedback)
    TextView tvBuyersFeedback;
    @BindView(R.id.tv_take_man)
    TextView tvTakeMan;
    @BindView(R.id.tv_take_phone)
    TextView tvTakePhone;
    @BindView(R.id.tv_take_address)
    TextView tvTakeAddress;
    @BindView(R.id.tv_take_express)
    TextView tvTakeExpress;
    @BindView(R.id.tv_express_number)
    EditText tvExpressNumber;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private View headerview;
    private PopupWindow pw;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private FXMallControl control = new FXMallControl();
    private List<ExpressCompanyBean> expressList = new ArrayList<>();
    private CommonAdapter<ExpressCompanyBean> adapter;
    private int expressId = -1;
    private OrderBean order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangpu_deliver);
        ButterKnife.bind(this);
        initHeaderView();
        getExpressData();

        order = (OrderBean) getIntent().getSerializableExtra("order");
        setData();
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void setData() {
        tvDeliverNumber.setText(order.getOrderNum());
        tvBuyersFeedback.setText(order.getBuyerLeave());
        tvTakeMan.setText(order.getTakeMan());
        tvTakePhone.setText(order.getTakePhone());
        tvTakeAddress.setText(order.getTakeAddress());

    }

    private void getExpressData() {
        control.getExpressList(this, client, new OnGetExpressCompanyFinishedListener() {
            @Override
            public void onGetExpressCompanyFinished(List<ExpressCompanyBean> expressCompanies) {
                expressList.addAll(expressCompanies);
            }
        });
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "发货");
    }

    @OnClick(R.id.tv_take_express)
    public void selectExpress(){
        View contentview = getLayoutInflater().inflate(R.layout.layout_express_select_dialog, null);
        pw = new PopupWindow(contentview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        ListView lvExpress = (ListView) contentview.findViewById(R.id.lv_express);
        adapter = new CommonAdapter<ExpressCompanyBean>(this,R.layout.item_express_select,expressList) {
            @Override
            protected void convert(ViewHolder viewHolder, ExpressCompanyBean item, int position) {
                viewHolder.setText(R.id.tv_express,item.getName());
            }
        };
        lvExpress.setAdapter(adapter);


        lvExpress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ExpressCompanyBean express = expressList.get(position);
                expressId = express.getId();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTakeExpress.setText(express.getName());
                        pw.dismiss();
                    }
                });
            }
        });

        //设置触摸对话框以外区域，对话框消失
        pw.setFocusable(true);
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#00000000"));
        pw.setBackgroundDrawable(cd);
        pw.showAsDropDown(tvTakeExpress);
    }


    @OnClick(R.id.btn_submit)
    public void submit(){
        String expressNum = tvExpressNumber.getText().toString();
        if(TextUtils.isEmpty(expressNum)){
            toast("请填写物流单号");
            return;
        }
        if(expressId == -1){
            toast("您还未选择快递类型");
            return;
        }
        FormBody body = new FormBody.Builder()
                .add("user.token",sp.get("token"))
                .add("id",order.getId()+"")
                .add("waybill",expressNum)
                .add("expressCode.id",expressId+"")
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.SHANGPU_DELIVER_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(ShangpuDeliverActivity.this,"网络异常！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                    toastInUI(ShangpuDeliverActivity.this,"发货成功！");
                }
            }
        });
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
