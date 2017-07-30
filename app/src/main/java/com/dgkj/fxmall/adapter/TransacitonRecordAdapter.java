package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.TransactionRecordBean;
import com.dgkj.fxmall.utils.TimeFormatUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Android004 on 2017/4/1.
 */

public class TransacitonRecordAdapter extends CommonAdapter<TransactionRecordBean> {
    private static final int INPUT_TYPE = 0;
    private static final int OUTPUT_TYPE = 1;
    private Context context;

    public TransacitonRecordAdapter(Context context, List<TransactionRecordBean> datas) {
        super(context, -1, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, TransactionRecordBean transactionRecordBean, int position) {
        holder.setText(R.id.tv_transaction_time, TimeFormatUtils.long2String(Long.parseLong(transactionRecordBean.getTime())));
        int itemViewType = getItemViewType(position);
        if(itemViewType==INPUT_TYPE){
            holder.setText(R.id.tv_transaction_money,"+"+transactionRecordBean.getMoney());
        }else {
            holder.setText(R.id.tv_transaction_money,"-"+transactionRecordBean.getMoney());
        }

    }

    @Override
    public int getItemViewType(int position) {
        int type = mDatas.get(position).getType();
        switch (type){
            case 1:
            case 2:
            case 0:
                return INPUT_TYPE;
            case 4:
            case 3:
                return OUTPUT_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null ;
        View view;
        if(viewType==INPUT_TYPE){
            view = mInflater.inflate(R.layout.item_transaction_record_red,parent,false);
            viewHolder = new ViewHolder(context,view);
        }else {
            view = mInflater.inflate(R.layout.item_transaction_record,parent,false);
            viewHolder = new ViewHolder(context,view);
        }
        return viewHolder;
    }
}
