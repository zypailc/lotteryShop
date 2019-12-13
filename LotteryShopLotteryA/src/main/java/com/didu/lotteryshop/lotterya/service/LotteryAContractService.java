package com.didu.lotteryshop.lotterya.service;

import com.didu.lotteryshop.common.service.GasProviderService;
import com.didu.lotteryshop.common.utils.Web3jUtils;
import com.didu.lotteryshop.lotterya.contract.LotteryAContract;
import com.didu.lotteryshop.lotterya.entity.LotteryAContractResultEntity;
import com.didu.lotteryshop.lotterya.entity.LotteryaBuy;
import com.didu.lotteryshop.lotterya.entity.LotteryaInfo;
import com.didu.lotteryshop.lotterya.entity.LotteryaIssue;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaBuyServiceImpl;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaInfoServiceImpl;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaIssueServiceImpl;
import com.github.abel533.sql.SqlMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * A彩票智能合约Service
 * @author CHJ
 * @date 2019-11-26
 */
@Service
public class LotteryAContractService extends LotteryABaseService {
    private static final Logger logger = LoggerFactory.getLogger(LotteryAContractService.class);
    @Autowired
    private Web3jService web3jService;
    @Autowired
    private LotteryaInfoServiceImpl lotteryaInfoService;
    @Autowired
    private LotteryaIssueServiceImpl lotteryaIssueService;
    @Autowired
    private LotteryaBuyServiceImpl lotteryaBuyService;
    @Autowired
    private GasProviderService gasProviderService;

    /**
     * 是否能购买彩票
     * @return boolean true能购买，false关闭购买
     */
    public boolean isBuyLotteryA(){
        boolean bool = false;
        Date nowDate = new Date();
        LotteryaIssue lotteryaIssue = lotteryaIssueService.findCurrentPeriodLotteryaIssue();
        if(lotteryaIssue != null && nowDate.before(lotteryaIssue.getEndTime()) && lotteryaIssue.getBuyStatus().equals("0"))
            bool = true;
        return bool;
    }

    /**
     * 判断合约金额是否充裕，够发当期奖金
     * @param currentBonus 当期奖金
     * @return
     */
    public boolean isContractBalanceSufficient(Integer lotteryaIssueId,BigDecimal currentBonus){
        boolean bool = false;
        LotteryaInfo lotteryaInfo = lotteryaInfoService.findLotteryaInfo();
        BigDecimal contractBalance = web3jService.getContractBalanceByEther(lotteryaInfo.getContractAddress());
        BigDecimal adjustTotal = BigDecimal.ZERO;
        String  bonusGrant = "0";
        if(contractBalance.compareTo(currentBonus) >= 0){
            bool = true;
            bonusGrant = "1";
        }else{
            adjustTotal = currentBonus.subtract(contractBalance);
        }
        //修改允许发放奖金，0：不允许；1：允许发放
        SqlMapper sqlMapper = super.getSqlMapper();
        String updateSql = "update lotterya_issue set current_total="+contractBalance+",adjust_total="+adjustTotal+",bonus_grant='"+bonusGrant+"' where id="+lotteryaIssueId;
        sqlMapper.update(updateSql);
        return bool;
    }

    /**
     * 计算当期奖金总额
     * @param lotteryaIssueId 期数ID
     * @param luckNum 中奖号码
     * @return
     */
    public  BigDecimal calculateCurrentBonus(Integer lotteryaIssueId,String luckNum){
        BigDecimal allTotal = BigDecimal.ZERO;
        String sql = " select sum(multiple_num) as allMultipleNum " +
                " from lotterya_buy " +
                " where lotterya_issue_id="+lotteryaIssueId+" and transfer_status = '1' and luck_num='"+luckNum+"'";
        SqlMapper sqlMapper = super.getSqlMapper();
        Map<String, Object> mMap =  sqlMapper.selectOne(sql);
        if(mMap != null && !mMap.isEmpty()){
            //获取幸运号码总倍数
            Integer allMultipleNum = Integer.parseInt(mMap.get("allMultipleNum").toString());
            LotteryaInfo lotteryaInfo = lotteryaInfoService.findLotteryaInfo();
            //单注奖金，公式：(price*1000)/100*50
            BigDecimal pTotal = lotteryaInfoService.calculateSingleBonus(lotteryaInfo);
            //幸运号码中奖奖金总额
            allTotal = pTotal.multiply(new BigDecimal(allMultipleNum));
        }
        //更新当期奖金
        String updateSql = "update lotterya_issue set luck_total="+allTotal+",luck_num='"+luckNum+"' where id="+lotteryaIssueId;
        sqlMapper.update(updateSql);
        return allTotal;
    }

