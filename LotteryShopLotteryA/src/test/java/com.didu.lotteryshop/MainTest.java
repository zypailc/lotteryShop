package com.didu.lotteryshop;

import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTest {
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("^[0-9]{3}$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    @Test
    public void testx(){
       /* String str = "092111";
       // System.out.println(this.isNumeric(str));
        BigDecimal x = BigDecimal.ZERO;
        System.out.println(x.divide(new BigDecimal("100")));*/

        try {
            Credentials ALICE = WalletUtils.loadBip39Credentials("123456","CHJ");
            //String fileName = WalletUtils.generateNewWalletFile(payPassword, new File(walletFilePath), false);
//           String fileName = "UTC--2019-12-23T07-11-33.648000000Z--9338f115c54a2d9ac096c4ae3f6aefd01f8a0b20.json";
//           Credentials ALICE = WalletUtils.loadCredentials(payPassword, walletFilePath+fileName);
            String fileName = "UTC--2019-12-23T07-24-35.439000000Z--f1fc36114fce73cd10504aad836764a5c1991007.json";
            // Credentials ALICE = WalletUtils.loadCredentials(payPassword, walletFilePath+fileName);
            System.out.println("fileName:"+fileName); //钱包文件名称
            System.out.println(ALICE.getAddress());//钱包地址
            System.out.println(ALICE.getEcKeyPair().getPrivateKey());//私钥
            System.out.println(ALICE.getEcKeyPair().getPublicKey());//公钥
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
