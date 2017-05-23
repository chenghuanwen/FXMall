package com.dgkj.fxmall.view.myView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.listener.OnSelectAccountFinishedListener;

/**
 * Created by Android004 on 2017/4/5.
 */
@SuppressLint("ValidFragment")
public class WithdrawalAccountSelectDialog extends DialogFragment {
    private OnSelectAccountFinishedListener listener;
    private String title;
    private int resId;

    public WithdrawalAccountSelectDialog(String title,int resId) {
        this.title = title;
        this.resId = resId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity());
       // getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        final Dialog dialog = new Dialog(getActivity(), R.style.ColorAndSizeSelectDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 必须在设置布局之前调用
        dialog.setContentView(resId);
        dialog.setCanceledOnTouchOutside(true);

        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        TextView tvCancle = (TextView) dialog.findViewById(R.id.tv_cancle);
        tvCancle.setText(title);

        RadioGroup rgWithdrawabl = (RadioGroup) dialog.findViewById(R.id.rg_pay);

        rgWithdrawabl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.tv_zhifubao_pay:
                        listener.OnSelectAccountFinished("支付宝");
                        dialog.dismiss();
                        break;
                    case R.id.tv_weixi_pay:
                        listener.OnSelectAccountFinished("微信");
                        dialog.dismiss();
                        break;
                    case R.id.tv_balance_pay:
                        if(MyApplication.balance<200.0){
                            Toast.makeText(getContext(),"余额不足,请选择其他支付方式",Toast.LENGTH_LONG).show();
                            return;
                        }
                        listener.OnSelectAccountFinished("余额");
                        dialog.dismiss();
                        break;

                }
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    public void setSelectListener(OnSelectAccountFinishedListener listener){
        this.listener = listener;
    }


    @Override
    public void onStart() {
        super.onStart();
        //设置背景透明
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.0f;
        window.setAttributes(windowParams);
    }
}
