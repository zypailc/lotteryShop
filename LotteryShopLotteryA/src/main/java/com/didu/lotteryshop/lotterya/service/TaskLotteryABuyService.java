package com.didu.lotteryshop.lotterya.service;

import com.didu.lotteryshop.lotterya.entity.LotteryaBuy;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaBuyServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * A彩票购买定时任务Service
 * @author CHJ
 * @date 2019-11-27
 */
@Service
public class TaskLotteryABuyService extends LotteryABaseService{
    private static final Logger logger = LoggerFactory.getLogger(TaskLotteryABuyService.class);
    @Autowired
    private LotteryaBuyServiceImpl lotteryaBuyService;
    @Autowired
    private Web3jService web3jService;

    /**
     * 购买A彩票，定时任务处理未处理的数据
     */
    public void disposeLotteryABuyWait(){
        List<LotteryaBuy> labList = lotteryaBuyService.findTransferStatusWait();
        int success = 0;
        int fail = 0;
        int wait = 0;
        if(labList != null && labList.size() > 0){
            Map<String,Object> rWeb3jMap = null;
            for(LotteryaBuy lab : labList){
                if(StringUtils.isBlank(lab.getTransferHashValue())) continue;
                rWeb3jMap = web3jService.findTransactionStatus(lab.getTransferHashValue());
                if(rWeb3jMap != null && !rWeb3jMap.isEmpty()){
                   String status = rWeb3jMap.get(Web3jService.TRANSACTION_STATUS).toString();
                   String gasUsed = rWeb3jMap.get(Web3jService.TRANSACTION_GASUSED).toString();
                   if(StringUtils.isNotBlank(status)){
                       if(status.equals("1")){
                           success++;
                           lotteryaBuyService.updateLotteryABuyTransferStatus(lab.getId(),LotteryaBuyServiceImpl.TRANSFER_STATUS_SUCCESS,new BigDecimal(gasUsed));
                           continue;
                       }
                       if(status.equals("2")){
                           fail++;
                           lotteryaBuyService.updateLotteryABuyTransferStatus(lab.getId(),LotteryaBuyServiceImpl.TRANSFER_STATUS_FAIL,BigDecimal.ZERO);
                           continue;
                       }
                       wait++;
                   }
                }
            }
        }
        //购买A彩票未处理的数据，共x条
        logger.info("==============================☆☆ BuyLotteryA： BuyLotteryA Wait  [total:"+labList.size()+";success:"+success+";fail:"+fail+";wait:"+wait+";] ☆☆==============================================");
    }

}
