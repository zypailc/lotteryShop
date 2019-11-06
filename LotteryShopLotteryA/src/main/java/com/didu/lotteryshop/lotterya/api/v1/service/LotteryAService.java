package com.didu.lotteryshop.lotterya.api.v1.service;

import cn.hutool.core.convert.Convert;
import com.didu.lotteryshop.common.base.service.BaseService;
import com.didu.lotteryshop.common.enumeration.ResultCode;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.lotterya.service.Web3jService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 **
  * A彩票 Service
  * @author CHJ
  * @date 2019-11-01 14:09
  */
@Service
public class LotteryAService extends BaseService {
    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;
    @Autowired
    private Web3jService web3jService;

    /**
     * 获取彩票信息
     * @return
     */
    public ResultUtil getLotteryInfo(){
        Map<String,String> rMap = new HashMap<>();
        //彩票单价
        rMap.put("price",web3jService.getLotterAPrice());
        //当前期数
        //当前状态
        //彩票名称
        //开奖时间
        return ResultUtil.successJson(rMap);
    }

    /**
     * 购买a彩票
     * @param luckNum 幸运号码
     * @param payPasswod 支付密码
     * @return
     */
    public ResultUtil ethBuyLottery(String luckNum,String payPasswod){
        //step 1 TODO 获取当前用户
        String walletFileName = "";
        String formAddress = "";
        String toAddress = web3jService.getManagerAddress();
        String etherValue = web3jService.getLotterAPrice();
        Map<String,Object> map = new HashMap<>();
        map.put("walletFileName",walletFileName);
        map.put("payPassword",payPasswod);
        map.put("formAddress",formAddress);
        map.put("toAddress",toAddress);
        map.put("etherValue",etherValue);
        String str =  Convert.toStr(map);
        str = super.getEncryptRequest(str);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
        HttpEntity<String> strEntity = new HttpEntity<String>(str,headers);
        String reStr =   oAuth2RestTemplate.postForObject("http://wallet-service/v1/wallet/transfer",strEntity,String.class);
        ResultUtil result = super.getDecryptRequestToResultUtil(reStr);
        if(result != null){
            //成功
            if(result.getCode() == ResultCode.SUCCESS.getCode()){
                //step 3 TODO 购买记录，进行转账
                //step 4 TODO 修改金额，冻结金额
            }else{
             //失败
                return ResultUtil.errorJson(result.getMsg());
            }
        }
        return ResultUtil.errorJson("Execution error, please contact administrator!");
    }
}
