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

@Service
public class LotterybIssueService {

    @Autowired
    private LotterybIssueServiceImpl lotterybIssueService;
    @Autowired
    private LotterybInfoServiceImpl lotterybInfoService;

    /**
     * 根据Lotteryb玩法Id生成下一期
     * @param lotterybInfoId
     * @return
     */
    public LotterybIssue createNextLotterybIssue(String lotterybInfoId){

        //查询是否有本玩法期数
        Wrapper<LotterybIssue> wrapper = new EntityWrapper<>();
        wrapper.and().eq("lotteryb_info_id",lotterybInfoId);
        wrapper.orderBy("issue_num",false);
        wrapper.last("limit 1");
        LotterybIssue lotterybIssue = lotterybIssueService.selectOne(wrapper);
        LotterybInfo lotterybInfo = lotterybInfoService.selectById(Integer.parseInt(lotterybInfoId));
        lotterybIssue.setCreateTime(new Date());
        lotterybIssue.setByStatus(0);
        lotterybIssue.setBsTime(new Date());
        lotterybIssue.setLuckTotal(BigDecimal.ZERO);
        lotterybIssue.setByStatus(0);
        lotterybIssue.setBonusStatusTime(new Date());
        lotterybIssue.setBonusGrant(0);
        boolean b = false;
        if(lotterybIssue == null){
            //生成新的数据
            lotterybIssue = new LotterybIssue();
            Date date = new Date();
            lotterybIssue.setStartTime(date);
            lotterybIssue.setEndTime(DateUtil.getDateAddMinute(date,lotterybInfo.getPeriodDate()));
            lotterybIssue.setIssueNum(lotterybIssueService.createIssueNum(lotterybInfoId,null));
            lotterybIssue.setLotterybInfoId(Integer.parseInt(lotterybInfoId));
        }else {
            //生成数据
            lotterybIssue.setId(null);
            Date date = lotterybIssue.getEndTime();
            lotterybIssue.setStartTime(date);
            lotterybIssue.setEndTime(DateUtil.getDateAddMinute(date,lotterybInfo.getPeriodDate()));
            lotterybIssue.setIssueNum(lotterybIssueService.createIssueNum(lotterybInfoId,lotterybIssue.getIssueNum()));
        }
        lotterybIssueService.insert(lotterybIssue);
        return lotterybIssue;
    }

}
