package com.didu.lotteryshop;

import org.junit.Test;
import org.web3j.crypto.*;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class WalletTest {
    private String walletFilePath = "E:/IDEAWorkspace/LotteryShop2/LotteryShopLotteryA/src/test/pictures/";
    @Test
    public void test1(){
       try{
           String payPassword = "123456";
//           Bip39Wallet bip39Wallet = WalletUtils.generateBip39WalletFromMnemonic(payPassword,"CHJ",new File(walletFilePath));
//           String fileName = bip39Wallet.getFilename();
//           String mnemonic = bip39Wallet.getMnemonic();
          Credentials ALICE = WalletUtils.loadBip39Credentials(payPassword,"CHJ");
           //String fileName = WalletUtils.generateNewWalletFile(payPassword, new File(walletFilePath), false);
//           String fileName = "UTC--2019-12-23T07-11-33.648000000Z--9338f115c54a2d9ac096c4ae3f6aefd01f8a0b20.json";
//           Credentials ALICE = WalletUtils.loadCredentials(payPassword, walletFilePath+fileName);
           String fileName = "UTC--2019-12-23T07-24-35.439000000Z--f1fc36114fce73cd10504aad836764a5c1991007.json";
          // Credentials ALICE = WalletUtils.loadCredentials(payPassword, walletFilePath+fileName);
           System.out.println("fileName:"+fileName); //钱包文件名称
           System.out.println(ALICE.getAddress());//钱包地址
           System.out.println(ALICE.getEcKeyPair().getPrivateKey());//私钥
           System.out.println(ALICE.getEcKeyPair().getPublicKey());//公钥
//           fileName:UTC--2019-12-23T07-11-33.648000000Z--9338f115c54a2d9ac096c4ae3f6aefd01f8a0b20.json
//           0x9338f115c54a2d9ac096c4ae3f6aefd01f8a0b20
//           69320492614987622331338641031043215462904799588623289808487778245139660199669
//           738245263450896627038440292429773446310632057456403677011803647036105875735641080103873295069407739767476830696913775485315737506008641393855125993257479
           Credentials credentials1 = Credentials.create(ALICE.getEcKeyPair().getPrivateKey().toString());
           System.out.println(credentials1.getAddress());//钱包地址
           System.out.println(credentials1.getEcKeyPair().getPrivateKey());//私钥
           System.out.println(credentials1.getEcKeyPair().getPublicKey());//公钥


           //fileName:UTC--2019-12-23T07-24-35.439000000Z--f1fc36114fce73cd10504aad836764a5c1991007.json
           //0xf1fc36114fce73cd10504aad836764a5c1991007
           //38516750307555273739535194895220521621379365912925188378011841005225406631001
           //2163580987962148168927696429048663713649263121004918063038405380398421337884354340079727812878124664589257097283381757814431681165127062969754487747955181
           //0xa64eee38e20e880ae6f10cb5db781607d5e86c82
           //114721756843780183575327988594947785437701892855805541921357534321041431209134877595456966657
           //1401662768610138559998862503372874335575026144165110522687956484195430402134276354192579490752210739491418077458767747755200851687228104224177923081713155
        }catch (Exception e){
           e.printStackTrace();
        }
    }
}
