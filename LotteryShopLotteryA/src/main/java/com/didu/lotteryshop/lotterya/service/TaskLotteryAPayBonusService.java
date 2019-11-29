package com.didu.lotteryshop.lotterya.service;

import com.didu.lotteryshop.lotterya.entity.LotteryaIssue;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaIssueServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * A彩票奖金发放定时任务Service
 * @author CHJ
 * @date 2019-11-28
 */
@Service
public class TaskLotteryAPayBonusService {
    private static final Logger logger = LoggerFactory.getLogger(TaskLotteryAPayBonusService.class);
    @Autowired
    private LotteryaIssueServiceImpl lotteryaIssueService;
    @Autowired
    private LotteryAContractService lotteryAContractService;
    /**
     * A彩票发放奖金
     */
    public void LotteryAPayBonus() {
        LotteryaIssue lotteryaIssue = lotteryaIssueService.findCurrentPeriodLotteryaIssue();
        if(lotteryaIssue.getBonusStatus().equals("0") && lotteryaIssue.getBonusGrant().equals("1")){
            boolean bool = false;
            //step 4：发放奖金。
            logger.info("==============================☆☆ PayBonusLotteryA: Start payBonus ☆☆==============================================");
            bool = lotteryAContractService.payBonus(lotteryaIssue);
            if(!bool){
                //错误，奖金发放失败
                logger.error(" PayBonusLotteryA: Start payBonus --> error:Bonus payment failure!");
                return;
            }
            logger.info("==============================☆☆ PayBonusLotteryA: end payBonus ☆☆==============================================");
        }else{
            //A彩票 没有奖金可发
            logger.info("==============================☆☆ PayBonusLotteryA: There are no bonuses to be paid ☆☆==============================================");
        }
    }
}
