package com.dgkj.fxmall.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.PostageBean;
import com.dgkj.fxmall.bean.SuperPostageBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.view.PostageSettingEditActivity;
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
 * Created by Android004 on 2017/4/1.
 */

public class SetPostageAdapter extends CommonAdapter<SuperPostageBean> {
    private Context context;
    private Activity activity;
    private OkHttpClient client;
    private SharedPreferencesUnit sp;
    private Handler handler = new Handler(Looper.getMainLooper());
    public SetPostageAdapter(Context context,Activity activity, int layoutId, List<SuperPostageBean> datas) {
        super(context, layoutId, datas);
        this.context = context;
        this.activity = activity;
        client = new OkHttpClient.Builder().build();
        sp = SharedPreferencesUnit.getInstance(context);
    }

    @Override
    protected void convert(ViewHolder holder, final SuperPostageBean postageBean, final int position) {
        holder.setText(R.id.tv_postage_mode_name,postageBean.getPosts().get(0).getModeName());
        holder.setText(R.id.tv_postage_mode_setting,postageBean.getPosts().get(0).getModeSetting());
        holder.setOnClickListener(R.id.tv_postage_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {//编辑邮费设置
                Intent intent = new Intent(context, PostageSettingEditActivity.class);
                intent.putExtra("from","edit");
                intent.putExtra("postage",postageBean);
                intent.putExtra("position",position);
                activity.startActivityForResult(intent,131);
            }
        });

       TextView tvDelete = holder.getView(R.id.tv_postage_delete);
        if(position == 0){
            tvDelete.setVisibility(View.GONE);
        }else {
            tvDelete.setVisibility(View.VISIBLE);
            holder.setOnClickListener(R.id.tv_postage_delete, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.remove(position);
                    notifyDataSetChanged();
                    //TODO 通知服务器删除该模板
                    deletePostage(position);
                }
            });
        }

    }

    /**
     * 删除服务器对应模板
     * @param position
     */
    private void deletePostage(int position) {
        FormBody body = new FormBody.Builder()
                .add("store.user.token",sp.get("token"))
                .add("id",mDatas.get(position).getId()+"")
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.DELETE_SUPER_POSTAGE_MODEL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
