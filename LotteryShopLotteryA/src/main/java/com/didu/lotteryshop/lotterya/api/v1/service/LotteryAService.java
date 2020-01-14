package com.didu.lotteryshop.lotterya.api.v1.service;

import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.service.form.impl.EsEthaccountsServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.EsEthwalletServiceImpl;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.lotterya.entity.LotteryAContractResultEntity;
import com.didu.lotteryshop.lotterya.entity.LotteryaBuy;
import com.didu.lotteryshop.lotterya.entity.LotteryaInfo;
import com.didu.lotteryshop.lotterya.entity.LotteryaIssue;
import com.didu.lotteryshop.lotterya.service.LotteryABaseService;
import com.didu.lotteryshop.lotterya.service.LotteryAContractService;
import com.didu.lotteryshop.lotterya.service.Web3jService;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaBuyServiceImpl;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaInfoServiceImpl;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaIssueServiceImpl;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaPmDetailServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 **
  * A彩票 Service
  * @author CHJ
  * @date 2019-11-01 14:09
  */
@Service
public class LotteryAService extends LotteryABaseService {
    @Autowired
    private LotteryaInfoServiceImpl lotteryaInfoService;
    @Autowired
    private LotteryaIssueServiceImpl lotteryaIssueService;
    @Autowired
    private LotteryaBuyServiceImpl lotteryaBuyService;
    @Autowired
    private EsEthwalletServiceImpl esEthwalletService;
    @Autowired
    private EsEthaccountsServiceImpl esEthaccountsService;
    @Autowired
    private LotteryAContractService lotteryAContractService;
    @Autowired
    private LotteryaPmDetailServiceImpl lotteryaPmDetailService;

    @Autowired
    private KafkaTemplate kafkaTemplate;


    /**
     * 获取彩票信息
     * @return
     */
    public ResultUtil getLotteryInfo(){
        Map<String,Object> rMap = new HashMap<>();
        LotteryaInfo lotteryaInfo = lotteryaInfoService.findLotteryaInfo();
        LotteryaIssue lotteryaIssue = lotteryaIssueService.findCurrentPeriodLotteryaIssue();
        //彩票名称
        rMap.put("zhtitle",lotteryaInfo.getZhtitle());
        rMap.put("entitle",lotteryaInfo.getEntitle());
        //彩票单价
        rMap.put("price",lotteryaInfo.getPrice().toPlainString());
        //当前期数
        rMap.put("issueNum",lotteryaIssue.getIssueNum());
        //當前期數id
        rMap.put("issueNumId",lotteryaIssue.getId());
        //购买状态，0：开启，1：关闭
        rMap.put("buyStatus",lotteryaIssue.getBuyStatus());
        //周期开始时间
        rMap.put("startTime",lotteryaIssue.getStartTime());
        //当前时间
        rMap.put("presentTime",new Date());
        //开奖时间
        rMap.put("endTime",lotteryaIssue.getEndTime());
        //合约地址
        rMap.put("contractAddress",lotteryaInfo.getContractAddress());
        //提成有效xx期数
        rMap.put("pmVnum",lotteryaInfo.getPmVnum());
        //提成补签xx注数
        rMap.put("pmRnum",lotteryaInfo.getPmRnum());
        //购买提成比例（单位%）
        rMap.put("buyPm",lotteryaInfo.getBuyPm());
        //中奖提成比例（单位%）
        rMap.put("drawPm",lotteryaInfo.getDrawPm());
        //中奖金额1注1倍
        rMap.put("total",lotteryaInfoService.calculateSingleBonus(lotteryaInfo));
        //修改合约彩票单价
        if(StringUtils.isNotBlank(lotteryaInfo.getContractAddress()) && lotteryaInfo.getPrice() != null)
        lotteryAContractService.updateLotteryAPrice(lotteryaInfo.getContractAddress(),lotteryaInfo.getPrice());
        return ResultUtil.successJson(rMap);
    }

    /**
     * 分页查询彩票期数表
     * @param currentPage
     * @param pageSize
     * @return
     */
    public ResultUtil getLotteryIssue(int currentPage,int pageSize){
        return ResultUtil.successJson(lotteryaIssueService.findPageLotteryaIssue(currentPage,pageSize));
    }

