package com.didu.lotteryshop.common.utils;

import org.apache.commons.lang.StringUtils;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.web3j.crypto.Keys.ADDRESS_LENGTH_IN_HEX;

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
        BigDecimal gas =  gasToEtherByBigDecimal(gasPrice,gasLimit);
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
        return Convert.fromWei(gas,Convert.Unit.ETHER);
    }

    /**
     * 判断钱包地址是否正确
     * @param addres 钱包地址
     * @return  boolean true 正确；false 错误
     */
    public static boolean isETHValidAddress(String addres) {
        if (StringUtils.isBlank(addres) || !addres.startsWith("0x"))
            return false;
        return isValidAddress(addres);
    }

    /**
     * 判断钱包地址
     * @param addres 钱包地址
     * @return boolean true 正确；false 错误
     */
    private static boolean isValidAddress(String addres) {
        String cleanInput = Numeric.cleanHexPrefix(addres);
        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }
        return cleanInput.length() == ADDRESS_LENGTH_IN_HEX;
    }

    /**
     * 事务返回状态判断（成功）
     * @param status
     * @return
     */
    public static boolean transactionReceiptStatusSuccess(String status){
        boolean bool = false;
        if("0x1".equals(status)){
            bool = true;
        }
        return bool;
    }
    /**
     * 事务返回状态判断（等待）
     * @param status
     * @return
     */
    public static boolean transactionReceiptStatusWait(String status){
        boolean bool = false;
        //TODO 正式坏境测试，状态码，进行修改
        if(true){
            bool = true;
        }
        return bool;
    }
    /**
     * 事务返回状态判断（失败）
     * @param status
     * @return
     */
    public static boolean transactionReceiptStatusFail(String status){
        boolean bool = false;
        //TODO 正式坏境测试，状态码，进行修改
        if(true){
            bool = true;
        }
        return bool;
    }
}
