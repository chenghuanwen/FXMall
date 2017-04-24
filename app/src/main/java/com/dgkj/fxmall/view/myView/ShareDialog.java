package com.dgkj.fxmall.view.myView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.MainProductBean;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Android004 on 2017/4/5.
 */
@SuppressLint("ValidFragment")
public class ShareDialog extends DialogFragment implements View.OnClickListener{

    private MainProductBean goods;
    private Dialog dialog;
    private ClipboardManager clipboardManager;
    private PackageManager mPackageManager;
    private FragmentActivity activity;
    public ShareDialog(MainProductBean goods){
        this.goods = goods;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        clipboardManager = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
        mPackageManager = getContext().getPackageManager();
        activity = getActivity();

        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity());
        dialog = new Dialog(getActivity(), R.style.ColorAndSizeSelectDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 必须在设置布局之前调用
        dialog.setContentView(R.layout.layout_share_dialog);
        dialog.setCanceledOnTouchOutside(true);

        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        TextView tvQQ = (TextView) dialog.findViewById(R.id.tv_share_QQ);
        TextView tvWinxin = (TextView) dialog.findViewById(R.id.tv_share_weixin);
        TextView tvAlipay = (TextView) dialog.findViewById(R.id.tv_share_alipay);

       /* TextView tvQZone = (TextView) dialog.findViewById(R.id.tv_share_qzone);
        TextView tvFriend = (TextView) dialog.findViewById(R.id.tv_share_friends);
        TextView tvSina = (TextView) dialog.findViewById(R.id.tv_share_weibo);*/
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_share_cancel);
       /* tvFriend.setOnClickListener(this);
        tvQZone.setOnClickListener(this);
        tvSina.setOnClickListener(this);*/
        tvQQ.setOnClickListener(this);
        tvWinxin.setOnClickListener(this);
        tvAlipay.setOnClickListener(this);
        btnCancel.setOnClickListener(this);


        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_share_QQ:
                dismiss();
                showCommandDialog("QQ");
                break;
            case R.id.tv_share_weixin:
                dismiss();
                showCommandDialog("WeChat");
                break;
            case R.id.tv_share_alipay:
                dismiss();
                showCommandDialog("alipay");
                break;
           /* case R.id.tv_share_qzone:
               dismiss();
                showCommandDialog("QQZone");
                break;*/
          /*  case R.id.tv_share_friends:
                dismiss();
                showCommandDialog("Friends");
                break;*/
          /*  case R.id.tv_share_weibo:
                dismiss();
                showCommandDialog("Sina");
                break;*/
            case R.id.btn_share_cancel:
                dismiss();
                break;
        }
    }

  private class ShareListener implements PlatformActionListener{

      @Override
      public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
          dismiss();
      }

      @Override
      public void onError(Platform platform, int i, Throwable throwable) {
          dismiss();
      }

      @Override
      public void onCancel(Platform platform, int i) {
            dismiss();
      }
  }


    private void showCommandDialog(final String from){
        View contentview = getActivity().getLayoutInflater().inflate(R.layout.layout_command_create_dialog, null);
        final AlertDialog pw = new AlertDialog.Builder(getContext()).create();
        pw.setView(contentview);
        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_confirm);
        TextView tvCommand = (TextView) contentview.findViewById(R.id.tv_command);
        //TODO 生成口令
        tvCommand.setText("【"+goods.getIntroduce()+"】复制这条信息，打开分销商城app进行查看"+"¥FXMall¥");

        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                switch (from){
                    case "QQZone":
                        QZone.ShareParams sp = new QZone.ShareParams();
                        sp.setShareType(Platform.SHARE_TEXT);
                        //生成分享口令
                        // sp.setTitle("【"+goods.getIntroduce()+"】");
                        sp.setText("【"+goods.getIntroduce()+"】"+"复制这条信息，打开分销商城app进行查看"+"¥FXMall¥");
                        sp.setTitleUrl("http://www.baidu.com");
                        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                        qzone.setPlatformActionListener(new ShareListener());
                        clipboardManager.setPrimaryClip(ClipData.newPlainText(null,sp.getText()));
                        qzone.share(sp);
                        break;
                    case "QQ":
                      /*  QQ.ShareParams spQ = new QQ.ShareParams();
                        spQ.setShareType(Platform.SHARE_TEXT);
                      //  spQ.setTitle("【"+goods.getIntroduce()+"】");
                        spQ.setText("【"+goods.getIntroduce()+"】"+"复制这条信息，打开分销商城app进行查看"+"¥FXMall¥");
                        Platform qq  = ShareSDK.getPlatform(QQ.NAME);
                        qq.setPlatformActionListener(new ShareListener());
                        clipboardManager.setPrimaryClip(ClipData.newPlainText(null,spQ.getText()));
                        qq.share(spQ);*/
                        clipboardManager.setPrimaryClip(ClipData.newPlainText(null,"【"+goods.getIntroduce()+"】"+"复制这条信息，打开分销商城app进行查看"+"¥FXMall¥"));
                        openApp("com.tencent.mobileqq");
                        break;
                    case "WeChat":
                       /* Wechat.ShareParams spW = new Wechat.ShareParams();
                        spW.setShareType(Platform.SHARE_TEXT);
                        spW.setTitle("【"+goods.getIntroduce()+"】");
                        spW.setText("【"+goods.getIntroduce()+"】"+"复制这条信息，打开分销商城app进行查看"+"¥FXMall¥");
                        Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
                        weixin.setPlatformActionListener(new ShareListener());
                        clipboardManager.setPrimaryClip(ClipData.newPlainText(null,spW.getText()));
                        weixin.share(spW);*/
                        clipboardManager.setPrimaryClip(ClipData.newPlainText(null,"【"+goods.getIntroduce()+"】"+"复制这条信息，打开分销商城app进行查看"+"¥FXMall¥"));
                        openApp("com.tencent.mm");
                        break;
                    case "alipay":
                        clipboardManager.setPrimaryClip(ClipData.newPlainText(null,"【"+goods.getIntroduce()+"】"+"复制这条信息，打开分销商城app进行查看"+"¥FXMall¥"));
                        openApp("Alipay");
                        break;
                    case "Friends":
                        WechatMoments.ShareParams spF = new WechatMoments.ShareParams();
                        spF.setShareType(Platform.SHARE_TEXT);
                        spF.setTitle("【"+goods.getIntroduce()+"】");
                        spF.setText("【"+goods.getIntroduce()+"】"+"复制这条信息，打开分销商城app进行查看"+"¥FXMall¥");
                        Platform friends = ShareSDK.getPlatform(WechatMoments.NAME);
                        friends.setPlatformActionListener(new ShareListener());
                        clipboardManager.setPrimaryClip(ClipData.newPlainText(null,spF.getText()));
                        friends.share(spF);
                        break;
                    case "Sina":
                        SinaWeibo.ShareParams spS = new SinaWeibo.ShareParams();
                        spS.setShareType(Platform.SHARE_TEXT);
                        spS.setTitle("【"+goods.getIntroduce()+"】");
                        spS.setText("【"+goods.getIntroduce()+"】"+"复制这条信息，打开分销商城app进行查看"+"¥FXMall¥");
                        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                        weibo.setPlatformActionListener(new ShareListener());
                        clipboardManager.setPrimaryClip(ClipData.newPlainText(null,spS.getText()));
                        weibo.share(spS);
                        break;
                }

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

    private void openApp(String str){
        //应用过滤条件
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
      //  System.out.println("testrrr");

        //System.out.println("tesr");
        List<ResolveInfo> mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);
        //按报名排序
        Collections.sort(mAllApps, new ResolveInfo.DisplayNameComparator(mPackageManager));

        for(ResolveInfo res : mAllApps){
            //该应用的包名和主Activity
            String pkg = res.activityInfo.packageName;
            String cls = res.activityInfo.name;
          //  System.out.println("pkg---" +pkg);
            // System.out.println("打印出来的----" + str);

            // 打开QQ pkg中包含"qq"，打开微信，pkg中包含"mm"
            if(pkg.contains(str)){
                ComponentName componet = new ComponentName(pkg, cls);
                Intent intent = new Intent();
                intent.setComponent(componet);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        }
    }

}
