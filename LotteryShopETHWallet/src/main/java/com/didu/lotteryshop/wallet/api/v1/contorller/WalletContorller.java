package com.didu.lotteryshop.wallet.api.v1.contorller;

import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.wallet.api.v1.service.WalletService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 钱包Contorller
 * @author CHJ
 * @date 2019-09-26 10:06
 * @param
 * @return
 */
@Controller
@RequestMapping("/v1/wallet")
public class WalletContorller {
    @Autowired
    private WalletService walletService;


    /**
     * 创建钱包
     * @param userId 用户ID
     * @param payPassword 口令
     * @return
     */
    @RequestMapping(value = "/generate")
    @ResponseBody
    public ResultUtil generateWallet(String userId, String payPassword){
        if(StringUtils.isBlank(userId)){
            //用户ID不能为空！
            return ResultUtil.errorJson("The userId cannot be empty!");
        }
        if(StringUtils.isBlank(payPassword)){
            //支付密码不能为空！
           return ResultUtil.errorJson("The payPassword cannot be empty!");
        }
        return walletService.generateWallet(userId,payPassword);
    }

    /**
     *  查询钱包余额
     * @param address 钱包地址
     * @return
     */
    @RequestMapping(value = "/findBalance")
    @ResponseBody
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
     * @param ether ether
     * @return
     */
    @RequestMapping(value = "/transfer")
    @ResponseBody
    public ResultUtil transfer(String walletFileName, String payPassword, String formAddress, String toAddress, String ether){
        if(StringUtils.isBlank(walletFileName) || StringUtils.isBlank(payPassword) || StringUtils.isBlank(formAddress)
                || StringUtils.isBlank(toAddress) || StringUtils.isBlank(ether)){
            //参数错误！
            return ResultUtil.errorJson("Invalid parameter!");
        }
        //TODO 需要判断钱包地址规则
        return walletService.transfer(walletFileName,payPassword,formAddress,toAddress,ether);
    }

    /**
     * 查询交易状态
     * @param transactionHashValue 转账事务哈希码
     * @return
     */
    @RequestMapping(value = "/findTransactionStatus")
    @ResponseBody
    public ResultUtil findTransactionStatus(String transactionHashValue){
        if(StringUtils.isBlank(transactionHashValue)){
            //transactionHashValue 参数不能为空
            return ResultUtil.errorJson("The transactionHashValue cannot be empty!");
        }
        return walletService.findTransactionStatus(transactionHashValue);
    }
}
