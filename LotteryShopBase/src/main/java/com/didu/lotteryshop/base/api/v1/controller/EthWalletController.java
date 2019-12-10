package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.base.api.v1.service.EthWalletService;
import com.didu.lotteryshop.base.controller.BaseBaseController;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
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
    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;
    /**
     * 查询钱包
     * @return
     */
    @RequestMapping("/findEthWallet")
    @ResponseBody
    public ResultUtil findEthwallet(){
        return ethWalletService.findEthwallet();
    }
}
