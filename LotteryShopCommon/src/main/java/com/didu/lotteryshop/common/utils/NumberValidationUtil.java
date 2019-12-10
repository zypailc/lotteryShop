package com.didu.lotteryshop.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数字验证工具
 * @author CHJ
 * @date 2019-12-10
 *
 */
public class NumberValidationUtil {
    /** 3位纯数字 */
    private static String REGEX_3NUMBER =  "^[0-9]{3}$";

    /**
     * 验证3位纯数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile(REGEX_3NUMBER);
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}
