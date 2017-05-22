package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.DistrictSelectBean;
import com.dgkj.fxmall.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Android004 on 2017/4/7.
 */

public class PostageDistrictSelectAdapter extends BaseExpandableListAdapter {
    private Map<String,List<DistrictSelectBean>> dataSet;
    private List<DistrictSelectBean> parentList;
    private Context context;
    private List<String> selectDistricts ;

    public PostageDistrictSelectAdapter(List<DistrictSelectBean> parentList,Map<String, List<DistrictSelectBean>> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
        this.parentList = parentList;
        selectDistricts = new ArrayList<>();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return dataSet.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return dataSet.get(parentList.get(groupPosition).getDistrict()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dataSet.get(parentList.get(groupPosition).getDistrict());
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dataSet.get(parentList.get(groupPosition).getDistrict()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_postage_district_select,null,false);
            vh.tvDistrict = (TextView) convertView.findViewById(R.id.tv_district);
            vh.cbSelect = (CheckBox) convertView.findViewById(R.id.cb_select_district);
            vh.ivRight = (ImageView) convertView.findViewById(R.id.iv_super_district_item);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }

        final DistrictSelectBean districtSelectBean = parentList.get(groupPosition);
        vh.tvDistrict.setText(districtSelectBean.getDistrict());
        if(isExpanded){
            vh.ivRight.setImageResource(R.mipmap.dpsp_fhj);
            vh.tvDistrict.setTextColor(Color.parseColor("#62b1fe"));
        }else {
            vh.ivRight.setImageResource(R.mipmap.dpsp_xlj);
            vh.tvDistrict.setTextColor(Color.parseColor("#333333"));
        }

       /* if(districtSelectBean.isSelected()){
            vh.cbSelect.setChecked(true);
        }else {
            vh.cbSelect.setChecked(false);
        }


        final List<DistrictSelectBean> selectBeanList = dataSet.get(districtSelectBean.getDistrict());
        vh.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    districtSelectBean.setSelected(true);//子项全选
                    for (DistrictSelectBean selectBean : selectBeanList) {
                        selectBean.setSelected(true);
                    }
                    LogUtil.i("TAG","全选子项=========");
                }else {
                    districtSelectBean.setSelected(false);//子项取消全选
                    for (DistrictSelectBean selectBean : selectBeanList) {
                        selectBean.setSelected(false);
                    }
                    LogUtil.i("TAG","取消全选=========");
                }

                notifyDataSetChanged();
            }
        });*/
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_postage_sub_district_select,null,false);
            vh.tvDistrict = (TextView) convertView.findViewById(R.id.tv_district);
            vh.cbSelect = (CheckBox) convertView.findViewById(R.id.cb_select_district);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }

        final DistrictSelectBean bean = dataSet.get(parentList.get(groupPosition).getDistrict()).get(childPosition);
        vh.tvDistrict.setText(bean.getDistrict());
        if(bean.isSelected()){
            vh.cbSelect.setChecked(true);
        }else {
            vh.cbSelect.setChecked(false);
        }

        vh.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    bean.setSelected(true);
                    selectDistricts.add(bean.getDistrict());
                }else {
                    bean.setSelected(false);
                    selectDistricts.remove(bean.getDistrict());
                }
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    class ViewHolder{
        private TextView tvDistrict;
        private CheckBox cbSelect;
        private ImageView ivRight;
    }

    public List<String> getSelectDistricts(){
        return selectDistricts;
    }
}
