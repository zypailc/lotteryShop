package com.didu.lotteryshop.common.config;

import java.util.Random;

/**
 * 全局静态常量配置文件
 * @author CHJ
 * @date 2019-10-23
 */
public class Constants {
    /**
     *  ethwallt 钱包服务api数据传输加密Key，16位
     */
    public static final String AES_ETHWALLET_KEY = "vvuvj8vyl6obu64w";
    
    public static void main(String[] args) {
        System.out.println(getRandomString(16));
    }

    /**
     * 随机生成字符串
     * @param length 表示生成字符串的长度
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
