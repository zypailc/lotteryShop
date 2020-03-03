package com.didu.lotteryshop.lotteryb.service.form.impl;

import com.didu.lotteryshop.common.entity.EsGdethconfig;
import com.didu.lotteryshop.common.entity.EsGdethconfigAcquire;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.entity.SysConfig;
import com.didu.lotteryshop.common.service.form.impl.MemberServiceImpl;
import com.didu.lotteryshop.lotteryb.entity.EsGdlsbconfig;
import com.didu.lotteryshop.lotteryb.entity.EsGdlsbconfigAcquire;
import com.didu.lotteryshop.lotteryb.entity.LotterybDi;
import com.didu.lotteryshop.lotteryb.entity.LotterybInfo;
import com.didu.lotteryshop.lotteryb.mapper.LotterybDiMapper;
import com.didu.lotteryshop.lotteryb.service.LotterybBuyService;
import com.didu.lotteryshop.lotteryb.service.form.ILotterybDiService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-03-03
 */
@Service
public class LotterybDiServiceImpl extends ServiceImpl<LotterybDiMapper, LotterybDi> implements ILotterybDiService {


    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private LotterybInfoServiceImpl lotterybInfoService;
    @Autowired
    private EsGdlsbconfigServiceImpl gdlsbconfigService;
    @Autowired
    private LotterybBuyService lotterybBuyService;
    @Autowired
    private EsGdlsbconfigAcquireServiceImpl gdlsbconfigAcquireService;

    /**
     * 推广分成奖励
     * @param lotteryaIssueId
     * @return
     */
    public boolean generalizeDividedInto(Integer lotterybInfoId,Integer lotteryaIssueId){
        boolean bool = false;
        List<Member> memberList = memberService.findGeneralizeMembers();
        if(memberList != null && memberList.size() > 0){
            LotterybInfo lotterybInfo = lotterybInfoService.find(lotterybInfoId);
            EsGdlsbconfig esGdethconfig = gdlsbconfigService.findEsGdethconfig(lotterybInfoId);
            Integer cLevel = esGdethconfig.getcLevel();
            cLevel = cLevel == -1 ? null : cLevel;
            BigDecimal diTotalCuntAll = BigDecimal.ZERO;
            for(Member m : memberList){
                //本期下级消费情况
                Map<String,Object> lbMap = lotterybBuyService.calculateLowerLevelBuyTotal(lotteryaIssueId,m.getId());
                if(lbMap != null && !lbMap.isEmpty()){
                    //下级活跃人数 //TODO 需要重新写
                    //int activeMembers = memberService.findActiveMembers(m.getId(),esGdethconfig.getConsumeTotal(),cLevel,esGdethconfig.getCycleDay());
                    //购买总金额
                    BigDecimal total = new BigDecimal(lbMap.get("total") == null ? "0" : lbMap.get("total").toString());
                    //中奖总金额
                    BigDecimal luckTotal = new BigDecimal(lbMap.get("luckTotal") == null ? "0" : lbMap.get("luckTotal").toString());
                    LotterybDi lotteryaDi = new LotterybDi();
                    lotteryaDi.setMemberId(m.getId());
                    lotteryaDi.setLotterybIssueId(lotteryaIssueId);
                    lotteryaDi.setBuyTotal(total);
                    lotteryaDi.setLuckTotal(luckTotal);
                    //lotteryaDi.setActiveMcnts();活跃用户暂时不计算
                    lotteryaDi.setLuckRatio(lotterybInfo.getWinningPm());
                    lotteryaDi.setOperationRatio(esGdethconfig.getDiRatio());
                    //推广分成 购买总金额*25%
                    BigDecimal diTotalAll = total.divide(new BigDecimal("100"))
                            .multiply(esGdethconfig.getDiRatio())
                            .setScale(4,BigDecimal.ROUND_DOWN);
                    //中奖提成
                    BigDecimal luckDiTotal = luckTotal.divide(new BigDecimal("100"))
                            .multiply(lotterybInfo.getWinningPm())
                            .setScale(4,BigDecimal.ROUND_DOWN);
                    BigDecimal diTotal = diTotalAll.subtract(luckDiTotal);
                    lotteryaDi.setOperationTotal(diTotal);
                    //根据活跃人数查询从分成比例(每天已结算 查询当天的用户活越人数，在结算时在查询)
                    /*EsGdlsbconfigAcquire esGdlsbconfigAcquire = gdlsbconfigAcquireService.findEsGdlsbconfigAcquireByActiveMembers(activeMembers,esGdethconfig.getId());
                    if(esGdlsbconfigAcquire != null) {
                        diTotal = diTotal.divide(new BigDecimal("100"))
                                .multiply(esGdlsbconfigAcquire.getRatio())
                                .setScale(4, BigDecimal.ROUND_DOWN);

                        diTotalCuntAll = diTotalCuntAll.add(diTotal);
                        lotteryaDi.setDiRatio(esGdlsbconfigAcquire.getRatio());
                    }else {*/
                    lotteryaDi.setDiRatio(BigDecimal.ZERO);
                   /* }*/
                    lotteryaDi.setDiTotal(diTotal);
                    lotteryaDi.setLuckDiTotal(luckDiTotal);
                    lotteryaDi.setCreateTime(new Date());
                    bool =  super.insert(lotteryaDi);
                }
            }
        }else{
            bool = true;
        }
        return bool;
    }

}
