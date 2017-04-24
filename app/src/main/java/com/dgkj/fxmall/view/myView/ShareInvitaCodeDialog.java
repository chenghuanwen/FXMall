package com.dgkj.fxmall.view.myView;

import android.annotation.SuppressLint;
import android.app.Dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dgkj.fxmall.R;

import java.util.HashMap;

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
public class ShareInvitaCodeDialog extends DialogFragment implements View.OnClickListener{
    private String invitaCode;
    private SpannableString sp;

    public ShareInvitaCodeDialog(String invitaCode) {
        this.invitaCode = invitaCode;
        sp = new SpannableString(invitaCode);
        sp.setSpan(new ForegroundColorSpan(Color.RED),0,invitaCode.length(),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity());
        Dialog dialog = new Dialog(getActivity(), R.style.ColorAndSizeSelectDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 必须在设置布局之前调用
        dialog.setContentView(R.layout.layout_share_invita_code_dialog);
        dialog.setCanceledOnTouchOutside(true);

        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        TextView tvQQ = (TextView) dialog.findViewById(R.id.tv_share_QQ);
        TextView tvWinxin = (TextView) dialog.findViewById(R.id.tv_share_weixin);
        TextView tvQZone = (TextView) dialog.findViewById(R.id.tv_share_qzone);
        TextView tvFriend = (TextView) dialog.findViewById(R.id.tv_share_friends);
        TextView tvSina = (TextView) dialog.findViewById(R.id.tv_share_weibo);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_share_cancel);
        tvFriend.setOnClickListener(this);
        tvQZone.setOnClickListener(this);
        tvSina.setOnClickListener(this);
        tvQQ.setOnClickListener(this);
        tvWinxin.setOnClickListener(this);
        btnCancel.setOnClickListener(this);


        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_share_QQ:
                dismiss();
                QQ.ShareParams spQ = new QQ.ShareParams();
                spQ.setShareType(Platform.SHARE_TEXT);
                spQ.setTitle("【来自分销商城的邀请码】");
                spQ.setText("分销商城邀请码："+sp.toString()+"，马上下载分销商城app使用吧，购物优惠多多哦！");
                Platform qq  = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(new ShareListener());
                qq.share(spQ);
                break;
            case R.id.tv_share_weixin:
                dismiss();
                Wechat.ShareParams spW = new Wechat.ShareParams();
                spW.setShareType(Platform.SHARE_TEXT);
                spW.setTitle("【来自分销商城的邀请码】");
                spW.setText("分销商城邀请码："+sp.toString()+"，马上下载分销商城app使用吧，购物优惠多多哦！");
                Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
                weixin.setPlatformActionListener(new ShareListener());
                weixin.share(spW);
                break;
            case R.id.tv_share_qzone:
               dismiss();
                QZone.ShareParams spZ = new QZone.ShareParams();
                spZ.setShareType(Platform.SHARE_TEXT);
                //生成分享口令
                spZ.setTitle("【来自分销商城的邀请码】");
                spZ.setText("分销商城邀请码："+sp.toString()+"，马上下载分销商城app使用吧，购物优惠多多哦！");
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                qzone.setPlatformActionListener(new ShareListener());
                qzone.share(spZ);
                break;
            case R.id.tv_share_friends:
                dismiss();
                WechatMoments.ShareParams spF = new WechatMoments.ShareParams();
                spF.setShareType(Platform.SHARE_TEXT);
                spF.setTitle("【来自分销商城的邀请码】");
                spF.setText("分销商城邀请码："+sp.toString()+"，马上下载分销商城app使用吧，购物优惠多多哦！");
                Platform friends = ShareSDK.getPlatform(WechatMoments.NAME);
                friends.setPlatformActionListener(new ShareListener());
                friends.share(spF);
                break;
            case R.id.tv_share_weibo:
                dismiss();
                SinaWeibo.ShareParams spS = new SinaWeibo.ShareParams();
                spS.setShareType(Platform.SHARE_TEXT);
                spS.setTitle("【来自分销商城的邀请码】");
                spS.setText("分销商城邀请码："+sp.toString()+"，马上下载分销商城app使用吧，购物优惠多多哦！");
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(new ShareListener());
                weibo.share(spS);
                break;
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




}
