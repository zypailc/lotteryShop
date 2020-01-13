package com.didu.lotteryshop.base.service;

import com.didu.lotteryshop.common.entity.EthTransfer;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class WalletEthService extends BaseBaseService{

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;


    /**
     * Eth转账
     * @param payPassword
     * @param toAddress
     * @param etherValue
     * @return
     */
    public EthTransfer ethTransferAccounts(String payPassword, String pAddress,String toAddress,BigDecimal etherValue){
        LoginUser loginUser = getLoginUser();
        return ethTransferAccounts( loginUser.getWalletName(),payPassword, pAddress,toAddress,etherValue);
    }

    /**
     * ETh转账
     * @param walletFileName 钱包名称
     * @param payPassword 支付密码
     * @param formAddress 出账地址
     * @param toAddress 入账地址
     * @param etherValue 金额
     * @return
     */
    private EthTransfer ethTransferAccounts(String walletFileName, String payPassword, String formAddress, String toAddress, BigDecimal etherValue){
        try {
            //转账
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("walletFileName",walletFileName);
            map.put("payPassword",payPassword);
            map.put("formAddress",formAddress);//出
            map.put("toAddress",toAddress);//入
            map.put("etherValue",etherValue);
            String reStr = oAuth2RestTemplate.postForObject("http://wallet-service/v1/wallet/transfer", super.getEncryptRequestHttpEntity(map), String.class);
            if (reStr == null || "".equals(reStr)) {
                return null;
            }
            ResultUtil result = super.getDecryptResponseToResultUtil(reStr); //解密
            //判斷是否成功
            if (result != null && result.getCode() != ResultUtil.SUCCESS_CODE) {
                return null;
            }
            Map<String,Object> map1 = (Map<String, Object>) result.getExtend().get(ResultUtil.DATA_KEY);
            EthTransfer ethTransfer = new EthTransfer();
            ethTransfer.setTransactionHashvalue(map1.get(Web3jService.TRANSACTION_HASHVALUE) == null ? "" : map1.get(Web3jService.TRANSACTION_HASHVALUE).toString());
            ethTransfer.setTransactionStatus(map1.get(Web3jService.TRANSACTION_STATUS) == null ? "" : map1.get(Web3jService.TRANSACTION_STATUS).toString());
            ethTransfer.setTransactionGasUsed(map1.get(Web3jService.TRANSACTION_GASUSED) == null ? "" : map1.get(Web3jService.TRANSACTION_GASUSED).toString());
            return  ethTransfer;
        }catch (Exception e){
            e.printStackTrace();
        }
         return null;
    }
}
