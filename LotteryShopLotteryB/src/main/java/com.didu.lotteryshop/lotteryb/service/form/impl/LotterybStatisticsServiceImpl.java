package com.didu.lotteryshop.lotteryb.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.lotteryb.entity.LotterybStatistics;
import com.didu.lotteryshop.lotteryb.mapper.LotterybStatisticsMapper;
import com.didu.lotteryshop.lotteryb.service.form.ILotterybStatisticsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-02-22
 */
@Service
public class LotterybStatisticsServiceImpl extends ServiceImpl<LotterybStatisticsMapper, LotterybStatistics> implements ILotterybStatisticsService {


    /**
     * 新购买统计
     * @param lotterybIssueId 期数Id
     * @param lotterybNumber 购买号码
     * @param total 购买金额
     * @return
     */
    public boolean update(Integer lotterybIssueId, String lotterybNumber, BigDecimal total){
        Wrapper<LotterybStatistics> wrapper = new EntityWrapper();
        wrapper.and().eq("lotteryb_number",lotterybNumber).eq("lotteryb_issue_id",lotterybIssueId);
        wrapper.last("limit 1");
        LotterybStatistics lotterybStatistics = selectOne(wrapper);
        lotterybStatistics.setAmount(lotterybStatistics.getAmount().add(total));
        return super.updateById(lotterybStatistics);
    }




}
