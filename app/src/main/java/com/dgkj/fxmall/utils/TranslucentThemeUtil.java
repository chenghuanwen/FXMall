package com.dgkj.fxmall.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/** 使标题栏透明化工具，为标题栏着色
 * Created by Android004 on 2016/10/28.
 */
public class TranslucentThemeUtil {

    /**
     * 标题栏透明化
     */
    public  static void translucentTheme(Activity activity){
        //5.0
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);//将布局延伸到状态栏
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);//将状态栏设为透明状态
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){//4.4到5.0
            WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
            layoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);
        }else {//4.4以下
           // activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 创建并添加新的带颜色的状态栏
     */
    public static void addStatusBarView(Context activity ,ViewGroup headerview){
        View view = new View(activity);
        view.setBackgroundColor(Color.parseColor("#ff6138"));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(activity));

        headerview.addView(view,layoutParams);

    }

    /**
     * 获取状态栏高度
     * @param activity
     * @return
     */
    private static int getStatusBarHeight(Context activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
