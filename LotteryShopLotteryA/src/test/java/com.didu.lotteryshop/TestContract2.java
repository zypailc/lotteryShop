package com.didu.lotteryshop;

import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.lotterya.contract.LotteryAContract;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

public class TestContract2 {
    @Test
  public void test(){
    try{
        String xx = AesEncryptUtil.encrypt("3E5E0BC6DA93639AA9FA5C1E36091E552404F20A5D6F410788FA8B5CCBFF8E7F",Constants.AES_ETHMANAGER_PRIVATEKEY);
        System.out.println(xx);
    }catch (Exception e){

    }
  }
@Test
  public void test1(){
      try {
          Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545/"));
          Credentials credentials = Credentials.create("3E5E0BC6DA93639AA9FA5C1E36091E552404F20A5D6F410788FA8B5CCBFF8E7F");
          DefaultGasProvider defaultGasProvider = new DefaultGasProvider();
          LotteryAContract lotteryAContract = LotteryAContract.deploy(web3j,credentials,defaultGasProvider,"0x9c3b669D55C411c5d70d87ce2bA44Cf8476141CA").send();
          String  contractAddress = lotteryAContract.getContractAddress();
         // contractAddress = "0x4678ce017543f99801dd0f67c1a67e7c4792086d";
          System.out.println(contractAddress);

      } catch (Exception e) {
          e.printStackTrace();
      }
  }

}
