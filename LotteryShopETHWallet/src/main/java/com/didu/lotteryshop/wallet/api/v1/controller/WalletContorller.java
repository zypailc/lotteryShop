package com.didu.lotteryshop.wallet.api.v1.controller;

import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.wallet.annotation.SecurityParameter;
import com.didu.lotteryshop.wallet.api.v1.RequestEntity.FindWalletDetailEntity;
import com.didu.lotteryshop.wallet.api.v1.RequestEntity.GenerateWalletEntity;
import com.didu.lotteryshop.wallet.api.v1.service.WalletService;
import com.didu.lotteryshop.wallet.controller.WalletBaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 钱包Contorller
 * @author CHJ
 * @date 2019-09-26 10:06
 */
@Controller
@RequestMapping("/v1/wallet")
public class WalletContorller extends WalletBaseController {
    @Autowired
    private WalletService walletService;


    /**
     * 创建钱包
     * @param gwEntity
     * @return
     */
    @PostMapping(value = "/generate",consumes = "application/json")
    @ResponseBody
    @SecurityParameter
    public ResultUtil generateWallet( @RequestBody  GenerateWalletEntity gwEntity){
        if(gwEntity == null){
            //参数错误
            return ResultUtil.errorJson("Parameter error!");
        }
        if(StringUtils.isBlank(gwEntity.getUserId())){
            //用户ID不能为空！
            return ResultUtil.errorJson("The userId cannot be empty!");
        }
        if(StringUtils.isBlank(gwEntity.getPaymentCode())){
            //支付密码不能为空！
           return ResultUtil.errorJson("The payPassword cannot be empty!");
        }
        return walletService.generateWallet(gwEntity.getUserId(),gwEntity.getPaymentCode());
    }

    /**
     *  查询钱包余额
     * @param address 钱包地址
     * @return
     */
    @RequestMapping(value = "/findBalance")
    @ResponseBody
    @SecurityParameter
    public ResultUtil findBalance(String address){
        if(StringUtils.isBlank(address)){
            //支付密码不能为空！
            return ResultUtil.errorJson("The address cannot be empty!");
        }
        //TODO 需要判断钱包地址规则
        return walletService.findBalance(address);
    }


     /**
     *  转账
     * @param walletFileName 钱包名字
     * @param payPassword 支付密码
     * @param formAddress 出账地址
     * @param toAddress  入账地址
     * @param etherValue etherValue
     * @return
     */
    @RequestMapping(value = "/transfer")
    @ResponseBody
    @SecurityParameter
    public ResultUtil transfer(String walletFileName, String payPassword, String formAddress, String toAddress, String etherValue){
        if(StringUtils.isBlank(walletFileName) || StringUtils.isBlank(payPassword) || StringUtils.isBlank(formAddress)
                || StringUtils.isBlank(toAddress) || StringUtils.isBlank(etherValue)){
            //参数错误！
            return ResultUtil.errorJson("Invalid parameter!");
        }
        //TODO 需要判断钱包地址规则
        return walletService.transfer(walletFileName,payPassword,formAddress,toAddress,etherValue);
    }

    /**
     * 查询交易状态
     * @param transactionHashValue 转账事务哈希码
     * @return
     */
    @RequestMapping(value = "/findTransactionStatus")
    @ResponseBody
    @SecurityParameter
    public ResultUtil findTransactionStatus(String transactionHashValue){
        if(StringUtils.isBlank(transactionHashValue)){
            //transactionHashValue 参数不能为空
            return ResultUtil.errorJson("The transactionHashValue cannot be empty!");
        }
        return walletService.findTransactionStatus(transactionHashValue);
    }

    /**
     * 查询钱包明细
     * @return
     */
    @PostMapping(value = "/findWalletDetail",consumes = "application/json")
    @ResponseBody
    @SecurityParameter
    public ResultUtil findWalletDetail(@RequestBody FindWalletDetailEntity findWalletDetailEntity){

        if(findWalletDetailEntity == null || StringUtils.isBlank(findWalletDetailEntity.getWalletFileName()) || StringUtils.isBlank(findWalletDetailEntity.getPayPassword())){
            //参数错误
            return ResultUtil.errorJson("Parameter error!");
        }
        return walletService.findWalletDetail(findWalletDetailEntity.getWalletFileName(),findWalletDetailEntity.getPayPassword());
    }
}
