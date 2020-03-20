package com.didu.lotteryshop.lotteryb.service;

import com.didu.lotteryshop.common.service.form.impl.EsEthaccountsServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.EsLsbaccountsServiceImpl;
import com.didu.lotteryshop.common.utils.Web3jUtils;
import com.didu.lotteryshop.lotteryb.entity.LotterybBuy;
import com.didu.lotteryshop.lotteryb.entity.LotterybConfig;
import com.didu.lotteryshop.lotteryb.entity.LotterybInfo;
import com.didu.lotteryshop.lotteryb.entity.LotterybIssue;
import com.didu.lotteryshop.lotteryb.service.form.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
public class LotterybWinningService extends LotteryBBaseService {

    @Autowired
    private LotterybInfoServiceImpl lotterybInfoService;
    @Autowired
    public LotterybIssueServiceImpl lotterybIssueService;
    @Autowired
    public LotterybBuyService lotterybBuyService;
    @Autowired
    private LotterybBuyServiceImpl lotterybBuyServiceIml;
    @Autowired
    public LotterybConfigServiceImpl lotterybConfigService;
    @Autowired
    public EsLsbaccountsServiceImpl esLsbaccountsService;
    @Autowired
    public LotterybPmDetailServiceImpl lotterybPmDetailServiceIml;

    /**
     * 中奖分成
     * @param lotterybInfoId
     * @return
     */
    public boolean winning(Integer lotterybInfoId,LotterybIssue lotterybIssue){
        boolean bool = false;
        //查询竞猜基本玩法
        LotterybInfo lotterybInfo = lotterybInfoService.find(lotterybInfoId);
        //查询期数基本信息
        //LotterybIssue lotterybIssue = lotterybIssueService.findUpLotteryaIssue(lotterybInfoId);
        if(lotterybIssue != null) {
            //判断奖金未发放，并且允许发放
            if (lotterybIssue.getBonusStatus() == 0 && lotterybIssue.getBonusGrant() == 1) {
                //是否有中奖用户(中奖金额大于0)
                boolean isLuckMmber = lotterybIssue.getLuckTotal().compareTo(BigDecimal.ZERO) > 0;
                //要有中奖者，才进行发放
                if (isLuckMmber) {
                    lotterybIssue.setBonusStatus(1);
                    lotterybIssue.setBonusStatusTime(new Date());
                    bool = lotterybIssueService.updateAllColumnById(lotterybIssue);
                    //新增账单记录和中奖平台币分成；
                    if (bool && isLuckMmber) {
                        bool = this.updateIsLuck(lotterybIssue, lotterybInfo);
                    }
                }
            }
        }
        return bool;
    }

    /**
     * 更新中奖状态
     * @param lotteryaIssue
     * @param lotteryaInfo
     * @return
     */
    public boolean updateIsLuck(LotterybIssue lotteryaIssue, LotterybInfo lotteryaInfo){
        boolean bool = false;
        List<LotterybBuy> lotterybBuyList = lotterybBuyService.findLuckLotteryaBuy(lotteryaIssue);
        if(lotterybBuyList != null && lotterybBuyList.size() > 0){
            for(LotterybBuy lb : lotterybBuyList){
                LotterybConfig lotterybConfig = lotterybConfigService.selectById(lb.getLotterybConfigIds().split(",")[0]);
                lb.setIsLuck(1);
                BigDecimal luckTotal = lb.getTotal().multiply(lotterybConfig.getLines());
                lb.setLuckTotal(luckTotal);
                bool =  lotterybBuyServiceIml.updateAllColumnById(lb);
                //新增流水账记录
                if(lotteryaInfo.getId() == 1){
                if(bool)
                    bool =  esLsbaccountsService.addInSuccess(lb.getMemberId(), EsLsbaccountsServiceImpl.DIC_TYPE_WINLOTTERYB_1,luckTotal,lb.getId().toString());
                }
                if(lotteryaInfo.getId() == 2){
                    if(bool)
                        bool =  esLsbaccountsService.addInSuccess(lb.getMemberId(), EsLsbaccountsServiceImpl.DIC_TYPE_WINLOTTERYB_2,luckTotal,lb.getId().toString());
                }
                if(lotteryaInfo.getId() == 3){
                    if(bool)
                        bool =  esLsbaccountsService.addInSuccess(lb.getMemberId(), EsLsbaccountsServiceImpl.DIC_TYPE_WINLOTTERYB_3,luckTotal,lb.getId().toString());
                }
                //中奖分成
                if(bool)
                    bool = lotterybPmDetailServiceIml.drawPM(lb,lotteryaInfo);
                if(!bool) return false;
            }
            bool = true;
        }
        return bool;
    }

}
