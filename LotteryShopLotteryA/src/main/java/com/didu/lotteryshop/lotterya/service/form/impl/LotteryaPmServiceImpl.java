package com.didu.lotteryshop.lotterya.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.SysConfig;
import com.didu.lotteryshop.common.service.form.impl.EsDlbaccountsServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.SysConfigServiceImpl;
import com.didu.lotteryshop.lotterya.entity.LotteryaInfo;
import com.didu.lotteryshop.lotterya.entity.LotteryaIssue;
import com.didu.lotteryshop.lotterya.entity.LotteryaPm;
import com.didu.lotteryshop.lotterya.mapper.LotteryaPmMapper;
import com.didu.lotteryshop.lotterya.service.LotteryABaseService;
import com.didu.lotteryshop.lotterya.service.Web3jService;
import com.didu.lotteryshop.lotterya.service.form.ILotteryaPmService;
import com.github.abel533.sql.SqlMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * A彩票提成 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-03
 */
@Service
public class LotteryaPmServiceImpl extends ServiceImpl<LotteryaPmMapper, LotteryaPm> implements ILotteryaPmService {
    @Autowired
    private LotteryaIssueServiceImpl lotteryaIssueService;
    @Autowired
    private LotteryaInfoServiceImpl lotteryaInfoService;
    @Autowired
    private LotteryaBuyServiceImpl lotteryaBuyService;
    @Autowired
    private EsDlbaccountsServiceImpl esDlbaccountsService;
    @Autowired
    private LotteryABaseService lotteryABaseService;
    @Autowired
    private SysConfigServiceImpl sysConfigService;
    @Autowired
    private Web3jService web3jService;
    /** 未转账 */
    public static final String TRANSFER_STATUS_UNTREATED = "-1";
    /** 等待确认 */
    public static final String TRANSFER_STATUS_WAIT = "0";
    /** 已经确认 */
    public static final String TRANSFER_STATUS_SUCCESS = "1";
    /** 失败 */
    public static final String TRANSFER_STATUS_FAIL = "2";

    /**
     * 待领取
     */
    public static final  Integer STATUS_TO_RECEIVE = 0;
    /**
     * 已领取
     */
    public static final  Integer STATUS_ALREADY_RECEIVE = 1;
    /**
     * 过期
     */
    public static final  Integer STATUS_PAST_DUE = 2;


    /**
     * 新增中奖提成
     * @param memberId
     * @param lotteryAIssueId
     * @param total
     * @return
     */
    public LotteryaPm addDraw(String memberId, Integer lotteryAIssueId, BigDecimal total){
        return this.add(memberId,lotteryAIssueId,total,2);
    }

    /**
     * 新增购买提成
     * @param memberId
     * @param lotteryAIssueId
     * @param total
     * @return
     */
    public LotteryaPm addBuy(String memberId, Integer lotteryAIssueId, BigDecimal total){
        return this.add(memberId,lotteryAIssueId,total,1);
    }

    /**
     * 新增提成
     * @param memberId
     * @param lotteryAIssueId
     * @param total
     * @param type
     * @return
     */
	public LotteryaPm add(String memberId, Integer lotteryAIssueId, BigDecimal total, Integer type){
        LotteryaPm lotteryaPm = new LotteryaPm();
        lotteryaPm.setMemberId(memberId);
        lotteryaPm.setLotteryaIssueId(lotteryAIssueId);
        lotteryaPm.setTotal(total);
        lotteryaPm.setStatus("0");
        lotteryaPm.setStatusTime(new Date());
        lotteryaPm.setCreateTime(new Date());
        lotteryaPm.setType(type);
        boolean bool = super.insert(lotteryaPm);
        return bool ? lotteryaPm : null;
    }

    /**
     * 查询中奖提成数据
     * @param memberId
     * @param lotteryAIssueId
     * @return
     */
    public LotteryaPm findDraw(String memberId, Integer lotteryAIssueId){
	    return find(memberId,lotteryAIssueId,2);
    }

    /**
     * 查询购买提成数据
     * @param memberId
     * @param lotteryAIssueId
     * @return
     */
    public LotteryaPm findBuy(String memberId, Integer lotteryAIssueId){
	    return this.find(memberId,lotteryAIssueId,1);
    }

