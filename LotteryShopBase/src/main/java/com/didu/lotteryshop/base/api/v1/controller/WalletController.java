package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.base.api.v1.service.WalletService;
import com.didu.lotteryshop.base.controller.BaseBaseController;
import com.didu.lotteryshop.common.service.form.impl.SysConfigServiceImpl;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Controller
@RequestMapping("/v1/baseWallet")
public class WalletController extends BaseBaseController {


    @Autowired
    private WalletService walletService;

    /**
     * 获取钱包信息
     * @return
     */
    @ResponseBody
    @RequestMapping("/walletConfig")
    public ResultUtil walletConfig(){
        return walletService.getWalletConfig();
    }

    /**
     * Eth提现
     * @param sum 提现数
     * @param playCode 支付密码
     * @return
     */
    @ResponseBody
    @RequestMapping("/withdrawCashEth")
    public ResultUtil withdrawCashEth(BigDecimal sum,String playCode){
        return walletService.withdrawCashEth(sum,playCode);
    }

    /**
     * 平台币转ETH
     * @param sum 提现数
     * @return
     */
    @ResponseBody
    @RequestMapping("/withdrawCashLsbToEth")
    public ResultUtil withdrawCashLsbToEth(BigDecimal sum){
        return  walletService.withdrawCashLsbToEth(sum);
    }

    /**
     * Eth转平台币
     * @param sum 数量
     * @param playCode 支付密码
     * @return
     */
    @ResponseBody
    @RequestMapping("/withdrawCashEthToLsb")
    public ResultUtil withdrawCashEthToLsb(BigDecimal sum,String playCode){
        return walletService.withdrawCashEthToLsb(sum,playCode);
    }

}
