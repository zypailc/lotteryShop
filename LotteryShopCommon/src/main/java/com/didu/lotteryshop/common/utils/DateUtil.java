package com.didu.lotteryshop.common.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

    /**
     * 获取当前时间字符串
     * @param dateFormat  转换格式"yyyyMMdd"
     * @return
     */
    public static String getDateFormat(String dateFormat){
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        String dateStr = format.format(new Date());
        return  dateStr;
    }

    /**
     * 设置时间加几分钟
     * 时间加 " 分钟 "
     * @return
     */
    public static Date getDateAddMinute(Date date,Integer minute){
        Calendar nowTime = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HHmmss");
        System.out.println(format.format(nowTime.getTime()));
        nowTime.setTime(date);
        nowTime.add(Calendar.MINUTE, 5);
        System.out.println(format.format(nowTime.getTime()));
        return nowTime.getTime();
    }

    /**
     * 根据开始日期和结束日期返回两个日期之间的日期字符集
     * @param startDate
     * @param endDate
     * @param dataFormat 参数的时间格式
     * @return
     * @throws ParseException
     */
    public static List<String> getDates(String startDate, String endDate,String dataFormat) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(dataFormat);
        List<String> list = new ArrayList<String>(); //保存日期的集合 
        Date date_start = format.parse(startDate);
        Date date_end = format.parse(endDate);
        Date date =date_start;
        Calendar cd = Calendar.getInstance();//用Calendar 进行日期比较判断
        while (date.getTime() <= date_end.getTime()){
        list.add(format.format(date));
        cd.setTime(date);
        cd.add(Calendar.DATE, 1);//增加一天 放入集合
        date=cd.getTime();
        }
        return list;

    }

    public static void main(String [] args){
        /*String s = getDateFormat("yyyyMMdd");
        System.out.println(s);
        Map<String,Object> map = new HashMap<>();
        map.put("1","1");
        String issueDate = "yyyyMMdd0001".substring(0,7);
        String issueNumber = "yyyyMMdd0001".substring(8,"yyyyMMdd0001".length());
        Integer num = Integer.parseInt(issueNumber) + 1;
        System.out.println(issueDate);
        System.out.println(issueNumber);
        System.out.println(num);
        if(map.isEmpty()){
            System.out.println("我是空的");
        }else {
            System.out.println("我有值");
        }*/
        //getDateAddMinute(new Date(),5);
        BigDecimal b1 = new BigDecimal("11");
        BigDecimal b2 = new BigDecimal("13");
        System.out.println(b1.divide(b2,4,BigDecimal.ROUND_HALF_DOWN));
        System.out.println(b1.divide(b2,8,BigDecimal.ROUND_HALF_DOWN));
        System.out.println(b2.divide(b1,4,BigDecimal.ROUND_HALF_DOWN));
        System.out.println(b2.divide(b1,8,BigDecimal.ROUND_HALF_DOWN));
    }

}
