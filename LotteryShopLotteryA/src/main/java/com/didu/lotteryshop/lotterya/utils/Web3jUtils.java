package com.didu.lotteryshop.lotterya.utils;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Web3j工具类
 * @author CHJ
 * @date 2019-10-23
 */
public class Web3jUtils {
    /**
     * eth转换工具
     * @param bigInteger
     * @return BigDecimal
     */
    public static BigDecimal bigIntegerToBigDecimal(BigInteger bigInteger){
        BigDecimal integerToDecimalBalance = new BigDecimal(bigInteger.toString());
        BigDecimal bigDecimal = Convert.fromWei(integerToDecimalBalance,Convert.Unit.ETHER);
        return bigDecimal;
    }

    /**
     * ether+gas总的allEther
     * @param etherValue
     * @param gasPrice
     * @param gasLimit
     * @return
     */
    public static BigDecimal etherToAllEther(BigDecimal etherValue,BigInteger gasPrice,BigInteger gasLimit){
        BigDecimal gas =  bigIntegerToBigDecimal(gasPrice.multiply(gasLimit));
        return gas.add(etherValue);
    }
}
