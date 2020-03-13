package com.didu.lotteryshop.lotteryb.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.common.entity.SysConfig;
import com.didu.lotteryshop.common.service.form.impl.EsDlbaccountsServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.EsEthaccountsServiceImpl;
import com.didu.lotteryshop.lotteryb.entity.*;
import com.didu.lotteryshop.lotteryb.mapper.LotterybPmMapper;
import com.didu.lotteryshop.lotteryb.service.LotterybBuyService;
import com.didu.lotteryshop.lotteryb.service.LotterybIssueService;
import com.didu.lotteryshop.lotteryb.service.form.ILotterybPmService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-02-29
 */
@Service
public class LotterybPmServiceImpl extends ServiceImpl<LotterybPmMapper, LotterybPm> implements ILotterybPmService {


    @Autowired
    private LotterybIssueServiceImpl lotterybIssueServiceIml;
    @Autowired
    private LotterybInfoServiceImpl lotterybInfoServiceIml;
    @Autowired
    private LotterybIssueService lotterybIssueService;
    @Autowired
    private LotterybBuyService lotterybBuyService;
    @Autowired
    private EsDlbaccountsServiceImpl esDlbaccountsService;

    /**
     * 类型 中奖
     */
    public static final Integer TYPE_WINNING = 2;

    /**
     * 类型 提成
     */
    public static final  Integer TYPE_BUY = 1;

    /**
     * 待领取
     */
    public static final  Integer STATUS_TO_RECEIVE = 0;
    /**
     * 已领取
     */
    public static final  Integer STATUS_ALREADY_RECEIVE = 1;
    /**
     * 过期
     */
    public static final  Integer STATUS_PAST_DUE = 2;

    /**
     * 购买或新增中奖提成数据
     * @param memberId
     * @param lotteryAIssueId
     * @param total
     * @return
     */
    public LotterybPm updateOrInsertTotalWinning(String memberId, Integer lotteryAIssueId,Integer lotterybInfoId,BigDecimal total){
        return this.updateOrInsertTotal(memberId,lotteryAIssueId,lotterybInfoId,total,LotterybPmServiceImpl.TYPE_WINNING);
    }

    /**
     * 修改或新增购买提成数据
     * @param memberId
     * @param lotteryAIssueId
     * @param total
     * @return
     */
    public LotterybPm updateOrInsertTotalBuy(String memberId, Integer lotteryAIssueId,Integer lotterybInfoId,BigDecimal total){
        return this.updateOrInsertTotal(memberId,lotteryAIssueId,lotterybInfoId,total,LotterybPmServiceImpl.TYPE_BUY);
    }

    /**
     * 增加或修改提成数据
     * @param memberId
     * @param lotterybIssueId
     * @param total
     * @param type
     * @return
     */
    public LotterybPm updateOrInsertTotal(String memberId, Integer lotterybIssueId, Integer lotterybInfoId,BigDecimal total, Integer type) {
        LotterybPm lotterybPm = this.find(memberId,lotterybIssueId,type);
        if(lotterybPm != null && lotterybPm.getId() != null){
            lotterybPm.setTotal(lotterybPm.getTotal().add(total));
            lotterybPm = super.updateAllColumnById(lotterybPm) ? lotterybPm : null;
        }else{
            lotterybPm = this.add(memberId,lotterybIssueId,lotterybInfoId,total,type);
        }
        return lotterybPm;
    }


    /**
     * 查询所有待领取的提成
     * @return
     */
    public List<LotterybPm> findToReceive(Integer lotterybInfoId){
        Wrapper<LotterybPm> wrapper = new EntityWrapper<>();
        wrapper.and().eq("lotteryb_info_id",lotterybInfoId).eq("status",LotterybPmServiceImpl.STATUS_TO_RECEIVE);
        return super.selectList(wrapper);
    }

    /**
     * 领取提成
     * @param lotterybInfoId
     * @return
     */
    public boolean updateStatus(Integer lotterybInfoId,LotterybIssue nowLotterybIssue){
        boolean bool = false;
        List<LotterybPm> lotterybPmList = this.findToReceive(lotterybInfoId);
        //本期
        //LotterybIssue nowLotterybIssue = lotterybIssueServiceIml.findUpLotteryaIssue(lotterybInfoId);
        //上期
        //LotteryaIssue upLotteryaIssue = lotteryaIssueService.findUpLotteryaIssue();
        LotterybInfo lotterybInfo =  lotterybInfoServiceIml.find(lotterybInfoId);
        if(lotterybPmList != null && lotterybPmList.size() > 0){
            for(LotterybPm lbpm : lotterybPmList){
                //提成当期
                LotterybIssue lotterybIssue = lotterybIssueServiceIml.selectById(lbpm.getLotterybIssueId());
                if(lotterybIssueService.getIssueNumDifference(lotterybInfoId,nowLotterybIssue.getIssueNum(),lotterybIssue.getIssueNum()) <= lotterybInfo.getPmVnum()){
                    //有效提成数据
                    int cnt =  lotterybBuyService.getBuyCount(lbpm.getMemberId(),lotterybInfoId,nowLotterybIssue.getId());
                    if((cnt >= 1 && lbpm.getLotterybIssueId() == nowLotterybIssue.getId()) || (cnt >= lotterybInfo.getPmRnum())){
                        //领取提成
                        lbpm.setStatus(1);
                        lbpm.setStatusTime(new Date());
                        bool = super.updateById(lbpm);
                        if(bool){
                            //增加待领币
                            String dicType = lbpm.getType() == 1 ? EsDlbaccountsServiceImpl.DIC_TYPE_BUYLOTTERYB_PM : EsDlbaccountsServiceImpl.DIC_TYPE_WINLOTTERYB_PM;
                            bool = esDlbaccountsService.addInSuccess(lbpm.getMemberId(),dicType,lbpm.getTotal(),lbpm.getId().toString());
                        }
                        if(!bool) return bool;
                    }
                }else{
                    //作废数据
                    lbpm.setStatus(2);
                    lbpm.setStatusTime(new Date());
                    bool = super.updateById(lbpm);
                    if(!bool) return bool;
                }
            }
            bool = true;
        }else{
            bool = true;
        }
        return bool;
    }




    /**
     * 查询提成数据
     * @param memberId
     * @param lotterybIssueId
     * @param type
     * @return
     */
    private LotterybPm find(String memberId, Integer lotterybIssueId, Integer type) {
        Wrapper<LotterybPm> wrapper = new EntityWrapper<>();
        wrapper.eq("member_id",memberId)
        .and().eq("lotteryb_issue_id",lotterybIssueId)
        .and().eq("type",type);
        return super.selectOne(wrapper);
    }

    /**
     * 增加提成数据
     * @param memberId
     * @param lotterybIssueId
     * @param total
     * @param type
     * @return
     */
    private LotterybPm add(String memberId, Integer lotterybIssueId,Integer lotterybInfoId,BigDecimal total, Integer type) {
        LotterybPm lotterybPm = new LotterybPm();
        lotterybPm.setMemberId(memberId);
        lotterybPm.setLotterybIssueId(lotterybIssueId);
        lotterybPm.setTotal(total);
        lotterybPm.setStatus(0);
        lotterybPm.setStatusTime(new Date());
        lotterybPm.setCreateTime(new Date());
        lotterybPm.setType(type);
        lotterybPm.setLotterybInfoId(lotterybInfoId);
        boolean bool = super.insert(lotterybPm);
        return bool ? lotterybPm : null;
    }



}
