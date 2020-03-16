package com.didu.lotteryshop.manage.controller;

import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.manage.service.LotteryAManagerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/LotteryAManager")
public class LotteryAManagerContorller {
    @Autowired
    private LotteryAManagerService lotteryAManagerService;

    @RequestMapping("/findLotteryAInfo")
    @ResponseBody
    public ResultUtil findLotteryAInfo(){
        return lotteryAManagerService.findLotteryAInfo();
    }

    @RequestMapping("/contractBalanceIn")
    @ResponseBody
    public ResultUtil contractBalanceIn(String privateKey){
        if(StringUtils.isBlank(privateKey)){
            return ResultUtil.errorJson("parameter error!");
        }
        return lotteryAManagerService.contractBalanceIn("privateKey");
    }
}
