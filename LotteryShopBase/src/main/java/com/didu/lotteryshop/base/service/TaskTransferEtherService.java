package com.didu.lotteryshop.base.service;

import com.didu.lotteryshop.common.entity.EsGdethaccounts;
import com.didu.lotteryshop.common.service.form.impl.EsEthaccountsServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.EsGdethaccountsServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 转账ETH 异步响应定时任务 Service
 * @author CHJ
 * @date 20119-12-20
 */
@Service
public class TaskTransferEtherService extends BaseBaseService {
    private static final Logger logger = LoggerFactory.getLogger(TaskTransferEtherService.class);
    @Autowired
    private Web3jService web3jService;
    @Autowired
    private EsGdethaccountsServiceImpl esGdethaccountsService;
    @Autowired
    private EsEthaccountsServiceImpl esEthaccountsService;
    /**
     * ETH 转账异步响应定时任务Service
     */
    public void  baseTransferEther(){
        //处理推广分成结账等待处理的数据
        logger.info("==============================☆☆ baseTransferEther：Start disposeGdEthAccountsWait  ☆☆==============================================");
        this.disposeGdEthAccountsWait();
        logger.info("==============================☆☆ baseTransferEther：End disposeGdEthAccountsWait  ☆☆==============================================");

        // A彩票推广分成数据未处理的数据，共x条
        logger.info("==============================☆☆ baseTransferEther：Start LotteryADiTransfer  ☆☆==============================================");
        //this.disposeLotteryADiWait();
        logger.info("==============================☆☆ baseTransferEther：End LotteryADiTransfer  ☆☆==============================================");

        //A彩票上级提成数据未处理的数据，共x条
        logger.info("==============================☆☆ baseTransferEther：Start LotteryAPmTransfer  ☆☆==============================================");
        //this.disposeLotteryAPmWait();
        logger.info("==============================☆☆ baseTransferEther：End LotteryAPmTransfer  ☆☆==============================================");
    }

    /**
     * 处理推广分成结账等待处理的数据
     */
    public void disposeGdEthAccountsWait(){
        int success = 0;
        int fail = 0;
        int wait = 0;
        List<EsGdethaccounts> esGdethaccountsList = esGdethaccountsService.findSATransferStatusWait();
        if(esGdethaccountsList != null && esGdethaccountsList.size() > 0){
            Map<String,Object> rWeb3jMap = null;
            boolean bool = false;
            for(EsGdethaccounts egea : esGdethaccountsList){
                if(egea.getTransferHashValue() == null || StringUtils.isBlank(egea.getTransferHashValue())) continue;
                rWeb3jMap = web3jService.findTransactionStatus(egea.getTransferHashValue());
                if(rWeb3jMap != null && !rWeb3jMap.isEmpty()){
                    String status = rWeb3jMap.get(Web3jService.TRANSACTION_STATUS).toString();
                    String gasUsed = rWeb3jMap.get(Web3jService.TRANSACTION_GASUSED).toString();
                    if(StringUtils.isNotBlank(status)){
                        if(status.equals("1")){
                            success++;
                            bool = esGdethaccountsService.updateSuccessById(egea.getId(),new BigDecimal(gasUsed));
                            if(bool){
                                bool = esEthaccountsService.addInSuccess(egea.getMemberId(), EsEthaccountsServiceImpl.DIC_TYPE_GSA,egea.getAmount(),"-1");
                                if(! bool ) return;
                                continue;
                            }
                        }
                        if(status.equals("2")){
                            fail++;
                            esGdethaccountsService.updateFailById(egea.getId());
                            continue;
                        }
                        wait++;
                    }
                }
            }
        }
        //处理推广分成结账未处理的数据，共x条
        logger.info("==============================☆☆ disposeGdEthAccountsWait： disposeGdEthAccountsWait Wait  [total:"+esGdethaccountsList.size()+";success:"+success+";fail:"+fail+";wait:"+wait+";] ☆☆==============================================");
    }

}
