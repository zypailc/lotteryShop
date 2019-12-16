package com.didu.lotteryshop.base.api.v1.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.base.service.BaseBaseService;
import com.didu.lotteryshop.common.entity.EsDlbwallet;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.service.form.impl.EsDlbaccountsServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.EsDlbwalletServiceImpl;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return ResultUtil.successJson(esDlbwalletService.findByMemberId(loginUser.getId()));

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
