package com.dgkj.fxmall.view.myView;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.dgkj.fxmall.utils.LogUtil;

/**
 * Created by Android004 on 2017/3/23.
 */

public class MyGridLayoutManager extends GridLayoutManager {
    public MyGridLayoutManager(Context context, int spanCount) {
        //默认方向是VERTICAL
        super(context, spanCount);
    }

    public MyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        int height = 0;
        int childCount = getItemCount();
        if(childCount>0){
            for (int i = 0; i < childCount; i++) {
                //View child = recycler.getViewForPosition(i);//会报数组越界错误
                View child = getChildAt(i);
                if(child==null) return;
                measureChild(child, widthSpec, heightSpec);
                if (i % getSpanCount() == 0) {
                    int measuredHeight = child.getMeasuredHeight() + getDecoratedBottom(child);
                    height += measuredHeight;
                }
            }
            setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), height);
        }else {
            super.onMeasure(recycler,state,widthSpec,heightSpec);
        }

    }
    }

