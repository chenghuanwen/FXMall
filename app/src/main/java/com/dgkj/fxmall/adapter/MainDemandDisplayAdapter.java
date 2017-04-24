package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorStringLoader;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.MainDemandBean;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.view.DemandDetialActivity;
import com.dgkj.fxmall.view.ProductDetialActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Android004 on 2017/3/23.
 */

public class MainDemandDisplayAdapter extends CommonAdapter<MainDemandBean> {
    private List<MainDemandBean> datas;
    private Context context;
    private String from;//判断从需求大厅来还是从产品大厅来
    private OkHttpClient client;
    private Handler handler ;
    private SharedPreferencesUnit sp;
    public MainDemandDisplayAdapter(Context context, int layoutId, List<MainDemandBean> datas, String from) {
        super(context, layoutId, datas);
        this.datas = datas;
        this.context = context;
        this.from = from;
        client = new OkHttpClient.Builder().build();
        handler = new Handler(Looper.getMainLooper());
        sp = SharedPreferencesUnit.getInstance(context);
    }

    @Override
    protected void convert(ViewHolder viewHolder, final MainDemandBean product, final int position) {

        //viewHolder.setImageUrl(R.id.iv_main_item,product.getUrl());
        viewHolder.setText(R.id.tv_main_item_title,product.getIntroduce());
        viewHolder.setText(R.id.tv_main_item_address,product.getAddress());
        viewHolder.setText(R.id.tv_main_item_sale,"需求数量："+product.getDemand());
        ImageView view = viewHolder.getView(R.id.iv_main_item);
        ImageView ivdelete = viewHolder.getView(R.id.iv_demand_delete);
        if("demand".equals(from)){
            ivdelete.setVisibility(View.GONE);
        }else {
            ivdelete.setVisibility(View.VISIBLE);
        }

        Glide.with(context).load(product.getUrls().get(0)).placeholder(R.mipmap.android_quanzi).into(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if("product".equals(from)){
                    intent = new Intent(context, ProductDetialActivity.class);
                }else {
                    intent = new Intent(context, DemandDetialActivity.class);
                }
                intent.putExtra("product",product);
                context.startActivity(intent);
            }
        });

        viewHolder.setOnClickListener(R.id.iv_demand_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 删除服务器数据
                deleteRemote(product.getId());
                mDatas.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 删除服务器需求数据
     * @param id
     */
    private void deleteRemote(int id) {
        FormBody body = new FormBody.Builder()
                .add("user.token",sp.get("token"))
                .add("ids",id+"")
                .build();
        Request request = new Request.Builder()
                .url(FXConst.DELETE_MY_DEMAND_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"网络异常！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"删除成功！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"删除失败！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}
