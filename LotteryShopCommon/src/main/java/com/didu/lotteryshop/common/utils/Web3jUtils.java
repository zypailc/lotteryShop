package com.didu.lotteryshop.common.utils;

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
    public static BigDecimal etherToAllEtherByBigInteger(BigDecimal etherValue,BigInteger gasPrice,BigInteger gasLimit){
        BigDecimal gas =  bigIntegerToBigDecimal(gasPrice.multiply(gasLimit));
        return gas.add(etherValue);
    }

    /**
     * ether+gas总的allEther
     * @param etherValue
     * @param gasPrice
     * @param gasLimit
     * @return
     */
    public static BigDecimal etherToAllEtherByBigDecimal(BigDecimal etherValue,BigDecimal gasPrice,BigDecimal gasLimit){
        BigDecimal gas =  gasPrice.multiply(gasLimit);
        return gas.add(etherValue);
    }

    /**
     * gas最高限制燃气费（ether）
     * @param gasPrice
     * @param gasLimit
     * @return
     */
    public static BigDecimal gasToEtherByBigInteger(BigInteger gasPrice,BigInteger gasLimit){
        BigDecimal gas =  bigIntegerToBigDecimal(gasPrice.multiply(gasLimit));
        return gas;
    }
    /**
     * gas最高限制燃气费（ether）
     * @param gasPrice
     * @param gasLimit
     * @return
     */
    public static BigDecimal gasToEtherByBigDecimal(BigDecimal gasPrice,BigDecimal gasLimit){
        BigDecimal gas =  gasPrice.multiply(gasLimit);
        return gas;
    }
}
