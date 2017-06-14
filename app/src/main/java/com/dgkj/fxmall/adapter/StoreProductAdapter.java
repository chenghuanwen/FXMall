package com.dgkj.fxmall.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.StoreProductBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.view.MyStoreProductDetialActivity;
import com.dgkj.fxmall.view.ProductDetialActivity;
import com.dgkj.fxmall.view.myView.ShareDialog;
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

public class StoreProductAdapter extends CommonAdapter<StoreProductBean> {
    private Context context;
    private AlertDialog pw;
    private AppCompatActivity activity;
    private String from;
    private OkHttpClient client;
    private SharedPreferencesUnit sp;
    private Handler handler = new Handler(Looper.getMainLooper());

    public StoreProductAdapter(Context context, AppCompatActivity activity, int layoutId, List<StoreProductBean> datas, String from) {
        super(context, layoutId, datas);
        this.context = context;
        this.activity = activity;
        this.from = from;
        client = new OkHttpClient.Builder().build();
        sp = SharedPreferencesUnit.getInstance(context);
    }

    @Override
    protected void convert(ViewHolder holder, final StoreProductBean storeProduct, final int position) {
        holder.setText(R.id.tv_store_goods_introduce, storeProduct.getDescribe());
        holder.setText(R.id.tv_store_goods_price, "¥" + storeProduct.getPrice());
        holder.setText(R.id.tv_store_goods_sales, "销量" + storeProduct.getSales());
        holder.setText(R.id.tv_store_goods_inventory, "库存" + storeProduct.getInventory());

        ImageView iv = holder.getView(R.id.iv_product_goods);
        if ("sale".equals(from)) {

            holder.setOnClickListener(R.id.tv_offline, new View.OnClickListener() {
                @Override
                public void onClick(View v) {//下架
                    showDeleteDialog(position, "确定要下架此商品吗？");
                }
            });
            holder.setOnClickListener(R.id.tv_share, new View.OnClickListener() {
                @Override
                public void onClick(View v) {//分享
                    MainProductBean productBean = new MainProductBean();
                    productBean.setIntroduce(storeProduct.getDescribe());
                    ShareDialog dialog = new ShareDialog(productBean);
                    dialog.show(activity.getSupportFragmentManager(), "");
                }
            });
        } else {

            holder.setOnClickListener(R.id.tv_online, new View.OnClickListener() {
                @Override
                public void onClick(View v) {//上架
                    showDeleteDialog(position, "确定要上架此商品吗？");
                }
            });
        }

        Glide.with(context).load(storeProduct.getUrl()).placeholder(R.mipmap.android_quanzi).into(iv);
        holder.setOnClickListener(R.id.iv_store_goods_detial, new View.OnClickListener() {
            @Override
            public void onClick(View v) {//店铺商品详情
                Intent intent = new Intent(context, MyStoreProductDetialActivity.class);
                if ("sale".equals(from)) {
                    intent.putExtra("from","sale");
                } else {
                    intent.putExtra("from","rest");
                }
                intent.putExtra("position",position);
                intent.putExtra("product",storeProduct);
                activity.startActivityForResult(intent,161);

            }
        });
        holder.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {//删除
                showDeleteDialog(position, "确定要删除此商品吗？");
            }
        });

    }

    private void showDeleteDialog(final int position, final String titel) {
        View contentview = mInflater.inflate(R.layout.layout_delete_dialog, null);
        pw = new AlertDialog.Builder(context).create();
        pw.setView(contentview);
        TextView ttTitel = (TextView) contentview.findViewById(R.id.tv_dialog_titel);
        ttTitel.setText(titel);
        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_confirm);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titel.contains("删除")) {
                    //通知服务器删除
                    deleteRemote(mDatas.get(position).getId());


                } else {
                    StoreProductBean productBean = mDatas.get(position);
                    online(productBean.getId(), productBean.getStatu());
                }
                pw.dismiss();
                mDatas.remove(position);
                notifyDataSetChanged();

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
     * 删除商品
     * @param id
     */
    private void deleteRemote(int id) {
        FormBody body = new FormBody.Builder()
                .add("token",sp.get("token"))
                .add("ids",id+"".trim())
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.STORE_DELETE_PRODUCTS_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"已删除",Toast.LENGTH_SHORT).show();
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

    /**
     * 上架或下架商品
     *
     * @param statu 区分是出售中的还是仓库中的
     * @param id    商品ID
     */
    private void online(int id, int statu) {
        FormBody.Builder builder = new FormBody.Builder()
                .add("store.user.token", sp.get("token"))
                .add("ids", id + "");//TODO 此处为数组id
        if(statu==0){
           builder.add("status", 1 + "".trim());
        }else {
            builder.add("status", 0 + "".trim());
        }
        FormBody body = builder.build();
        final Request request = new Request.Builder()
                .post(body)
                .url(FXConst.PRODUCT_ONLINE_OFFLINE)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络异常！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body().string().contains("1000")) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "操作成功！", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "操作失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
