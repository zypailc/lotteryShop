package com.didu.lotteryshop.lotterya.service.form.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.EsGdethconfig;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.service.form.impl.EsGdethaccountsServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.EsGdethconfigServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.MemberServiceImpl;
import com.didu.lotteryshop.lotterya.entity.LotteryaDi;
import com.didu.lotteryshop.lotterya.entity.LotteryaInfo;
import com.didu.lotteryshop.lotterya.mapper.LotteryaDiMapper;
import com.didu.lotteryshop.lotterya.service.form.ILotteryaDiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * A彩票推广账户分成表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-17
 */
@Service
public class LotteryaDiServiceImpl extends ServiceImpl<LotteryaDiMapper, LotteryaDi> implements ILotteryaDiService {
    @Autowired
    private EsGdethaccountsServiceImpl esGdethaccountsService;
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private LotteryaBuyServiceImpl lotteryaBuyService;
    @Autowired
    private EsGdethconfigServiceImpl esGdethconfigService;
    @Autowired
    private LotteryaInfoServiceImpl lotteryaInfoService;
    /** 等待确认 */
    public static final String TRANSFER_STATUS_WAIT = "0";
    /** 已经确认 */
    public static final String TRANSFER_STATUS_SUCCESS = "1";
    /** 失败 */
    public static final String TRANSFER_STATUS_FAIL = "2";

    /**
     * 修改A彩票推广账户分成表，转账状态
     * @param id
     * @param transferStatus
     * @return
     */
    public boolean updateLotteryADiTransferStatus(Integer id, String transferStatus){
        boolean bool = false;
        if(id == null || StringUtils.isBlank(transferStatus)) return bool;
        LotteryaDi lotteryaDi = super.selectById(id);
        if(lotteryaDi != null){
            lotteryaDi.setTransferStatus(transferStatus);
            lotteryaDi.setTransferStatusTime(new Date());
            bool = super.updateById(lotteryaDi);
            if(bool){
                if(transferStatus.equals(LotteryaDiServiceImpl.TRANSFER_STATUS_SUCCESS)){
                    bool = esGdethaccountsService.addInSuccess(lotteryaDi.getMemberId(),EsGdethaccountsServiceImpl.DIC_TYPE_GENERALIZELOTTERYA,lotteryaDi.getDiTotal(),lotteryaDi.getId().toString());
                }
            }
        }
        return bool;
    }

    /**
     * 推广分成奖励
     * @param lotteryaIssueId
     * @return
     */
    public boolean generalizeDividedInto(Integer lotteryaIssueId){
        boolean bool = false;
        List<Member> memberList = memberService.findGeneralizeMembers();
        if(memberList != null && memberList.size() > 0){
            LotteryaInfo lotteryaInfo = lotteryaInfoService.findLotteryaInfo();
            EsGdethconfig esGdethconfig = esGdethconfigService.findEsGdethconfig();
            Integer cLevel = esGdethconfig.getcLevel();
            cLevel = cLevel == -1 ? null : cLevel;
            for(Member m : memberList){
                //本期下级消费情况
                Map<String,Object> lbMap = lotteryaBuyService.calculateLowerLevelBuyTotal(lotteryaIssueId,m.getId());
                if(lbMap != null && !lbMap.isEmpty()){
                    //下级活跃人数
                    int activeMembers = memberService.findActiveMembers(m.getId(),esGdethconfig.getConsumeTotal(),cLevel,esGdethconfig.getCycleDay());
                    //购买总金额
                    BigDecimal total = new BigDecimal(lbMap.get("total").toString());
                    //购买总注数
                    Integer counts = Integer.valueOf(lbMap.get("counts").toString());
                    //中奖总金额
                    BigDecimal luckTotal = new BigDecimal(lbMap.get("luckTotal").toString());
                    //中奖总注数
                    Integer luckCounts = Integer.valueOf(lbMap.get("luckCounts").toString());

                    LotteryaDi lotteryaDi = new LotteryaDi();
                    lotteryaDi.setMemberId(m.getId());
                    lotteryaDi.setLotteryaIssueId(lotteryaIssueId);
                    lotteryaDi.setBuyTotal(total);
                    lotteryaDi.setBuyCnts(counts);
                    lotteryaDi.setLuckTotal(luckTotal);
                    lotteryaDi.setLuckCnts(luckCounts);
                    lotteryaDi.setActiveMcnts(activeMembers);
                    lotteryaDi.setLuckRatio(lotteryaInfo.getDrawPm());
                    lotteryaDi.setDiRatio(esGdethconfig.getDiRatio());
                    //推广分成 购买总金额*25%
                    BigDecimal diTotalAll = total.divide(new BigDecimal("100"))
                                                .multiply(esGdethconfig.getDiRatio())
                                                .setScale(4,BigDecimal.ROUND_DOWN);
                    //中奖提成
                    BigDecimal luckDiTotal = diTotalAll.divide(new BigDecimal("100"))
                                                .multiply(lotteryaInfo.getDrawPm())
                                                .setScale(4,BigDecimal.ROUND_DOWN);
                    lotteryaDi.setLuckDiTotal(luckDiTotal);
                    lotteryaDi.setDiTotal(diTotalAll.subtract(luckDiTotal));
                    lotteryaDi.setCreateTime(new Date());
                    bool =  super.insert(lotteryaDi);
                    if(bool){
                        bool = esGdethaccountsService.addInSuccess(lotteryaDi.getMemberId(),EsGdethaccountsServiceImpl.DIC_TYPE_GENERALIZELOTTERYA,lotteryaDi.getDiTotal(),lotteryaDi.getId().toString());
                    }
                }
            }
        }else{
            bool = true;
        }
        return bool;
    }

}
