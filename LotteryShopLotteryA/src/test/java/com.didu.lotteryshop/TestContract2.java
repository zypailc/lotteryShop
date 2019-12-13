package com.didu.lotteryshop;

import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.lotterya.contract.LotteryAContract;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  @Test
  public void showContractData(){
      try {
          Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545/"));
          Credentials credentials = Credentials.create("3E5E0BC6DA93639AA9FA5C1E36091E552404F20A5D6F410788FA8B5CCBFF8E7F");
          String contractAddress = "0x4678ce017543f99801dd0f67c1a67e7c4792086d";
          DefaultGasProvider defaultGasProvider = new DefaultGasProvider();
          LotteryAContract lotteryAContract = LotteryAContract.load(contractAddress,web3j,credentials,defaultGasProvider);
          String luckNum = lotteryAContract.ShowLuckNum().send();
          System.out.println(luckNum);
          List<String> list = this.getBuyLuckNumber(lotteryAContract);
          List<Map<String,String>> xx =   this.getBuyMapping(lotteryAContract,"123");

      } catch (Exception e) {
          e.printStackTrace();
      }
  }

    /**
     * 获取购买者数据
     * @param lotteryAContract
     * @return
     * @throws Exception
     */
    public List<String> getBuyLuckNumber(LotteryAContract lotteryAContract) throws Exception{
        List<String> buyLuckNumberList = new ArrayList<>();
       // LotteryAContract lotteryAContract = LotteryAContract.load(contractAddress);
        boolean bool = true;
        int index = 1;
        do {
            try{
                String buyLuckNumber = lotteryAContract.getBuyLuckNumberArray(BigInteger.valueOf(index)).send();
                if(buyLuckNumber != null){
                    if(!"".equals(buyLuckNumber)) {
                        buyLuckNumberList.add(buyLuckNumber);
                        index++;
                        continue;
                    }
                }
                bool = false;
            } catch (Exception e) {
                bool = false;
                throw new Exception(" Method 'getBuyLuckNumber' Exception："+e.getMessage());
            }
        }while (bool);
        return buyLuckNumberList;
    }

    /**
     * 查询中奖数据
     * @param lotteryAContract
     * @return
     * @throws Exception
     */
    public List<Map<String,String>> getLuckMember(LotteryAContract lotteryAContract) throws Exception{
        List<Map<String,String>> luckMemberList = new ArrayList<>();
        Map<String,String> luckMemberMap = new HashMap<>();
      //  LotteryAContract lotteryAContract = web3jService.loadManagerContract(lotteryaInfoService.findLotteryaInfo().getContractAddress());
        boolean bool = true;
        int index = 0;
        do {
            try{
                Tuple2<String, BigInteger> tuple2Result = lotteryAContract.getBuyerLuckList(BigInteger.valueOf(index)).send();
                if(tuple2Result != null){
                    if(!tuple2Result.component1().equals("0x0000000000000000000000000000000000000000")
                            && !tuple2Result.component2().toString().equals("0")) {
                        luckMemberMap.put("ethAddress", tuple2Result.component1());//eth 钱包地址
                        luckMemberMap.put("multipleNumber", tuple2Result.component2().toString());// 购买彩票倍数
                        luckMemberList.add(luckMemberMap);
                        index++;
                        continue;
                    }
                }
                bool = false;
            } catch (Exception e) {
                bool = false;
                throw new Exception(" Method 'getLuckMember' Exception："+e.getMessage());
            }
        }while (bool);
        return luckMemberList;
    }

    /**
     * 查询彩票号码Mapping映射数据
     * @param lotteryAContract
     * @param buyLuckNum
     * @return
     * @throws Exception
     */
    public List<Map<String,String>> getBuyMapping(LotteryAContract lotteryAContract,String buyLuckNum) throws Exception{
        List<Map<String,String>> luckMemberList = new ArrayList<>();
        Map<String,String> luckMemberMap = new HashMap<>();
        //  LotteryAContract lotteryAContract = web3jService.loadManagerContract(lotteryaInfoService.findLotteryaInfo().getContractAddress());
        boolean bool = true;
        int index = 0;
        do {
            try{
                Tuple2<String, BigInteger> tuple2Result = lotteryAContract.getBuyMapping(buyLuckNum,BigInteger.valueOf(index)).send();
                if(tuple2Result != null){
                    if(!tuple2Result.component1().equals("0x0000000000000000000000000000000000000000")
                            && !tuple2Result.component2().toString().equals("0")) {
                        luckMemberMap.put("ethAddress", tuple2Result.component1());//eth 钱包地址
                        luckMemberMap.put("multipleNumber", tuple2Result.component2().toString());// 购买彩票倍数
                        luckMemberList.add(luckMemberMap);
                        index++;
                        continue;
                    }
                }
                bool = false;
            } catch (Exception e) {
                bool = false;
                throw new Exception(" Method 'getLuckMember' Exception："+e.getMessage());
            }
        }while (bool);
        return luckMemberList;
    }
}
