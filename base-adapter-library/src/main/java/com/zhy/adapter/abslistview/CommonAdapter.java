package com.zhy.adapter.abslistview;

import android.content.Context;
import android.view.View;

import com.zhy.adapter.abslistview.base.ItemViewDelegate;

import java.util.List;

public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T>
{

    private List<T> mDatas;
    public CommonAdapter(Context context, final int layoutId, List<T> datas)
    {
        super(context, datas);
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position)
            {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position)
            {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder viewHolder, T item, int position);

    public void addAll(List<T> data,boolean flag){
            if(flag){
                mDatas.clear();
            }
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

}
