package com.didu.lotteryshop.lotterya.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.EsGdethconfig;
import com.didu.lotteryshop.common.entity.EsGdethconfigAcquire;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.entity.SysConfig;
import com.didu.lotteryshop.common.service.form.impl.*;
import com.didu.lotteryshop.lotterya.entity.LotteryaDi;
import com.didu.lotteryshop.lotterya.entity.LotteryaInfo;
import com.didu.lotteryshop.lotterya.mapper.LotteryaDiMapper;
import com.didu.lotteryshop.lotterya.service.LotteryABaseService;
import com.didu.lotteryshop.lotterya.service.Web3jService;
import com.didu.lotteryshop.lotterya.service.form.ILotteryaDiService;
import com.github.abel533.sql.SqlMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
    @Autowired
    private Web3jService web3jService;
    @Autowired
    private SysConfigServiceImpl sysConfigService;
    @Autowired
    private LotteryABaseService lotteryABaseService;
    @Autowired
    private EsGdethconfigAcquireServiceImpl esGdethconfigAcquireService;
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
     * @param transferHashValue
     * @param transferGasfee
     * @return
     */
    public boolean updateLotteryADiTransferStatus(Integer id, String transferStatus,String transferHashValue,BigDecimal transferGasfee){
        boolean bool = false;
        if(id == null || StringUtils.isBlank(transferStatus)) return bool;
        LotteryaDi lotteryaDi = super.selectById(id);
        if(lotteryaDi != null){
            lotteryaDi.setTransferHashValue(transferHashValue);
            lotteryaDi.setTransferStatus(transferStatus);
            lotteryaDi.setTransferStatusTime(new Date());
            lotteryaDi.setTransferGasfee(transferGasfee);
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
     * 根据A彩票期数ID修改A彩票推广账户分成表，转账状态
     * @param lotteryaIssueId
     * @param transferStatus
     * @param transferHashValue
     * @param transferGasfee
     * @return
     */
    public boolean updateLotteryADiTransferStatusByLotteryaIssueId(Integer lotteryaIssueId, String transferStatus,String transferHashValue,BigDecimal transferGasfee){
        boolean bool = false;
        Wrapper<LotteryaDi> wrapper = new EntityWrapper<>();
        wrapper.eq("lotterya_issue_id",lotteryaIssueId);
        List<LotteryaDi> lotteryaDiList = super.selectList(wrapper);
        if(lotteryaDiList != null && lotteryaDiList.size() > 0){
            for(LotteryaDi lad : lotteryaDiList){
               bool =  this.updateLotteryADiTransferStatus(lad.getId(),transferStatus,transferHashValue,transferGasfee);
               if(!bool) return bool;
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
            BigDecimal diTotalCuntAll = BigDecimal.ZERO;
            for(Member m : memberList){
                //本期下级消费情况
                Map<String,Object> lbMap = lotteryaBuyService.calculateLowerLevelBuyTotal(lotteryaIssueId,m.getId());
                if(lbMap != null && !lbMap.isEmpty()){
                    //下级活跃人数
                    int activeMembers = memberService.findActiveMembers(m.getId(),esGdethconfig.getConsumeTotal(),cLevel,esGdethconfig.getCycleDay());
                    //购买总金额
                    BigDecimal total = new BigDecimal(lbMap.get("total") == null ? "0" : lbMap.get("total").toString());
                    //购买总注数
                    Integer counts = Integer.valueOf(lbMap.get("counts") == null ? "0" : lbMap.get("counts").toString());
                    //中奖总金额
                    BigDecimal luckTotal = new BigDecimal(lbMap.get("luckTotal") == null ? "0" : lbMap.get("luckTotal").toString());
                    //中奖总注数
                    Integer luckCounts = Integer.valueOf(lbMap.get("luckCounts") == null ? "0" : lbMap.get("luckCounts").toString());

                    LotteryaDi lotteryaDi = new LotteryaDi();
                    lotteryaDi.setMemberId(m.getId());
                    lotteryaDi.setLotteryaIssueId(lotteryaIssueId);
                    lotteryaDi.setBuyTotal(total);
                    lotteryaDi.setBuyCnts(counts);
                    lotteryaDi.setLuckTotal(luckTotal);
                    lotteryaDi.setLuckCnts(luckCounts);
                    lotteryaDi.setActiveMcnts(activeMembers);
                    lotteryaDi.setLuckRatio(lotteryaInfo.getDrawPm());
                    lotteryaDi.setOperationRatio(esGdethconfig.getDiRatio());
                    //推广分成 购买总金额*25%
                    BigDecimal diTotalAll = total.divide(new BigDecimal("100"))
                                                .multiply(esGdethconfig.getDiRatio())
                                                .setScale(4,BigDecimal.ROUND_DOWN);
                    //中奖提成
                    BigDecimal luckDiTotal = luckTotal.divide(new BigDecimal("100"))
                                                .multiply(lotteryaInfo.getDrawPm())
                                                .setScale(4,BigDecimal.ROUND_DOWN);
                    BigDecimal diTotal = diTotalAll.subtract(luckDiTotal);
                    lotteryaDi.setOperationTotal(diTotal);
                    //根据活跃人数查询从分成比例
                    EsGdethconfigAcquire esGdethconfigAcquire = esGdethconfigAcquireService.findEsGdethconfigAcquireByActiveMembers(activeMembers);
                    if(esGdethconfigAcquire != null) {
                        diTotal = diTotal.divide(new BigDecimal("100"))
                                .multiply(esGdethconfigAcquire.getRatio())
                                .setScale(4, BigDecimal.ROUND_DOWN);

                        diTotalCuntAll = diTotalCuntAll.add(diTotal);
                        lotteryaDi.setDiRatio(esGdethconfigAcquire.getRatio());
                    }else {
                        lotteryaDi.setDiRatio(BigDecimal.ZERO);
                    }
                    lotteryaDi.setDiTotal(diTotal);
                    lotteryaDi.setLuckDiTotal(luckDiTotal);
                    lotteryaDi.setCreateTime(new Date());
                    lotteryaDi.setTransferHashValue("");
                    lotteryaDi.setTransferStatus("0");
                    lotteryaDi.setTransferStatusTime(new Date());
                    lotteryaDi.setTransferGasfee(BigDecimal.ZERO);
                    bool =  super.insert(lotteryaDi);
                }
            }
            //管理员转账，到推广分成账户。
            if(bool && diTotalCuntAll.compareTo(BigDecimal.ZERO) > 0){
                SysConfig sysConfig = sysConfigService.getSysConfig();
                Map<String,Object> rMap =  web3jService.managerSendToETH(sysConfig.getDiAddress(),diTotalCuntAll);
                if(rMap != null && !rMap.isEmpty()){
                    String hashvalue = rMap.get(Web3jService.TRANSACTION_HASHVALUE).toString();
                    String status = rMap.get(Web3jService.TRANSACTION_STATUS).toString();
                    BigDecimal gasUsed = new BigDecimal(rMap.get(Web3jService.TRANSACTION_GASUSED).toString());
                    String transferStatus = "";
                    if(status.equals("0")){
                        transferStatus = TRANSFER_STATUS_WAIT;
                    }else if(status.equals("1")){
                        transferStatus = TRANSFER_STATUS_SUCCESS;
                    }else if(status.equals("2")){
                        transferStatus = TRANSFER_STATUS_FAIL;
                    }
                    bool = this.updateLotteryADiTransferStatusByLotteryaIssueId(lotteryaIssueId,transferStatus,hashvalue,gasUsed);
                }
            }
        }else{
            bool = true;
        }
        return bool;
    }

    /**
     * 查询推广分成，转账等待确认的数据
     * @return
     */
    public List<Map<String,Object>> findTransferStatusWait(){
        List<Map<String,Object>> mapList = new ArrayList<>();
        SqlMapper sqlMapper = lotteryABaseService.getSqlMapper();
        String sql = "select " +
                        " lad_.lotterya_issue_id as lotteryaIssueId " +
                        " ,lad_.transfer_hash_value  as transferHashValue " +
                    " from lotterya_di as lad_" +
                    " where lad_.transfer_status = '0' and lad_.transfer_hash_value is not null and lad_.transfer_hash_value<>''" +
                    " GROUP BY lad_.lotterya_issue_id,lad_.transfer_hash_value";
        mapList =  sqlMapper.selectList(sql);
        return mapList;
    }
}
