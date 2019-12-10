package com.didu.lotteryshop;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTest {
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("^[0-9]{3}$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    @Test
    public void testx(){
        String str = "092111";
        System.out.println(this.isNumeric(str));
    }

}
