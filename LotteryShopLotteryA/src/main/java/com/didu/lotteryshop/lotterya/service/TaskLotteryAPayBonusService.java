package com.didu.lotteryshop.lotterya.service;

import com.didu.lotteryshop.lotterya.entity.LotteryaIssue;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaDiServiceImpl;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaIssueServiceImpl;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaPmServiceImpl;
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
    @Autowired
    private LotteryaPmServiceImpl lotteryaPmService;
    @Autowired
    private LotteryaDiServiceImpl lotteryaDiService;
    /**
     * A彩票发放奖金
     */
    public void LotteryAPayBonus() {
        LotteryaIssue lotteryaIssue = lotteryaIssueService.findCurrentPeriodLotteryaIssue();
        if(lotteryaIssue.getBonusStatus().equals("0") && lotteryaIssue.getBonusGrant().equals("1")){
            boolean bool = false;
            //step 1：发放奖金、中奖数据、中奖提成、清除合约数据
            logger.info("==============================☆☆ PayBonusLotteryA: Start payBonus ☆☆==============================================");
            bool = lotteryAContractService.payBonus(lotteryaIssue);
            if(!bool){
                //错误，奖金发放失败
                logger.error(" PayBonusLotteryA: Start payBonus --> error:Bonus payment failure!");
                return;
            }
            //step 2:发放提成（待领币）
            logger.info("==============================☆☆ PayBonusLotteryA: Start updateStatus  ☆☆==============================================");
            bool = lotteryaPmService.updateStatus();
            if(!bool){
                //错误，提成发放失败
                logger.error(" PayBonusLotteryA: Start updateStatus --> error:PushMoney payment failure!");
                return;
            }
            logger.info("==============================☆☆ PayBonusLotteryA: End updateStatus  ☆☆==============================================");
           //step 3:核算一级推广商分成，并把管理员账户ether发送到分成钱包地址。
            logger.info("==============================☆☆ PayBonusLotteryA: Start generalizeDividedInto  ☆☆==============================================");
            bool =  lotteryaDiService.generalizeDividedInto(lotteryaIssue.getId());
            if(!bool){
                //错误，推广分成错误
                logger.error(" PayBonusLotteryA: Start generalizeDividedInto --> error:Generalization sharing error!");
                return;
            }
            logger.info("==============================☆☆ PayBonusLotteryA: End generalizeDividedInto  ☆☆==============================================");
            //step 4:核算平台币分成eher，并把管理员账户ether发送到平台币钱包地址。
            logger.info("==============================☆☆ PayBonusLotteryA: Start LotteryAPmTransfer  ☆☆==============================================");
            bool = lotteryaPmService.LotteryAPmTransfer();
            if(!bool){
                //错误，提成发放失败
                logger.error(" PayBonusLotteryA: Start LotteryAPmTransfer --> error:LotteryAPmTransfer error!");
                return;
            }
            logger.info("==============================☆☆ PayBonusLotteryA: Start LotteryAPmTransfer  ☆☆==============================================");
            //step 5:生成下期彩票彩票数据
            logger.info("==============================☆☆ PayBonusLotteryA: Start createNext  ☆☆==============================================");
            bool = lotteryaIssueService.createNext();
            if(!bool){
                //错误，重新生成下一期数据错误
                logger.error(" PayBonusLotteryA: Start createNext --> error:Regenerating the next data error!");
                return;
            }
            logger.info("==============================☆☆ PayBonusLotteryA: End createNext  ☆☆==============================================");
            logger.info("==============================☆☆ PayBonusLotteryA: End payBonus ☆☆==============================================");
        }else{
            //A彩票 没有奖金可发
            logger.info("==============================☆☆ PayBonusLotteryA: There are no bonuses to be paid ☆☆==============================================");
        }
    }
}
