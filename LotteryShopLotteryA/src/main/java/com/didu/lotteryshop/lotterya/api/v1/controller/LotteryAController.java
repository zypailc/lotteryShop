package com.didu.lotteryshop.lotterya.api.v1.controller;

import com.didu.lotteryshop.common.utils.NumberValidationUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.lotterya.api.v1.service.LotteryAService;
import com.didu.lotteryshop.lotterya.controller.LotteryABaseController;
import com.didu.lotteryshop.lotterya.entity.LotteryaBuy;
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
           luckNum.length() != 3 || !NumberValidationUtil.isNumeric(luckNum)
        ){
            String msg = "parameter error !";
            if(super.isChineseLanguage()){
                msg = "參數錯誤!";
            }
            //参数错误
            return  ResultUtil.errorJson(msg);
        }
        if(multipleNumber <= 0 || multipleNumber > 100){
            String msg = "If the multiple exceeds the maximum multiple, lower the multiple !";
            if(super.isChineseLanguage()){
                msg = "如果倍數超過最大倍數，則降低倍數!";
            }
            return ResultUtil.errorJson(msg);
        }
        return lotteryAService.ethBuyLottery(luckNum,multipleNumber,payPasswod);
    }

    /**
     * 查询购买数据
     * @param currentPage
     * @param pageSize
     * @param isOneself 是否只查询自己 0：否 ；1：是
     * @param mTransferStatus 状态格式 :1,2
     * @param lotteryaBuy
     * @param type
     * @return
     */
    @RequestMapping("/getLotteryBuy")
    @ResponseBody
    public ResultUtil getLotteryBuy(Integer currentPage, Integer pageSize, Integer isOneself, String mTransferStatus, LotteryaBuy lotteryaBuy, Integer type){
        currentPage =  currentPage == null ? 1:currentPage;
        pageSize = pageSize == null ? 20 : pageSize;
        type = type == null ? 1:type;
        return lotteryAService.getLotteryBuy(currentPage,pageSize,isOneself,mTransferStatus,lotteryaBuy,type);
    }

}
