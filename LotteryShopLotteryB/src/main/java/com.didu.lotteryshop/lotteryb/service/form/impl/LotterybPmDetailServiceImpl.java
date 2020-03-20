package com.didu.lotteryshop.lotteryb.service.form.impl;

import com.didu.lotteryshop.common.entity.SysConfig;
import com.didu.lotteryshop.common.service.form.impl.MemberServiceImpl;
import com.didu.lotteryshop.lotteryb.entity.*;
import com.didu.lotteryshop.lotteryb.mapper.LotterybPmDetailMapper;
import com.didu.lotteryshop.lotteryb.service.form.ILotterybPmDetailService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
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
public class LotterybPmDetailServiceImpl extends ServiceImpl<LotterybPmDetailMapper, LotterybPmDetail> implements ILotterybPmDetailService {

    @Autowired
    private LotterybPmServiceImpl lotterybPmService;
    @Autowired
    private LotterybInfoPcServiceImpl lotterybInfoPcServiceIml;
    @Autowired
    private MemberServiceImpl memberService;

    /**
     * 新增中奖提成明细
     * @param memberId
     * @param lotterybIssueId
     * @param lotterybBuyId
     * @param total
     * @param level
     * @param ratio
     * @return
     */
    public boolean addWinning(String memberId,Integer lotterybIssueId,Integer lotterybInfoId,Integer lotterybBuyId, BigDecimal total, Integer level, BigDecimal ratio){
        return this.add(memberId,lotterybIssueId,lotterybInfoId,lotterybBuyId,total,level,ratio,2);
    }

    /**
     * 新增购买提成明细
     * @param memberId
     * @param lotterybIssueId
     * @param lotterybBuyId
     * @param total
     * @param level
     * @param ratio
     * @return
     */
    public boolean addBuy(String memberId,Integer lotterybIssueId,Integer lotterybInfoId,Integer lotterybBuyId, BigDecimal total, Integer level, BigDecimal ratio){
        return this.add(memberId,lotterybIssueId,lotterybInfoId,lotterybBuyId,total,level,ratio,1);
    }

    /**
     * 新增提成明细数据
     * @param memberId
     * @param lotterybIssueId
     * @param lotterybBuyId
     * @param total
     * @param level
     * @param ratio
     * @param type
     * @return
     */
    public boolean add(String memberId, Integer lotterybIssueId, Integer lotterybInfoId ,Integer lotterybBuyId, BigDecimal total, Integer level, BigDecimal ratio, Integer type){
        boolean bool = false;
        if(StringUtils.isBlank(memberId) || lotterybIssueId == null || lotterybIssueId == null || total == null || level == null || ratio == null || type == null){
            return bool;
        }
        LotterybPm lotterybPm = lotterybPmService.updateOrInsertTotal(memberId,lotterybIssueId,lotterybInfoId,total,type);
        if(lotterybPm != null){
            LotterybPmDetail lotterybPmDetail = new LotterybPmDetail();
            lotterybPmDetail.setLotterybPmId(lotterybPm.getId());
            lotterybPmDetail.setLotterybBuyId(lotterybBuyId);
            lotterybPmDetail.setTotal(total);
            lotterybPmDetail.setLevel(level);
            lotterybPmDetail.setRatio(ratio);
            lotterybPmDetail.setCreateTime(new Date());
            bool = super.insert(lotterybPmDetail);
        }
        return bool;
    }

    /**
     * 中奖提成
     * @param lotterybBuy
     * @param lotterybInfo
     * @return
     */
    public boolean drawPM(LotterybBuy lotterybBuy, LotterybInfo lotterybInfo){
        boolean bool = false;
        List<LotterybInfoPc> LotterybInfoPcList = lotterybInfoPcServiceIml.findWinning(lotterybInfo.getId());
        BigDecimal totalDlb = lotterybBuy.getLuckTotal();
        //中奖提成 10%
        totalDlb = totalDlb.divide(new BigDecimal("100")).multiply(lotterybInfo.getWinningPm());
        if(LotterybInfoPcList != null && LotterybInfoPcList.size() > 0){
            for(LotterybInfoPc laipc:LotterybInfoPcList){
                String upMemberId  = memberService.findLevelMemberId(lotterybBuy.getMemberId(),laipc.getLevel());
                if(StringUtils.isBlank(upMemberId)) return true;
                BigDecimal total = (totalDlb.divide(new BigDecimal("100")).multiply(laipc.getRatio())).setScale(4,BigDecimal.ROUND_DOWN);
                bool = this.addWinning(upMemberId,lotterybBuy.getLotterybIssueId(),lotterybBuy.getLotterybInfoId(),lotterybBuy.getId(),total,laipc.getLevel(),laipc.getRatio());
                if(!bool) return bool;

            }
        }
        return bool;
    }


    /**
     * 购买提成
     * @param lotterybBuy
     * @param lotterybInfo
     * @return
     */
    public boolean buyPM(LotterybBuy lotterybBuy, LotterybInfo lotterybInfo){
        boolean bool = false;
        List<LotterybInfoPc> LotterybInfoPcList = lotterybInfoPcServiceIml.findBuy(lotterybInfo.getId());
        BigDecimal totalDlb = lotterybBuy.getTotal();
        //购买提成 5%
        totalDlb = totalDlb.divide(new BigDecimal("100")).multiply(lotterybInfo.getBuyPm());
        if(LotterybInfoPcList != null && LotterybInfoPcList.size() > 0){
            for(LotterybInfoPc laipc:LotterybInfoPcList){
                String upMemberId  = memberService.findLevelMemberId(lotterybBuy.getMemberId(),laipc.getLevel());
                if(StringUtils.isBlank(upMemberId)) return true;
                BigDecimal total = (totalDlb.divide(new BigDecimal("100")).multiply(laipc.getRatio())).setScale(4,BigDecimal.ROUND_DOWN);
                bool = this.addBuy(upMemberId,lotterybBuy.getLotterybIssueId(),lotterybInfo.getId(),lotterybBuy.getId(),total,laipc.getLevel(),laipc.getRatio());
                if(!bool) return bool;
            }
        }
        return bool;
    }

}
