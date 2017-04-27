package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.AdressManagerAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.TakeGoodsAddressBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetAllAddressFinishedListener;

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

public class TakeGoodsAdressActivity extends BaseActivity {

    @BindView(R.id.tv_empty_view)
    TextView tvEmptyView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.rv_goods_address)
    RecyclerView rvAdress;



    private View headerview;
    private List<TakeGoodsAddressBean> addressList;
    private AdressManagerAdapter adressManagerAdapter;
    private FXMallControl control = new FXMallControl();
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1://TODO 设为默认发送到服务器
                    TakeGoodsAddressBean address = (TakeGoodsAddressBean) msg.obj;
                    adressManagerAdapter.notifyDataSetChanged();
                    FormBody body = new FormBody.Builder()
                            .add("user.token",sp.get("token"))
                            .add("id",address.getId()+"")
                            .build();
                    Request request = new Request.Builder()
                            .url(FXConst.SET_DEFAULT_TAKE_ADDRESS)
                            .post(body)
                            .build();
                    client.newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            toastInUI(TakeGoodsAdressActivity.this,"网络异常");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if(response.body().string().contains("1000")){
                                toastInUI(TakeGoodsAdressActivity.this,"已成功设为默认地址");
                            }
                        }
                    });
                    break;
                case 2://TODO 取消默认
                    adressManagerAdapter.notifyDataSetChanged();
                    break;
                case 3://删除
                    TakeGoodsAddressBean address1 = (TakeGoodsAddressBean) msg.obj;
                    final int positon = msg.arg1;
                    FormBody body1 = new FormBody.Builder()
                            .add("user.token",sp.get("token"))
                            .add("id",address1.getId()+"")
                            .build();
                    Request request1 = new Request.Builder()
                            .url(FXConst.DELETE_TAKE_ADDRESS)
                            .post(body1)
                            .build();
                    client.newCall(request1).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            toastInUI(TakeGoodsAdressActivity.this,"网络异常");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if(response.body().string().contains("1000")){
                                toastInUI(TakeGoodsAdressActivity.this,"删除成功");
                                addressList.remove(positon);
                                adressManagerAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_goods_adress);
        ButterKnife.bind(this);

        initHeaderView();
        initData();
        refresh();
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void refresh() {
        control.getAllTakeAddress(this,sp.get("token"),client, new OnGetAllAddressFinishedListener() {
            @Override
            public void onGetAllAddressFinished(final List<TakeGoodsAddressBean> addressList) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(addressList.size() > 0){
                            tvEmptyView.setVisibility(View.GONE);
                            rvAdress.setVisibility(View.VISIBLE);
                            adressManagerAdapter.addAll(addressList,true);
                        }
                    }
                });
            }
        });
    }

    private void initData() {
        addressList = new ArrayList<>();
        adressManagerAdapter = new AdressManagerAdapter(this,this,R.layout.item_takegoods_address,addressList,handler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvAdress.setLayoutManager(layoutManager);
        rvAdress.setAdapter(adressManagerAdapter);


    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview,"管理收货地址");
    }


    @OnClick(R.id.btn_add)
    public void add(){
        Intent intent = new Intent(this,AddGoodsAdressActivity.class);
        intent.putExtra("from","add");
        startActivityForResult(intent,140);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==140 && resultCode==141){
            tvEmptyView.setVisibility(View.GONE);
            rvAdress.setVisibility(View.VISIBLE);
           TakeGoodsAddressBean address = (TakeGoodsAddressBean) data.getSerializableExtra("address");
            List<TakeGoodsAddressBean> list = new ArrayList<>();
            list.addAll(addressList);
            list.add(address);
            adressManagerAdapter.addAll(list,true);
        }else if(requestCode==151 && resultCode==141){
            TakeGoodsAddressBean address = (TakeGoodsAddressBean) data.getSerializableExtra("address");
            int position = data.getIntExtra("position", -1);
            addressList.remove(position);
            addressList.add(position,address);
            adressManagerAdapter.notifyItemChanged(position);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    @OnClick(R.id.iv_back)
    public void back(){
        finish();
    }
}
