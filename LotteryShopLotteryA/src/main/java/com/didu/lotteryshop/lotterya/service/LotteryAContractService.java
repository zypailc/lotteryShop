package com.didu.lotteryshop.lotterya.service;

import com.didu.lotteryshop.common.utils.Web3jUtils;
import com.didu.lotteryshop.lotterya.contract.LotteryAContract;
import com.didu.lotteryshop.lotterya.entity.LotteryAContractResultEntity;
import com.didu.lotteryshop.lotterya.entity.LotteryaInfo;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaInfoServiceImpl;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaIssueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * A彩票智能合约Service
 * @author CHJ
 * @date 2019-11-26
 */
@Service
public class LotteryAContractService extends LotteryABaseService {
    @Autowired
    private Web3jService web3jService;
    @Autowired
    private LotteryaInfoServiceImpl lotteryaInfoService;
    @Autowired
    private LotteryaIssueServiceImpl lotteryaIssueService;

    /**
     * 是否能购买彩票
     * @return boolean true能购买，false关闭购买
     */
    public boolean isBuyLotteryA(){
        boolean bool = false;
        if(lotteryaIssueService.findCurrentPeriodLotteryaIssue().getBuyStatus().equals("0"))
            bool = true;
        return bool;
    }

    /**
     * 判断当前购买彩票号码，是否超出最高倍数限制
     * @param luckNum 彩票号码
     * @param multipleNumber 倍数
     * @return boolean true超出限制，false 未超出限制
     */
    public boolean isBuyMultipleNumber(String luckNum,Integer multipleNumber){
        boolean bool = false;
        String sql = " select sum(multiple_num) as allMultipleNum " +
                    " from lotterya_buy " +
                    " where transfer_status in('0','1') and luck_num='"+luckNum+"'";
        Map<String, Object> mMap =  super.getSqlMapper().selectOne(sql);
        if(mMap != null && !mMap.isEmpty()){
            //获取幸运号码总倍数
            Integer allMultipleNum = Integer.parseInt(mMap.get("allMultipleNum").toString());
            allMultipleNum = allMultipleNum == null ? 0 : allMultipleNum;
            allMultipleNum += multipleNumber;
            LotteryaInfo lotteryaInfo = lotteryaInfoService.findLotteryaInfo();
            //最高限制奖金
            BigDecimal upTotal = BigDecimal.ZERO;
            //单注奖金，公式：(price*1000)/100*50
            BigDecimal pTotal =  lotteryaInfo.getPrice().multiply(new BigDecimal("1000"))
                            .divide(new BigDecimal("100"))
                            .multiply(new BigDecimal("50"));
            //幸运号码中奖奖金总额
            BigDecimal cTotal = pTotal.multiply(new BigDecimal(allMultipleNum));
            //合约余额（当期奖金）
            BigDecimal cBalance  = web3jService.getContractBalanceByEther(lotteryaInfo.getContractAddress());
            //调节基金余额
            BigDecimal afBalance  = web3jService.getAdjustFundBalanceByEther();
            if(cBalance.compareTo(lotteryaInfo.getCurrenBonusDown()) >= 0){
                upTotal = cBalance.divide(new BigDecimal("100")).multiply(lotteryaInfo.getCurrenBonusRatio());
            }else{
                upTotal = cBalance;
            }
            if(afBalance.compareTo(lotteryaInfo.getAdjustBonusDown()) >= 0 ){
                upTotal.add(afBalance.divide(new BigDecimal("100")).multiply(lotteryaInfo.getAdjustBonusRatio()));
            }else{
                upTotal.add(lotteryaInfo.getAdjustBonusUp());
            }
            if(cTotal.compareTo(upTotal) > 0){
                bool = true;
            }
        }
        return bool;
    }

    /**
     * 购买彩票
     * @param luckNum 彩票号码
     * @param multipleNumber 倍数
     * @param amount 金额
     * @return LotteryAContractResultEntity
     */
    public LotteryAContractResultEntity buyLotterA(String luckNum, Integer multipleNumber, BigDecimal amount){
        LotteryAContractResultEntity lacre = new LotteryAContractResultEntity();
        try {
            LotteryAContract lotteryAContract  = web3jService.loadLoginMemberContract(lotteryaInfoService.findLotteryaInfo().getContractAddress());
            TransactionReceipt transactionReceipt = lotteryAContract.BuyLottery(luckNum, BigInteger.valueOf(multipleNumber),amount.toBigInteger()).send();
            //事务哈希值
           String transactionHash = transactionReceipt.getTransactionHash();
            lacre.setTransactionHash(transactionHash);
            //状态 TODO 状态需要测试进行修改
            String status = transactionReceipt.getStatus();
            lacre.setStatus(LotteryAContractResultEntity.STATUS_SUCCESS);
            //购买A彩票成功
            lacre.setMsg("Buy lottery A successfully!");
            //购买A彩票失败
            // lacre.setMsg("Failed to buy the lottery A!");
            //燃气费
            BigInteger gasUsed =  transactionReceipt.getGasUsed();
            lacre.setGasUsed(Web3jUtils.bigIntegerToBigDecimal(gasUsed));
        } catch (Exception e) {
            e.printStackTrace();
            lacre.setStatus(LotteryAContractResultEntity.STATUS_FAIL);
            //购买A彩票失败
            lacre.setMsg("Failed to buy the lottery A!");
        }
        return lacre;
    }
}
