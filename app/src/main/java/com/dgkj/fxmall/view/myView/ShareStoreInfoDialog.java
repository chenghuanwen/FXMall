package com.dgkj.fxmall.view.myView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.dgkj.fxmall.utils.LogUtil;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Android004 on 2017/4/5.
 */
@SuppressLint("ValidFragment")
public class ShareStoreInfoDialog extends DialogFragment implements View.OnClickListener{
    private String storeName,storeLogo;
    private SpannableString sp;
    private Bitmap bitmap;

    public ShareStoreInfoDialog(String storeName,String storeLogo) {
        this.storeName = storeName;
        this.storeLogo = storeLogo;
        sp = new SpannableString(storeName);
        sp.setSpan(new ForegroundColorSpan(Color.RED),0,storeName.length(),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.icon1024);

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
                QQ.ShareParams  spQ = new QQ.ShareParams();
                spQ.setTitle("【来自哎购商城的优质商店】");
                spQ.setTitleUrl(storeLogo);
                spQ.setSite("哎购商城");
                spQ.setText("哎购商城:"+sp.toString()+"，马上下载哎购商城app使用吧，购物优惠多多哦！");
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(new ShareListener());
                qq.share(spQ);
                break;
            case R.id.tv_share_weixin:
                dismiss();
                Wechat.ShareParams spW = new Wechat.ShareParams();
                spW.setShareType(Platform.SHARE_TEXT);
                spW.setTitle("【来自哎购商城的优质商店】");
                spW.setText("哎购商城:"+sp.toString()+"，马上下载哎购商城app使用吧，购物优惠多多哦！");
                Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
                weixin.setPlatformActionListener(new ShareListener());
                weixin.share(spW);
                break;
            case R.id.tv_share_qzone:
               dismiss();
                QZone.ShareParams spZ = new QZone.ShareParams();
                spZ.setTitle("【来自哎购商城的优质商店】");
                spZ.setTitleUrl(storeLogo); // 标题的超链接
                spZ.setText("哎购商城:"+sp.toString()+"，马上下载哎购商城app使用吧，购物优惠多多哦！");
                //   spZ.setImageUrl("http://www.someserver.com/测试图片网络地址.jpg");
                spZ.setSite("哎购商城");
               // spZ.setSiteUrl("发布分享网站的地址");
                spZ.setImageData(bitmap);

                Platform qzone = ShareSDK.getPlatform (QZone.NAME);
              // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener (new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
                    }
                    public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                        //分享成功的回调
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        //取消分享的回调
                    }
                });
             // 执行图文分享
                qzone.share(spZ);
                break;
            case R.id.tv_share_friends:
                dismiss();
                WechatMoments.ShareParams spF = new WechatMoments.ShareParams();
                spF.setShareType(Platform.SHARE_TEXT);
                spF.setTitle("【来自哎购商城的优质商店】");
                spF.setText("哎购商城:"+sp.toString()+"，马上下载哎购商城app使用吧，购物优惠多多哦！");
                spF.setImageData(bitmap);
                Platform friends = ShareSDK.getPlatform(WechatMoments.NAME);
                friends.setPlatformActionListener(new ShareListener());
                friends.share(spF);
                break;
            case R.id.tv_share_weibo:
                dismiss();
                SinaWeibo.ShareParams spS = new SinaWeibo.ShareParams();
                spS.setShareType(Platform.SHARE_TEXT);
                spS.setTitle("【来自哎购商城的优质商店】");
                spS.setText("哎购商城:"+sp.toString()+"，马上下载哎购商城app使用吧，购物优惠多多哦！");
                spS.setImageData(bitmap);
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
