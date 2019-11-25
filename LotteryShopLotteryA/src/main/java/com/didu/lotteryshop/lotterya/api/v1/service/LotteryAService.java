package com.didu.lotteryshop.lotterya.api.v1.service;

import cn.hutool.core.convert.Convert;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.enumeration.ResultCode;
import com.didu.lotteryshop.common.service.form.impl.EsEthwalletServiceImpl;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.lotterya.entity.LotteryaBuy;
import com.didu.lotteryshop.lotterya.entity.LotteryaInfo;
import com.didu.lotteryshop.lotterya.entity.LotteryaIssue;
import com.didu.lotteryshop.lotterya.service.LotteryABaseService;
import com.didu.lotteryshop.lotterya.service.Web3jService;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaBuyServiceImpl;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaInfoServiceImpl;
import com.didu.lotteryshop.lotterya.service.form.impl.LotteryaIssueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    private OAuth2RestTemplate oAuth2RestTemplate;
    @Autowired
    private Web3jService web3jService;
    @Autowired
    private LotteryaInfoServiceImpl lotteryaInfoService;
    @Autowired
    private LotteryaIssueServiceImpl lotteryaIssueService;
    @Autowired
    private LotteryaBuyServiceImpl lotteryaBuyService;
    @Autowired
    private EsEthwalletServiceImpl esEthwalletService;

    public  ResultUtil test1(){
        Map<String,Object> map = new HashMap<>();
        map.put("name","CHJ");
        String str =  Convert.toStr(map);
        str = super.getEncryptRequest(str);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("access_token",oAuth2RestTemplate.getAccessToken().getValue());
        HttpEntity<String> strEntity = new HttpEntity<String>(str,headers);
        String reStr = oAuth2RestTemplate.postForObject("http://wallet-service/v1/test/save?access_token="+oAuth2RestTemplate.getAccessToken().getValue(),strEntity,String.class);
       // String reStr  = oAuth2RestTemplate.postForEntity("http://wallet-service/v1/test/save",strEntity,String.class).getBody();
        //String reStr = oAuth2RestTemplate.getForObject("http://wallet-service/v1/test/save",String.class,map);
        ResultUtil result = super.getDecryptRequestToResultUtil(reStr);
        if(result != null){

        }
//        String reStr = oAuth2RestTemplate.postForObject("http://wallet-service/v1/test/test1",str,String.class);
//        ResultUtil result = super.getDecryptRequestToResultUtil(reStr);
//        if(result != null){
//
//        }
        return ResultUtil.successJson("成功");
    }
    public  ResultUtil test(){
        Map<String,Object> map = new HashMap<>();
        map.put("userId",getLoginUser().getId());
        map.put("paymentCode","123");
       // String str =  Convert.toStr(map);
        JSONObject jObject = new JSONObject(map);
        String str = jObject.toString();
        str = super.getEncryptRequest(str);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
       // headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> strEntity = new HttpEntity<String>(str,headers);
        //生成钱包
        String reStr =   oAuth2RestTemplate.postForObject("http://wallet-service/v1/wallet/generate",strEntity,String.class);
        ResultUtil result = super.getDecryptRequestToResultUtil(reStr);
        if(result != null){

        }
        return ResultUtil.successJson("成功");
    }


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
        //购买状态，0：开启，1：关闭
        rMap.put("buyStatus",lotteryaIssue.getBuyStatus());
        //开奖时间
        rMap.put("endTime",lotteryaIssue.getEndTime());
        //开奖间隔时间
        return ResultUtil.successJson(rMap);
    }

    /**
     * eth购买彩票
     * @param luckNum 幸运号码
     * @param multipleNumber 倍数
     * @param  payPasswod 支付密码
     * @return
     */
    public ResultUtil ethBuyLottery(String luckNum,Integer multipleNumber,String payPasswod){
        LoginUser loginUser = super.getLoginUser();
        String walletFileName = loginUser.getWalletName();
        String formAddress = loginUser.getpAddress();
        String memberId  = loginUser.getId();
        String toAddress = web3jService.getManagerAddress();
        LotteryaInfo lotteryaInfo = lotteryaInfoService.findLotteryaInfo();
        BigDecimal eValue = lotteryaInfo.getPrice().multiply(BigDecimal.valueOf(multipleNumber));
        if(!esEthwalletService.judgeBalance(memberId,eValue)){
            //账户余额不足，请先充值！
            return ResultUtil.errorJson("Account balance is insufficient, please recharge first!");
        }





        Map<String,Object> map = new HashMap<>();
        map.put("walletFileName",walletFileName);
        map.put("payPassword",payPasswod);
        map.put("formAddress",formAddress);
        map.put("toAddress",toAddress);
        map.put("etherValue",eValue.toPlainString());
        String str =  Convert.toStr(map);
        str = super.getEncryptRequest(str);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
        HttpEntity<String> strEntity = new HttpEntity<String>(str,headers);
        //ETH 转账
        String reStr =   oAuth2RestTemplate.postForObject("http://wallet-service/v1/wallet/transfer",strEntity,String.class);
        ResultUtil result = super.getDecryptRequestToResultUtil(reStr);
        if(result != null){
            //成功
            if(result.getCode() == ResultCode.SUCCESS.getCode()){
                //存入购买记录
                LotteryaBuy lotteryaBuy = new LotteryaBuy();
                if(result.getExtend() != null && !result.getExtend().isEmpty()){
                    lotteryaBuy.setMemberId(memberId);
                    lotteryaBuy.setLuckNum(luckNum);
                    //lotteryaBuy.setSecondNum(secondNum);
                    //lotteryaBuy.setThirdlyNum(thirdlyNum);
                    lotteryaBuy.setTotal(eValue);
                    lotteryaBuy.setTransferHashValue(result.getExtend().get("transactionHashValue").toString());
                    lotteryaBuy.setTransferStatus(result.getExtend().get("transactionStatus").toString());
                    lotteryaBuy.setTransferStatusTime(new Date());
                    lotteryaBuy.setCreateTime(new Date());
                }
                boolean bool =  lotteryaBuyService.insert(lotteryaBuy);
                if(bool){
                    //TODO 存入转账记录

                }

            }else{
                //失败
                return ResultUtil.errorJson(result.getMsg());
            }
        }
        //执行错误，请联系管理员！
        return ResultUtil.errorJson("Execution error, please contact administrator!");
    }

}
