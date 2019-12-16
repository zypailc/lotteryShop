package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.base.api.v1.service.WalletTestService;
import com.didu.lotteryshop.base.controller.BaseBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/wallet")
public class WalletTestController extends BaseBaseController {
    @Autowired
    private WalletTestService walletTestService;

    /**
     * 测试获取加密数据
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public String get(){
        return walletTestService.get();
    }

    /**
     * 测试加密数据保存及返回
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public String save(){
       return walletTestService.save();
    }

    /**
     * 测试非加密数据
     * @return
     */
    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        return walletTestService.test();
    }

    @RequestMapping("/test1")
    @ResponseBody
    public String test1(){
        return "test_CHJ";
    }

}
