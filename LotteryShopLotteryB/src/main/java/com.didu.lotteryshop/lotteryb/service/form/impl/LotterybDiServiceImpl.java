package com.didu.lotteryshop.lotteryb.service.form.impl;

import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.service.form.impl.MemberServiceImpl;
import com.didu.lotteryshop.lotteryb.entity.EsGdlsbconfig;
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
 * @since 2020-03-12
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
            EsGdlsbconfig esGdethconfig = gdlsbconfigService.findEsGdethconfig(lotterybInfoId);
            Integer cLevel = esGdethconfig.getcLevel();
            cLevel = cLevel == -1 ? null : cLevel;
            BigDecimal diTotalCuntAll = BigDecimal.ZERO;
            for(Member m : memberList){
                //本期下级消费情况
                Map<String,Object> lbMap = lotterybBuyService.calculateLowerLevelBuyTotal(lotteryaIssueId,m.getId());
                if(lbMap != null && !lbMap.isEmpty()){
                    //购买总金额
                    BigDecimal total = new BigDecimal(lbMap.get("total") == null ? "0" : lbMap.get("total").toString());
                    //中奖总金额
                    BigDecimal luckTotal = new BigDecimal(lbMap.get("luckTotal") == null ? "0" : lbMap.get("luckTotal").toString());
                    LotterybDi lotterybDi = new LotterybDi();
                    lotterybDi.setMemberId(m.getId());
                    lotterybDi.setLotterybIssueId(lotteryaIssueId);
                    lotterybDi.setBuyTotal(total);
                    lotterybDi.setOperationRatio(BigDecimal.ZERO);
                    lotterybDi.setOperationTotal(BigDecimal.ZERO);
                    //推广分成 购买总金额*25%
                    BigDecimal diTotalAll = total.divide(new BigDecimal("100"))
                            .multiply(esGdethconfig.getDiRatio())
                            .setScale(4,BigDecimal.ROUND_DOWN);
                    lotterybDi.setDiRatio(esGdethconfig.getDiRatio());
                    lotterybDi.setDiTotal(diTotalAll);
                    lotterybDi.setCreateTime(new Date());
                    bool =  super.insert(lotterybDi);
                }
            }
        }else{
            bool = true;
        }
        return bool;
    }

}
