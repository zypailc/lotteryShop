package com.didu.lotteryshop.lotteryb.api.v1.controller;


import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.lotteryb.controller.LotteryBBaseController;
import com.didu.lotteryshop.lotteryb.entity.LotterybBuy;
import com.didu.lotteryshop.lotteryb.service.LotterybBuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2020-02-18
 */
@Controller
@RequestMapping("/v1/lotteryB")
public class LotteryBController extends LotteryBBaseController {

    @Autowired
    private LotterybBuyService lotterybBuyService;

    /**
     * 平台币购买LotteryB
     * @param dataInfo 数据格式[{'lotterybInfoId':'1'},{'issue':'2020030400001'},{'dataInfo':,'[{'lotterybConfig':'5','type':'1','money':'2','number':'1'}]']
     * @param  issueNum 期数
     * @param lotterybInfoId 玩法Id
     * @param total 购买总金额
     * @return
     */
    @ResponseBody
    @RequestMapping("/buyLotteryb")
    public ResultUtil lsbBuyLottery(String dataInfo, String issueNum, String lotterybInfoId, BigDecimal total){
        if(lotterybInfoId == null || "".equals(lotterybInfoId) ||
            issueNum == null || "".equals(issueNum) ||
            dataInfo == null || "".equals(dataInfo) ||
        total.compareTo(BigDecimal.ZERO) < 1){
            String msg = "parameter error !";
            if(super.isChineseLanguage()){
                msg = "參數錯誤!";
            }
            //参数错误
            return  ResultUtil.errorJson(msg);
        }
        return lotterybBuyService.lsbBuyLottery(lotterybInfoId,issueNum,dataInfo,total);
    }


}
