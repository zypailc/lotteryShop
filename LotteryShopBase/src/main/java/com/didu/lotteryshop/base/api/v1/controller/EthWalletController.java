package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.base.api.v1.service.EthWalletService;
import com.didu.lotteryshop.base.controller.BaseBaseController;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * eth钱包Controller
 * @author CHJ
 * @date 2019-12-10
 */
@Controller
@RequestMapping("/v1/ethWallet")
public class EthWalletController extends BaseBaseController {
    @Autowired
    private EthWalletService ethWalletService;
    /**
     * 查询ETH钱包
     * @return
     */
    @RequestMapping("/findEthWallet")
    @ResponseBody
    public ResultUtil findEthwallet(){
        return ethWalletService.findEthwallet();
    }

    /**
     * 查詢Eth流水記錄
     * @param currentPage
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param status
     * @return
     */
    @ResponseBody
    @RequestMapping("/findEthRecord")
    public ResultUtil findEthRecord(Integer currentPage,Integer pageSize,String startTime,String endTime,String status){
        currentPage =  currentPage == null ? 1:currentPage;
        pageSize = pageSize == null ? 20 : pageSize;
        return ethWalletService.findEthRecord(currentPage,pageSize,startTime,endTime,status);
    }

}
