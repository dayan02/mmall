package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by 大燕 on 2018/12/4.
 */

public class DateTimeUtil {
    //使用joda-time更快进行时间格式转换

    public final static  String STANDARD_FORMATE ="yyyy-MM-dd HH:mm:ss";
    //str--date或者date--str
    public static Date strToDate(String dateTimeStr,String formatStr){
        //SimpleDateFormat
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

public static  String dateToStr(Date date,String formatStr){
    if (date == null){
         return StringUtils.EMPTY;
    }else{
         DateTime dateTime = new DateTime(date);
         return dateTime.toString(formatStr);
}
}
    public static Date strToDate(String dateTimeStr){
        //SimpleDateFormat
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMATE);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static  String dateToStr(Date date){
        if (date == null){
            return StringUtils.EMPTY;
        }else{
            DateTime dateTime = new DateTime(date);
            return dateTime.toString(STANDARD_FORMATE);
        }
    }
    public static void main(String[] args) {
        System.out.println(strToDate("2018-11-11 11:11:11","yyyy-MM-dd HH:mm:ss"));
        System.out.println(dateToStr(new Date(),"yyyy-MM--dd HH:mm:ss"));
    }
}