    /**
     * 分页查询购买数据
     * @param currentPage
     * @param pageSize
     * @param isOneself 是否只查询自己 0：否 ；1：是
     * @param mTransferStatus 状态格式 :'1','2'
     * @param lotteryaBuy
     * @return
     */
    public ResultUtil getLotteryBuy(Integer currentPage, Integer pageSize,Integer isOneself,String mTransferStatus, LotteryaBuy lotteryaBuy,Integer type){
        if(isOneself != null && isOneself == 1){
            if(super.getLoginUser() != null){
                lotteryaBuy.setMemberId(super.getLoginUser().getId());
            }else {
                lotteryaBuy.setMemberId("-1");
            }
        }else{
            lotteryaBuy.setMemberId(null);
        }
        if(type == 2){
            return ResultUtil.successJson(lotteryaBuyService.getPageLotteryBuyAll(currentPage,pageSize,mTransferStatus,lotteryaBuy));
        }
        return ResultUtil.successJson(lotteryaBuyService.getPageLotteryBuy(currentPage,pageSize,mTransferStatus,lotteryaBuy));
    }


    /**
     * eth购买彩票
     * @param luckNum 幸运号码
     * @param multipleNumber 倍数
     * @param  payPasswod 支付密码
     * @return
     */
    public ResultUtil ethBuyLottery(String luckNum,Integer multipleNumber,String payPasswod){
        //判断支付密码是否错误 //支付密码错
        if(!super.getLoginUser().getPaymentCode().equals(AesEncryptUtil.encrypt_code(payPasswod, Constants.KEY_TOW) )){
            String msg = "Payment password error!";
            if(super.isChineseLanguage()){
                msg = "支付密碼錯誤!";
            }
            return ResultUtil.errorJson(msg);
        }
        //判断是否正在开奖中 //正在开奖，禁止购买
        LotteryaIssue lotteryaIssue = lotteryaIssueService.findCurrentPeriodLotteryaIssue();
        if(!lotteryAContractService.isBuyLotteryA()){
            String msg = "Lottery drawing in progress, no purchase!\"";
            if(super.isChineseLanguage()){
                msg = "正在開獎，禁止購買!";
            }
            return ResultUtil.errorJson(msg);
        }
        //判断倍数是否超出最高倍数限制 //该注幸运号码已经达到最高注数，请降低倍数或更换其它幸运号码！
        if(lotteryAContractService.isBuyMultipleNumber(lotteryaIssue.getId(),luckNum,multipleNumber)){
            String msg = "This note lucky number has reached the highest note number, please lower the multiple or replace other lucky number!";
            if(super.isChineseLanguage()){
                msg = "該幸運號碼已達到最高倍數限制，請降低倍數或更換其他幸運號碼！";
            }
            return ResultUtil.errorJson(msg);
        }
        //判断账户余额是否充足
        LotteryaInfo lotteryaInfo = lotteryaInfoService.findLotteryaInfo();
        BigDecimal eValue = lotteryaInfo.getPrice().multiply(BigDecimal.valueOf(multipleNumber));
        if(!esEthwalletService.judgeBalance(super.getLoginUser().getId(),eValue)){
            String msg = "Account balance is insufficient, please recharge first!";
            if(super.isChineseLanguage()){
                msg = "賬戶余額不足，請先充值！";
            }
            //账户余额不足，请先充值！
            return ResultUtil.errorJson(msg);
        }
        //LotteryAContractResultEntity lacre = lotteryAContractService.buyLotterA(luckNum,multipleNumber,eValue);
        //存入购买记录
        LotteryaBuy lotteryaBuy = new LotteryaBuy();
        lotteryaBuy.setMemberId(super.getLoginUser().getId());
        lotteryaBuy.setLuckNum(luckNum);
        lotteryaBuy.setTotal(eValue);
        //lotteryaBuy.setTransferHashValue(lacre.getTransactionHash());
        lotteryaBuy.setTransferHashValue("");
        lotteryaBuy.setTransferStatus("1");
//        //等待确认
//        if(lacre.getStatus() == LotteryAContractResultEntity.STATUS_WAIT){
//            lotteryaBuy.setTransferStatus("1");
//        }
//        //成功
//        if(lacre.getStatus() == LotteryAContractResultEntity.STATUS_SUCCESS){
//          lotteryaBuy.setTransferStatus("1");
//        }
//        //失败
//        if(lacre.getStatus() == LotteryAContractResultEntity.STATUS_FAIL){
//            lotteryaBuy.setTransferStatus("2");
//        }
        lotteryaBuy.setTransferStatusTime(new Date());
        lotteryaBuy.setCreateTime(new Date());
        lotteryaBuy.setMultipleNum(multipleNumber);
        lotteryaBuy.setIsLuck("0");
        lotteryaBuy.setLotteryaIssueId(lotteryaIssue.getId());
        lotteryaBuy.setLuckTotal(BigDecimal.ZERO);
        boolean bool =  lotteryaBuyService.insert(lotteryaBuy);
        if(bool){
            bool = esEthaccountsService.addOutBeingProcessed(super.getLoginUser().getId(),EsEthaccountsServiceImpl.DIC_TYPE_BUYLOTTERYA,eValue,lotteryaBuy.getId().toString());
            if(bool){
                //kafka 执行公链购买彩票
                kafkaTemplate.send("kafkaBuyLottery","lotteryaBuyId",lotteryaBuy.getId().toString());
            }
//        if(bool && (lacre.getStatus() == LotteryAContractResultEntity.STATUS_WAIT || lacre.getStatus() == LotteryAContractResultEntity.STATUS_SUCCESS)){
//            if(lacre.getStatus() == LotteryAContractResultEntity.STATUS_WAIT ){
//               bool = esEthaccountsService.addOutBeingProcessed(super.getLoginUser().getId(),EsEthaccountsServiceImpl.DIC_TYPE_BUYLOTTERYA,eValue,lotteryaBuy.getId().toString());
//            }
//            if(lacre.getStatus() == LotteryAContractResultEntity.STATUS_SUCCESS){
//                bool = esEthaccountsService.addOutSuccess(super.getLoginUser().getId(),EsEthaccountsServiceImpl.DIC_TYPE_BUYLOTTERYA,eValue,lotteryaBuy.getId().toString(),lacre.getGasUsed());
//                if(bool){
//                    //购买提成
//                    bool = lotteryaPmDetailService.buyPM(lotteryaBuy,lotteryaInfo);
//                }
//            }
            if(bool){
                String msg = "Purchase succeeds!";
                if(super.isChineseLanguage()){
                    msg = "購買成功!";
                }
                return ResultUtil.successJson(msg);
            }
        }
        String msg = "Execution error, please contact administrator!";
        if(super.isChineseLanguage()){
            msg = "執行錯誤，請聯系管理員";
        }
        //执行错误，请联系管理员！
       return ResultUtil.errorJson(msg);
    }

