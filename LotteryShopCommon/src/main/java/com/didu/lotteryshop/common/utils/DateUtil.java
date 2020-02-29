package com.didu.lotteryshop.common.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {

    /**
     * 获取时间字符串
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
