package com.didu.lotteryshop.wallet.api.v1.service;

import com.didu.lotteryshop.common.utils.Result;
import com.didu.lotteryshop.wallet.service.GasProviderService;
import com.didu.lotteryshop.wallet.service.Web3jService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;

/**
 * 钱包Service
 * @author CHJ
 * @date 2019-10-23
 */
@Service
public class WalletService {
    /** 生成钱包文件地址*/
    @Value("${ethwallet.filePath}")
    private String walletFilePath;

    @Autowired
    private Web3jService web3jService;
    @Autowired
    private GasProviderService gasProviderService;

    /**
     * 创建钱包
     * @param userId 用户ID
     * @param payPassword 口令
     * @return
     */
    public Result generateWallet(String userId,String payPassword){
        if(StringUtils.isBlank(walletFilePath)){
            //钱包文件地址没有配置，请先配置。
            return Result.errorJson("WalletFilePath not configured!");
        }
        try {
            String fileName = WalletUtils.generateNewWalletFile(payPassword, new File(walletFilePath), false);
            Credentials ALICE = WalletUtils.loadCredentials(payPassword, walletFilePath+fileName);
            // System.out.println("fileName:"+fileName); //钱包文件名称
            //System.out.println(ALICE.getAddress());//钱包地址
            //System.out.println(ALICE.getEcKeyPair().getPrivateKey());//私钥
            //System.out.println(ALICE.getEcKeyPair().getPublicKey());//公钥
            Map<String,Object> reMap = new HashMap<>();
            reMap.put("address",ALICE.getAddress());
            reMap.put("fileName",fileName);
            reMap.put("userId",userId);
            return Result.successJson(reMap);
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        //生成eth钱包错误！
        return Result.errorJson("Error generating  eth wallet!");
    }

    /**
     *  查询钱包余额
     * @param address
     * @return
     */
    public Result findBalance(String address){
        Map<String,Object>  reMap = new HashMap<>();
        //钱包地址
        reMap.put("address",address);
        try {
            BigDecimal  ether = web3jService.getBalanceByEther(address);
            //钱包余额
            reMap.put("ether",ether.toBigInteger());
            return Result.successJson(reMap);
        } catch (IOException e) {
            e.printStackTrace();
            //错误信息
            reMap.put("errorMsg",e.getMessage());
        }
        return Result.errorJson(reMap);
    }

    /**
     *  转账
     * @param walletFileName 钱包名字
     * @param payPassword 支付密码
     * @param formAddress 出账地址
     * @param toAddress  入账地址
     * @param ether ether
     * @return
     */
    public  Result transfer(String walletFileName,String payPassword,String formAddress,String toAddress,String ether){
        Map<String,Object>  reMap = new HashMap<>();
        //交易地址
        reMap.put("formAddress",formAddress);
        try {
            Credentials credentials = WalletUtils.loadCredentials(payPassword, walletFilePath+walletFileName);
            BigDecimal  etherBalance =  web3jService.getBalanceByEther(formAddress);
            BigDecimal allEther = gasProviderService.getAllEther(new BigDecimal(ether));
            if(etherBalance.compareTo(allEther) >= 0){
                String transactionHashValue = web3jService.transfer(credentials,toAddress,ether);
                //转账事务hash值，可用来查看交易是否被确认
                reMap.put("transactionHashValue",transactionHashValue);
                //交易确认状态，0-未确认；1-已确认
                reMap.put("transactionStatus",0);
                Map<String,Object> sMap = web3jService.findTransactionStatus(transactionHashValue);
                if(sMap != null && sMap.isEmpty()){
                    if(sMap.get(Web3jService.TRANSACTION_STATUS).equals("1")){
                        reMap.put("transactionStatus",1);
                        //实际产生得到燃气费
                        reMap.put("transactionCasUsed",sMap.get(Web3jService.TRANSACTION_CASUSED));
                    }
                }
                return Result.successJson(reMap);
            }else{
                //错误信息
                reMap.put("errorMsg","Insufficient Balance!("+allEther.toPlainString()+")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //错误信息
            reMap.put("errorMsg",e.getMessage());
        }
        return Result.errorJson(reMap);
    }

    /**
     * 查询交易状态
     * @param transactionHashValue 转账事务哈希码
     * @return
     */
    public Result findTransactionStatus(String transactionHashValue){
        Map<String,Object>  reMap = new HashMap<>();
        //转账事务哈希码
        reMap.put("transactionHashValue",transactionHashValue);
        try {
            //交易确认状态，0-未确认；1-已确认
            reMap.put("transactionStatus",0);
            Map<String,Object> sMap = web3jService.findTransactionStatus(transactionHashValue);
            if(sMap != null && sMap.isEmpty()){
                if(sMap.get(Web3jService.TRANSACTION_STATUS).equals("1")){
                    reMap.put("transactionStatus",1);
                    //实际产生得到燃气费
                    reMap.put("transactionCasUsed",sMap.get(Web3jService.TRANSACTION_CASUSED));
                }
            }
            return Result.successJson(reMap);
        } catch (IOException e) {
            e.printStackTrace();
            //错误信息
            reMap.put("errorMsg",e.getMessage());
        }
        return Result.errorJson(reMap);
    }
}
