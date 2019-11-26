package com.didu.lotteryshop.lotterya.api.v1.controller;

import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.lotterya.api.v1.service.LotteryAService;
import com.didu.lotteryshop.lotterya.controller.LotteryABaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A彩票Controller
 * @author CHJ
 * @date 2019-11-01
 */
@Controller
@RequestMapping("/v1/lotteryA")
public class LotteryAController extends LotteryABaseController {
    @Autowired
    private LotteryAService lotteryAService;

    /**
     * 获取彩票基本信息
     * @return
     */
    @RequestMapping("/getLotteryInfo")
    @ResponseBody
    public ResultUtil getLotteryInfo(){
        return lotteryAService.getLotteryInfo();
    }

    /**
     * eth购买彩票
     * @param luckNum 幸运号码
     * @param multipleNumber 倍数
     * @param  payPasswod 支付密码
     * @return
     */
    @RequestMapping("/ethBuyLottery")
    @ResponseBody
    public ResultUtil ethBuyLottery(String luckNum,Integer multipleNumber,String payPasswod){
        if(StringUtils.isBlank(luckNum) || multipleNumber == null || StringUtils.isBlank(payPasswod) ||
           luckNum.length() != 3 || multipleNumber <= 0 || multipleNumber > 100
        ){
            //参数错误
            return ResultUtil.errorJson("Parameter error!");
        }
        return lotteryAService.ethBuyLottery(luckNum,multipleNumber,payPasswod);
    }

}
