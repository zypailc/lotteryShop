package com.didu.lotteryshop.lotteryb.api.v1.service;

import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.lotteryb.entity.LotterybBuy;
import com.didu.lotteryshop.lotteryb.entity.LotterybInfo;
import com.didu.lotteryshop.lotteryb.entity.LotterybIssue;
import com.didu.lotteryshop.lotteryb.service.LotteryBBaseService;
import com.didu.lotteryshop.lotteryb.service.LotterybIssueService;
import com.didu.lotteryshop.lotteryb.service.LotterybStatisticsService;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybBuyServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybInfoServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybIssueServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybStatisticsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LotteryBService extends LotteryBBaseService {

    @Autowired
    private LotterybInfoServiceImpl lotterybInfoService;
    @Autowired
    private LotterybIssueServiceImpl lotterybIssueServiceIml;
    @Autowired
    private LotterybIssueService lotterybIssueService;
    @Autowired
    private LotterybStatisticsService lotterybStatisticsService;
    @Autowired
    private LotterybBuyServiceImpl lotterybBuyServiceIml;

    /**
     * 根据id获取竞猜基本信息
     * @return
     */
    public ResultUtil getLotteryBInfo(Integer lotteryBInfoId){
        Map map = new HashMap();
        LotterybInfo lotterybInfo = lotterybInfoService.selectById(lotteryBInfoId);
        LotterybIssue lotterybIssue = lotterybIssueServiceIml.getLotterybIssue(lotteryBInfoId);
        if(lotterybIssue == null){
            lotterybIssue = lotterybIssueService.createNextLotterybIssue(lotteryBInfoId);
            lotterybStatisticsService.createAllCombinationData(lotterybIssue.getId(),lotteryBInfoId);
        }
        //名称
        map.put("zhTitle",lotterybInfo.getZhTitle());
        map.put("enTitle",lotterybInfo.getEnTitle());
        //服务器时间
        map.put("newDate",new Date());
        //开始时间
        map.put("startDate",lotterybIssue.getStartTime());
        //结束时间
        map.put("endDate",lotterybIssue.getEndTime());
        //期数
        map.put("issueNum",lotterybIssue.getIssueNum());
        //周期间隔时间
        map.put("periodDate",lotterybInfo.getPeriodDate());
        //购买最小金额
        map.put("minMoney",lotterybInfo.getMinMoney());
        //购买最大金额
        map.put("maxMoney",lotterybInfo.getMaxMoney());
        return ResultUtil.successJson(map);
    }

    /**
     * 查询购买数据
     * @param currentPage
     * @param pageSize
     * @param isOneself 是否只查询自己 0：否 ；1：是
     * @param mTransferStatus 状态格式 :1,2
     * @param lotterybBuy
     * @param type
     * @return
     */
    public ResultUtil getLotteryBuy(Integer currentPage, Integer pageSize, Integer isOneself, String mTransferStatus, LotterybBuy lotterybBuy, Integer type) {
        currentPage = currentPage == null ? 1:currentPage;
        pageSize = pageSize == null ? 20:pageSize;
        if(isOneself != null && isOneself == 1){
            if(super.getLoginUser() != null){
                lotterybBuy.setMemberId(super.getLoginUser().getId());
            }else {
                lotterybBuy.setMemberId("-1");
            }
        }else{
            lotterybBuy.setMemberId(null);
        }
        if(type == 2){//查询全部
            return ResultUtil.successJson(lotterybBuyServiceIml.getPageLotteryBuyAll(currentPage,pageSize,mTransferStatus,lotterybBuy));
        }
        return ResultUtil.successJson(lotterybBuyServiceIml.getPageLotteryBuy(currentPage,pageSize,mTransferStatus,lotterybBuy));
    }
}
