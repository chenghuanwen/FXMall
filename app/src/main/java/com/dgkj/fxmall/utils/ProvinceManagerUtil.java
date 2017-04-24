package com.dgkj.fxmall.utils;

import com.dgkj.fxmall.bean.DistrictSelectBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全国行政区域划分
 * Created by Android004 on 2017/4/7.
 */

public class ProvinceManagerUtil {
    private String[] districts = {"华东地区", "华南地区", "华中地区", "华北地区", "西北地区", "西南地区", "东北地区", "港澳台地区"};
    private String[][] provinces = {{"山东省", "江苏省", "安徽省", "浙江省", "福建省", "上海市"}, {"广东省", "广西省", "海南省"}, {"湖北省", "湖南省", "河南省", "江西省"},
            {"北京市", "天津市", "河北省", "山西省", "内蒙古"}, {"宁夏省", "新疆省", "青海省", "陕西省", "甘肃省"}, {"四川省", "云南省", "贵州省", "西藏", "重庆市"}, {"辽宁省", "吉林省", "黑龙江省"}, {"香港", "澳门", "台湾"}};

    public List<DistrictSelectBean> getDistricts() {
        List<DistrictSelectBean> districtsData = new ArrayList<>();
        for (int i = 0; i < districts.length; i++) {
            DistrictSelectBean bean = new DistrictSelectBean();
            bean.setDistrict(districts[i]);
            bean.setSelected(false);
            districtsData.add(bean);
        }
        return districtsData;
    }


    public List<DistrictSelectBean> getProvince(int position) {
        List<DistrictSelectBean> districtsData = new ArrayList<>();
        String[] province = provinces[position];
        for (int i = 0; i < province.length; i++) {
            DistrictSelectBean bean = new DistrictSelectBean();
            bean.setDistrict(province[i]);
            bean.setSelected(false);
            districtsData.add(bean);
        }
        return districtsData;
    }

    public String queryDistrictForProvince(int groupId){
        return districts[groupId];
    }

}
