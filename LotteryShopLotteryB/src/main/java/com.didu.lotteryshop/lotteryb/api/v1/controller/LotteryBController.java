package com.didu.lotteryshop.lotteryb.api.v1.controller;


import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.lotteryb.controller.LotteryBBaseController;
import com.didu.lotteryshop.lotteryb.entity.LotterybBuy;
import com.didu.lotteryshop.lotteryb.service.LotterybBuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
     * @param lotterybInfoId LotteryB 玩法Id
     * @param lotteryConfigId LotteryB 可购买的配置Id
     * @param issueNum 购买期数
     * @param total 购买金额
     * @param playCode 支付密码
     * @return
     */
    public ResultUtil lsbBuyLottery(String lotterybInfoId,String lotteryConfigId,String issueNum,String total,String playCode){
        if(lotterybInfoId == null || "".equals(lotterybInfoId) ||
        lotteryConfigId == null || "".equals(lotteryConfigId) ||
        issueNum == null || "".equals(issueNum) ||
        playCode == null || "".equals(playCode) ||
        total == null || "".equals(total)){
            String msg = "parameter error !";
            if(super.isChineseLanguage()){
                msg = "參數錯誤!";
            }
            //参数错误
            return  ResultUtil.errorJson(msg);
        }
        return lotterybBuyService.lsbBuyLottery(lotterybInfoId,lotteryConfigId,issueNum,playCode,total);
    }


}
