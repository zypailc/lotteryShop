package com.didu.lotteryshop.lotteryb.api.v1.service;

import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.lotteryb.entity.LotterybInfo;
import com.didu.lotteryshop.lotteryb.entity.LotterybIssue;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybInfoServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybIssueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LotteryBService {

    @Autowired
    private LotterybInfoServiceImpl lotterybInfoService;
    @Autowired
    private LotterybIssueServiceImpl lotterybIssueService;

    /**
     * 根据id获取竞猜基本信息
     * @return
     */
    public ResultUtil getLotteryBInfo(String lotteryBInfoId){
        Map map = new HashMap();
        LotterybInfo lotterybInfo = lotterybInfoService.selectById(Integer.parseInt(lotteryBInfoId));
        LotterybIssue lotterybIssue = lotterybIssueService.getLotterybIssue(lotteryBInfoId);
        //名称
        map.put("zhTitle",lotterybInfo.getZhTitle());
        map.put("enTitle",lotterybInfo.getEnTitle());
        //服务器时间
        map.put("newDate",new Date());
        //结束时间
        map.put("endDate",lotterybInfo.getPeriodDate());
        //期数
        map.put("issueNum",lotterybIssue.getIssueNum());
        //购买最小金额
        map.put("minMoney",lotterybInfo.getMinMoney());
        //购买最大金额
        map.put("maxMoney",lotterybInfo.getMaxMoney());
        return ResultUtil.successJson(map);
    }

}
