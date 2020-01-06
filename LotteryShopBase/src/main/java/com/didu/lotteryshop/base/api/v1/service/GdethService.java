package com.didu.lotteryshop.base.api.v1.service;

import com.didu.lotteryshop.base.service.BaseBaseService;
import com.didu.lotteryshop.common.entity.EsGdethwallet;
import com.didu.lotteryshop.common.entity.EsLsbwallet;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.service.form.impl.EsGdethaccountsServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.EsGdethwalletServiceImpl;
import com.didu.lotteryshop.common.utils.BigDecimalUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class GdethService extends BaseBaseService {

    @Autowired
    private EsGdethwalletServiceImpl esGdethwalletService;
    @Autowired
    private EsGdethaccountsServiceImpl esGdethaccountsService;

    /**
     * 查询顶级推广商利益分红
     * @return
     */
    public ResultUtil findGdEthWallet(){
        LoginUser loginUser = getLoginUser();
        EsGdethwallet esGdethwallet = esGdethwalletService.findByMemberId(loginUser.getId());
        if(esGdethwallet != null){
            esGdethwallet.setTotal(BigDecimalUtil.bigDecimalToPrecision(esGdethwallet.getTotal()));
            esGdethwallet.setBalance(BigDecimalUtil.bigDecimalToPrecision(esGdethwallet.getBalance()));
            esGdethwallet.setFreeze(BigDecimalUtil.bigDecimalToPrecision(esGdethwallet.getFreeze()));
        }else {
            esGdethwallet = new EsGdethwallet();
            esGdethwallet.setTotal(new BigDecimal("0"));
            esGdethwallet.setBalance(new BigDecimal("0"));
            esGdethwallet.setFreeze(new BigDecimal("0"));
        }
        return ResultUtil.successJson(esGdethwallet);
    }

    /**
     * 查询顶级推广商的推广分红记录
     * @param currentPage
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    public ResultUtil findGdEthRecord(Integer currentPage,Integer pageSize,String startTime,String endTime){
        LoginUser loginUser = getLoginUser();
        return ResultUtil.successJson(esGdethaccountsService.findGdethRecordPagination(loginUser.getId(),currentPage,pageSize,startTime,endTime));
    }

}
