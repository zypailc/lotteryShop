package com.didu.lotteryshop.lotteryb.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.service.form.impl.EsLsbaccountsServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.MemberServiceImpl;
import com.didu.lotteryshop.lotteryb.entity.*;
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

    /**
     * 已结算
     */
    public static final Integer STATUS_CLOSE_CONTRACT = 0;

    /**
     * 未结算
     */
    public static final  Integer STATUS_OPEN_CONTRACT = 1;

    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private EsGdlsbconfigServiceImpl gdlsbconfigService;
    @Autowired
    private LotterybBuyService lotterybBuyService;
    @Autowired
    private EsGdlsbconfigAcquireServiceImpl gdlsbconfigAcquireService;
    @Autowired
    private LotterybDiServiceImpl lotterybDiService;
    @Autowired
    private EsLsbaccountsServiceImpl lsbaccountsService;

    /**
     * 推广分成奖励
     * @param lotterybInfoId
     * @return
     */
    public boolean generalizeDividedInto(Integer lotterybInfoId,LotterybIssue lotterybIssue){
        boolean bool = false;
        EsGdlsbconfig esGdethconfig = gdlsbconfigService.findEsGdethconfig(lotterybInfoId);
        //2020-04-02 lyl 增加0为关闭推广分成奖励
        if(esGdethconfig != null && esGdethconfig.getDiRatio() != null && esGdethconfig.getDiRatio().compareTo(BigDecimal.ZERO) == 0){
            return true;
        }
        List<Member> memberList = memberService.findGeneralizeMembers();
        if(memberList != null && memberList.size() > 0){
            Integer cLevel = esGdethconfig.getcLevel();
            cLevel = cLevel == -1 ? null : cLevel;
            BigDecimal diTotalCuntAll = BigDecimal.ZERO;
            for(Member m : memberList){
                //本期下级消费情况
                Map<String,Object> lbMap = lotterybBuyService.calculateLowerLevelBuyTotal(lotterybIssue.getId(),m.getId());
                if(lbMap != null && !lbMap.isEmpty()){
                    //购买总金额
                    BigDecimal total = new BigDecimal(lbMap.get("total") == null ? "0" : lbMap.get("total").toString());
                    //中奖总金额
                    BigDecimal luckTotal = new BigDecimal(lbMap.get("luckTotal") == null ? "0" : lbMap.get("luckTotal").toString());
                    LotterybDi lotterybDi = new LotterybDi();
                    lotterybDi.setMemberId(m.getId());
                    lotterybDi.setLotterybIssueId(lotterybIssue.getId());
                    lotterybDi.setBuyTotal(total);
                    //推广分成 购买总金额*25%
                    BigDecimal diTotalAll = total.divide(new BigDecimal("100"))
                            .multiply(esGdethconfig.getDiRatio())
                            .setScale(4,BigDecimal.ROUND_DOWN);
                    lotterybDi.setDiRatio(esGdethconfig.getDiRatio());
                    lotterybDi.setDiTotal(diTotalAll);
                    lotterybDi.setCreateTime(new Date());
                    lotterybDi.setStatus(1);
                    lotterybDi.setStatusTime(new Date());
                    bool =  super.insert(lotterybDi);
                }
            }
        }else{
            bool = true;
        }
        return bool;
    }

    /**
     * 结算之前所有未结算的数推广分成
     * @return
     */
    public void dividedSettleAccounts(){
        //查询下级活跃用户
        //memberService.findLsbActiveMembers();
        EsGdlsbconfig esGdlsbconfig = gdlsbconfigService.findEsGdethconfig(1);
        Integer cLevel = esGdlsbconfig.getcLevel();
        cLevel = cLevel == -1 ? null : cLevel;
        List<Member> memberList = memberService.findGeneralizeMembers();
        if(memberList != null && memberList.size() > 0) {
            for (Member m : memberList) {
                //查询下级活跃用户
                Integer activeMembers = memberService.findLsbActiveMembers(m.getId(),esGdlsbconfig.getConsumeTotal(),cLevel,1);
                //查询推广商分成的未结算的数据
                List<LotterybDi> list = findLotterybDi(m.getId(),LotterybDiServiceImpl.STATUS_OPEN_CONTRACT);
                //查询分成比例
                EsGdlsbconfigAcquire gdlsbconfigAcquire = gdlsbconfigAcquireService.findEsGdlsbconfigAcquireByActiveMembers(activeMembers,esGdlsbconfig.getId());
                BigDecimal allTotal = BigDecimal.ZERO;
                Date date = new Date();
                for (LotterybDi ld : list) {
                    allTotal = allTotal.add(ld.getDiTotal());
                    ld.setStatus(LotterybDiServiceImpl.STATUS_CLOSE_CONTRACT);
                    ld.setStatusTime(date);
                }
                allTotal = allTotal.multiply(gdlsbconfigAcquire.getRatio().divide(new BigDecimal(100)));
                //结算平台币到推广用户账号
                boolean b = lsbaccountsService.addInSuccess(m.getId(),EsLsbaccountsServiceImpl.DIC_TYPE_GSA,allTotal,"-1");
                //修改结算状态和结算时间
                super.updateBatchById(list);
            }
        }
    }

    /**
     * 查询推广商利益分成
     * @param memberId 用户ID
     * @param status 状态 0 已结算 1 未结算
     * @return
     */
    private List<LotterybDi> findLotterybDi(String memberId,Integer status){
        Wrapper<LotterybDi> wrapper = new EntityWrapper();
        wrapper.and().eq("member_id",memberId);
        wrapper.eq("status",status);
        return super.selectList(wrapper);
    }



}
