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
import com.didu.lotteryshop.common.utils.CodeUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.lotteryb.entity.LotterybBuy;
import com.didu.lotteryshop.lotteryb.entity.LotterybIssue;
import com.didu.lotteryshop.lotteryb.entity.LotterybPm;
import com.didu.lotteryshop.lotteryb.service.form.impl.*;
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
    @Autowired
    private LotterybIssueServiceImpl lotterybIssueService;

    /**
     * 购买
     * @param lotterybInfoId 玩法Id
     * @param issueNum 期数
     * @param dataInfo 购买的数据
     * @param total 购买总金额
     * @return
     */
    public ResultUtil lsbBuyLottery(Integer lotterybInfoId,String issueNum, String dataInfo,BigDecimal total) {
        //判断是否可购买
        boolean b = lotterybInfoService.isBuyLotteryB(lotterybInfoId);
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
        //解析购买数据
        List<String> list = JSONObject.parseArray(dataInfo, String.class);
        String msg = "";
        String serialNumber = CodeUtil.getUuid();
        LotterybIssue lotterybIssue = lotterybIssueService.getLotterybIssue(lotterybInfoId,issueNum);
        for (String s : list) {
            Map<String,Object> map = (Map<String, Object>) JSONObject.parse(s);
            String number = map.get("number").toString();
            String lotterybConfigIds = map.get("lotterybConfig").toString();
            String type = map.get("type").toString();
            String money = map.get("money").toString();

            //新增购买记录
            LotterybBuy lotterybBuy = new LotterybBuy();
            lotterybBuy.setMemberId(loginUser.getId());
            lotterybBuy.setLotterybInfoId(lotterybInfoId);
            lotterybBuy.setLotterybConfigIds(lotterybConfigIds);
            lotterybBuy.setLotterybIssueId(lotterybIssue.getId());
            lotterybBuy.setTotal(new BigDecimal(money));
            lotterybBuy.setIsLuck(0);
            lotterybBuy.setLuckTotal(BigDecimal.ZERO);
            lotterybBuy.setSerialNumber(serialNumber);
            lotterybBuy.setCreateTime(new Date());
            b = lotterybBuyService.insert(lotterybBuy);
            if(!b){
                msg = "purchase failed !";

                if(super.isChineseLanguage()){
                    msg = "购买失败！";
                }
                return ResultUtil.errorJson(msg);
            }

            b = lotterybStatisticsService.lotteryStatistice(lotterybBuy.getId(),lotterybConfigIds,lotterybInfoId,lotterybIssue.getId(),type,new BigDecimal(money));
            //购买提成计算
            lotterybPmDetailServiceIml.buyPM(lotterybBuy,lotterybInfoService.find(lotterybInfoId));
        }
        //这里一次性会购买多种组合 平台币出账只出一次账 因为只算购买了一次 在购买记录里面可查看购买的每种组合的明细
        b = lsbaccountsService.addOutSuccess(loginUser.getId(),getGuessDicTypeValue(lotterybInfoId),total,serialNumber);
        //新增平台币购买记录（出账成功记录）
        if(!b){
            msg = "operation failure !";
            if(super.isChineseLanguage()){
                msg = "操作失败！";
            }
            return ResultUtil.errorJson(msg);
        }
        msg = "purchase succeeded !";
        if(super.isChineseLanguage()){
            msg = "购买成功！";
        }
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
    private String getGuessDicTypeValue(Integer lotteryInfoId){
        if(lotteryInfoId == LotterybInfoServiceImpl.TYPE_ID_1){
            return "4";
        }
        if(lotteryInfoId == LotterybInfoServiceImpl.TYPE_ID_3){
            return "5";
        }
        if(lotteryInfoId == LotterybInfoServiceImpl.TYPE_ID_5){
            return "6";
        }
        return "";
    }

    /**
     * 查询中奖数据
     * @param lotterybIssue
     * @return
     */
    public List<LotterybBuy> findLuckLotteryaBuy(LotterybIssue lotterybIssue) {
        Wrapper<LotterybBuy> wrapper = new EntityWrapper<>();
        wrapper.where(" find_in_set( id,"+lotterybIssue.getLotterybBuyIds()+") and lotteryb_issue_id = "+lotterybIssue.getId()+"");
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
                " sum(lbb_.total) as total," + //购买总金额
                " sum(lbb_.luck_total) as luckTotal " + //中奖总金额
                " from lotteryb_buy lbb_ " +
                " left join es_member em_ on(lbb_.member_id = em_.id) " +
                " where lbb_.lotteryb_issue_id="+lotterybIssueId +
                " and (em_.generalize_member_ids like '%"+memberId+"%' or lbb_.member_id='"+memberId+"')";
        SqlMapper sqlMapper = super.getSqlMapper();
        return sqlMapper.selectOne(sql);
    }

    /**
     * 查询购买
     * @param currentPage
     * @param pageSize
     * @param lotterybInfoId 玩法Id
     */
    public ResultUtil lotterybBuyRecodeFind(Integer currentPage, Integer pageSize, Integer lotterybInfoId) {
        LoginUser loginUser = getLoginUser();
        LotterybIssue lotterybIssue = lotterybIssueService.getLotterybIssue(lotterybInfoId);
        String sql = " select lb_.total,lc_.type,lc_.lines, " +
                " (select group_concat(lc1_.en_title) from lotteryb_config lc1_ where lc1_.id in (lb_.lotteryb_config_ids) " +
                " GROUP BY lc1_.type) as enTitle, " +
                " (select group_concat(lc1_.zh_title) from lotteryb_config lc1_ where lc1_.id in (lb_.lotteryb_config_ids) " +
                " GROUP BY lc1_.type) as zhTitle ,lb_.create_time as createTime" +
                " from lotteryb_buy lb_ " +
                " left join lotteryb_config lc_ on (lc_.id = SUBSTRING_INDEX(lb_.lotteryb_config_ids,',',1)) " +
                " where lb_.lotteryb_issue_id = '"+lotterybIssue.getId()+"' ";
        if(loginUser != null){
            sql += " and lb_.member_id = '" + loginUser.getId() + "'";
        }
        if(lotterybInfoId != null && lotterybInfoId > 0){
            sql += " and lb_.lotteryb_info_id = " + lotterybInfoId + "";
        }
        sql += " order by createTime desc ";
        if(currentPage != null && currentPage > 0 & pageSize != null && pageSize > 0){
            currentPage = (currentPage - 1) * pageSize;
            sql += " LIMIT " + currentPage + "," + pageSize;
        }
        List<Map<String,Object>> list = getSqlMapper().selectList(sql);
        return ResultUtil.successJson(list);
    }
}
