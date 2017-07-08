package com.dgkj.fxmall.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.DemandMsgBean;
import com.dgkj.fxmall.bean.NotifyMsgBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
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
 * Created by Android004 on 2017/4/5.
 */

public class NotifyMsgAdapter extends CommonAdapter<NotifyMsgBean> {
    private static final int PRODUCT_MSG = 1;
    private static final int NOTIFY_MSG = 2;
    private AlertDialog pw;
    private SharedPreferencesUnit sp;
    private OkHttpClient client;

    public NotifyMsgAdapter(Context context,List<NotifyMsgBean> datas) {
        super(context, -1, datas);
        sp = SharedPreferencesUnit.getInstance(context);
        client = new OkHttpClient.Builder().build();
    }

    @Override
    protected void convert(ViewHolder holder, NotifyMsgBean logisticsMsg, final int position) {
        holder.setText(R.id.tv_order_state,logisticsMsg.getTitle());
        holder.setText(R.id.tv_msg_goods_introduce,logisticsMsg.getCotent());
        holder.setText(R.id.tv_msg_time,logisticsMsg.getTime());

        holder.setOnClickListener(R.id.iv_delete_msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             showDeleteDialog(position);
            }
        });
        if(getItemViewType(position)==PRODUCT_MSG){
            ImageView iv = holder.getView(R.id.iv_msg_goods);
            Glide.with(mContext).load(logisticsMsg.getUrl()).error(R.mipmap.android_quanzi).into(iv);
        }

    }


    @Override
    public int getItemViewType(int position) {
        String type = mDatas.get(position).getType();
        if("order".equals(type)){
            return PRODUCT_MSG;
        }else {
            return NOTIFY_MSG;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null ;
        View view;
        if(viewType==PRODUCT_MSG){
            view = mInflater.inflate(R.layout.item_wuliu_msg,parent,false);
            viewHolder = new ViewHolder(mContext,view);
        }else {
            view = mInflater.inflate(R.layout.item_notify_msg,parent,false);
            viewHolder = new ViewHolder(mContext,view);
        }
        return viewHolder;
    }


    private void showDeleteDialog(final int position){
        View contentview = mInflater.inflate(R.layout.layout_delete_dialog, null);
        pw = new AlertDialog.Builder(mContext).create();
        pw.setView(contentview);
        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_confirm);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO 通知服务器删除
                deleteMsgOfServe(mDatas.get(position).getId());

                mDatas.remove(position);
                notifyDataSetChanged();
                pw.dismiss();
            }
        });
        TextView tvBoy = (TextView) contentview.findViewById(R.id.tv_cancle);
        tvBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });


        //设置触摸对话框以外区域，对话框消失
        pw.setCanceledOnTouchOutside(true);
        pw.show();
    }


    /**
     * 删除该条信息
     * @param id
     */
    private void deleteMsgOfServe(int id) {
        FormBody body = new FormBody.Builder()
                .add("id",id+"".trim())
                .add("toUser.token",sp.get("token"))
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.DELETE_NOTIFY_MSG_URL)
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
}