    /**
     * kafka 公链购买彩票
     * @param lotteryaBuyId
     */
    public void kafkaBuyLottery(Integer lotteryaBuyId){
        if(lotteryaBuyId == null) return;
        LotteryaBuy lotteryaBuy = lotteryaBuyService.selectById(lotteryaBuyId);
        if(lotteryaBuy != null && lotteryaBuy.getId() != null){
            //购买彩票
            LotteryAContractResultEntity lacre = lotteryAContractService.buyLotterA(lotteryaBuy.getLuckNum(),lotteryaBuy.getMultipleNum(),lotteryaBuy.getTotal());
            if(lacre != null){
                lotteryaBuy.setTransferHashValue(lacre.getTransactionHash());
                if(lacre.getStatus() == LotteryAContractResultEntity.STATUS_WAIT){
                    lotteryaBuy.setTransferStatus("1");
                }
                //成功
                if(lacre.getStatus() == LotteryAContractResultEntity.STATUS_SUCCESS){
                    lotteryaBuy.setTransferStatus("1");
                }
                //失败
                if(lacre.getStatus() == LotteryAContractResultEntity.STATUS_FAIL){
                    lotteryaBuy.setTransferStatus("2");
                }
                lotteryaBuyService.updateById(lotteryaBuy);
                if(lacre.getStatus() == LotteryAContractResultEntity.STATUS_SUCCESS){
                    lotteryaBuyService.updateLotteryABuyTransferStatus(lotteryaBuy.getId(),LotteryaBuyServiceImpl.TRANSFER_STATUS_SUCCESS,lacre.getGasUsed());
                }
                if(lacre.getStatus() == LotteryAContractResultEntity.STATUS_FAIL){
                    lotteryaBuyService.updateLotteryABuyTransferStatus(lotteryaBuy.getId(),LotteryaBuyServiceImpl.TRANSFER_STATUS_FAIL,BigDecimal.ZERO);
                }
            }
        }
    }

}
