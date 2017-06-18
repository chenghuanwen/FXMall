package com.dgkj.fxmall.view.myView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.fragment.RecommendFragment;
import com.dgkj.fxmall.utils.LoadProgressDialogUtil;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Android004 on 2017/4/5.
 */
@SuppressLint("ValidFragment")
public class RecommendStoreDialog extends DialogFragment implements View.OnClickListener{

    private int demandId;
    private Context context;
    private OkHttpClient client;
    private EditText etStoreName;
    private SharedPreferencesUnit sp;
    private Handler handler;
    private LoadProgressDialogUtil loadProgressDialogUtil;

    public RecommendStoreDialog(Context context,int demandId){
        this.context = context;
        this.demandId = demandId;
        client = new OkHttpClient.Builder().build();
        sp = SharedPreferencesUnit.getInstance(context);
        handler = new Handler(Looper.getMainLooper());
        loadProgressDialogUtil = new LoadProgressDialogUtil(context);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity());
        Dialog dialog = new Dialog(getActivity(), R.style.ColorAndSizeSelectDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 必须在设置布局之前调用
        dialog.setContentView(R.layout.layout_recommend_store_dialog);
        dialog.setCanceledOnTouchOutside(true);

        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        etStoreName = (EditText) dialog.findViewById(R.id.et_store_name);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tv_cancle);
        TextView tvRecommend = (TextView) dialog.findViewById(R.id.tv_recommend);
        tvCancel.setOnClickListener(this);
        tvRecommend.setOnClickListener(this);

        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancle:
                dismiss();
                break;
            case R.id.tv_recommend:
                String name = etStoreName.getText().toString();
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(context,"请先填写被推荐的店名",Toast.LENGTH_SHORT).show();
                    return;
                }
                doRecommend(name);
                break;
        }
    }

    /**
     * 推荐店铺
     * @param name
     */
    private void doRecommend(String name) {
        loadProgressDialogUtil.buildProgressDialog();
        FormBody body = new FormBody.Builder()
                .add("user.token",sp.get("token"))
                .add("store.storeName",name)
                .add("desired.id",demandId+"")
                .build();
        Request request = new Request.Builder()
                .url(FXConst.RECOMMEND_STORE_FOR_DEMAND)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadProgressDialogUtil.cancelProgressDialog();
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
                                loadProgressDialogUtil.cancelProgressDialog();
                                Toast.makeText(context,"推荐成功！",Toast.LENGTH_SHORT).show();
                            }
                        });
                        dismiss();
                    }else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                loadProgressDialogUtil.cancelProgressDialog();
                                Toast.makeText(context,"推荐失败！",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
            }
        });
    }


}
