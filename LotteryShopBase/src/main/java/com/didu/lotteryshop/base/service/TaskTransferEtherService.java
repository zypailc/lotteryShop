package com.didu.lotteryshop.base.service;

import com.didu.lotteryshop.common.entity.EsEthaccounts;
import com.didu.lotteryshop.common.entity.EsGdethaccounts;
import com.didu.lotteryshop.common.entity.EsLsbaccounts;
import com.didu.lotteryshop.common.entity.SysConfig;
import com.didu.lotteryshop.common.service.form.impl.*;
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
    @Autowired
    private EsLsbaccountsServiceImpl esLsbaccountsService;
    @Autowired
    private SysConfigServiceImpl sysConfigService;

    /**
     * ETH 转账异步响应定时任务Service
     */
    public void  baseTransferEther(){
        //处理推广分成结账等待处理的数据
        logger.info("==============================☆☆ baseTransferEther：Start disposeGdEthAccountsWait  ☆☆==============================================");
        this.disposeGdEthAccountsWait();
        logger.info("==============================☆☆ baseTransferEther：End disposeGdEthAccountsWait  ☆☆==============================================");
        //处理ETH 提现是否已经完成
        logger.info("==============================☆☆ baseTransferEther：Start ETH  withdraw deposit ☆☆==============================================");
        this.disposeEthAccountsWait();
        logger.info("==============================☆☆ baseTransferEther：End withdraw deposit  ☆☆==============================================");
        //处理Lsb转ETH和ETH转平台币的处理结果
        logger.info("==============================☆☆ baseTransferEther：Start ETH  withdraw deposit ☆☆==============================================");
        this.disposeLsbAndEth();
        logger.info("==============================☆☆ baseTransferEther：End withdraw deposit  ☆☆==============================================");
    }

    /**
     * 处理Lsb和Eth之间转换待处理的数据
     */
    public void disposeLsbAndEth(){

        int success = 0;
        int fail = 0;
        int wait = 0;
        //查询Eth和lsb之间相互转换正在处理中的数据
        List<EsLsbaccounts> esLsbaccountsList = esLsbaccountsService.findSATransferStatusWait();
        if(esLsbaccountsList != null && esLsbaccountsList.size() > 0){
            Map<String,Object> rWeb3jMap = null;
            boolean bool = false;
            SysConfig sysConfig = sysConfigService.getSysConfig();
            for (EsLsbaccounts esLsbaccounts:esLsbaccountsList) {
                if(esLsbaccounts.getTransferHashValue() != null && !"".equals(esLsbaccounts.getTransferHashValue())){
                    rWeb3jMap = web3jService.findTransactionStatus(esLsbaccounts.getTransferHashValue());
                    if(rWeb3jMap != null && !rWeb3jMap.isEmpty()){
                        String status = rWeb3jMap.get(Web3jService.TRANSACTION_STATUS).toString();
                        String gasUsed = rWeb3jMap.get(Web3jService.TRANSACTION_GASUSED).toString();
                        if(StringUtils.isNotBlank(status)){
                            if(status.equals("1")){
                                success++;
                               //修改记录为成功
                                if(esLsbaccounts.getType() == EsLsbaccountsServiceImpl.TYPE_IN){//lsbToEth(平台入账，ETH出账)
                                    bool = esLsbaccountsService.updateSuccess(esLsbaccounts.getId(),EsLsbaccountsServiceImpl.DIC_TYPE_IN,new BigDecimal(gasUsed));
                                    if(! bool ) return;
                                    //记录一条成功的ETH账目记录(出账)
                                    bool = esEthaccountsService.addOutSuccess(esLsbaccounts.getMemberId(),EsEthaccountsServiceImpl.DIC_TYPE_ETHTOLSB,esLsbaccounts.getAmount().divide(sysConfig.getEthToLsb(),4,BigDecimal.ROUND_DOWN),esLsbaccounts.getId().toString(),new BigDecimal(gasUsed));
                                }else{
                                    bool = esLsbaccountsService.updateSuccess(esLsbaccounts.getId(),EsLsbaccountsServiceImpl.DIC_TYPE_DRAW,new BigDecimal(gasUsed));
                                    if(! bool ) return;
                                    //记录一条成功的ETH账目记录(入账)
                                    //bool = esEthaccountsService.addInBeingprocessed(esLsbaccounts.getMemberId(), EsEthaccountsServiceImpl.DIC_TYPE_LSBTOETH,esLsbaccounts.getAmount().multiply(sysConfig.getLsbToEth()),esLsbaccounts.getId().toString());
                                    bool = esEthaccountsService.addInSuccess(esLsbaccounts.getMemberId(), EsEthaccountsServiceImpl.DIC_TYPE_LSBTOETH,esLsbaccounts.getAmount().divide(sysConfig.getLsbToEth(),4,BigDecimal.ROUND_DOWN),esLsbaccounts.getId().toString());

                                }
                            }
                            if(status.equals("2")){
                                fail++;
                                esLsbaccountsService.updateFail(esLsbaccounts.getId());
                                //修改记录为失败
                                //添加一条失败的ETH账目记录
                                continue;
                            }
                            wait++;
                        }
                    }
                }
            }
        }
        //处理推广分成结账未处理的数据，共x条
        logger.info("==============================☆☆ disposeGdEthAccountsWait： disposeGdEthAccountsWait Wait  [total:"+esLsbaccountsList.size()+";success:"+success+";fail:"+fail+";wait:"+wait+";] ☆☆==============================================");
    }

    /**
     * 处理ETH提现数据
     */
    public void disposeEthAccountsWait(){
        int success = 0;
        int fail = 0;
        int wait = 0;
        //查询所有出账数据且待处理的数据
        List<EsEthaccounts> esEthaccountsList = esEthaccountsService.findSATransferStatusWait();
        if(esEthaccountsList != null && esEthaccountsList.size() > 0){
            Map<String,Object> rWeb3jMap = null;
            boolean bool = false;
            for (EsEthaccounts esEthaccounts:esEthaccountsList ) {
                if(esEthaccounts.getTransferHashValue() != null && !"".equals(esEthaccounts.getTransferHashValue())){
                    rWeb3jMap = web3jService.findTransactionStatus(esEthaccounts.getTransferHashValue());
                    if(rWeb3jMap != null && !rWeb3jMap.isEmpty()){
                        String status = rWeb3jMap.get(Web3jService.TRANSACTION_STATUS).toString();
                        String gasUsed = rWeb3jMap.get(Web3jService.TRANSACTION_GASUSED).toString();
                       if(StringUtils.isNotBlank(status)){
                            if(status.equals("1")){
                                success++;
                                bool = esEthaccountsService.updateSuccess(esEthaccounts.getId(),EsEthaccountsServiceImpl.DIC_TYPE_DRAW,new BigDecimal(gasUsed));
                            }
                            if(status.equals("2")){
                                fail++;
                                esEthaccountsService.updateFail(esEthaccounts.getId(),EsEthaccountsServiceImpl.DIC_TYPE_DRAW);
                                continue;
                            }
                            wait++;
                        }
                    }
                }
            }
        }
        //处理推广分成结账未处理的数据，共x条
        logger.info("==============================☆☆ disposeGdEthAccountsWait： disposeGdEthAccountsWait Wait  [total:"+esEthaccountsList.size()+";success:"+success+";fail:"+fail+";wait:"+wait+";] ☆☆==============================================");
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
