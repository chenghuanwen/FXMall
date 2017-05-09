package com.dgkj.fxmall.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dgkj.fxmall.R;

/**
 * Created by Android004 on 2017/5/9.
 */

public class ToastUtil {
    private static Toast toast;
    public static void show(Context context,Activity activity, String msg){
        if(toast == null){
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        View view = activity.getLayoutInflater().inflate(R.layout.layout_toast_dialog, null);
        TextView content = (TextView) view.findViewById(R.id.tv_toast_content);
        content.setText(msg);
        toast.setView(view);
        toast.show();
    }
}
