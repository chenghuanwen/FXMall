package com.dgkj.fxmall.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;

/**
 * Created by Android004 on 2017/5/5.
 */

public class LoadProgressDialogUtil {
    private Dialog progressDialog;
    private Context context;

    public LoadProgressDialogUtil(Context context) {
        this.context = context;
    }

    /**
     *
     * @Description: TODO 自定义加载提示内容
     * @param @param id
     * @return void 用法buildProgressDialog(R.string.loding)
     * @throws
     */
    public void buildProgressDialog(int id) {
        if (progressDialog == null) {
            progressDialog = new Dialog(context, R.style.progress_dialog);
        }
        progressDialog.setContentView(R.layout.layout_load_dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        TextView msg = (TextView) progressDialog
                .findViewById(R.id.id_tv_loadingmsg);
        msg.setText(context.getString(id));
        progressDialog.show();
    }

    /**
     * @Description: TODO 固定加载提示内容
     */

    public void buildProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new Dialog(context, R.style.progress_dialog);
        }
        progressDialog.setContentView(R.layout.layout_load_dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        TextView msg = (TextView) progressDialog
                .findViewById(R.id.id_tv_loadingmsg);
        msg.setText("卖力加载中");
        progressDialog.show();
    }

    /**
     *   * @Description: TODO 取消加载框
     */

    public void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