    /**
     * 判断当前购买彩票号码，是否超出最高倍数限制
     * @param lotteryaIssueId 期数ID
     * @param luckNum 彩票号码
     * @param multipleNumber 倍数
     * @return boolean true超出限制，false 未超出限制
     */
    public boolean isBuyMultipleNumber(Integer lotteryaIssueId,String luckNum,Integer multipleNumber){
        boolean bool = false;
        String sql = " select sum(multiple_num) as allMultipleNum " +
                    " from lotterya_buy " +
                    " where lotterya_issue_id="+lotteryaIssueId+" and transfer_status in('0','1') and luck_num='"+luckNum+"'";
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
            BigDecimal pTotal = lotteryaInfoService.calculateSingleBonus(lotteryaInfo);
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
                upTotal = upTotal.add(afBalance.divide(new BigDecimal("100")).multiply(lotteryaInfo.getAdjustBonusRatio()));
            }else{
                upTotal = upTotal.add(lotteryaInfo.getAdjustBonusUp());
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
            TransactionReceipt transactionReceipt = lotteryAContract.BuyLottery(luckNum, BigInteger.valueOf(multipleNumber), Convert.toWei(amount.toPlainString(), Convert.Unit.ETHER).toBigInteger()).sendAsync().get();
            //事务哈希值
            String transactionHash = transactionReceipt.getTransactionHash();
            lacre.setTransactionHash(transactionHash);
            String status = transactionReceipt.getStatus();
            if(Web3jUtils.transactionReceiptStatusSuccess(status)){
                lacre.setStatus(LotteryAContractResultEntity.STATUS_SUCCESS);
                //购买A彩票成功
                lacre.setMsg("Buy lottery A successfully!");
                //燃气费
                BigInteger gasUsed =  transactionReceipt.getGasUsed();
                lacre.setGasUsed(Web3jUtils.bigIntegerToBigDecimal( gasProviderService.getGasPrice().multiply(gasUsed)));
            }else if(Web3jUtils.transactionReceiptStatusWait(status)){
                lacre.setStatus(LotteryAContractResultEntity.STATUS_WAIT);
                //等待交易确认
                lacre.setMsg("Waiting for transaction confirmation!");
            }else if(Web3jUtils.transactionReceiptStatusFail(status)){
                lacre.setStatus(LotteryAContractResultEntity.STATUS_FAIL);
                //购买A彩票失败
                lacre.setMsg("Failed to buy the lottery A!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lacre.setTransactionHash("");
            lacre.setStatus(LotteryAContractResultEntity.STATUS_FAIL);
            //购买A彩票失败
            lacre.setMsg("Failed to buy the lottery A!");
        }
        return lacre;
    }

    /**
     * 开奖函数，返回中奖号码
     * @return lunckNumber
     */
    public String drawLotterA(){
        String lunckNumber = "";
        try {
            LotteryAContract lotteryAContract = web3jService.loadManagerContract(lotteryaInfoService.findLotteryaInfo().getContractAddress());
            Random random = new Random();
            String randomStr = "";
            for(int i=0;i < 20 ; i++){
                randomStr += random.nextInt(10);
            }
            TransactionReceipt transactionReceipt =  lotteryAContract.DrawLottery(new BigInteger(randomStr)).send();
            String status = transactionReceipt.getStatus();
            if(Web3jUtils.transactionReceiptStatusSuccess(status)){
                lunckNumber = lotteryAContract.ShowLuckNum().send();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lunckNumber;
    }

    /**
     * 判断合约中奖记录是否等于平台用户记录。
     * @notice 对比中奖人数，与平台购买人数是否一致，如果不一致，说明有非法用户之间调用ETH智能合约进行操作，停止发放奖金，
     *         由管理员对数据审核通过后，再进行奖金发放
     * @param lotteryaIssueId A彩票期数表主键ID
     * @param luckNum 中奖号码
     * @return
     */
    public boolean isContractLuckPersonNumEqPlatformLuckPersonNum(Integer lotteryaIssueId,String luckNum){
        boolean bool = false;
        if(lotteryaIssueId != null && StringUtils.isNotBlank(luckNum)){
            try {
                List<Map<String,String>> luckMemberList = this.getLuckMember();
                List<LotteryaBuy> lotteryaBuyList = lotteryaBuyService.findLuckLotteryaBuy(lotteryaIssueId,luckNum);
                if(luckMemberList != null && lotteryaBuyList != null){
                    if(luckMemberList.size() == lotteryaBuyList.size()){
                        bool = true;
                        return bool;
                    }else{
                        //非法购买彩票数据
                        String errorStr = " Illegal to buy LotteryA:{";
                        for(Map<String,String> lm : luckMemberList){
                            String sql = "select lab_.id as id " +
                                    " from lotterya_buy as lab_ " +
                                    " left join es_member as em_ on(lab_.member_id = em_.id) " +
                                    " where lab_.lotterya_issue_id ="+lotteryaIssueId+
                                        " and lab_.multiple_num="+Integer.valueOf(lm.get("multipleNumber"))+
                                         " and lab_.transfer_status='1' and lab_.luck_num='"+luckNum+"' and em_.p_address='"+lm.get("ethAddress")+"' " +
                                    " limit 1";
                            Map<String, Object>  jrMap =  super.getSqlMapper().selectOne(sql);
                            if(jrMap == null || jrMap.isEmpty() || jrMap.get("id") == null || "".equals(jrMap.get("id").toString())){
                                errorStr += "[address:"+lm.get("ethAddress")+",multipleNumber:"+lm.get("multipleNumber")+"],";
                            }
                        }
                        errorStr = errorStr.substring(0, errorStr.length()-1);
                        logger.error(errorStr);
                    }
                }
            }catch (Exception e) {
                logger.error("Method 'isContractLuckPersonNumEqPlatformLuckPersonNum' Error:"+e.getMessage());
            }
        }
        return bool;
    }

    /**
     * 获取中奖用户
     * @return List<Map<String,Integer>>
     * @throws Exception
     */
    public List<Map<String,String>> getLuckMember() throws Exception{
        List<Map<String,String>> luckMemberList = new ArrayList<>();
        Map<String,String> luckMemberMap = new HashMap<>();
        LotteryAContract lotteryAContract = web3jService.loadManagerContract(lotteryaInfoService.findLotteryaInfo().getContractAddress());
        boolean bool = true;
        int index = 0;
        do {
            try{
                Tuple2<String, BigInteger> tuple2Result = lotteryAContract.getBuyerLuckList(BigInteger.valueOf(index)).send();
                if(tuple2Result != null){
                    if(!tuple2Result.component1().equals("0x0000000000000000000000000000000000000000")
                            && !tuple2Result.component2().toString().equals("0")) {
                        luckMemberMap.put("ethAddress", tuple2Result.component1());//eth 钱包地址
                        luckMemberMap.put("multipleNumber", tuple2Result.component2().toString());// 购买彩票倍数
                        luckMemberList.add(luckMemberMap);
                        index++;
                        continue;
                    }
                }
                bool = false;
            } catch (Exception e) {
                bool = false;
                logger.error("Method 'getLuckMember' Error:"+e.getMessage());
                throw new Exception(" Method 'getLuckMember' Exception："+e.getMessage());
            }
        }while (bool);
        return luckMemberList;
    }

    /**
     * 发放奖金
     * @param lotteryaIssue
     * @return
     */
    public boolean payBonus(LotteryaIssue lotteryaIssue){
        boolean bool = false;
        LotteryaInfo lotteryaInfo = lotteryaInfoService.findLotteryaInfo();
        //判断奖金未发放，并且允许发放
        if(lotteryaIssue.getBonusStatus().equals("0") && lotteryaIssue.getBonusGrant().equals("1")){
            LotteryAContract lotteryAContract = web3jService.loadManagerContract(lotteryaInfo.getContractAddress());
            //是否有中奖用户
            boolean isLuckMmber = lotteryaIssue.getLuckTotal().compareTo(BigDecimal.ZERO) > 0;
            //要有中奖者，才进行发放
            if(isLuckMmber){
                boolean forBool = true;
                Integer cycleIndex = 1;
                do{
                    try {
                        TransactionReceipt transactionReceipt = lotteryAContract.PayBonus(BigInteger.valueOf(cycleIndex)).send();
                        String status = transactionReceipt.getStatus();
                        //状态 ,状态需要测试进行修改
                        if(Web3jUtils.transactionReceiptStatusSuccess(status)){
                            List<LotteryAContract.PayBonusIsExecuteEventEventResponse>  pList =  lotteryAContract.getPayBonusIsExecuteEventEvents(transactionReceipt);
                            if(pList != null && pList.size() > 0){
                                LotteryAContract.PayBonusIsExecuteEventEventResponse pb = pList.get(0);
                                if(!pb.IsExecute){
                                    forBool = false;
                                }
                            }
                        }
                    } catch (Exception e) {
                        forBool = false;
                        logger.error("Method 'payBonus' Error:"+e.getMessage());
                        e.printStackTrace();
                        return bool;
                    }
                    cycleIndex++;
                }while (forBool);
            }
            lotteryaIssue.setBonusStatus("1");
            lotteryaIssue.setBonusStatusTime(new Date());
            bool = lotteryaIssueService.updateAllColumnById(lotteryaIssue);
            //新增账单记录和中奖平台币分成；
            if(bool && isLuckMmber){
                bool = lotteryaBuyService.updateIsLuck(lotteryaIssue.getId(),lotteryaIssue.getLuckNum(),lotteryaInfo);
            }
            //清除合约数据
            if(bool){
                try {
                    //删除购买彩票组
                   List<String> sList = this.getBuyLuckNumber();
                   if(sList != null && sList.size() > 0){
                       for(String s:sList){
                           TransactionReceipt transactionReceipt = lotteryAContract.resetBuyMapping(s).send();
                           //状态 状态需要测试进行修改
                           String status = transactionReceipt.getStatus();
                           if(!Web3jUtils.transactionReceiptStatusSuccess(status)){
                               return false;
                           }
                       }
                   }
                    //删除购买记录；删除中奖者；幸运号码职重置为“”
                    TransactionReceipt transactionReceipt = lotteryAContract.resetData().send();
                    //状态 状态需要测试进行修改
                    String status = transactionReceipt.getStatus();
                    if(!Web3jUtils.transactionReceiptStatusSuccess(status)){
                        return false;
                    }
                }catch (Exception e){
                    logger.error("Method 'getBuyLuckNumber OR resetBuyMapping OR ' Error:"+e.getMessage());
                    e.printStackTrace();
                    bool = false;
                    return bool;
                }
            }
        }
        return bool;
    }
    /**
     * 获取购买记录
     * @return List<String>
     * @throws Exception
     */
    public List<String> getBuyLuckNumber() throws Exception{
        List<String> buyLuckNumberList = new ArrayList<>();
        LotteryAContract lotteryAContract = web3jService.loadManagerContract(lotteryaInfoService.findLotteryaInfo().getContractAddress());
        boolean bool = true;
        int index = 0;
        do {
            try{
                String buyLuckNumber = lotteryAContract.getBuyLuckNumberArray(BigInteger.valueOf(index)).send();
                if(buyLuckNumber != null){
                    if(!"".equals(buyLuckNumber)) {
                        buyLuckNumberList.add(buyLuckNumber);
                        index++;
                        continue;
                    }
                }
                bool = false;
            } catch (Exception e) {
                bool = false;
                logger.error("Method 'getBuyLuckNumber' Error:"+e.getMessage());
                throw new Exception(" Method 'getBuyLuckNumber' Exception："+e.getMessage());
            }
        }while (bool);
        return buyLuckNumberList;
    }

    /**
     * 修改合约彩票单价
     * @param contractAddress
     * @param price
     * @return
     */
    public  boolean updateLotteryAPrice(String contractAddress,BigDecimal price){
        boolean bool = false;
        try {
            LotteryAContract lotteryAContract = web3jService.loadManagerContract(contractAddress);
            BigInteger cPrice = lotteryAContract.ShowPrice().send();
            cPrice = cPrice == null ? BigInteger.ZERO : cPrice;
            BigDecimal cbPrice =  Web3jUtils.bigIntegerToBigDecimal(cPrice);
            if(cbPrice.compareTo(price) != 0){
               TransactionReceipt transactionReceipt = lotteryAContract.updatePrice(Convert.toWei(price.toPlainString(), Convert.Unit.ETHER).toBigInteger()).send();
               if(Web3jUtils.transactionReceiptStatusSuccess(transactionReceipt.getStatus())){
                   bool = true;
               }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Method 'updateLotteryAPrice' Error:"+e.getMessage());
        }
        return bool;

    }

}
