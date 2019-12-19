package com.didu.lotteryshop.base.api.v1.service;

import com.didu.lotteryshop.base.service.BaseBaseService;
import com.didu.lotteryshop.common.entity.EsEthaccounts;
import com.didu.lotteryshop.common.entity.EsEthwallet;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.service.form.impl.EsDlbaccountsServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.EsEthaccountsServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.EsEthwalletServiceImpl;
import com.didu.lotteryshop.common.utils.BigDecimalUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * eth钱包Service
 * @author CHJ
 * @date 2019-12-10
 */
@Service
public class EthWalletService extends BaseBaseService {
    @Autowired
    private EsEthwalletServiceImpl esEthwalletService;
    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;
    @Autowired
    private EsEthaccountsServiceImpl esEthaccountsService;

    /**
     * 查询eth钱包
     * @return
     */
   public ResultUtil findEthwallet(){
       LoginUser loginUser = super.getLoginUser();
       EsEthwallet esEthwallet = esEthwalletService.findByMemberId(loginUser.getId());
       if(esEthwallet != null){
           try{
               /*
                查询公链上余和本地eth钱包余额碎是一致，
                不一致的话，增加一条充值记录
                */
               Map<String,Object> map = new HashMap<String,Object>();
               map.put("address",loginUser.getPAddress());
               String reStr =   oAuth2RestTemplate.postForObject("http://wallet-service/v1/wallet/findBalance",super.getEncryptRequestHttpEntity(map),String.class);
               ResultUtil result = super.getDecryptResponseToResultUtil(reStr); //解密
               //保存
               if(result != null && result.getCode() == ResultUtil.SUCCESS_CODE){
                   Map<String,Object> resultMap = (Map<String,Object>) result.getExtend().get(ResultUtil.DATA_KEY);
                   if(resultMap != null && !resultMap.isEmpty() && resultMap.get("ether") != null){
                      BigDecimal cTotal = new BigDecimal(resultMap.get("ether").toString());
                       cTotal = cTotal.setScale(10,BigDecimal.ROUND_DOWN);
                      if(cTotal != null && cTotal.compareTo(esEthwallet.getTotal()) > 0){
                            BigDecimal amount =  cTotal.subtract(esEthwallet.getTotal());
                            boolean bool = esEthaccountsService.addInSuccess(loginUser.getId(),EsEthaccountsServiceImpl.DIC_TYPE_IN,amount,"-1");
                            if(bool)
                                esEthwallet = esEthwalletService.findByMemberId(loginUser.getId());

                      }
                   }
               }
           }catch (Exception e){
               e.printStackTrace();
           }
       }
       //精度设置
       if(esEthwallet == null){
           esEthwallet = new EsEthwallet();
           esEthwallet.setTotal(new BigDecimal("0"));
           esEthwallet.setBalance(new BigDecimal("0"));
           esEthwallet.setFreeze(new BigDecimal("0"));
       }else {
           esEthwallet.setTotal(BigDecimalUtil.bigDecimalToPrecision( esEthwallet.getTotal()));
           esEthwallet.setBalance(BigDecimalUtil.bigDecimalToPrecision(esEthwallet.getBalance()));
           esEthwallet.setFreeze(BigDecimalUtil.bigDecimalToPrecision(esEthwallet.getFreeze()));
       }

       return ResultUtil.successJson(esEthwallet) ;
   }

    /**
     * 查詢ETH流水記錄
     * @param currentPage
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param status
     * @return
     */
   public ResultUtil findEthRecord(Integer currentPage,Integer pageSize,String startTime,String endTime,String status){
        LoginUser loginUser = getLoginUser();
        return  ResultUtil.successJson(esEthaccountsService.findEthRecordPagination(loginUser.getId(),currentPage,pageSize,startTime,endTime,status));
   }


}
