package com.didu.lotteryshop.common.utils;


import java.util.Random;
import java.util.UUID;

/**
 * 编码生成工具
 */
public class CodeUtil {


    /**
     * 生成UUID
     * @return
     */
    public static String getUuid(){
        return UUID.randomUUID().toString().replaceAll("_","");
    }

    /**
     * 随机生成数字字符串
     */
    public static String getCode(int number){
        //return (Long)((Math.random()*9+1)*1000000000)+"";
        String numbStr = "";
        for(int i = 0; i <  number; i++){
            numbStr += (int)(Math.random()*10);
        }
        return numbStr;
    }

    public static void main(String [] args){
        System.out.println(getCode(10));
    }

}