    /**
     * 查询提成数据
     * @param memberId
     * @param lotteryAIssueId
     * @param type
     * @return
     */
    public LotteryaPm find(String memberId, Integer lotteryAIssueId,Integer type){
        Wrapper<LotteryaPm> wrapper = new EntityWrapper<>();
        wrapper.eq("member_id",memberId)
        .and().eq("lotterya_issue_id",lotteryAIssueId)
        .and().eq("type",type);
        return super.selectOne(wrapper);
    }

    /**
     * 购买或新增中奖提成数据
     * @param memberId
     * @param lotteryAIssueId
     * @param total
     * @return
     */
    public LotteryaPm updateOrInsertTotalDraw(String memberId, Integer lotteryAIssueId,BigDecimal total){
        return this.updateOrInsertTotal(memberId,lotteryAIssueId,total,2);
    }

    /**
     * 修改或新增购买提成数据
     * @param memberId
     * @param lotteryAIssueId
     * @param total
     * @return
     */
    public LotteryaPm updateOrInsertTotalBuy(String memberId, Integer lotteryAIssueId,BigDecimal total){
        return this.updateOrInsertTotal(memberId,lotteryAIssueId,total,1);
    }

    /**
     * 修改或新增提成数据
     * @param memberId
     * @param lotteryAIssueId
     * @param total
     * @param type
     * @return
     */
    public LotteryaPm updateOrInsertTotal(String memberId, Integer lotteryAIssueId,BigDecimal total,Integer type){
        LotteryaPm lotteryaPm = this.find(memberId,lotteryAIssueId,type);
        if(lotteryaPm != null && lotteryaPm.getId() != null){
            lotteryaPm.setTotal(lotteryaPm.getTotal().add(total));
            lotteryaPm = super.updateAllColumnById(lotteryaPm) ? lotteryaPm : null;
        }else{
            lotteryaPm = this.add(memberId,lotteryAIssueId,total,type);
        }
        return lotteryaPm;
    }

    /**
     * 查询所有待领取的提成
     * @return
     */
    public List<LotteryaPm>  findToReceive(){
        Wrapper<LotteryaPm> wrapper = new EntityWrapper<>();
        wrapper.eq("status",0);
        return super.selectList(wrapper);
    }

    /**
     * 领取提成
     * @return
     */
    public boolean updateStatus(){
        boolean bool = false;
        List<LotteryaPm> lotteryaPmList = this.findToReceive();
        //本期
        LotteryaIssue nowLotteryaIssue = lotteryaIssueService.findCurrentPeriodLotteryaIssue();
        //上期
        //LotteryaIssue upLotteryaIssue = lotteryaIssueService.findUpLotteryaIssue();
        LotteryaInfo lotteryaInfo =  lotteryaInfoService.findLotteryaInfo();
        if(lotteryaPmList != null && lotteryaPmList.size() > 0){
            for(LotteryaPm lapm : lotteryaPmList){
                //提成当期
                LotteryaIssue lotteryaIssue = lotteryaIssueService.selectById(lapm.getLotteryaIssueId());
                if(nowLotteryaIssue.getIssueNum() - lotteryaIssue.getIssueNum() <= lotteryaInfo.getPmVnum()){
                    //有效提成数据
                  int cnt =  lotteryaBuyService.getBuyCount(lapm.getMemberId(),nowLotteryaIssue.getId());
                  if((cnt >= 1 && lapm.getLotteryaIssueId() == nowLotteryaIssue.getId()) || (cnt >= lotteryaInfo.getPmRnum())){

                      //领取提成
                      lapm.setStatus("1");
                      lapm.setStatusTime(new Date());
                      //增加转账状态
                      SysConfig sysConfig = sysConfigService.getSysConfig();
                      lapm.setTransferStatus("-1");
                      lapm.setTotalEther(lapm.getTotal().divide(sysConfig.getLsbToEth(),25,BigDecimal.ROUND_DOWN));
                      bool = super.updateById(lapm);
                      if(bool){
                          //增加待领币
                          String dicType = lapm.getType() == 1 ? EsDlbaccountsServiceImpl.DIC_TYPE_BUYLOTTERYA_PM : EsDlbaccountsServiceImpl.DIC_TYPE_BUYLOTTERYA_PM;
                          bool = esDlbaccountsService.addInSuccess(lapm.getMemberId(),dicType,lapm.getTotal(),lapm.getId().toString());
                      }
                      if(!bool) return bool;
                  }
                }else{
                    //作废数据
                    lapm.setStatus("2");
                    lapm.setStatusTime(new Date());
                    bool = super.updateById(lapm);
                    if(!bool) return bool;
                }
            }
            bool = true;
        }else{
            bool = true;
        }
        return bool;
    }

