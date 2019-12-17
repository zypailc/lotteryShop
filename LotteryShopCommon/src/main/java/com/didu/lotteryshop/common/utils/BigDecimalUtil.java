package com.didu.lotteryshop.common.utils;

import java.math.BigDecimal;

public class BigDecimalUtil {
    /**页面显示BigDecimal精度*/
    public static final Integer BIGDECIMAL_PRECISION = 6;

    /**
     * bigDecimal 精度转换
     * @param bigDecimal
     * @return
     */
    public static BigDecimal bigDecimalToPrecision(BigDecimal bigDecimal){
        return bigDecimalToPrecision(bigDecimal,BIGDECIMAL_PRECISION);
    }

    /**
     * bigDecimal 精度转换
     * @param bigDecimal
     * @param PRECISION 保留小数位数
     * @return
     */
    public static BigDecimal bigDecimalToPrecision(BigDecimal bigDecimal,Integer PRECISION){
        return bigDecimal.setScale(PRECISION,BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros();
    }

}
