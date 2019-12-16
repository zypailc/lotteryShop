package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.base.api.v1.service.LsbWalletService;
import com.didu.lotteryshop.base.controller.BaseBaseController;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/v1/lsbWallet")
public class LsbController extends BaseBaseController {

    @Autowired
    private LsbWalletService lsbWalletService;

    /**
     * 查询平台币
     * @return
     */
    @RequestMapping("/findLsbWallet")
    public ResultUtil findLsbWallet(){
        return lsbWalletService.findLsbWallet();
    }

    /**
     * 查詢Dlb流水記錄
     * @param currentPage
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param status
     * @return
     */
    @ResponseBody
    @RequestMapping("/findLsbRecord")
    public ResultUtil findEthRecord(Integer currentPage,Integer pageSize,String startTime,String endTime,String status){
        currentPage =  currentPage == null ? 1:currentPage;
        pageSize = pageSize == null ? 20 : pageSize;
        return lsbWalletService.findLsbWalletRecord(currentPage,pageSize,startTime,endTime,status);
    }

}
