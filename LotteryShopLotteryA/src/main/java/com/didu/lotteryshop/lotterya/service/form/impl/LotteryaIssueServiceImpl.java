package com.didu.lotteryshop.lotterya.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.lotterya.entity.LotteryaIssue;
import com.didu.lotteryshop.lotterya.mapper.LotteryaIssueMapper;
import com.didu.lotteryshop.lotterya.service.form.ILotteryaIssueService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * A彩票期数表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-11-07
 */
@Service
public class LotteryaIssueServiceImpl extends ServiceImpl<LotteryaIssueMapper, LotteryaIssue> implements ILotteryaIssueService {

    /**
     * 查询本期基本信息
     * @return
     */
    public LotteryaIssue findCurrentPeriodLotteryaIssue(){
        Wrapper<LotteryaIssue> wrapper = new EntityWrapper<>();
        wrapper.orderBy("issueNum",false);
        wrapper.last("limit 1");
        return super.selectOne(wrapper);
    }

    /**
     * 查询上期基本信息
     * @return
     */
    public LotteryaIssue findUpLotteryaIssue(){
        Wrapper<LotteryaIssue> wrapper = new EntityWrapper<>();
        wrapper.eq("issueNum",this.findCurrentPeriodLotteryaIssue().getIssueNum()-1);
        wrapper.last("limit 1");
        return super.selectOne(wrapper);
    }

}
