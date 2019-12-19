package com.didu.lotteryshop.lotterya.service;

import com.didu.lotteryshop.lotterya.entity.LotteryaBuy;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaBuyServiceImpl;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaDiServiceImpl;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaPmServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * ETH 转账异步响应定时任务Service
 * @author CHJ
 * @date 2019-11-27
 */
@Service
public class TaskETHTransferService extends LotteryABaseService{
    private static final Logger logger = LoggerFactory.getLogger(TaskETHTransferService.class);
    @Autowired
    private LotteryaBuyServiceImpl lotteryaBuyService;
    @Autowired
    private Web3jService web3jService;
    @Autowired
    private LotteryaDiServiceImpl lotteryaDiService;
    @Autowired
    private LotteryaPmServiceImpl lotteryaPmService;

    /**
     * ETH 转账异步响应定时任务Service
     */
    public void  lotteryAETHTransfer(){
        //购买A彩票未处理的数据，共x条
        logger.info("==============================☆☆ LotteryAETHTransfer：Start BuyLotteryA  ☆☆==============================================");
        this.disposeLotteryABuyWait();
        logger.info("==============================☆☆ LotteryAETHTransfer：End BuyLotteryA  ☆☆==============================================");

        // A彩票推广分成数据未处理的数据，共x条
        logger.info("==============================☆☆ LotteryAETHTransfer：Start LotteryADiTransfer  ☆☆==============================================");
        this.disposeLotteryADiWait();
        logger.info("==============================☆☆ LotteryAETHTransfer：End LotteryADiTransfer  ☆☆==============================================");

        //A彩票上级提成数据未处理的数据，共x条
        logger.info("==============================☆☆ LotteryAETHTransfer：Start LotteryAPmTransfer  ☆☆==============================================");
        this.disposeLotteryAPmWait();
        logger.info("==============================☆☆ LotteryAETHTransfer：End LotteryAPmTransfer  ☆☆==============================================");
    }


    /**
     * A彩票推广分成数据，定时任务处理未处理的数据
     */
    private void disposeLotteryADiWait(){
        int success = 0;
        int fail = 0;
        int wait = 0;
        List<Map<String,Object>> wMapList = lotteryaDiService.findTransferStatusWait();
        if(wMapList != null && wMapList.size() > 0){
            Map<String,Object> rWeb3jMap = null;
            for(Map<String,Object> m : wMapList){
                if(m.get("transferHashValue") == null || StringUtils.isBlank(m.get("transferHashValue").toString())) continue;
                rWeb3jMap = web3jService.findTransactionStatus(m.get("transferHashValue").toString());
                if(rWeb3jMap != null && !rWeb3jMap.isEmpty()){
                    String status = rWeb3jMap.get(Web3jService.TRANSACTION_STATUS).toString();
                    String gasUsed = rWeb3jMap.get(Web3jService.TRANSACTION_GASUSED).toString();
                    if(StringUtils.isNotBlank(status)){
                        if(status.equals("1")){
                            success++;
                            lotteryaDiService.updateLotteryADiTransferStatusByLotteryaIssueId(Integer.valueOf(m.get("lotteryaIssueId").toString())
                                    ,LotteryaDiServiceImpl.TRANSFER_STATUS_SUCCESS
                                    ,m.get("transferHashValue").toString()
                                    ,new BigDecimal(gasUsed));
                            continue;
                        }
                        if(status.equals("2")){
                            fail++;
                            lotteryaDiService.updateLotteryADiTransferStatusByLotteryaIssueId(Integer.valueOf(m.get("lotteryaIssueId").toString())
                                    ,LotteryaDiServiceImpl.TRANSFER_STATUS_FAIL
                                    ,m.get("transferHashValue").toString()
                                    ,new BigDecimal(gasUsed));
                            continue;
                        }
                        wait++;
                    }
                }
            }
        }
        //A彩票推广分成数据未处理的数据，共x条
        logger.info("==============================☆☆ LotteryADiTransfer： LotteryADiTransfer Wait  [total:"+wMapList.size()+";success:"+success+";fail:"+fail+";wait:"+wait+";] ☆☆==============================================");
    }

    /**
     * A彩票上级提成（下级购买提成，下级中奖提成，6层），定时任务处理未处理的数据
     */
    private void disposeLotteryAPmWait(){
        int success = 0;
        int fail = 0;
        int wait = 0;
        List<Map<String,Object>> wMapList = lotteryaPmService.findTransferStatusWait();
        if(wMapList != null && wMapList.size() > 0){
            Map<String,Object> rWeb3jMap = null;
            for(Map<String,Object> m : wMapList){
                if(m.get("transferHashValue") == null || StringUtils.isBlank(m.get("transferHashValue").toString())) continue;
                rWeb3jMap = web3jService.findTransactionStatus(m.get("transferHashValue").toString());
                if(rWeb3jMap != null && !rWeb3jMap.isEmpty()){
                    String status = rWeb3jMap.get(Web3jService.TRANSACTION_STATUS).toString();
                    String gasUsed = rWeb3jMap.get(Web3jService.TRANSACTION_GASUSED).toString();
                    if(StringUtils.isNotBlank(status)){
                        if(status.equals("1")){
                            success++;
                            lotteryaPmService.updateLotteryAPmTransferStatus(LotteryaPmServiceImpl.TRANSFER_STATUS_SUCCESS
                                    ,m.get("transferHashValue").toString()
                                    ,new BigDecimal(gasUsed));
                            continue;
                        }
                        if(status.equals("2")){
                            fail++;
                            lotteryaPmService.updateLotteryAPmTransferStatus(LotteryaPmServiceImpl.TRANSFER_STATUS_FAIL
                                    ,m.get("transferHashValue").toString()
                                    ,new BigDecimal(gasUsed));
                            continue;
                        }
                        wait++;
                    }
                }
            }
        }
        //A彩票上级提成数据未处理的数据，共x条
        logger.info("==============================☆☆ LotteryAPmTransfer： LotteryAPmTransfer Wait  [total:"+wMapList.size()+";success:"+success+";fail:"+fail+";wait:"+wait+";] ☆☆==============================================");
    }


    /**
     * 购买A彩票，定时任务处理未处理的数据
     */
    private void disposeLotteryABuyWait(){
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
