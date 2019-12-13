package com.didu.lotteryshop.lotterya.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.EsDlbaccountsServiceImpl;
import com.didu.lotteryshop.lotterya.entity.LotteryaInfo;
import com.didu.lotteryshop.lotterya.entity.LotteryaIssue;
import com.didu.lotteryshop.lotterya.entity.LotteryaPm;
import com.didu.lotteryshop.lotterya.mapper.LotteryaPmMapper;
import com.didu.lotteryshop.lotterya.service.form.ILotteryaPmService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * A彩票提成 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-03
 */
@Service
public class LotteryaPmServiceImpl extends ServiceImpl<LotteryaPmMapper, LotteryaPm> implements ILotteryaPmService {
    @Autowired
    private LotteryaIssueServiceImpl lotteryaIssueService;
    @Autowired
    private LotteryaInfoServiceImpl lotteryaInfoService;
    @Autowired
    private LotteryaBuyServiceImpl lotteryaBuyService;
    @Autowired
    private EsDlbaccountsServiceImpl esDlbaccountsService;
    /**
     * 新增中奖提成
     * @param memberId
     * @param lotteryAIssueId
     * @param total
     * @return
     */
    public LotteryaPm addDraw(String memberId, Integer lotteryAIssueId, BigDecimal total){
        return this.add(memberId,lotteryAIssueId,total,2);
    }

    /**
     * 新增购买提成
     * @param memberId
     * @param lotteryAIssueId
     * @param total
     * @return
     */
    public LotteryaPm addBuy(String memberId, Integer lotteryAIssueId, BigDecimal total){
        return this.add(memberId,lotteryAIssueId,total,1);
    }

    /**
     * 新增提成
     * @param memberId
     * @param lotteryAIssueId
     * @param total
     * @param type
     * @return
     */
	public LotteryaPm add(String memberId, Integer lotteryAIssueId, BigDecimal total, Integer type){
        LotteryaPm lotteryaPm = new LotteryaPm();
        lotteryaPm.setMemberId(memberId);
        lotteryaPm.setLotteryaIssueId(lotteryAIssueId);
        lotteryaPm.setTotal(total);
        lotteryaPm.setStatus("0");
        lotteryaPm.setStatusTime(new Date());
        lotteryaPm.setCreateTime(new Date());
        lotteryaPm.setType(type);
        boolean bool = super.insert(lotteryaPm);
        return bool ? lotteryaPm : null;
    }

    /**
     * 查询中奖提成数据
     * @param memberId
     * @param lotteryAIssueId
     * @return
     */
    public LotteryaPm findDraw(String memberId, Integer lotteryAIssueId){
	    return find(memberId,lotteryAIssueId,2);
    }

    /**
     * 查询购买提成数据
     * @param memberId
     * @param lotteryAIssueId
     * @return
     */
    public LotteryaPm findBuy(String memberId, Integer lotteryAIssueId){
	    return this.find(memberId,lotteryAIssueId,1);
    }

    /**
     * 查询提成数据
     * @param memberId
     * @param lotteryAIssueId
     * @param type
     * @return
     */
    public LotteryaPm find(String memberId, Integer lotteryAIssueId,Integer type){
        Wrapper<LotteryaPm> wrapper = new EntityWrapper<>();
        wrapper.eq("member_id",memberId)
        .and().eq("lotterya_issue_id",lotteryAIssueId)
        .and().eq("type",type);
        return super.selectOne(wrapper);
    }

    /**
     * 购买或新增中奖提成数据
     * @param memberId
     * @param lotteryAIssueId
     * @param total
     * @return
     */
    public LotteryaPm updateOrInsertTotalDraw(String memberId, Integer lotteryAIssueId,BigDecimal total){
        return this.updateOrInsertTotal(memberId,lotteryAIssueId,total,2);
    }

    /**
     * 修改或新增购买提成数据
     * @param memberId
     * @param lotteryAIssueId
     * @param total
     * @return
     */
    public LotteryaPm updateOrInsertTotalBuy(String memberId, Integer lotteryAIssueId,BigDecimal total){
        return this.updateOrInsertTotal(memberId,lotteryAIssueId,total,1);
    }

    /**
     * 修改或新增提成数据
     * @param memberId
     * @param lotteryAIssueId
     * @param total
     * @param type
     * @return
     */
    public LotteryaPm updateOrInsertTotal(String memberId, Integer lotteryAIssueId,BigDecimal total,Integer type){
        LotteryaPm lotteryaPm = this.find(memberId,lotteryAIssueId,type);
        if(lotteryaPm != null && lotteryaPm.getId() != null){
            lotteryaPm.setTotal(lotteryaPm.getTotal().add(total));
            lotteryaPm = super.updateAllColumnById(lotteryaPm) ? lotteryaPm : null;
        }else{
            lotteryaPm = this.add(memberId,lotteryAIssueId,total,type);
        }
        return lotteryaPm;
    }

    /**
     * 查询所有待领取的提成
     * @return
     */
    public List<LotteryaPm>  findToReceive(){
        Wrapper<LotteryaPm> wrapper = new EntityWrapper<>();
        wrapper.eq("status",0);
        return super.selectList(wrapper);
    }

    /**
     * 领取提成
     * @return
     */
    public boolean updateStatus(){
        boolean bool = false;
        List<LotteryaPm> lotteryaPmList = this.findToReceive();
        //本期
        LotteryaIssue nowLotteryaIssue = lotteryaIssueService.findCurrentPeriodLotteryaIssue();
        //上期
        //LotteryaIssue upLotteryaIssue = lotteryaIssueService.findUpLotteryaIssue();
        LotteryaInfo lotteryaInfo =  lotteryaInfoService.findLotteryaInfo();
        if(lotteryaPmList != null && lotteryaPmList.size() > 0){
            for(LotteryaPm lapm : lotteryaPmList){
                //提成当期
                LotteryaIssue lotteryaIssue = lotteryaIssueService.selectById(lapm.getLotteryaIssueId());
                if(nowLotteryaIssue.getIssueNum() - lotteryaIssue.getIssueNum() <= lotteryaInfo.getPmVnum()){
                    //有效提成数据
                  int cnt =  lotteryaBuyService.getBuyCount(lapm.getMemberId(),nowLotteryaIssue.getId());
                  if((cnt >= 1 && lapm.getLotteryaIssueId() == nowLotteryaIssue.getId()) || (cnt >= lotteryaInfo.getPmRnum())){
                      //领取提成
                      lapm.setStatus("1");
                      lapm.setStatusTime(new Date());
                      bool = super.updateById(lapm);
                      if(bool){
                          //增加待领币
                          String dicType = lapm.getType() == 1 ? EsDlbaccountsServiceImpl.DIC_TYPE_BUYLOTTERYA_PM : EsDlbaccountsServiceImpl.DIC_TYPE_BUYLOTTERYA_PM;
                          bool = esDlbaccountsService.addInSuccess(lapm.getMemberId(),dicType,lapm.getTotal(),lapm.getId().toString());
                      }
                      if(!bool) return bool;
                  }
                }else{
                    //作废数据
                    lapm.setStatus("2");
                    lapm.setStatusTime(new Date());
                    bool = super.updateById(lapm);
                    if(!bool) return bool;
                }
            }
            bool = true;
        }else{
            bool = true;
        }
        return bool;
    }

}
