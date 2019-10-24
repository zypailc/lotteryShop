package com.didu.lotteryshop.wallet.service;

import com.didu.lotteryshop.wallet.utils.Web3jUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * gas费用Service
 */
@Service
public class GasProviderService {

    /**
     *  获取gas单价
     * @return
     */
    public  BigInteger getGasPrice(){
        //TODO 数库库提取
       return BigInteger.valueOf(22000000000L);
    }

    /**
     * 获取gas限制
     * @return
     */
    public BigInteger getGasLimit(){
        //TODO 数据库提取
        return BigInteger.valueOf(4300000L);
    }

    /**
     * 获取etherValue+gas费用总的ether
     * @param etherValue
     * @return
     */
    public BigDecimal getAllEther(BigDecimal etherValue){
        return Web3jUtils.etherToAllEther(etherValue,getGasPrice(),getGasLimit());
    }

}
