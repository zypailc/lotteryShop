package com.didu.lotteryshop.lotteryb.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.lotteryb.entity.LotterybInfo;
import com.didu.lotteryshop.lotteryb.entity.LotterybIssue;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybBuyServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybInfoServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybIssueServiceImpl;
import jnr.ffi.annotations.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LotterybInfoService extends LotteryBBaseService {

    @Autowired
    private LotterybInfoServiceImpl lotterybInfoService;
    @Autowired
    private LotterybIssueServiceImpl lotterybIssueService;

    /**
     * 判断是否可购买
     * @param lotteryInfoId
     * @return
     */
    public boolean isBuyLotteryB(Integer lotteryInfoId){
        LotterybIssue lotterybIssue = lotterybIssueService.getLotterybIssue(lotteryInfoId);
        if(lotterybIssue != null){
            if(lotterybIssue.getByStatus() == LotterybIssueServiceImpl.BY_STATUS_START){
                return true;
            }
        }
        return false;
    }

    /**
     * 查询竞猜玩法
     * @param lotterybInfoId
     * @return
     */
    public LotterybInfo find(Integer lotterybInfoId){
        return lotterybInfoService.selectById(lotterybInfoId);
    }

}
