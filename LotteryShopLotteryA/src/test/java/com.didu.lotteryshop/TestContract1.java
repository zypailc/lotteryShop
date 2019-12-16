package com.didu.lotteryshop;

import com.didu.lotteryshop.common.utils.Web3jUtils;
import com.didu.lotteryshop.lotterya.service.Web3jService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestContract1 {
    public static BigInteger gasPrice = new BigInteger("22000000000");
//    @Autowired
//    private Web3jService web3jService;
    public static void main(String[] args) {
        try {
            Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545/"));
            //查询所有账户余额
            fianAccountsBalance(web3j);
            String outPerKey = "0x11c2b099d0b14e6ee7364b3824deb39064e43f9491946ff3a0d9d41aa473fa0e";
            String outAddress = "0x1bAED5B2c1cB35cC8D14cE634703223dDeecf1Ab";
            //管理员账号
            //String toAddress = "0x3fF8eEC1b063E09Eeb8d4e13d2dbD5728325FDFB";
            //1CHJ
           //String toAddress = "0x62fe3dd9d1ba47d9e0816bf68d224cc0677df4b5";
            //CHJ1
            //String toAddress = "0x5ab04ba376756bd3d24cb3a433a4d59f0d2565a2";
            //CHJ2
            String toAddress = "0xb7d818d2fd6aa70a960a6bf00653c52cda7d9bf0";

//            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(outAddress, DefaultBlockParameterName.LATEST).send();
//            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
//            RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(
//                    nonce, BigInteger.valueOf(22000000000L),
//                    BigInteger.valueOf(4300000L),
//                    toAddress,
//                    Convert.toWei("90", Convert.Unit.ETHER).toBigInteger());
//
//            Credentials credentials = Credentials.create(outPerKey);
//            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//            String hexValue = Numeric.toHexString(signedMessage);
//            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
//            String transactionHashValue = ethSendTransaction.getTransactionHash();
//            EthGetTransactionReceipt transactionReceipt =  web3j.ethGetTransactionReceipt(transactionHashValue).send();
//            //是否确认状态，0为确认，1已确认
//
//            if (transactionReceipt.getTransactionReceipt().isPresent()) {
//               String xx =  transactionReceipt.getTransactionReceipt().get().getStatus();
//                //实际确认产生的gas费用
//                System.out.println("转账燃气费："+ Web3jUtils.bigIntegerToBigDecimal(gasPrice.multiply(transactionReceipt.getTransactionReceipt().get().getGasUsed())).toPlainString());
//            }
            //账户余额
            EthGetBalance ethGetBalance2 = web3j.ethGetBalance(toAddress, DefaultBlockParameter.valueOf("latest")).send();
            System.out.println("账户余额余额："+bigIntegerToBigDecimal(ethGetBalance2.getBalance()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * eth转换工具
     * @param bigInteger
     * @return
     */
    public static BigDecimal bigIntegerToBigDecimal(BigInteger bigInteger){
        BigDecimal integerToDecimalBalance = new BigDecimal(bigInteger.toString());
        BigDecimal bigDecimal = Convert.fromWei(integerToDecimalBalance,Convert.Unit.ETHER);
        return bigDecimal;
    }

    //查询所有账户余额
    public static void fianAccountsBalance(Web3j web3j){
        try{
            Map<String, BigDecimal> printMap = new HashMap<>();
            EthAccounts ethAccounts = web3j.ethAccounts().send();
            List<String> accountsList =  ethAccounts.getAccounts();
            for (String account : accountsList){
                EthGetBalance ethGetBalance1 = web3j.ethGetBalance(account, DefaultBlockParameter.valueOf("latest")).send();
                //eth默认会部18个0这里处理比较随意，大家可以随便处理哈
                printMap.put(account,bigIntegerToBigDecimal(ethGetBalance1.getBalance()));
            }
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("《==================  所有账户余额.start  ==========================》");
            if(printMap != null && !printMap.isEmpty()){
                for(Map.Entry<String,BigDecimal> entry : printMap.entrySet()){
                    System.out.println(entry.getKey()+":"+entry.getValue());
                }
            }
            System.out.println("《==================  所有账户余额.end  ==========================》");
            System.out.println();
            System.out.println();
            System.out.println();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
