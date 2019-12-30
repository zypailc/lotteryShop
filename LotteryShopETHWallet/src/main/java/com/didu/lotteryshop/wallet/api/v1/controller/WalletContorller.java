package com.didu.lotteryshop.wallet.api.v1.controller;

import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.common.utils.Web3jUtils;
import com.didu.lotteryshop.wallet.annotation.SecurityParameter;
import com.didu.lotteryshop.wallet.api.v1.RequestEntity.FindBalanceEntity;
import com.didu.lotteryshop.wallet.api.v1.RequestEntity.FindTransactionStatusEntity;
import com.didu.lotteryshop.wallet.api.v1.RequestEntity.GenerateWalletEntity;
import com.didu.lotteryshop.wallet.api.v1.RequestEntity.TransferEntity;
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
            //交易密文不能为空！
           return ResultUtil.errorJson("The payPassword cannot be empty!");
        }
        return walletService.generateWallet(gwEntity.getUserId(),gwEntity.getPaymentCode());
    }

    /**
     *  查询钱包余额
     * @return
     */
    @PostMapping(value = "/findBalance",consumes = "application/json")
    @ResponseBody
    @SecurityParameter
    public ResultUtil findBalance(@RequestBody FindBalanceEntity findBalanceEntity){
        if(findBalanceEntity == null){
            //参数错误
            return ResultUtil.errorJson("Parameter error!");
        }
        if(StringUtils.isBlank(findBalanceEntity.getAddress()))
            return ResultUtil.errorJson("The address cannot be empty!");  //钱包地址不能为空！
        if(!Web3jUtils.isETHValidAddress(findBalanceEntity.getAddress()))
            return ResultUtil.errorJson("The address error!");//钱包地址不能为空！
        return walletService.findBalance(findBalanceEntity.getAddress());
    }


     /**
     *  转账
     * @return
     */
    @PostMapping(value = "/transfer",consumes = "application/json")
    @ResponseBody
    @SecurityParameter
    public ResultUtil transfer(@RequestBody TransferEntity transferEntity){
        if(transferEntity == null || StringUtils.isBlank(transferEntity.getWalletFileName())
                || StringUtils.isBlank(transferEntity.getPayPassword()) || StringUtils.isBlank(transferEntity.getFormAddress())
                || StringUtils.isBlank(transferEntity.getToAddress()) || StringUtils.isBlank(transferEntity.getEtherValue())){
            //参数错误！
            return ResultUtil.errorJson("Invalid parameter!");
        }
        if(!Web3jUtils.isETHValidAddress(transferEntity.getFormAddress())){
            return  ResultUtil.errorJson("Invalid formAddress!");
        }
        if(!Web3jUtils.isETHValidAddress(transferEntity.getToAddress())){
            return  ResultUtil.errorJson("Invalid toAddress!");
        }
        return walletService.transfer(transferEntity.getWalletFileName(),transferEntity.getPayPassword(),transferEntity.getFormAddress(),transferEntity.getToAddress(),transferEntity.getEtherValue());
    }

    /**
     * 查询交易状态
     * @return
     */
    @PostMapping(value = "/findTransactionStatus",consumes = "application/json")
    @ResponseBody
    @SecurityParameter
    public ResultUtil findTransactionStatus(@RequestBody FindTransactionStatusEntity findTransactionStatusEntity){
        if(StringUtils.isBlank(findTransactionStatusEntity.getTransactionHashValue())){
            //transactionHashValue 参数不能为空
            return ResultUtil.errorJson("The transactionHashValue cannot be empty!");
        }
        return walletService.findTransactionStatus(findTransactionStatusEntity.getTransactionHashValue());
    }


// TODO 2019-12-11 lyl 注释，因返回钱包秘钥，直接用秘钥生成Credentials 无法进行操作。需要进一步研究。

//    /**
//     * 查询钱包明细
//     * @return
//     */
//    @PostMapping(value = "/findWalletDetail",consumes = "application/json")
//    @ResponseBody
//    @SecurityParameter
//    public ResultUtil findWalletDetail(@RequestBody FindWalletDetailEntity findWalletDetailEntity){
//
//        if(findWalletDetailEntity == null || StringUtils.isBlank(findWalletDetailEntity.getWalletFileName()) || StringUtils.isBlank(findWalletDetailEntity.getPayPassword())){
//            //参数错误
//            return ResultUtil.errorJson("Parameter error!");
//        }
//        return walletService.findWalletDetail(findWalletDetailEntity.getWalletFileName(),findWalletDetailEntity.getPayPassword());
//    }
}
