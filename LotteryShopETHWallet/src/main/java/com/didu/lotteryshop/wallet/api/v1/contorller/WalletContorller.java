package com.didu.lotteryshop.wallet.api.v1.contorller;

import com.didu.lotteryshop.wallet.service.WalletService;
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
     * @param password 口令
     * @return 秘钥文件名称
     */
    @RequestMapping(value = "/createWallet")
    @ResponseBody
    public String createWallet(String password){
        System.out.println("xxx口令："+password);

        return "xxx:"+password;
    }
}
