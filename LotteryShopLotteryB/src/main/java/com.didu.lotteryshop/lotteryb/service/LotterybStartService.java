package com.didu.lotteryshop.lotteryb.service;

import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.didu.lotteryshop.lotteryb.entity.*;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybBuyServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybConfigServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybInfoServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybIssueServiceImpl;
import jnr.ffi.annotations.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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
    @Autowired
    private LotterybBuyServiceImpl lotterybBuyServiceIml;


    /**
     * 根据玩法Id开奖本玩法
     * @param lotterybInfoId 玩法类型
     */
    public void lotteryBDraw(Integer lotterybInfoId){
        //查询开奖玩法
        LotterybInfo lotterybInfo = lotterybInfoService.selectById(lotterybInfoId);
        //查询当前期数(在开奖前已经生成下期数据，所有这里要查询上期的数据)
        LotterybIssue lotterybIssue = lotterybIssueServiceIml.findUpLotteryaIssue(lotterybInfoId);
        if(lotterybIssue == null){
            //lotterybIssueService.createNextLotterybIssue(lotterybInfoId);
            return;
        }
        //立即修改此期不可购买 有可能出现计算开奖号码延时导致数据误差
        lotterybIssue.setByStatus(1);
        lotterybIssue.setBsTime(new Date());
        lotterybIssueServiceIml.updateById(lotterybIssue);
        //开始背刺开奖
        //获取本次返奖率
        LotterybProportion lotterybProportion = lotterybProportionService.getProportionNext(lotterybIssue.getLotterybProportionId());
        //查询本次购买的赔偿金额统计
        List<LotterybStatistics> list = lotterybStatisticsService.findLotterybStatistics(lotterybInfoId,lotterybIssue.getId());
        //查询购买本期的总金额
        BigDecimal buyAllTotal =  lotterybBuyServiceIml.findBuyStatistics(lotterybIssue.getId());
        LotterybStatistics lotterybStatistics = null;
        if(buyAllTotal.compareTo(BigDecimal.ZERO) == 1){
            BigDecimal proportion = BigDecimal.ZERO;
            BigDecimal differenceValue  = BigDecimal.ZERO;
            for(LotterybStatistics statistics : list){
                /*BigDecimal z = statistics.getAmount().divide(buyAllTotal,4,BigDecimal.ROUND_DOWN);//中奖赔付比例
                BigDecimal p = new BigDecimal(lotterybProportion.getProportion()).divide(new BigDecimal(100));
                if(z.compareTo(p) == -1){
                    differenceValue = p.multiply(z);
                }else {
                    differenceValue = z.multiply(p);
                }
                if(proportion.compareTo(BigDecimal.ZERO) != 0 && differenceValue.compareTo(proportion) ==  -1){
                    proportion = differenceValue;
                    lotterybStatistics = statistics;
                }*/
                if(statistics.getAmount().compareTo(BigDecimal.ZERO) == 1){
                    lotterybStatistics = statistics;
                }

            }
        }
        if(lotterybStatistics == null){
            Random random = new Random();
            lotterybStatistics = list.get(random.nextInt(list.size()));
        }
        System.out.println("amount:"+lotterybStatistics.getAmount());
        lotterybIssue.setLuckTotal(lotterybStatistics.getAmount());
        lotterybIssue.setLotterybInfoId(lotterybInfoId);
        //计算开奖号
        lotterybIssue.setLuckNum(lotterybStatistics.getLotterybNumber());
        lotterybIssue.setLotterybProportionId(lotterybProportion.getId());
        lotterybIssue.setLotterybBuyIds(lotterybStatistics.getLotterybBuyIds());
        //保存中奖信息
        lotterybIssueServiceIml.updateById(lotterybIssue);
    }
}
