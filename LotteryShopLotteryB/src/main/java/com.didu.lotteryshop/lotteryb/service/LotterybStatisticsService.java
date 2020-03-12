package com.didu.lotteryshop.lotteryb.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.common.utils.CodeUtil;
import com.didu.lotteryshop.lotteryb.entity.LotterybConfig;
import com.didu.lotteryshop.lotteryb.entity.LotterybStatistics;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybConfigServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybStatisticsServiceImpl;
import com.didu.lotteryshop.lotteryb.utils.CombinationUtil;
import com.didu.lotteryshop.lotteryb.utils.NumberUtil;
import jnr.ffi.annotations.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class LotterybStatisticsService extends LotteryBBaseService {


    @Autowired
    private LotterybStatisticsServiceImpl lotterybStatisticsService;
    @Autowired
    private LotterybConfigServiceImpl lotterybConfigService;

    /**
     * 每次购买成功时统计购买金额
     * @param lotterybBuyId 购买的ID
     * @param lotterybConfigIds 玩法配置类型(有多种)
     * @param lotterybInfoId 玩法ID
     * @param lotterybIssueId 期数
     * @param type 法类型
     * @param amount 购买金额
     * @return
     */
    public boolean lotteryStatistice(Integer lotterybBuyId,String lotterybConfigIds, Integer lotterybInfoId, Integer lotterybIssueId, String type, BigDecimal amount){
        boolean b = false;
        if("1".equals(type)){
            //和值 lotterybConfigIds 只可以选择一种配置
            type_1(lotterybBuyId,lotterybConfigIds,lotterybInfoId,lotterybIssueId,amount,1);
        }
        if("2".equals(type)){
            //三同号通选
            type_1(lotterybBuyId,lotterybConfigIds,lotterybInfoId,lotterybIssueId,amount,2);
        }
        if("3".equals(type)){
            //三号单选
            type_1(lotterybBuyId,lotterybConfigIds,lotterybInfoId,lotterybIssueId,amount,2);
        }
        if("4".equals(type)){
            //三不同号
            String lotterybConfigIdGroup = "";
            String []lotterybConfigIdArray = lotterybConfigIds.split(",");
            List<String> list = CombinationUtil.combinationSelect(lotterybConfigIdArray,3);
            for (String s : list){
                if("".equals(lotterybConfigIdGroup)){
                    lotterybConfigIdGroup = s;
                }else {
                    lotterybConfigIdGroup += "|"+s;
                }
            }
            type_1(lotterybBuyId,lotterybConfigIdGroup,lotterybInfoId,lotterybIssueId,amount,3);
        }
        if("5".equals(type)){
            //三连号通选
            type_1(lotterybBuyId,lotterybConfigIds,lotterybInfoId,lotterybIssueId,amount,1);
        }
        if("6".equals(type)){
            //二同号通选
            type_1(lotterybBuyId,lotterybConfigIds,lotterybInfoId,lotterybIssueId,amount,2);
        }
        if("7".equals(type)){
            //二同号单选
            Wrapper<LotterybStatistics> wrapper = new EntityWrapper<>();
            wrapper.and("id in(?) and length(value) > 1",lotterybConfigIds);
            List<LotterybStatistics> lotterybStatisticsList_1 = lotterybStatisticsService.selectList(wrapper);
            wrapper = new EntityWrapper<>();
            wrapper.and("id in(?) and length(value) < 2",lotterybConfigIds);
            List<LotterybStatistics> lotterybStatisticsList_2 = lotterybStatisticsService.selectList(wrapper);
            String lotterybConfigIdGroup = "";
            for(LotterybStatistics l_1 : lotterybStatisticsList_1){
                for (LotterybStatistics l_2 : lotterybStatisticsList_2) {
                    if("".equals(lotterybConfigIdGroup)){
                        lotterybConfigIdGroup = l_1.getId()+"|"+l_2.getId();
                    }else {
                        lotterybConfigIdGroup = ","+l_1.getId()+"|"+l_2.getId();
                    }
                }
            }
            type_1(lotterybBuyId,lotterybConfigIdGroup,lotterybInfoId,lotterybIssueId,amount,3);
        }
        if("8".equals(type)){
            //二不同号
            String lotterybConfigIdGroup = "";
            String []lotterybConfigIdArray = lotterybConfigIds.split(",");
            List<String> list = CombinationUtil.combinationSelect(lotterybConfigIdArray,2);
            for (String s : list){
                if("".equals(lotterybConfigIdGroup)){
                    lotterybConfigIdGroup = s;
                }else {
                    lotterybConfigIdGroup += "|"+s;
                }
            }
            type_1(lotterybBuyId,lotterybConfigIdGroup,lotterybInfoId,lotterybIssueId,amount,3);
        }
        return b;
    }

    /**
     * 更新玩法配置类型  = ‘1’ 的统计数据
     * @param lotterybConfigIds
     * @param total
     * @return
     */
    private boolean type_1(Integer lotterybBuyId,String lotterybConfigIds,Integer lotterybInfoId,Integer lotterybIssueId,BigDecimal total,Integer type){
        boolean b = false;
        List<LotterybStatistics> list = findLotterybConfigIds(lotterybConfigIds,lotterybInfoId,lotterybIssueId,type);
        //只有和值的赔付比例不一样 其他类型都一样 故取一地个Id查询需要赔付的比例
        List<LotterybConfig> configs = lotterybConfigService.findList(lotterybConfigIds);
        for (LotterybStatistics lotterybStatistics : list) {
            lotterybStatistics.setAmount(lotterybStatistics.getAmount().add((total.multiply(configs.get(0).getLines()))));
            lotterybStatistics.setLotterybBuyIds(lotterybStatistics.getLotterybBuyIds() == null ? lotterybBuyId + "" :lotterybStatistics.getLotterybBuyIds() + "," + lotterybBuyId);
        }
        b = lotterybStatisticsService.updateBatchById(list);
        return b;
    }

    /**
     * 根据玩法配置Id查询需要统计的那些数据
     * @param lotterybConfigIds
     * @param type == 1 条件 ‘and’ type == 2 条件 ‘or’
     * @return
     */
    private List<LotterybStatistics> findLotterybConfigIds(String lotterybConfigIds,Integer lotterybInfoId,Integer lotterybIssueId,Integer type){
        String []lotterybConfigId = lotterybConfigIds.split(",");
        String sql = "";
        Wrapper<LotterybStatistics> wrapper = new EntityWrapper<>();

        for(int i = 0; i < lotterybConfigId.length ; i++){
                if(type == 1){
                    if("".equals(sql)){
                        sql = " find_in_set('"+lotterybConfigId[i]+"',lotteryb_config_ids) ";
                    }else {
                        sql += " and find_in_set('"+lotterybConfigId[i]+"',lotteryb_config_ids) ";
                    }
                    wrapper.where(sql,lotterybConfigId);
                }else if(type == 2){
                    if("".equals(sql)){
                        sql = " find_in_set('"+lotterybConfigId[i]+"',lotteryb_config_ids) ";
                    }else {
                        sql += " or find_in_set('"+lotterybConfigId[i]+"',lotteryb_config_ids) ";
                    }
                    wrapper.where(sql,lotterybConfigId);
                }else if(type == 3){
                    String []lotterybConfigGroupArray = lotterybConfigIds.split("|");
                    for (String s : lotterybConfigGroupArray) {
                        String [] lotterybConfigIdArray = s.split(",");
                        if(!"".equals(sql)){
                            sql += " or (";
                        }
                        for(String l : lotterybConfigIdArray){
                            if("".equals(sql)){
                                sql = " find_in_set('"+l+"',lotteryb_config_ids) ";
                            }else {
                                sql += " and find_in_set('"+l+"',lotteryb_config_ids) ";
                            }
                        }
                        sql +=  sql+" )";
                    }
                    wrapper.where(sql);
                }
            }
        wrapper.and().eq("lotteryb_issue_id",lotterybIssueId);
        wrapper.and().eq("lotteryb_info_id",lotterybInfoId);
        return lotterybStatisticsService.selectList(wrapper);
    }

    /**
     * 生成这种玩法的所有组合数据 并存储到数据库
     *
     */
    public boolean createAllCombinationData(Integer lotterybIssueId,Integer lotterybInfoId){
        boolean b = false;
        List<String> list = NumberUtil.createNumberCombination();
        List<LotterybStatistics> lotterybStatisticsList = new ArrayList<>();
        for (String s : list) {
            String [] p = s.split("#");
            LotterybStatistics lotterybStatistics = new LotterybStatistics();
            lotterybStatistics.setId(CodeUtil.getUuid());
            lotterybStatistics.setLotterybIssueId(lotterybIssueId);
            lotterybStatistics.setLotterybInfoId(lotterybInfoId);
            lotterybStatistics.setAmount(BigDecimal.ZERO);
            lotterybStatistics.setLotterybNumber(p[0]);
            lotterybStatistics.setLotterybConfigIds(p[1]);
            lotterybStatistics.setCreateDate(new Date());
            lotterybStatisticsList.add(lotterybStatistics);
        }
        b = lotterybStatisticsService.insertBatch(lotterybStatisticsList);
        return b;
    }

    /**
     * 根据玩法和周期获取本次购买
     * @param lotterybInfoId
     * @param lotterybIssueId
     * @return
     */
    public List<LotterybStatistics> findLotterybStatistics(Integer lotterybInfoId,Integer lotterybIssueId){
        Wrapper<LotterybStatistics> wrapper = new EntityWrapper<>();
        wrapper.and().eq("lotteryb_info_id",lotterybInfoId);
        wrapper.eq("lotteryb_issue_id",lotterybIssueId);
        return lotterybStatisticsService.selectList(wrapper);
    }
}
