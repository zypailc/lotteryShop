package com.didu.lotteryshop.lotteryb.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.common.utils.CodeUtil;
import com.didu.lotteryshop.common.utils.DateUtil;
import com.didu.lotteryshop.lotteryb.entity.LotterybInfo;
import com.didu.lotteryshop.lotteryb.entity.LotterybIssue;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybInfoServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybIssueServiceImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class LotterybIssueService extends LotteryBBaseService{

    @Autowired
    private LotterybIssueServiceImpl lotterybIssueService;
    @Autowired
    private LotterybInfoServiceImpl lotterybInfoService;


    /**
     * 计算；两个期数之间相差多少期
     * @param nowIssueNum
     * @param issueNum
     * @return
     */
    public Integer getIssueNumDifference(Integer lotterybInfoId,String nowIssueNum,String issueNum){
        Integer newIssueDate = lotterybIssueService.parseIssueNum(nowIssueNum,LotterybIssueServiceImpl.TYPE_DATE);
        Integer newIssuenum = lotterybIssueService.parseIssueNum(nowIssueNum,LotterybIssueServiceImpl.TYPE_NUM);
        Integer issueDate = lotterybIssueService.parseIssueNum(issueNum,LotterybIssueServiceImpl.TYPE_DATE);
        Integer issuenum = lotterybIssueService.parseIssueNum(issueNum,LotterybIssueServiceImpl.TYPE_NUM);
        //判断是否是在同一天期数
        if(newIssueDate == issueDate){
            return newIssuenum - issuenum;
        }else {
            //获取期数日期到现日期的日期数组(简单处理，写一个查询，直接查期数之间的)
            String sql = "select count(1) as num  from lotteryb_issue where between '"+issueNum+"' and '"+nowIssueNum+"'";
            Map<String,Object> m = getSqlMapper().selectOne(sql);
            return Integer.parseInt(m.get("num").toString());

        }
    }

    /**
     * 根据Lotteryb玩法Id生成下一期
     * @param lotterybInfoId
     * @return
     */
    public LotterybIssue createNextLotterybIssue(Integer lotterybInfoId){

        //查询是否有本玩法期数
        Wrapper<LotterybIssue> wrapper = new EntityWrapper<>();
        wrapper.and().eq("lotteryb_info_id",lotterybInfoId);
        wrapper.orderBy("issue_num",false);
        wrapper.last("limit 1");
        LotterybIssue lotterybIssue = lotterybIssueService.selectOne(wrapper);
        LotterybInfo lotterybInfo = lotterybInfoService.selectById(lotterybInfoId);
        boolean b = false;
        if(lotterybIssue == null){
            //生成新的数据
            lotterybIssue = new LotterybIssue();
            Date date = new Date();
            lotterybIssue.setStartTime(date);
            lotterybIssue.setEndTime(DateUtil.getDateAddMinute(date,lotterybInfo.getPeriodDate()));
            lotterybIssue.setIssueNum(lotterybIssueService.createIssueNum(lotterybInfoId,null));
            lotterybIssue.setLotterybInfoId(lotterybInfoId);
        }else {
            //生成数据
            lotterybIssue.setId(null);
            Date date = lotterybIssue.getEndTime();
            lotterybIssue.setStartTime(date);
            lotterybIssue.setEndTime(DateUtil.getDateAddMinute(date,lotterybInfo.getPeriodDate()));
            lotterybIssue.setIssueNum(lotterybIssueService.createIssueNum(lotterybInfoId,lotterybIssue.getIssueNum()));
        }
        lotterybIssue.setCreateTime(new Date());
        lotterybIssue.setByStatus(0);
        lotterybIssue.setBsTime(new Date());
        lotterybIssue.setLuckTotal(BigDecimal.ZERO);
        lotterybIssue.setByStatus(0);
        lotterybIssue.setBonusStatus(0);
        lotterybIssue.setBonusStatusTime(new Date());
        lotterybIssue.setBonusGrant(0);
        lotterybIssueService.insert(lotterybIssue);
        return lotterybIssue;
    }

}
