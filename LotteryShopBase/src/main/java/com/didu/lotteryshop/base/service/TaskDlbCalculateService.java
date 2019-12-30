package com.didu.lotteryshop.base.service;

import com.didu.lotteryshop.base.entity.EsDlbconfig;
import com.didu.lotteryshop.base.entity.EsDlbconfigAcquire;
import com.didu.lotteryshop.base.service.form.impl.EsDlbconfigAcquireServiceImpl;
import com.didu.lotteryshop.base.service.form.impl.EsDlbconfigServiceImpl;
import com.didu.lotteryshop.common.entity.EsDlbwallet;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.service.form.impl.*;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 定时提取待领币到平台币任务Service
 */
@Service
public class TaskDlbCalculateService {
    private static final Logger logger = LoggerFactory.getLogger(TaskDlbCalculateService.class);
    @Autowired
    private EsDlbaccountsServiceImpl esDlbaccountsService;
    @Autowired
    private EsDlbwalletServiceImpl esDlbwalletService;
    @Autowired
    private EsDlbconfigServiceImpl esDlbconfigService;
    @Autowired
    private EsEthaccountsServiceImpl esEthaccountsService;
    @Autowired
    private EsDlbconfigAcquireServiceImpl esDlbconfigAcquireService;
    @Autowired
    private EsLsbaccountsServiceImpl esLsbaccountsService;
    @Autowired
    private MemberServiceImpl memberService;

    /**
     * 提取待领币为平台币
     */
    public void dlbCalculate(){
        //step 1:查询待领余额大于10的所有用户。
        //step 2:查询用户周期内消费是否达到条件。
        //step 3:查询用户7层活跃用户数；
        //step 4:根据活跃用户数查询提取配置
        //step 5:根据提取配置，提取待领币为平台币。
        List<EsDlbwallet> esDlbwalletList = esDlbwalletService.findBalanceGtTen();
        if(esDlbwalletList != null && esDlbwalletList.size() > 0){
            EsDlbconfig esDlbconfig = esDlbconfigService.findEsDlbconfig();
            //int maxActiveMembers = esDlbconfigAcquireService.findMaxActiveMembers();
            //结算周期
            Date calculateDate = DateUtils.addDays(new Date(),-esDlbconfig.getCalculateDay());
            BigDecimal consumeTotal = BigDecimal.ZERO;
            for(EsDlbwallet edw : esDlbwalletList){
                Member member = memberService.selectById(edw.getMemberId());
                //新注册的会员未到结算周期
                if(member == null) continue;
                if(member.getCreateTime().compareTo(calculateDate) > 0) continue;
                //周期内是否已经结算过，结算过下一条
                if(esDlbaccountsService.findToSAByDay(member.getId(),esDlbconfig.getCalculateDay())) continue;
                consumeTotal = BigDecimal.ZERO;
                consumeTotal = esEthaccountsService.findConsumeTotalByDay(edw.getMemberId(),esDlbconfig.getCycleDay());
                //满足第一条件（周期内消费达到）
                if(consumeTotal.compareTo(esDlbconfig.getConsumeTotal()) >= 0){
                    //下级活跃人数
                    //int activeMembers = esEthaccountsService.findActiveMembers(edw.getMemberId(),esDlbconfig.getConsumeTotal(),esDlbconfig.getcLevel(),esDlbconfig.getCycleDay(),maxActiveMembers,0);
                    int activeMembers = memberService.findActiveMembers(edw.getMemberId(),esDlbconfig.getConsumeTotal(),esDlbconfig.getcLevel(),esDlbconfig.getCycleDay());
                    EsDlbconfigAcquire esDlbconfigAcquire = esDlbconfigAcquireService.findEsDlbconfigAcquireByActiveMembers(activeMembers);
                    if(esDlbconfigAcquire != null && esDlbconfigAcquire.getRatio() != null && esDlbconfigAcquire.getRatio().compareTo(BigDecimal.ZERO) > 0){
                        BigDecimal total = edw.getBalance().divide(new BigDecimal("100")).multiply(esDlbconfigAcquire.getRatio()).setScale(4,BigDecimal.ROUND_DOWN);
                       //待领币出账
                        boolean bool = esDlbaccountsService.addOutSuccess(edw.getMemberId(),EsDlbaccountsServiceImpl.DIC_TYPE_DRAW,total,"-1");
                        if(bool){
                            //平台币进账
                            bool = esLsbaccountsService.addInSuccess(edw.getMemberId(),EsLsbaccountsServiceImpl.DIC_TYPE_EXTRACT,total,"-1");
                        }
                        if(!bool) return;
                    }
                }
            }
        }

    }
}
