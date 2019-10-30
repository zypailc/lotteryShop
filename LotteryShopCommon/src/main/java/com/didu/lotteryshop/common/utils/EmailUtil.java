package com.didu.lotteryshop.common.utils;

public class EmailUtil {

    /**
     * 验证邮箱格式
     * @param email
     * @return
     */
    public static boolean  verificationEmail(String email){
        boolean tag = true;
        if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
            tag = false;
        }
        return tag;
    }

}
