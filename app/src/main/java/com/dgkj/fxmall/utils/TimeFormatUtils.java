package com.dgkj.fxmall.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Android004 on 2017/4/24.
 */

public class TimeFormatUtils {
    public static String long2String(long time){
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.format(date);
        return sdf.format(date);
    }
}