    /**
     * A彩票提成转账到平台币钱包
     * @return
     */
    public boolean LotteryAPmTransfer(){
        boolean bool = false;
        SqlMapper sqlMapper = lotteryABaseService.getSqlMapper();
        String sumSql = " select sum(lap_.total_ether) as allTotalEther from lotterya_pm as lap_ ";
        String conditionSql = " where lap_.status = '1' and lap_.transfer_status = '"+TRANSFER_STATUS_UNTREATED+"'";
        Map<String,Object> sMap = sqlMapper.selectOne(sumSql+conditionSql);
        if(sMap != null && !sMap.isEmpty() &&  sMap.get("allTotalEther") != null){
              BigDecimal allTotalEther =   new BigDecimal(sMap.get("allTotalEther").toString());
              if(allTotalEther.compareTo(BigDecimal.ZERO) > 0){
                  SysConfig sysConfig = sysConfigService.getSysConfig();
                  //TODO 如果A彩票管理员账号ETH不够发放中奖提成 暂时不发放 需要人工填账
                  if(web3jService.getManagerBalanceByEther().compareTo(allTotalEther) >= 0){
                      Map<String,Object> rMap =  web3jService.managerSendToETH(sysConfig.getLsbAddress(),allTotalEther);
                      if(rMap != null && !rMap.isEmpty()){
                          String hashvalue = rMap.get(Web3jService.TRANSACTION_HASHVALUE).toString();
                          String status = rMap.get(Web3jService.TRANSACTION_STATUS).toString();
                          BigDecimal gasUsed = new BigDecimal(rMap.get(Web3jService.TRANSACTION_GASUSED).toString());
                          String transferStatus = "";
                          if(status.equals("0")){
                              transferStatus = "0";
                          }else if(status.equals("1")){
                              transferStatus = "1";
                          }else if(status.equals("2")){
                              transferStatus = "2";
                          }
                          String updateSql = "update lotterya_pm as lap_ set lap_.transfer_status='"+transferStatus+"'"+
                                  ",lap_.transfer_hash_value='"+hashvalue+"'"+
                                  ",lap_.transfer_status_time=now()"+
                                  ",lap_.transfer_gasfee="+gasUsed;
                          bool = sqlMapper.update(updateSql+conditionSql) > 0 ? true : false;
                      }
                  }
              }else{
                  bool = true;
              }
        }else{
            bool = true;
        }
        return bool;
    }

   /**
    * 修改A彩票提成表，转账状态
     * @param transferStatus
     * @param transferHashValue
     * @param transferGasfee
     * @return
     */
    public boolean updateLotteryAPmTransferStatus(String transferStatus,String transferHashValue,BigDecimal transferGasfee){
        boolean bool = false;
        if(StringUtils.isBlank(transferHashValue)|| StringUtils.isBlank(transferStatus)) return bool;
        Wrapper<LotteryaPm> wrapper = new EntityWrapper<>();
        wrapper.eq("transfer_hash_value",transferHashValue);
        LotteryaPm lotteryaPm = new LotteryaPm();
        lotteryaPm.setTransferHashValue(transferHashValue);
        lotteryaPm.setTransferStatus(transferStatus);
        lotteryaPm.setTransferStatusTime(new Date());
        lotteryaPm.setTransferGasfee(transferGasfee);
        bool = super.update(lotteryaPm,wrapper);
        return bool;
    }

    /**
     * 查询A彩票提成，转账等待确认的数据
     * @return
     */
    public List<Map<String,Object>> findTransferStatusWait(){
        List<Map<String,Object>> mapList = new ArrayList<>();
        String sql = "select " +
                        " lap_.transfer_hash_value as transferHashValue" +
                    " from lotterya_pm as lap_" +
                    " where lap_.status = '1' and lap_.transfer_status = '"+TRANSFER_STATUS_WAIT+"' and lap_.transfer_hash_value is not null and lap_.transfer_hash_value<>'' " +
                    " group by transfer_hash_value";
        SqlMapper sqlMapper = lotteryABaseService.getSqlMapper();
        mapList = sqlMapper.selectList(sql);
        return mapList;
    }
}
