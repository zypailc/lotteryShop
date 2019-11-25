package com.didu.lotteryshop.common.service;

import com.didu.lotteryshop.common.service.form.impl.SysConfigServiceImpl;
import com.didu.lotteryshop.common.utils.Web3jUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * gas费用Service
 */
@Service
public class GasProviderService {

    @Autowired
    private SysConfigServiceImpl sysConfigService;

    /**
     *  获取gas单价
     * @return
     */
    public  BigInteger getGasPrice(){
       return sysConfigService.getSysConfig().getGasPrice().toBigInteger();
    }

    /**
     * 获取gas限制
     * @return
     */
    public BigInteger getGasLimit(){
        return sysConfigService.getSysConfig().getGasLimit().toBigInteger();
    }

    /**
     * 获取etherValue+gas费用总的ether
     * @param etherValue
     * @return
     */
    public BigDecimal getAllEther(BigDecimal etherValue){
        return Web3jUtils.etherToAllEtherByBigInteger(etherValue,getGasPrice(),getGasLimit());
    }

    /**
     *  获取StaticGasProvider 对象
     * @return
     */
    public StaticGasProvider getStaticGasProvider(){
        return new StaticGasProvider(getGasPrice(),getGasLimit());
    }

}
