package com.dgkj.fxmall.view.myView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.MainProductBean;
import com.dgkj.fxmall.bean.ShoppingCarBean;
import com.dgkj.fxmall.bean.ShoppingGoodsBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnSelectColorSizeFinishedListener;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.view.ConfirmOrderActivity;
import com.dgkj.fxmall.view.HomePageActivity;
import com.dgkj.fxmall.view.ProductDetialActivity;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

/**
 * Created by Android004 on 2017/4/5.
 */
@SuppressLint("ValidFragment")
public class ShareCommandDialog extends PopupWindow {

    private OkHttpClient client;
    private SharedPreferencesUnit sp;
    private FXMallControl control;
    private BaseActivity activity;
    private View conentView;
    private ClipboardManager clipboardManager;
    private Handler handler ;
    public ShareCommandDialog(BaseActivity activity, ClipboardManager clipboardManager){
        this.activity = activity;
        this.clipboardManager = clipboardManager;
        client = new OkHttpClient.Builder().build();
        sp = SharedPreferencesUnit.getInstance(activity);
        control = new FXMallControl();
        handler = new Handler(Looper.getMainLooper());
        init();
        setData();


    }

    private void setData() {

        TextView tvGirl = (TextView) conentView.findViewById(R.id.tv_confirm);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 获取商品数据
                Intent intent = new Intent(activity,ProductDetialActivity.class);
                MainProductBean product = new MainProductBean();
                intent.putExtra("product",product);
                activity.startActivity(intent);
                dismiss();
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null,""));
            }
        });
        TextView tvBoy = (TextView) conentView.findViewById(R.id.tv_cancle);
        tvBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null,""));
            }
        });



    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.layout_check_command_dialog, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(final View parent) {
        if(parent==null){return;}
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showAtLocation(parent,Gravity.CENTER,0,0);
                }
            },500);
        } else {
            this.dismiss();
        }
    }
}
