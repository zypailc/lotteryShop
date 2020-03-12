package com.didu.lotteryshop.lotteryb.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NumberUtil {

    /**
     * 生成数组 选三次
     */
    public static List<String> createNumberCombination(){
        List<String> list = new ArrayList<>();
        list.add("1,2,3#2,4,34,28,29,30,53,54,55,56,57,58,8");//双 小 6 顺子 三不同号 二不同号 （2,4, 34, 28,29,30, 53,54,55,56,57,58, 8）
        list.add("1,2,4#1,4,28,29,31,53,54,55,56,57,58,9");//单 小 7 三不同号 二不同号（1,4,28,29,31, 53,54,55,56,57,58, 9）
        list.add("1,2,5#2,4,28,29,32,53,54,55,56,57,58,10");//双 小 8 三不同号 二不同号（2,4,28,29,30,31,32,33, 53,54,55,56,57,58, 10）
        list.add("1,2,6#1,4,28,29,33,53,54,55,56,57,58,11");//单 小 9 三不同号 二不同号 （1,4,28,29,30,31,32,33, 53,54,55,56,57,58, 11）
        list.add("1,3,4#2,4,28,30,31,53,54,55,56,57,58,10");//双 小 8 三不同号 二不同号（2,4,28,29,30,31,32,33, 53,54,55,56,57,58,10）
        list.add("1,3,5#1,4,28,30,32,53,54,55,56,57,58,11");//单 小 9 三不同号 二不同号（1,4,28,29,30,31,32,33, 53,54,55,56,57,58, 11）
        list.add("1,3,6#2,4,28,30,33,53,54,55,56,57,58,12");//双 小 10 三不同号 二不同号（2,4,28,29,30,31,32,33, 53,54,55,56,57,58, 12）
        list.add("1,4,5#2,4,28,31,32,53,54,55,56,57,58,12");//双 小 10 三不同号 二不同号（2,4,28,29,30,31,32,33, 53,54,55,56,57,58, 12）
        list.add("1,4,6#1,3,28,31,33,53,54,55,56,57,58,13");//单 大 11 三不同号 二不同号（1,3,28,29,30,31,32,33, 53,54,55,56,57,58, 13）
        list.add("1,5,6#2,3,28,32,33,53,54,55,56,57,58,14");//双 大 12 三不同号 二不同号（2,3,28,29,30,31,32,33, 53,54,55,56,57,58, 14）
        list.add("2,3,4#1,4,34,29,30,31,53,54,55,56,57,58,11");//单 小 9 顺子 三不同号 二不同号（1,4, 34, 28,29,30,31,32,33, 53,54,55,56,57,58, 11）
        list.add("2,3,5#2,4,29,30,32,53,54,55,56,57,58,12");//双 小 10 三不同号 二不同号（2,4, 28,29,30,31,32,33, 53,54,55,56,57,58, 12）
        list.add("2,3,6#1,3,29,30,33,53,54,55,56,57,58,13");//单 大 11 三不同号 二不同号（1,3, 28,29,30,31,32,33, 53,54,55,56,57,58, 13）
        list.add("2,4,5#1,3,29,31,32,53,54,55,56,57,58,13");//单 大 11 三不同号 二不同号（1,3, 28,29,30,31,32,33, 53,54,55,56,57,58 ,13）
        list.add("2,4,6#2,3,29,31,33,53,54,55,56,57,58,14");//双 大 12 三不同号 二不同号（2,3, 28,29,30,31,32,33, 53,54,55,56,57,58, 14）
        list.add("2,5,6#1,3,29,32,33,53,54,55,56,57,58,15");//单 大 13 三不同号 二不同号（1,3, 28,29,30,31,32,33, 53,54,55,56,57,58 ,15）
        list.add("3,4,5#2,3,34,30,31,32,53,54,55,56,57,58,14");//双 大 12 顺子 三不同号 二不同号 （2,3, 34, 28,29,30,31,32,33, 53,54,55,56,57,58, 14）
        list.add("3,4,6#1,3,30,31,33,53,54,55,56,57,58,15");//单 大 13 三不同号 二不同号 （1,3, 28,29,30,31,32,33, 53,54,55,56,57,58, 15）
        list.add("3,5,6#2,3,30,32,33,53,54,55,56,57,58,16");//双 大 14 三不同号 二不同号（2,3, 28,29,30,31,32,33, 53,54,55,56,57,58, 16）
        list.add("4,5,6#1,3,34,31,32,33,53,54,55,56,57,58,17");//单 大 15 顺子 三不同号 二不同号（1,3, 34, 28,29,30,31,32,33, 53,54,55,56,57,58, 17）

        list.add("1,1,1#1,4,21,22,5");//单 小 3 同号 (1,4,21,22, 5)
        list.add("2,2,2#2,4,21,23,8");//双 小 6 同号(2,4,21,23, 8)
        list.add("3,3,3#1,4,21,24,11");//单 小 9 同号(1,4,21,24, 11)
        list.add("4,4,4#2,3,21,25,14");//双 大 12 同号(2,3,21,25, 14)
        list.add("5,5,5#1,3,21,26,17");//单 大 15 同号(1,3,21,26, 17)
        list.add("6,6,6#2,3,21,27,20");//双 大 18 同号(2,3,21,27, 20)

        list.add("1,1,2#2,4,35,41,48,53,54,6");//双 小 4 二同号 二不同号 (2,4, 35,36,37,38,39,40 ,41,48, 53,54, 6)
        list.add("1,1,3#1,4,35,41,49,53,55,7");//单 小 5 二同号 二不同号 (1,4, 35,36,37,38,39,40 ,41,49, 53,55, 7)
        list.add("1,1,4#2,4,35,41,50,53,56,8");//双 小 6 二同号 二不同号 (2,4, 35,36,37,38,39,40 ,41,50,  53,56, 8)
        list.add("1,1,5#1,4,35,41,51,53,57,9");//单 小 7 二同号 二不同号 (1,4, 35,36,37,38,39,40 ,41,51, 53,57, 9)
        list.add("1,1,6#2,4,35,41,52,53,58,10");//双 小 8 二同号 二不同号 (2,4, 35,36,37,38,39,40 ,41,52,  53,58, 10)

        list.add("1,2,2#1,4,36,42,47,53,54,7");//单 小 5 二同号 二不同号 (1,4, 35,36,37,38,39,40 ,42,47, 53,54, 7)
        list.add("2,2,3#1,4,36,42,49,54,55,9");//单 小 7 二同号 二不同号 (1,4, 35,36,37,38,39,40 ,42,49, 54,55, 9)
        list.add("2,2,4#2,4,36,42,50,54,56,10");//双 小 8 二同号 二不同号 (2,4, 35,36,37,38,39,40 ,42,50 54,56, 10)
        list.add("2,2,5#1,4,36,42,51,54,57,11");//单 小 9 二同号 二不同号 (1,4, 35,36,37,38,39,40 ,42,51, 54,57, 11)
        list.add("2,2,6#2,4,36,42,52,54,58,12");//双 小 10 二同号 二不同号 (2,4, 35,36,37,38,39,40 ,42,52  54,58, 12)

        list.add("1,3,3#1,4,37,43,47,53,55,9");//单 小 7 二同号 二不同号 (1,4, 35,36,37,38,39,40 ,43,47, 53,55, 9)
        list.add("2,3,3#2,4,37,43,48,54,55,10");//双 小 8 二同号 二不同号 (2,4, 35,36,37,38,39,40 ,43,48, 54,55, 10)
        list.add("3,3,4#2,4,37,43,50,55,56,12");//双 小 10 二同号 二不同号 (2,4, 35,36,37,38,39,40 ,43,50, 55,56, 12)
        list.add("3,3,5#1,3,37,43,51,55,57,13");//单 大 11 二同号 二不同号 (1,3, 35,36,37,38,39,40 ,43,51, 55,57, 13)
        list.add("3,3,6#2,3,37,43,52,55,58,14");//双 大 12 二同号 二不同号 (2,3, 35,36,37,38,39,40 ,43,52, 55,58, 14)

        list.add("1,4,4#1,4,38,44,47,53,56,11");//单 小 9 二同号 二不同号 (1,4, 35,36,37,38,39,40 ,44,47, 53,56, 11)
        list.add("2,4,4#2,4,38,44,48,54,56,12");//双 小 10 二同号 二不同号 (2,4, 35,36,37,38,39,40 ,44,48, 54,56, 12)
        list.add("3,4,4#2,3,38,44,49,55,56,13");//双 大 11 二同号 二不同号 (2,3, 35,36,37,38,39,40 ,44,49, 55,56, 13)
        list.add("4,4,5#1,3,38,44,51,56,57,15");//单 大 13 二同号 二不同号 (1,3, 35,36,37,38,39,40 ,44,51, 56,57, 15)
        list.add("4,4,6#2,3,38,44,52,56,58,16");//双 大 14 二同号 二不同号 (2,3, 35,36,37,38,39,40 ,44,52, 56,58, 16)

        list.add("1,5,5#1,3,39,45,47,53,57,13");//单 大 11 二同号 二不同号 (1,3, 35,36,37,38,39,40 ,45,47, 53,57, 13)
        list.add("2,5,5#2,3,39,45,48,54,57,14");//双 大 12 二同号 二不同号 (2,3, 35,36,37,38,39,40 ,45,48, 54,57, 14)
        list.add("5,5,3#1,3,39,45,49,55,57,15");//单 大 13 二同号 二不同号 (1,3, 35,36,37,38,39,40 ,45,49, 55,56,15)
        list.add("4,5,5#2,3,39,45,50,56,57,16");//双 大 14 二同号 二不同号 (2,3, 35,36,37,38,39,40 ,45,50, 56,57 16)
        list.add("5,5,6#2,3,39,45,52,57,58,18");//双 大 16 二同号 二不同号 (2,3, 35,36,37,38,39,40 ,45,52, 57,58, 18)

        list.add("1,6,6#1,3,40,46,47,53,58,15");//单 大 13 二同号 二不同号 (1,3, 35,36,37,38,39,40 ,46,47, 53,58, 15)
        list.add("2,6,6#2,3,40,46,48,54,58,16");//双 大 14 二同号 二不同号 (2,3, 35,36,37,38,39,40 ,46,48, 54,58, 16)
        list.add("3,6,6#1,3,40,46,49,55,58,17");//单 大 15 二同号 二不同号 (1,3, 35,36,37,38,39,40 ,46,49, 55,58, 17)
        list.add("4,6,6#2,3,40,46,50,56,58,18");//双 大 16 二同号 二不同号 (2,3, 35,36,37,38,39,40 ,46,50 ,56,58, 18)
        list.add("5,6,6#1,3,40,46,51,57,58,19");//单 大 17 二同号 二不同号 (1,3, 35,36,37,38,39,40 ,46,51, 57,58, 19)
        return list;
    }

    public static void main(String [] args){

        //System.out.println(xxxx("1,2,3,4".split(","),3));
        //System.out.println(createNumberCombination().size());
        BigDecimal b1 = new BigDecimal("3");
        BigDecimal b2 = new BigDecimal("2");
        System.out.println(b1.divide(b2));

    }

}
