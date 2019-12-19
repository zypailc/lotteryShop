package com.didu.lotteryshop.base.api.v1.service;

import com.didu.lotteryshop.base.service.BaseBaseService;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.EsLsbwallet;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.service.form.impl.EsLsbaccountsServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.EsLsbwalletServiceImpl;
import com.didu.lotteryshop.common.utils.BigDecimalUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LsbWalletService extends BaseBaseService {

    @Autowired
    private EsLsbwalletServiceImpl esLsbwalletService;
    @Autowired
    private EsLsbaccountsServiceImpl esLsbaccountsService;

    /**
     * 查询平台币
     * @return
     */
    public ResultUtil findLsbWallet(){
        LoginUser loginUser = getLoginUser();
        EsLsbwallet lsbwallet = esLsbwalletService.findByMemberId(loginUser.getId());
        //设置精度
        if(lsbwallet == null){
            lsbwallet = new EsLsbwallet();
            lsbwallet.setTotal(new BigDecimal("0"));
            lsbwallet.setBalance(new BigDecimal("0"));
            lsbwallet.setFreeze(new BigDecimal("0"));
        }else {
            lsbwallet.setTotal(BigDecimalUtil.bigDecimalToPrecision(lsbwallet.getTotal()));
            lsbwallet.setBalance(BigDecimalUtil.bigDecimalToPrecision(lsbwallet.getBalance()));
            lsbwallet.setFreeze(BigDecimalUtil.bigDecimalToPrecision(lsbwallet.getFreeze()));
        }
        return ResultUtil.successJson(lsbwallet);
    }

    /**
     * 查询平台币
     * @param currentPage
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param status 1,2
     * @return
     */
    public ResultUtil findLsbWalletRecord(Integer currentPage,Integer pageSize,String startTime,String endTime,String status){
        LoginUser loginUser = getLoginUser();
        return  ResultUtil.successJson(esLsbaccountsService.findLsbRecordPagination(loginUser.getId(),currentPage,pageSize,startTime,endTime,status));
    }

}
