package com.dgkj.fxmall.view.myView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Android004 on 2017/3/25.
 */

public class FullyGridView extends GridView {
    public FullyGridView(Context context) {
        super(context);
    }

    public FullyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
