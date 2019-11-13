package com.didu.lotteryshop.common.config;

import com.didu.lotteryshop.common.entity.Member;

import java.util.Random;

/**
 * 全局静态常量配置文件
 * @author CHJ
 * @date 2019-10-23
 */
public class Constants {
    /** ethwallt 钱包服务api数据传输加密Key，16位 */
    public static final String AES_ETHWALLET_KEY = "vvuvj8vyl6obu64w";
    /** 登录用户session_key */
    public static final String LOGIN_SESSION_KEY = "Login_session_key";
    /** 用户最后修改时间session_key */
    public static final String LOGIN_SESSION_UPDATE_KEY = "Login_session_update_key";
    /** eth钱包管理员私钥加密key，16位 */
    public static final String AES_ETHMANAGER_PRIVATEKEY = "t89k66blfwlrnlhc";
    /** 登陆用户的信息key **/
    public static final  String LOGIN_USER = "login_user";
    /** 全局头像地址配置**/
    public static final  String HEAD_PORTRAIT_URL = "../images/head/tx_01.jpg";

    public static void main(String[] args) {
        System.out.println(getRandomString(16));
    }

    /**
     * 随机生成字符串
     * @param length 表示生成字符串的长度
     * @return
     */
    private static String getRandomString(int length) {
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
