package com.didu.lotteryshop.lotterya.api.v1.controller;

import com.didu.lotteryshop.common.utils.ResultUtil;
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
public class LotteryAController {
    /**
     * eth购买彩票
     * @return
     */
    @RequestMapping("/ethBuyLottery")
    @ResponseBody
    public ResultUtil ethBuyLottery(){
        System.out.println("执行ethBuyLottery函数。。。。。");
        return ResultUtil.successJson("成功了！");
    }


}
