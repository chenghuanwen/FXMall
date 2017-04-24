package com.dgkj.fxmall.view.myView;

import android.app.AlertDialog;
import android.content.Context;

import com.dgkj.fxmall.R;

/**
 * Created by Android004 on 2017/4/15.
 */

public class TransparentDialog extends AlertDialog {
    protected TransparentDialog(Context context) {
        super(context);
    }

    protected TransparentDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected TransparentDialog(Context context, int themeResId) {
        super(context, R.style.transparentDialog);
    }
}
