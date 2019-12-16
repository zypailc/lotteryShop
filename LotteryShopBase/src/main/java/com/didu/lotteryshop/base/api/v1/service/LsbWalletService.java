package com.didu.lotteryshop.base.api.v1.service;

import com.didu.lotteryshop.base.service.BaseBaseService;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.service.form.impl.EsLsbaccountsServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.EsLsbwalletServiceImpl;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return ResultUtil.successJson(esLsbwalletService.findByMemberId(loginUser.getId()));
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
