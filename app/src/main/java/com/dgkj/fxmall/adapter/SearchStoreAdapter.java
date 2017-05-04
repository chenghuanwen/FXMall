package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.view.StoreMainPageActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Android004 on 2017/3/27.
 */

public class SearchStoreAdapter extends CommonAdapter<StoreBean> {
    private Context context;
    private String from;
    public SearchStoreAdapter(Context context, int layoutId, List<StoreBean> datas,String from) {
        super(context, layoutId, datas);
        this.from = from;
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, final StoreBean storeBean, int position) {
       CircleImageView icon =  holder.getView(R.id.civ_shangpu_item);
        TextView tvCancle = holder.getView(R.id.tv_delete_recommend);
        if("search".equals(from)){
            tvCancle.setVisibility(View.GONE);
        }else if("recommend".equals(from)){
            tvCancle.setVisibility(View.VISIBLE);
            tvCancle.setClickable(true);
        }else {
            tvCancle.setVisibility(View.VISIBLE);
            tvCancle.setText("推荐人："+storeBean.getRecommender());
            tvCancle.setClickable(false);
        }

        Glide.with(context).load(storeBean.getIconUrl()).placeholder(R.mipmap.android_quanzi).into(icon);
        holder.setText(R.id.tv_store_name,storeBean.getName());
        holder.setText(R.id.tv_store_address,storeBean.getAdress());
        holder.setText(R.id.tv_total_sales,""+storeBean.getTotalScals());
        holder.setText(R.id.tv_total_goods,""+storeBean.getGoodsCount());

        String describe = "描述相符 " + storeBean.getDescribeScore();
        String reasonable = "价格合理 " + storeBean.getPriceScore();
        String qualityOk = "质量满意 " + storeBean.getQualityScore();
       TextView tv1 =  holder.getView(R.id.tv_describe);
        tv1.setText(changeColor(describe));
        TextView tv2 =  holder.getView(R.id.tv_price_reasonable);
        tv2.setText(changeColor(reasonable));
        TextView tv3 =  holder.getView(R.id.tv_quality_ok);
        tv3.setText(changeColor(qualityOk));

        //TODO 根据平分数选择不同的星星图片
        float stars = storeBean.getStars();
        if(stars<=1.0){
            holder.setImageResource(R.id.rb_commend,R.mipmap.dppj_dj1);
        }else if(stars>1.0 && stars<1.9){
            holder.setImageResource(R.id.rb_commend,R.mipmap.lan_yiban);
        }else if(stars>=1.9 && stars<=2.4){
            holder.setImageResource(R.id.rb_commend,R.mipmap.dppj_dj2);
        }else if(stars>2.4 && stars<2.9){
            holder.setImageResource(R.id.rb_commend,R.mipmap.lan_erban);
        }else if(stars>=2.9 && stars<=3.4){
            holder.setImageResource(R.id.rb_commend,R.mipmap.dppj_dj3);
        }else if(stars>3.4 && stars<=3.9){
            holder.setImageResource(R.id.rb_commend,R.mipmap.lan_sanban);
        }else if(stars>3.9 && stars<=4.4){
            holder.setImageResource(R.id.rb_commend,R.mipmap.dppj_dj4);
        }else if(stars>4.4 && stars<=4.9){
            holder.setImageResource(R.id.rb_commend,R.mipmap.lan_siban);
        }else if(stars>4.9){
            holder.setImageResource(R.id.rb_commend,R.mipmap.dppj_dj5);
        }


        holder.setOnClickListener(R.id.tv_enter_store, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           // TODO 进入店铺详情界面
                Intent intent = new Intent(context, StoreMainPageActivity.class);
                intent.putExtra("store",storeBean);
                context.startActivity(intent);
            }
        });

        holder.setOnClickListener(R.id.tv_delete_recommend, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //TODO 通知服务器删除推荐
                mDatas.remove(storeBean);
                notifyDataSetChanged();
            }
        });

    }


    public SpannableString changeColor(String s){
        SpannableString sp = new SpannableString(s);
        Pattern pattern = Pattern.compile("\\d+\\.?\\d*");
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
           // LogUtil.i("TAG","start=="+start+"end=="+end);
            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#ff4a42")),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sp;
    }
}
