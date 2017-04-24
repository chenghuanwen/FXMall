package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.DemandMallClassifyBean;
import com.dgkj.fxmall.view.MoreClassifyActivity;
import com.dgkj.fxmall.view.SomeProductSuperClassifyActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Android004 on 2017/3/23.
 */

public class DemandClassifyAdapter extends CommonAdapter<DemandMallClassifyBean> {
    private Context context;
    private List<DemandMallClassifyBean> datas;
    public DemandClassifyAdapter(Context context, int layoutId, List<DemandMallClassifyBean> datas) {
        super(context, layoutId, datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    protected void convert(ViewHolder holder, final DemandMallClassifyBean classify, int position) {
        holder.setText(R.id.tv_classify_title,classify.getTitle());
        CircleImageView view = holder.getView(R.id.civ_classify_item);
        view.setImageResource(classify.getResId());

        holder.setOnClickListener(R.id.civ_classify_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("更多".equals(classify.getTitle())){
                    Intent intent = new Intent(context, MoreClassifyActivity.class);
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context, SomeProductSuperClassifyActivity.class);
                    intent.putExtra("type",classify.getTitle());
                    context.startActivity(intent);
                }
            }
        });

    }
}