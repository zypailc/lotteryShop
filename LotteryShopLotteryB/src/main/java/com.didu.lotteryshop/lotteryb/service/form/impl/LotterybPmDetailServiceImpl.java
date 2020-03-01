package com.didu.lotteryshop.lotteryb.service.form.impl;

import com.didu.lotteryshop.lotteryb.entity.LotterybPm;
import com.didu.lotteryshop.lotteryb.entity.LotterybPmDetail;
import com.didu.lotteryshop.lotteryb.mapper.LotterybPmDetailMapper;
import com.didu.lotteryshop.lotteryb.service.form.ILotterybPmDetailService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LotterybPmDetailServiceImpl extends ServiceImpl<LotterybPmDetailMapper, LotterybPmDetail> implements ILotterybPmDetailService {

    @Autowired
    private LotterybPmServiceImpl lotterybPmService;

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

}
