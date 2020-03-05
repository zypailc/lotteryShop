package com.didu.lotteryshop.lotteryb.service;

import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.didu.lotteryshop.lotteryb.entity.*;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybConfigServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybInfoServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybIssueServiceImpl;
import jnr.ffi.annotations.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 竞猜B类开奖
 */
@Service
public class LotterybStartService extends LotteryBBaseService {

    @Autowired
    private LotterybInfoServiceImpl lotterybInfoService;
    @Autowired
    private LotterybIssueServiceImpl lotterybIssueServiceIml;
    @Autowired
    private LotterybProportionService lotterybProportionService;
    @Autowired
    private LotterybIssueService lotterybIssueService;
    @Autowired
    private LotterybStatisticsService lotterybStatisticsService;
    @Autowired
    private LotterybConfigServiceImpl lotterybConfigService;

    /**
     * 根据玩法Id开奖本玩法
     * @param lotterybInfoId 玩法类型
     */
    public void lotteryBDraw(String lotterybInfoId){
        //查询开奖玩法
        LotterybInfo lotterybInfo = lotterybInfoService.selectById(Integer.parseInt(lotterybInfoId));
        //查询当前期数
        LotterybIssue lotterybIssue = lotterybIssueServiceIml.selectById(lotterybInfoId);
        //if(lotterybIssue != null){//如果没有查到当前玩法的期数 直接生成下一期数据
        //获取本次返奖率
        LotterybProportion lotterybProportion = lotterybProportionService.getProportionNext(lotterybIssue.getLotterybProportionId());
        //查询每组号码所买金额(和值)
        List<LotterybStatistics> lotterybStatistics_sum = lotterybStatisticsService.findLotterybStatistics(lotterybInfoId,lotterybIssue.getId().toString());
        //单双大小
        List<LotterybStatistics> lotterybStatistics_special = lotterybStatisticsService.findLotterybStatisticsSpecial(lotterybInfoId,lotterybIssue.getId().toString());

        //计算每组号码需赔付多少金币（和值）
        List<Map<String,Object>> list_sum = calculateSum(lotterybStatistics_sum);
        //单双大小
        List<Map<String,Object>> list_special = calculateSum(lotterybStatistics_special);
        BigDecimal single = BigDecimal.ZERO;
        BigDecimal doubles = BigDecimal.ZERO;
        BigDecimal large = BigDecimal.ZERO;
        BigDecimal small = BigDecimal.ZERO;
        for ( Map m : list_special ) {
            Integer lotterybConfigId = Integer.parseInt(m.get("lotterybConfigId").toString());
            if(lotterybConfigId == LotterybConfigServiceImpl.SINGLE_ID){
                single = new BigDecimal(m.get("propottion").toString());
            }
            if(lotterybConfigId == LotterybConfigServiceImpl.DOUBLE_ID){
                doubles = new BigDecimal(m.get("propottion").toString());
            }
            if(lotterybConfigId == LotterybConfigServiceImpl.LARGE_ID){
                large = new BigDecimal(m.get("propottion").toString());
            }
            if(lotterybConfigId == LotterybConfigServiceImpl.SMALL_ID){
                small = new BigDecimal(m.get("propottion").toString());
            }
        }
        Integer proportion = lotterybProportion.getProportion()/100;
        BigDecimal minAmount = BigDecimal.ZERO;
        String luckNumConfig = "";
        for (Map m : list_sum) {
            String lotterybConfigId = m.get("lotterybConfigId").toString();
            BigDecimal proportionPractical = new BigDecimal(m.get("propottion").toString());
            if((Integer.parseInt(lotterybConfigId) - LotterybConfigServiceImpl.DIFFERENCE_VALUE) <= LotterybConfigServiceImpl.TYPE_VALUE_12){
                //小
                proportionPractical = proportionPractical.add(small);
            }else {
                //大
                proportionPractical = proportionPractical.add(large);
            }
            if((Integer.parseInt(lotterybConfigId) - LotterybConfigServiceImpl.DIFFERENCE_VALUE)%2 == 0){
                //双
                proportionPractical.add(doubles);
            }else {
                //单
                proportionPractical.add(single);
            }
            Double d1 = (new BigDecimal(proportion).subtract(proportionPractical)).doubleValue();
            Double d2 = (proportionPractical.subtract((new BigDecimal(proportion)))).doubleValue();
            if(d1 < 0) d1 = d1 * (-1);
            if(d2 < 0) d2 = d2 * (-1);
            BigDecimal b = new BigDecimal(m.get("amount") == null ? "0" : m.get("amount").toString());
            if(minAmount == BigDecimal.ZERO){
                minAmount = b;
                luckNumConfig = lotterybConfigId;
            }else {
                if(b.compareTo(minAmount) < 0){
                    minAmount = b;
                    luckNumConfig = lotterybConfigId;
                }
            }
            if(d1 < 0.5 && d2 <= 0.5){//在这个比例 0.5 靠近 //如果都不在 则记录赔付最小值
                minAmount = b;
                //LotterybConfig lotterybConfig = lotterybConfigService.selectById(lotterybConfigId);
                luckNumConfig = lotterybConfigId;
                break;
            }
            lotterybIssue.setLuckTotal(b);
            lotterybIssue.setLotterybInfoId(Integer.parseInt(luckNumConfig));
            //计算开奖号
            String luckNum = lotterybConfigService.getLuckNum(Integer.parseInt(lotterybConfigId));
            lotterybIssue.setLuckNum(luckNum);
            lotterybIssue.setLotterybProportionId(lotterybProportion.getId());
            //保存中奖信息
        }
        //}else {
        lotterybIssueService.createNextLotterybIssue(Integer.parseInt(lotterybInfoId));//生成下一期数据
        //}
    }

    /**
     * 计算每组金额需要赔付多少
     * @return
     */
    private List<Map<String,Object>> calculateSum(List<LotterybStatistics> lotterybStatistics){
        Map<String,Object> map = lotterybConfigService.getConfigMap(null);
        List<Map<String,Object>> list = new ArrayList<>();
        BigDecimal allAmount = BigDecimal.ZERO;
        for (LotterybStatistics s : lotterybStatistics) {
            allAmount = allAmount.add(s.getAmount());
        }
        for (LotterybStatistics statistics : lotterybStatistics) {
            BigDecimal bigDecimal = map.get(statistics.getLotterybConfigId()) == null ? BigDecimal.ZERO:new BigDecimal(map.get(statistics.getLotterybConfigId()).toString());
            Map<String,Object> m = new HashMap<>();
            BigDecimal amount = bigDecimal.multiply(statistics.getAmount());
            m.put("amount",amount);
            m.put("lotterybConfigId",statistics.getLotterybConfigId());
            m.put("propottion",amount.divide(allAmount).setScale(4,BigDecimal.ROUND_DOWN));
            list.add(m);
        }
        return list;
    }

}
