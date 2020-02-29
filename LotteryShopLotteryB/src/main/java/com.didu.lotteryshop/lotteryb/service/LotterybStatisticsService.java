package com.didu.lotteryshop.lotteryb.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.common.utils.CodeUtil;
import com.didu.lotteryshop.lotteryb.entity.LotterybStatistics;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybConfigServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybStatisticsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class LotterybStatisticsService {


    @Autowired
    private LotterybStatisticsServiceImpl lotterybStatisticsService;

    /**
     * 每次购买成功时统计购买金额
     * @param lotterybConfigId 玩法配置类型
     * @param lotterybInfoId 玩法ID
     * @param lotterybIssueId 期数
     * @param amount 购买金额
     * @return
     */
    public boolean lotteryStatistice(String lotterybConfigId, String lotterybInfoId, String lotterybIssueId, BigDecimal amount){
        //判断是否需要新建数据
        Wrapper<LotterybStatistics> wrapper = new EntityWrapper<>();
        wrapper.and().eq("lotteryb_config_id",lotterybConfigId);
        wrapper.eq("lotteryb_info_id",lotterybInfoId);
        wrapper.eq("lotteryb_issue_id",lotterybIssueId);
        List<LotterybStatistics> list = lotterybStatisticsService.selectList(wrapper);
        boolean b = false;
        if(list != null && list.isEmpty() && list.size() > 0){
            //取原来的金额加上本次购买的金额
            LotterybStatistics lotterybStatistics = list.get(0);
            lotterybStatistics.setAmount(lotterybStatistics.getAmount().add(amount));
            b = lotterybStatisticsService.updateById(lotterybStatistics);
        }else {
            //新插入一条数据
            LotterybStatistics lotterybStatistics = new LotterybStatistics();
            lotterybStatistics.setId(CodeUtil.getUuid());
            lotterybStatistics.setLotterybConfigId(Integer.parseInt(lotterybConfigId));
            lotterybStatistics.setLotterybInfoId(Integer.parseInt(lotterybInfoId));
            lotterybStatistics.setLotterybIssueId(Integer.parseInt(lotterybIssueId));
            lotterybStatistics.setAmount(amount);
            lotterybStatistics.setCreateDate(new Date());
            b = lotterybStatisticsService.insert(lotterybStatistics);
        }
        return b;
    }

    /**
     * 根据玩法和周期获取本次购买统计(只包括和值)
     * @param lotterybInfoId
     * @param lotterybIssueId
     * @return
     */
    public List<LotterybStatistics> findLotterybStatistics(String lotterybInfoId,String lotterybIssueId){
        Wrapper<LotterybStatistics> wrapper = new EntityWrapper<>();
        wrapper.and().eq("lotteryb_info_id",lotterybInfoId);
        wrapper.eq("lotteryb_issue_id",lotterybIssueId);
        wrapper.and("lotteryb_config_id > ? ", LotterybConfigServiceImpl.ID_1);
        return lotterybStatisticsService.selectList(wrapper);
    }

    /**
     * 根据玩法和周期获取本次购买统计(特殊，单、双、大和小)
     * @param lotterybInfoId
     * @param lotterybIssueId
     * @return
     */
    public List<LotterybStatistics> findLotterybStatisticsSpecial(String lotterybInfoId,String lotterybIssueId){
        Wrapper<LotterybStatistics> wrapper = new EntityWrapper<>();
        wrapper.and().eq("lotteryb_info_id",lotterybInfoId);
        wrapper.eq("lotteryb_issue_id",lotterybIssueId);
        wrapper.and("lotteryb_config_id < ? ",LotterybConfigServiceImpl.ID_2);
        return lotterybStatisticsService.selectList(wrapper);
    }

    /**
     *  获取本期总金额
     * @param lotterybInfoId
     * @param lotterybIssueId
     * @return
     */
    public BigDecimal getAllAmount(String lotterybInfoId,String lotterybIssueId){
        List<LotterybStatistics> lotterybStatistics  = findLotterybStatistics(lotterybInfoId,lotterybIssueId);
        BigDecimal bigDecimal = BigDecimal.ZERO;
        for (LotterybStatistics s : lotterybStatistics) {
            bigDecimal = bigDecimal.add(s.getAmount());
        }
        return bigDecimal;
    }

}
