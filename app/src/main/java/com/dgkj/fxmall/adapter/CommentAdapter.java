package com.dgkj.fxmall.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.CommentBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Android004 on 2017/3/31.
 */

public class CommentAdapter extends CommonAdapter<CommentBean> {
    private Context context;
    public CommentAdapter(Context context, int layoutId, List<CommentBean> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, CommentBean commentBean, int position) {
        holder.setText(R.id.tv_comment_name,commentBean.getName());
        holder.setText(R.id.tv_comment_content,commentBean.getContent());
        CircleImageView civ = holder.getView(R.id.civ_comment);
        Glide.with(context).load(commentBean.getIcon()).placeholder(R.mipmap.android_quanzi).into(civ);
        //TODO 根据平分数选择不同的星星图片
        holder.setImageResource(R.id.iv_comment_stars,R.mipmap.sc_zcx);
    }
}
