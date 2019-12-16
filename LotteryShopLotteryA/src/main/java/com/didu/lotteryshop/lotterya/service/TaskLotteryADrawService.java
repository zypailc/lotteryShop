package com.didu.lotteryshop.lotterya.service;

import com.didu.lotteryshop.lotterya.entity.LotteryaInfo;
import com.didu.lotteryshop.lotterya.entity.LotteryaIssue;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaInfoServiceImpl;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaIssueServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * A彩票开奖定时任务Service
 * @author CHJ
 * @date 2019-11-27
 */
@Service
public class TaskLotteryADrawService extends LotteryABaseService{
    private static final Logger logger = LoggerFactory.getLogger(TaskLotteryADrawService.class);
    @Autowired
    private LotteryaIssueServiceImpl lotteryaIssueService;
    @Autowired
    private LotteryaInfoServiceImpl lotteryaInfoService;
    @Autowired
    private LotteryAContractService lotteryAContractService;
    /**
     * A彩票开奖方法
     */
    public void lotteryADraw(){
        LotteryaIssue lotteryaIssue = lotteryaIssueService.findCurrentPeriodLotteryaIssue();
        LotteryaInfo lotteryaInfo = lotteryaInfoService.findLotteryaInfo();
        Date nowDate = new Date();
        //彩票结束时间
        Date endTime = lotteryaIssue.getEndTime();
        //彩票开奖间隔时间(单位分钟)
        BigDecimal intervalDate = lotteryaInfo.getIntervalDate();
        //彩票结束时间加开奖间隔时间
        Date endAndIntervalDate = DateUtils.addMinutes(endTime,intervalDate.intValue());
        if(nowDate.after(endTime) && nowDate.before(endAndIntervalDate) && lotteryaIssue.getBuyStatus().equals("0")){
            boolean bool = false;
            //关闭彩票购买功能
            logger.info("==============================☆☆ DrawLotteryA: Start off  BuyLotteryA ☆☆==============================================");
            lotteryaIssue.setBuyStatus("1");
            lotteryaIssue.setBsTime(new Date());
            bool = lotteryaIssueService.updateById(lotteryaIssue);
            if(!bool){
                //错误中奖号码为空
                logger.error(" DrawLotteryA: Start off  BuyLotteryA --> error:Update failed!");
                return;
            }
            logger.info("==============================☆☆ DrawLotteryA: End off  BuyLotteryA ☆☆==============================================");
            //定时10分钟
            logger.info("==============================☆☆ DrawLotteryA: Start sleep Time 10 minutes ☆☆==============================================");
            try {
                Thread.sleep(1000*60*10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error(" DrawLotteryA: Start sleep Time 10 minutes <> error:"+e.getMessage());
                return;
            }
            logger.info("==============================☆☆ DrawLotteryA: End sleep Time 10 minutes ☆☆==============================================");
            //开奖
            logger.info("==============================☆☆ DrawLotteryA: Start draw LotteryA ☆☆==============================================");
            //开奖中奖号码
            String lunckNumber =  lotteryAContractService.drawLotterA();
            if(StringUtils.isBlank(lunckNumber)){
                //错误中奖号码为空
                logger.error(" DrawLotteryA: Start draw LotteryA --> error:The lucky number is empty!");
                return;
            }
            logger.info("==============================☆☆ DrawLotteryA: End draw LotteryA ☆☆==============================================");
            //step 1：对比中奖人数，与平台购买人数是否一致，如果不一致，说明有非法用户之间调用ETH智能合约进行操作，停止发放奖金，
            //由管理员对数据审核通过后，再进行奖金发放。

            //判断合约中奖人数是否等于平台购买人数
            logger.info("==============================☆☆ DrawLotteryA: Start ContractPersons == PlatformPersons ☆☆==============================================");
            bool = lotteryAContractService.isContractLuckPersonNumEqPlatformLuckPersonNum(lotteryaIssue.getId(),lunckNumber);
            if(!bool){
                //错误，合约代码存在非法购买
                logger.error(" DrawLotteryA: Start ContractPersons == PlatformPersons --> error:Illegal to buy LotteryA!");
                return;
            }
            logger.info("==============================☆☆ DrawLotteryA: End ContractPersons == PlatformPersons ☆☆==============================================");
            //step 2:计算当期奖金
            logger.info("==============================☆☆ DrawLotteryA: Start calculateCurrentBonus ☆☆==============================================");
            BigDecimal currentBonus = lotteryAContractService.calculateCurrentBonus(lotteryaIssue.getId(),lunckNumber);
            logger.info("==============================☆☆ DrawLotteryA: End calculateCurrentBonus ☆☆==============================================");

            //step 3：判断当期奖金是否够发放，不够发放需要调节基金进行调节，停止发放奖金由管理员进行转账再进行发方法。
            logger.info("==============================☆☆ DrawLotteryA: Start isContractBalanceSufficient ☆☆==============================================");
            bool = lotteryAContractService.isContractBalanceSufficient(lotteryaIssue.getId(),currentBonus);
            if(!bool){
                //错误，合约余额不足
                logger.error(" DrawLotteryA: Start isContractBalanceSufficient --> error:Insufficient contract balance!");
                return;
            }
            logger.info("==============================☆☆ DrawLotteryA: End isContractBalanceSufficient ☆☆==============================================");

        }else{
            //A彩票未到开奖时间
            logger.info("==============================☆☆ DrawLotteryA: It's not time to draw ☆☆==============================================");
        }
    }
}
