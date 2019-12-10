package com.didu.lotteryshop.lotterya.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.didu.lotteryshop.lotterya.entity.LotteryaIssue;
import com.didu.lotteryshop.lotterya.mapper.LotteryaIssueMapper;
import com.didu.lotteryshop.lotterya.service.form.ILotteryaIssueService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        wrapper.eq("issue_num",this.findCurrentPeriodLotteryaIssue().getIssueNum()-1);
        wrapper.last("limit 1");
        return super.selectOne(wrapper);
    }

    /**
     * 分页查询彩票期数表
     * @param currentPage
     * @param pageSize
     * @return
     */
    public Page<LotteryaIssue> findPageLotteryaIssue(int currentPage,int pageSize){
       Wrapper<LotteryaIssue> wrapper = new EntityWrapper<>();
       wrapper.eq("bonus_status","1");
       wrapper.orderBy("issueNum",false);
        Page<LotteryaIssue> pageLI = new Page<>(currentPage,pageSize);
        return super.selectPage(pageLI,wrapper);
    }

}
