package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.base.api.v1.service.DlbWalletService;
import com.didu.lotteryshop.base.controller.BaseBaseController;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/v1/dlbWallet")
public class DlbWalletController extends BaseBaseController {

    @Autowired
    private DlbWalletService dlbWalletService;

    /**
     * 查询代领币钱包
     * @return
     */
    @ResponseBody
    @RequestMapping("/findDlbWallet")
    public ResultUtil findDlbWallet(){
        return dlbWalletService.findDlbWallet();
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
    @RequestMapping("/findDlbRecord")
    public ResultUtil findEthRecord(Integer currentPage,Integer pageSize,String startTime,String endTime,String status){
        currentPage =  currentPage == null ? 1:currentPage;
        pageSize = pageSize == null ? 20 : pageSize;
        return dlbWalletService.findDlbWalletRecord(currentPage,pageSize,startTime,endTime,status);
    }

}
