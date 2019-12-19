package com.didu.lotteryshop.base.api.v1.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.base.service.BaseBaseService;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.EsDlbwallet;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.service.form.impl.EsDlbaccountsServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.EsDlbwalletServiceImpl;
import com.didu.lotteryshop.common.utils.BigDecimalUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DlbWalletService extends BaseBaseService {

    @Autowired
    private EsDlbwalletServiceImpl esDlbwalletService;
    @Autowired
    private EsDlbaccountsServiceImpl esDlbaccountsService;

    /**
     * 查询代领币钱包
     * @return
     */
    public ResultUtil findDlbWallet(){
        LoginUser loginUser = getLoginUser();
        EsDlbwallet esDlbwallet = esDlbwalletService.findByMemberId(loginUser.getId());
        //设置精度
        if(esDlbwallet == null){
            esDlbwallet = new EsDlbwallet();
            esDlbwallet.setTotal(new BigDecimal("0"));
            esDlbwallet.setBalance(new BigDecimal("0"));
            esDlbwallet.setFreeze(new BigDecimal("0"));
        }else {
            esDlbwallet.setTotal(BigDecimalUtil.bigDecimalToPrecision(esDlbwallet.getTotal()));
            esDlbwallet.setBalance(BigDecimalUtil.bigDecimalToPrecision(esDlbwallet.getBalance()));
            esDlbwallet.setFreeze(BigDecimalUtil.bigDecimalToPrecision(esDlbwallet.getFreeze()));
        }
        return ResultUtil.successJson(esDlbwallet);

    }

    /**
     *查询代领币流水记录
     * @param currentPage
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param status 1,2
     * @return
     */
    public ResultUtil findDlbWalletRecord(Integer currentPage,Integer pageSize,String startTime,String endTime,String status){
        LoginUser loginUser = getLoginUser();
        return  ResultUtil.successJson(esDlbaccountsService.findDlbRecordPagination(loginUser.getId(),currentPage,pageSize,startTime,endTime,status));
    }

}
