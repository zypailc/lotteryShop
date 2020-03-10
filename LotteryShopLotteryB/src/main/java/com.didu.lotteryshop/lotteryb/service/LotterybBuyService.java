package com.didu.lotteryshop.lotteryb.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.service.form.impl.EsLsbaccountsServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.EsLsbwalletServiceImpl;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.lotteryb.entity.LotterybBuy;
import com.didu.lotteryshop.lotteryb.entity.LotterybPm;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybBuyServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybInfoServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybPmDetailServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybPmServiceImpl;
import com.github.abel533.sql.SqlMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class LotterybBuyService extends LotteryBBaseService{

    @Autowired
    private EsLsbwalletServiceImpl esLsbwalletService;
    @Autowired
    private EsLsbaccountsServiceImpl lsbaccountsService;
    @Autowired
    private LotterybInfoService lotterybInfoService;
    @Autowired
    private LotterybBuyServiceImpl lotterybBuyService;
    @Autowired
    private LotterybStatisticsService lotterybStatisticsService;
    @Autowired
    private LotterybPmServiceImpl lotterybPmService;
    @Autowired
    private LotterybPmDetailServiceImpl lotterybPmDetailServiceIml;

    /**
     * 购买
     * @param lotterybInfoId 玩法Id
     * @param issueNum 期数
     * @param dataInfo 购买的数据
     * @param total 购买总金额
     * @return
     */
    public ResultUtil lsbBuyLottery(String lotterybInfoId,String issueNum, String dataInfo,BigDecimal total) {
        //判断是否可购买
        boolean b = lotterybInfoService.isBuyLotteryB(Integer.parseInt(lotterybInfoId));
        if(!b){
            String msg = "This time is closed. No purchase allowed !";
            if(super.isChineseLanguage()){
                msg = "本次已完结，禁止購買 !";
            }
            return ResultUtil.errorJson(msg);
        }
        LoginUser loginUser = getLoginUser();
        //判断平台币可用余额是否足够
        if(!esLsbwalletService.judgeBalance(loginUser.getId(),total)){
            String msg = "not sufficient funds !";
            if(super.isChineseLanguage()){
                msg = "余額不足!";
            }
            return ResultUtil.errorJson(msg);
        }
        String dicTypeValue = getGuessDicTypeValue(lotterybInfoId);
        //判断是否获取到DicTypeValue值
        if("".equals(dicTypeValue)){
            String msg = "parameter error !";
            if(super.isChineseLanguage()){
                msg = "參數錯誤!";
            }
            return  ResultUtil.errorJson(msg);
        }
        JSONArray jsonArray =  JSONObject.parseArray(dataInfo);
        //新增平台币购买记录（出账成功记录）
        //b = lsbaccountsService.addOutSuccess(loginUser.getId(),getGuessDicTypeValue(lotterybInfoId),totalBigdecimal,"-1");
        String msg = "";
        /*if(!b){
            msg = "operation failure !";
            if(super.isChineseLanguage()){
                msg = "操作失败！";
            }
            return ResultUtil.errorJson(msg);
        }*/
        //购买成功之后计入购买统计
        b = lotterybStatisticsService.lotteryStatistice(lotteryConfigId,lotterybInfoId,issueNum,totalBigdecimal);
        if(!b){
            msg = "purchase failed !";
            if(super.isChineseLanguage()){
                msg = "购买失败！";
            }
            return ResultUtil.errorJson(msg);
        }
        //新增购买记录
        LotterybBuy lotterybBuy = new LotterybBuy();
        lotterybBuy.setMemberId(loginUser.getId());
        lotterybBuy.setLotterybInfoId(Integer.parseInt(lotterybInfoId));
        lotterybBuy.setLotterybConfigId(Integer.parseInt(lotteryConfigId));
        lotterybBuy.setLotterybIssueId(Integer.parseInt(issueNum));
        lotterybBuy.setTotal(totalBigdecimal);
        lotterybBuy.setIsLuck(0);
        lotterybBuy.setLuckTotal(BigDecimal.ZERO);
        lotterybBuy.setCreateTime(new Date());
        b = lotterybBuyService.insert(lotterybBuy);
        if(!b){
            msg = "purchase failed !";

            if(super.isChineseLanguage()){
                msg = "购买失败！";
            }
            return ResultUtil.errorJson(msg);
        }
        msg = "purchase succeeded !";
        if(super.isChineseLanguage()){
            msg = "购买成功！";
        }
        //购买提成计算
        lotterybPmDetailServiceIml.buyPM(lotterybBuy,lotterybInfoService.find(Integer.parseInt(lotterybInfoId)));
        return ResultUtil.successJson(msg);
    }


    /**
     * 查询购买总笔数
     * @param memberId
     * @param lotterybIssueId
     * @param lotterybInfoId
     * @return
     */
    public int getBuyCount(String memberId,Integer lotterybInfoId,Integer lotterybIssueId){
        Wrapper<LotterybBuy> wrapper = new EntityWrapper<>();
        wrapper.and().eq("lotteryb_issue_id",lotterybIssueId)
                .and().eq("member_id",memberId)
                .and().eq("lotteryb_info_id",lotterybInfoId);
        return lotterybBuyService.selectCount(wrapper);
    }

    /**
     * 根据竞猜玩法获取操作类型Id
     * @param lotteryInfoId
     * @return
     */
    private String getGuessDicTypeValue(String lotteryInfoId){
        if(lotteryInfoId.equals(LotterybInfoServiceImpl.TYPE_ID_1)){
            return "4";
        }
        if(lotteryInfoId.equals(LotterybInfoServiceImpl.TYPE_ID_3)){
            return "5";
        }
        if(lotteryInfoId.equals(LotterybInfoServiceImpl.TYPE_ID_5)){
            return "6";
        }
        return "";
    }

    /**
     * 查询中奖数据
     * @param lotteryaIssueId
     * @param lotterybConfigId
     * @return
     */
    public List<LotterybBuy> findLuckLotteryaBuy(Integer lotteryaIssueId, Integer lotterybConfigId) {
        Wrapper<LotterybBuy> wrapper = new EntityWrapper<>();
        wrapper.and().eq("lotteryb_issue_id",lotteryaIssueId)
        .eq("lotteryb_config_id",lotterybConfigId);
        return lotterybBuyService.selectList(wrapper);
    }

    /**
     * 推广账户，核算自己和下级（无限层级）A彩票消费情况
     * 明细：total：购买总金额
     *       counts：购买总注数
     *       luckCounts：中奖总注数
     *       luckTotal：中奖总金额
     * @param lotterybIssueId
     * @param memberId
     * @return
     */
    public Map<String,Object> calculateLowerLevelBuyTotal(Integer lotterybIssueId, String memberId){
        if(lotterybIssueId == null || StringUtils.isBlank(memberId)) return null;
        String sql = "select " +
                " sum(lab_.total) as total," + //购买总金额
                " sum(lab_.luck_total) as luckTotal " + //中奖总金额
                " from lotterya_buy lab_ " +
                " left join es_member em_ on(lab_.member_id = em_.id) " +
                " where lab_.lotterya_issue_id="+lotterybIssueId+" and lab_.transfer_status='1' " +
                " and (em_.generalize_member_ids like '%"+memberId+"%' or lab_.member_id='"+memberId+"')";
        SqlMapper sqlMapper = super.getSqlMapper();
        return sqlMapper.selectOne(sql);
    }
}
