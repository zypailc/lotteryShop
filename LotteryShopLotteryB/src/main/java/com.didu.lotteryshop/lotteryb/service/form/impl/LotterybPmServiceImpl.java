package com.didu.lotteryshop.lotteryb.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.lotteryb.entity.LotterybPm;
import com.didu.lotteryshop.lotteryb.mapper.LotterybPmMapper;
import com.didu.lotteryshop.lotteryb.service.form.ILotterybPmService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

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
    public LotterybPm updateOrInsertTotalWinning(String memberId, Integer lotteryAIssueId,BigDecimal total){
        return this.updateOrInsertTotal(memberId,lotteryAIssueId,total,LotterybPmServiceImpl.TYPE_WINNING);
    }

    /**
     * 修改或新增购买提成数据
     * @param memberId
     * @param lotteryAIssueId
     * @param total
     * @return
     */
    public LotterybPm updateOrInsertTotalBuy(String memberId, Integer lotteryAIssueId,BigDecimal total){
        return this.updateOrInsertTotal(memberId,lotteryAIssueId,total,LotterybPmServiceImpl.TYPE_BUY);
    }

    /**
     * 增加或修改提成数据
     * @param memberId
     * @param lotterybIssueId
     * @param total
     * @param type
     * @return
     */
    public LotterybPm updateOrInsertTotal(String memberId, Integer lotterybIssueId, BigDecimal total, Integer type) {
        LotterybPm lotterybPm = this.find(memberId,lotterybIssueId,type);
        if(lotterybPm != null && lotterybPm.getId() != null){
            lotterybPm.setTotal(lotterybPm.getTotal().add(total));
            lotterybPm = super.updateAllColumnById(lotterybPm) ? lotterybPm : null;
        }else{
            lotterybPm = this.add(memberId,lotterybIssueId,total,type);
        }
        return lotterybPm;
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
    private LotterybPm add(String memberId, Integer lotterybIssueId, BigDecimal total, Integer type) {
        LotterybPm lotteryaPm = new LotterybPm();
        lotteryaPm.setMemberId(memberId);
        lotteryaPm.setLotterybIssueId(lotterybIssueId);
        lotteryaPm.setTotal(total);
        lotteryaPm.setStatus(0);
        lotteryaPm.setStatusTime(new Date());
        lotteryaPm.setCreateTime(new Date());
        lotteryaPm.setType(type);
        boolean bool = super.insert(lotteryaPm);
        return bool ? lotteryaPm : null;
    }
}
