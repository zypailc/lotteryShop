package com.didu.lotteryshop.manage.service;

import com.didu.lotteryshop.common.base.service.BaseService;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.github.abel533.sql.SqlMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class LotteryAManagerService extends BaseService {
    @Value("${LotteryAManagerAddress}")
    private String LotteryAManagerAddress;
    @Value("${LotteryAAdjustFundAddress}")
    private String LotteryAAdjustFundAddress;
    @Value("${LotteryABonusPoolAddress}")
    private String LotteryABonusPoolAddress;
    @Autowired
    private Web3jService web3jService;

    public ResultUtil findLotteryAInfo(){
        Map<String,Object> resultMap = new HashMap<>();
        SqlMapper sqlMapper = super.getSqlMapper();
        //查询A彩票期数数据
        String  sql = "select * from lotterya_issue order by issue_num desc limit 1";
        Map<String,Object> lotterAMap =  sqlMapper.selectOne(sql);
        if(lotterAMap != null && !lotterAMap.isEmpty()){
            resultMap.put("issue_num",lotterAMap.get("issue_num"));
            resultMap.put("luck_total",lotterAMap.get("luck_total"));
            resultMap.put("current_total",lotterAMap.get("current_total"));
            resultMap.put("adjust_total",lotterAMap.get("adjust_total"));
            resultMap.put("luck_num",lotterAMap.get("luck_num"));
        }
        resultMap.put("LotteryAManagerBalance",web3jService.getBalanceByEther(LotteryAManagerAddress));
        resultMap.put("LotteryAAdjustFundBalance", web3jService.getBalanceByEther(LotteryAAdjustFundAddress));
        resultMap.put("LotteryABonusPoolBalance",web3jService.getBalanceByEther(LotteryABonusPoolAddress));
        String sqlInfo = "select * from lotterya_info where id = 1";
        Map<String,Object> lotterAInfoMap =  sqlMapper.selectOne(sqlInfo);
        if(lotterAInfoMap != null && !lotterAInfoMap.isEmpty()){
            String  contractAddress = lotterAInfoMap.get("contract_address").toString();
            resultMap.put("LotteryAContractBalance",web3jService.getBalanceByEther(LotteryAManagerAddress));
        }
        return ResultUtil.successJson(resultMap);
    }


    public ResultUtil contractBalanceIn(String privateKey){
        String sqlInfo = "select * from lotterya_info where id = 1";
        SqlMapper sqlMapper = super.getSqlMapper();
        Map<String,Object> lotterAInfoMap =  sqlMapper.selectOne(sqlInfo);
        String contractAddress = null;
        BigDecimal total = BigDecimal.ZERO;
        if(lotterAInfoMap != null && !lotterAInfoMap.isEmpty() &&  lotterAInfoMap.get("contract_address") != null){
            contractAddress = lotterAInfoMap.get("contract_address").toString();
        }
        String  sql = "select * from lotterya_issue order by issue_num desc limit 1";
        Map<String,Object> lotterAMap =  sqlMapper.selectOne(sql);
        if(lotterAMap != null && !lotterAMap.isEmpty() && lotterAMap.get("adjust_total") != null){
            total = new BigDecimal(lotterAMap.get("adjust_total").toString());
        }
        if(StringUtils.isNotBlank(contractAddress) && total.compareTo(BigDecimal.ZERO) > 0 && StringUtils.isNotBlank(privateKey)){
            boolean bool = web3jService.contractBalanceIn(contractAddress,privateKey,total);
            return bool ? ResultUtil.successJson("successful") :ResultUtil.errorJson("operation failure ！");
        }
        return  ResultUtil.errorJson("operation failure ！");
    }
}
