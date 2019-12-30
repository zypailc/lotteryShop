package com.didu.lotteryshop.base.service;

import com.didu.lotteryshop.common.entity.EsGdethconfig;
import com.didu.lotteryshop.common.entity.EsGdethconfigWithdraw;
import com.didu.lotteryshop.common.entity.EsGdethwallet;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.service.form.impl.*;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 推广分成结账核算定时任务
 * @author CHJ
 * @date 2019-12-19
 */
@Service
public class TaskGdEthCalculateService extends BaseBaseService {
    @Autowired
    private EsGdethwalletServiceImpl esGdethwalletService;
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private EsGdethconfigServiceImpl esGdethconfigService;
    @Autowired
    private EsGdethconfigWithdrawServiceImpl esGdethconfigWithdrawService;
    @Autowired
    private EsGdethaccountsServiceImpl esGdethaccountsService;
    @Autowired
    private EsEthaccountsServiceImpl esEthaccountsService;
    @Autowired
    private Web3jService web3jService;
    /**
     * 推广分成 结账核算
     */
    public void gdEthCalculate(){
        //step 1：查询需要核算账户；
        //step 2：查询下级活跃数；
        //step 3: 判断该用户是否结账；
        //step 4: 系统推广钱包地址转账到推广用户eth钱包；
        List<EsGdethwallet> esGdethwalletList = esGdethwalletService.findBalanceGtZero();
        if(esGdethwalletList != null && esGdethwalletList.size() > 0){
            EsGdethconfig esGdethconfig = esGdethconfigService.findEsGdethconfig();
            Integer cLevel = esGdethconfig.getcLevel();
            cLevel = cLevel == -1 ? null : cLevel;

            for(EsGdethwallet egew : esGdethwalletList){
                //下级活跃人数
                int activeMembers = memberService.findActiveMembers(egew.getMemberId(),esGdethconfig.getConsumeTotal(),cLevel,esGdethconfig.getCycleDay());
                EsGdethconfigWithdraw esGdethconfigWithdraw = esGdethconfigWithdrawService.findEsGdethconfigWithdrawByActiveMembers(activeMembers);
                if(esGdethconfigWithdraw != null){
                    //结算周期
                    Date calculateDate = DateUtils.addDays(new Date(),-esGdethconfigWithdraw.getwDay());
                    Member member = memberService.selectById(egew.getMemberId());
                    //新注册的会员未到结算周期
                    if(member.getCreateTime().compareTo(calculateDate) > 0) continue;
                    //周期内有结账数据，有下一条，无则进行结账
                    if(esGdethaccountsService.findToSAByDay(member.getId(),esGdethconfigWithdraw.getwDay())) continue;
                    if(web3jService.geGdManagerBalanceByEther().compareTo(egew.getBalance()) >= 0) {
                        Map<String, Object> rMap = web3jService.divideIntoManagerSendToETH(member.getPAddress(), egew.getBalance());
                        if (rMap != null && !rMap.isEmpty()) {
                            String hashvalue = rMap.get(Web3jService.TRANSACTION_HASHVALUE).toString();
                            //是否确认状态，0未确认，1已确认，2失败
                            String status = rMap.get(Web3jService.TRANSACTION_STATUS).toString();
                            boolean bool = false;
                            BigDecimal gasUsed = new BigDecimal(rMap.get(Web3jService.TRANSACTION_GASUSED).toString());
                            if (status.equals("0")) {
                                bool = esGdethaccountsService.addOutBeingProcessed(egew.getMemberId(), EsGdethaccountsServiceImpl.DIC_TYPE_SA, egew.getBalance(), "-1", hashvalue);
                            } else if (status.equals("1")) {
                                bool = esGdethaccountsService.addOutSuccess(egew.getMemberId(), EsGdethaccountsServiceImpl.DIC_TYPE_SA, egew.getBalance(), "-1", gasUsed, hashvalue);
                                if (bool) {
                                    esEthaccountsService.addInSuccess(egew.getMemberId(), EsEthaccountsServiceImpl.DIC_TYPE_GSA, egew.getBalance(), "-1");
                                }
                            }
                            if (!bool) return;
                        }
                    }else {
                        //结账 TODO 结账失败 存储一条失败数据
                        esGdethaccountsService.addInFail(egew.getMemberId(),EsGdethaccountsServiceImpl.DIC_TYPE_SA, egew.getBalance(),"-1",EsLsbaccountsServiceImpl.STATUS_MSG_FAIL);
                    }
                }
            }
        }
    }
}
