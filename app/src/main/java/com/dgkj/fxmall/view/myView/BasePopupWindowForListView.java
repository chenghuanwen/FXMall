package com.dgkj.fxmall.view.myView;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import java.util.List;

/**自定义列表式弹窗基类
 * Created by 成焕文 on 2017/3/9.
 */

public abstract class BasePopupWindowForListView<T> extends PopupWindow {
    protected View mContentView;//布局最外层view
    protected Context context;
    protected List<T> mDatas;//listview 数据集合

    public BasePopupWindowForListView(View contentView, int width, int height, boolean focusable){
        this(contentView,width,height,focusable,null);
    }

    public BasePopupWindowForListView(View contentView, int width, int height, boolean focusable, List<T> mDatas){
        this(contentView,width,height,focusable,mDatas,new Object[0]);
    }

    public BasePopupWindowForListView(View contentView, int width, int height, boolean focusable, List<T> mDatas, Object... params){
        super(contentView,width,height,focusable);
        this.mContentView = contentView;
        context = contentView.getContext();
        if(mDatas!=null){
            this.mDatas = mDatas;
        }
        if(params!=null && params.length>0){
            beforeInitWeNeedSomeParams(params);
        }

        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        setOutsideTouchable(true);
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
                    return true;
                }
                return false;
            }
        });
        initViews();
        initEvents();
        init();

    }

    protected abstract void beforeInitWeNeedSomeParams(Object... params);
    public abstract void initViews();

    public abstract void initEvents();

    public abstract void init();

    public View findViewByID(int id){
        return mContentView.findViewById(id);
    }

    protected static int dp2px(Context context,int dp){
        return (int) (context.getResources().getDisplayMetrics().density*dp+0.5f);

    }

}
