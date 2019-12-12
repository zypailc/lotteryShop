package com.didu.lotteryshop.lotterya.service.form.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.SysConfig;
import com.didu.lotteryshop.common.service.form.impl.MemberServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.SysConfigServiceImpl;
import com.didu.lotteryshop.lotterya.entity.*;
import com.didu.lotteryshop.lotterya.mapper.LotteryaPmDetailMapper;
import com.didu.lotteryshop.lotterya.service.form.ILotteryaPmDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * A彩票提成明细 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-03
 */
@Service
public class LotteryaPmDetailServiceImpl extends ServiceImpl<LotteryaPmDetailMapper, LotteryaPmDetail> implements ILotteryaPmDetailService {
    @Autowired
    private LotteryaPmServiceImpl lotteryaPmService;
    @Autowired
    private LotteryaInfoPcServiceImpl lotteryaInfoPcService;
    @Autowired
    private SysConfigServiceImpl sysConfigService;
    @Autowired
    private MemberServiceImpl memberService;
    /**
     * 新增中奖提成明细
     * @param memberId
     * @param lotteryAIssueId
     * @param lotteryABuyId
     * @param total
     * @param level
     * @param ratio
     * @return
     */
    public boolean addDraw(String memberId,Integer lotteryAIssueId,Integer lotteryABuyId, BigDecimal total, Integer level, BigDecimal ratio){
        return this.add(memberId,lotteryAIssueId,lotteryABuyId,total,level,ratio,2);
    }

    /**
     * 新增购买提成明细
     * @param memberId
     * @param lotteryAIssueId
     * @param lotteryABuyId
     * @param total
     * @param level
     * @param ratio
     * @return
     */
    public boolean addBuy(String memberId,Integer lotteryAIssueId,Integer lotteryABuyId, BigDecimal total, Integer level, BigDecimal ratio){
        return this.add(memberId,lotteryAIssueId,lotteryABuyId,total,level,ratio,1);
    }

    /**
     * 新增提成明细
     * @param memberId
     * @param lotteryAIssueId
     * @param lotteryABuyId
     * @param total
     * @param level
     * @param ratio
     * @param type
     * @return
     */
    public boolean add(String memberId,Integer lotteryAIssueId,Integer lotteryABuyId, BigDecimal total, Integer level, BigDecimal ratio,Integer type){
        boolean bool = false;
        if(StringUtils.isBlank(memberId) || lotteryAIssueId == null || lotteryABuyId == null || total == null || level == null || ratio == null || type == null){
            return bool;
        }
        LotteryaPm lotteryaPm = lotteryaPmService.updateOrInsertTotal(memberId,lotteryAIssueId,total,type);
        if(lotteryaPm != null){
            LotteryaPmDetail lotteryaPmDetail = new LotteryaPmDetail();
            lotteryaPmDetail.setLotteryaPmId(lotteryaPm.getId());
            lotteryaPmDetail.setLotteryaBuyId(lotteryABuyId);
            lotteryaPmDetail.setTotal(total);
            lotteryaPmDetail.setLevel(level);
            lotteryaPmDetail.setRatio(ratio);
            lotteryaPmDetail.setCreateTime(new Date());
            bool = super.insert(lotteryaPmDetail);
        }
        return bool;
    }

    /**
     * 中奖提成
     * @param lotteryaBuy
     * @param lotteryaInfo
     * @return
     */
    public boolean drawPM(LotteryaBuy lotteryaBuy, LotteryaInfo lotteryaInfo){
        boolean bool = false;
        List<LotteryaInfoPc> LotteryaInfoPcList = lotteryaInfoPcService.findDraw(lotteryaInfo.getId());
        BigDecimal totalEther = lotteryaBuy.getLuckTotal();
        //中奖提成 10%
        totalEther = totalEther.divide(new BigDecimal("100")).multiply(lotteryaInfo.getDrawPm());
        SysConfig sysConfig = sysConfigService.getSysConfig();
        //转换为平台币
        BigDecimal  totalDlb = totalEther.multiply(sysConfig.getEthToLsb());
        if(LotteryaInfoPcList != null && LotteryaInfoPcList.size() > 0){
            for(LotteryaInfoPc laipc:LotteryaInfoPcList){
                String upMemberId  = memberService.findLevelMemberId(lotteryaBuy.getMemberId(),laipc.getLevel());
                if(StringUtils.isBlank(upMemberId)) return true;
                BigDecimal total = (totalDlb.divide(new BigDecimal("100")).multiply(laipc.getRatio())).setScale(4,BigDecimal.ROUND_DOWN);
                bool = this.addDraw(upMemberId,lotteryaBuy.getLotteryaIssueId(),lotteryaBuy.getId(),total,laipc.getLevel(),laipc.getRatio());
                if(!bool) return bool;
            }
        }
        return bool;
    }


    /**
     * 购买提成
     * @param lotteryaBuy
     * @param lotteryaInfo
     * @return
     */
    public boolean buyPM(LotteryaBuy lotteryaBuy, LotteryaInfo lotteryaInfo){
        boolean bool = false;
        List<LotteryaInfoPc> LotteryaInfoPcList = lotteryaInfoPcService.findBuy(lotteryaInfo.getId());
        BigDecimal totalEther = lotteryaBuy.getTotal();
        //购买提成 5%
        totalEther = totalEther.divide(new BigDecimal("100")).multiply(lotteryaInfo.getBuyPm());
        SysConfig sysConfig = sysConfigService.getSysConfig();
        //转换为平台币
        BigDecimal  totalDlb = totalEther.multiply(sysConfig.getEthToLsb());
        if(LotteryaInfoPcList != null && LotteryaInfoPcList.size() > 0){
            for(LotteryaInfoPc laipc:LotteryaInfoPcList){
                String upMemberId  = memberService.findLevelMemberId(lotteryaBuy.getMemberId(),laipc.getLevel());
                if(StringUtils.isBlank(upMemberId)) return true;
                BigDecimal total = (totalDlb.divide(new BigDecimal("100")).multiply(laipc.getRatio())).setScale(4,BigDecimal.ROUND_DOWN);
                bool = this.addBuy(upMemberId,lotteryaBuy.getLotteryaIssueId(),lotteryaBuy.getId(),total,laipc.getLevel(),laipc.getRatio());
                if(!bool) return bool;
            }
        }
        return bool;
    }

}
