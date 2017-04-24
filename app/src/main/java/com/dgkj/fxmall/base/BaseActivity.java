package com.dgkj.fxmall.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.constans.Position;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;

/**我的模块activity基类
 * Created by 成焕文 on 2016/8/8.
 */
public class BaseActivity extends AppCompatActivity {
    public boolean DEBUG_MODE = true;
    public Toast toast;
    public SharedPreferencesUnit sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
        if(Build.VERSION.SDK_INT >= 21 ){
            getWindow().setStatusBarColor(Color.parseColor("#62b1fe"));
        }
        sp = SharedPreferencesUnit.getInstance(this);
    }


    //设置头部布局的方法
    public void setHeaderTitle(View headerView, String title, Position position) {
        TextView tv = (TextView) headerView.findViewById(R.id.tv_title_center);

        if (title == null) {
            tv.setText("TITLE");
        } else {
            tv.setText(title);
        }

        switch (position) {
            case LEFT:
                tv.setGravity(Gravity.LEFT);
                break;

            default:
                tv.setGravity(Gravity.CENTER);
                break;
        }

    }

    //重载方法，设置标题时不指定位置，就让标题居中显示
    public void setHeaderTitle(View headerView, String title) {
        setHeaderTitle(headerView, title, Position.CENTER);
    }

    /**
     * @param headerView
     * @param resId
     * @param position   LEFT 是设置头部左侧的ImageView
     *                   RIGHT 、 CENTER均为设置头部右侧的ImageView
     * @param listener
     */
    public void setHeaderImage(View headerView, int resId,String right, Position position, View.OnClickListener listener) {
        TextView tv = null;
        ImageView iv = null;
        FrameLayout fl = null;


        switch (position) {
            case LEFT:
                iv = (ImageView) headerView.findViewById(R.id.iv_title_left);
                fl = (FrameLayout) headerView.findViewById(R.id.fl_left);
                iv.setImageResource(resId);
                break;

            case RIGHT:
                tv = (TextView) headerView.findViewById(R.id.iv_title_right);
                tv.setText(right);
                fl = (FrameLayout) headerView.findViewById(R.id.fl_right);
                break;
        }

        // iv.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        if (listener != null) {
           /* iv.setOnClickListener(listener);
            tv.setOnClickListener(listener);*/
            fl.setOnClickListener(listener);
        }
    }

    public void setHeaderImage(View headerView, int resId, Position position) {
        setHeaderImage(headerView, resId,null, position, null);
    }

    public void setHeaderImage(View headerView, int resId) {
        setHeaderImage(headerView, resId, Position.LEFT);
    }

    //写一些打印“吐司”和Log的方法

    public void toast(String text) {

        if (TextUtils.isEmpty(text)) {
            return;
        }
        toast.setText(text);
        toast.show();
    }

    public void log(String log) {
        if (DEBUG_MODE)
            LogUtil.d("TAG", this.getClass().getSimpleName() + ": " + log);
    }

    public void toastAndLog(String text, String log) {
        toast(text);
        log(log);
    }

    //写一些界面跳转的方法
    //简单的界面跳转，不需要利用Intent传递参数
    public void jumpTo(Class<?> clazz, boolean isFinish) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        if (isFinish) {
            finish();
        }
    }

    //界面跳转时，需要Intent携带参数
    public void jumpTo(Intent intent, boolean isFinish) {
        startActivity(intent);
        if (isFinish) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    public void toastInUI(final Context context, final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
