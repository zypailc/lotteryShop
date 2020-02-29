package com.didu.lotteryshop.lotteryb.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.service.form.impl.EsLsbaccountsServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.EsLsbwalletServiceImpl;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.lotteryb.entity.LotterybBuy;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybBuyServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class LotterybBuyService extends LotteryBBaseService{

    @Autowired
    private EsLsbwalletServiceImpl esLsbwalletService;
    @Autowired
    private EsLsbaccountsServiceImpl lsbaccountsService;
    @Autowired
    private LotterybInfoService lotterybInfoService;
    @Autowired
    private LotterybBuyServiceImpl lotterybBuyService;
    @Autowired
    private LotterybStatisticsService lotterybStatisticsService;

    /**
     * 购买
     * @param lotterybInfoId
     * @param lotteryConfigId
     * @param issueNum
     * @param playCode
     * @param total
     * @return
     */
    public ResultUtil lsbBuyLottery(String lotterybInfoId, String lotteryConfigId, String issueNum, String playCode ,String total) {
        //判断支付密码是否错误 //支付密码错
        if(!super.getLoginUser().getPaymentCode().equals(AesEncryptUtil.encrypt_code(playCode, Constants.KEY_TOW) )){
            String msg = "Payment password error!";
            if(super.isChineseLanguage()){
                msg = "支付密碼錯誤!";
            }
            return ResultUtil.errorJson(msg);
        }
        //判断是否可购买
        boolean b = lotterybInfoService.isBuyLotteryB(lotterybInfoId);
        if(!b){
            String msg = "This time is closed. No purchase allowed !";
            if(super.isChineseLanguage()){
                msg = "本次已完结，禁止購買 !";
            }
            return ResultUtil.errorJson(msg);
        }
        BigDecimal totalBigdecimal = new BigDecimal(total);
        LoginUser loginUser = getLoginUser();
        //判断平台币可用余额是否足够
        if(!esLsbwalletService.judgeBalance(loginUser.getId(),totalBigdecimal)){
            String msg = "not sufficient funds !";
            if(super.isChineseLanguage()){
                msg = "余額不足!";
            }
            return ResultUtil.errorJson(msg);
        }
        String dicTypeValue = getGuessDicTypeValue(lotterybInfoId);
        //判断是否获取到DicTypeValue值
        if("".equals(dicTypeValue)){
            String msg = "parameter error !";
            if(super.isChineseLanguage()){
                msg = "參數錯誤!";
            }
            return  ResultUtil.errorJson(msg);
        }
        //新增平台币购买记录（出账成功记录）
        b = lsbaccountsService.addOutSuccess(loginUser.getId(),getGuessDicTypeValue(lotterybInfoId),totalBigdecimal,"-1");
        String msg = "";
        if(!b){
            msg = "operation failure !";
            if(super.isChineseLanguage()){
                msg = "操作失败！";
            }
            return ResultUtil.errorJson(msg);
        }
        //购买成功之后计入购买统计
        b = lotterybStatisticsService.lotteryStatistice(lotteryConfigId,lotterybInfoId,issueNum,totalBigdecimal);
        if(!b){
            msg = "purchase failed !";
            if(super.isChineseLanguage()){
                msg = "购买失败！";
            }
            return ResultUtil.errorJson(msg);
        }
        //新增购买记录
        LotterybBuy lotterybBuy = new LotterybBuy();
        lotterybBuy.setMemberId(loginUser.getId());
        lotterybBuy.setLotterybInfoId(Integer.parseInt(lotterybInfoId));
        lotterybBuy.setLotterybConfigId(Integer.parseInt(lotteryConfigId));
        lotterybBuy.setLotterybIssueId(issueNum);
        lotterybBuy.setTotal(totalBigdecimal);
        lotterybBuy.setIsLuck(0);
        lotterybBuy.setLuckTotal(BigDecimal.ZERO);
        lotterybBuy.setCreateTime(new Date());
        b = lotterybBuyService.insert(lotterybBuy);
        if(!b){
            msg = "purchase failed !";

            if(super.isChineseLanguage()){
                msg = "购买失败！";
            }
            return ResultUtil.errorJson(msg);
        }
        msg = "purchase succeeded !";
        if(super.isChineseLanguage()){
            msg = "购买成功！";
        }
        return ResultUtil.successJson(msg);
    }

    /**
     * 更新中奖信息
     * @return
     */
    public boolean updateBuyInfo(String lotterybInfoId,String lotterybIssueId,String lotterybConfigId){
        return  false;
    }

    /**
     * 根据竞猜玩法获取操作类型Id
     * @param lotteryInfoId
     * @return
     */
    private String getGuessDicTypeValue(String lotteryInfoId){
        if(lotteryInfoId.equals(LotterybInfoServiceImpl.TYPE_ID_1)){
            return "4";
        }
        if(lotteryInfoId.equals(LotterybInfoServiceImpl.TYPE_ID_3)){
            return "5";
        }
        if(lotteryInfoId.equals(LotterybInfoServiceImpl.TYPE_ID_5)){
            return "6";
        }
        return "";
    }

}
