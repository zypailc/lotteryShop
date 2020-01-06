package com.didu.lotteryshop;

import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.Web3jUtils;
import com.didu.lotteryshop.lotterya.contract.LotteryAContract;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestContract2 {
   public static BigInteger gasPrice = new BigInteger("22000000000");

    @Test
  public void test(){
    try{
        //平台币管理账户
         //String key = "70EC14BBA185AAF29938464E8F9042562A8A5AE48EFC3126B0A627984EE7B683";
        //推广账户
         //String key = "933B1C3A14704A82A5B6323CF9FEFE6150DD8BCD740EFBC88427BD0E223E2A37";
        //A彩票管理员账号
        // String key = "3E5E0BC6DA93639AA9FA5C1E36091E552404F20A5D6F410788FA8B5CCBFF8E7F";
        //String xx = AesEncryptUtil.encrypt(key,Constants.AES_ETHMANAGER_PRIVATEKEY);
        String xx = AesEncryptUtil.decrypt("JenwNPZmLgYzldxJSgDHJpP1v3QplY5LK/pIKNqpFfePINMMWK6bW33Iy1n6OpgkJ4RmnYLqyzESQKz7BUHR1U49931HpJWhbkm+FjxIMW62Q5B4QUfJrw4IeQBRJ9Ii", Constants.KEY_THREE);
        //Base64.decode("JenwNPZmLgYzldxJSgDHJpP1v3QplY5LK/pIKNqpFfePINMMWK6bW33Iy1n6OpgkJ4RmnYLqyzESQKz7BUHR1U49931HpJWhbkm+FjxIMW62Q5B4QUfJrw4IeQBRJ9Ii");
        System.out.println("xx:"+xx);
        Credentials credentials = WalletUtils.loadCredentials("123", "E:/IDEAWorkspace/LotteryShop2/LotteryShopETHWallet/pictures/"+"UTC--2019-12-25T02-47-00.612000000Z--2bf4aa7b78516a1259b570706c0ede305d577000.json");
        System.out.println("xx:"+credentials.getEcKeyPair().toString());
        /*56693158387536173117398375050602182503208718321592280486151479167780146663782
        2075841778241809472285607751455914905989529057676159752144929253420061300172126200272922487142362249576698649946874876127875332635244598323361233168698717*/
    }catch (Exception e){
        e.printStackTrace();
    }
  }
@Test
  public void test1(){
      try {
          Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545/"));
          Credentials credentials = Credentials.create("3E5E0BC6DA93639AA9FA5C1E36091E552404F20A5D6F410788FA8B5CCBFF8E7F");
          DefaultGasProvider defaultGasProvider = new DefaultGasProvider();
          EthGetBalance ethGetBalancel1 = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameter.valueOf("latest")).send();
          System.out.println("部署前余额："+Web3jUtils.bigIntegerToBigDecimal(ethGetBalancel1.getBalance()).toPlainString());
          LotteryAContract lotteryAContract = LotteryAContract.deploy(web3j,credentials,defaultGasProvider,"0x9c3b669D55C411c5d70d87ce2bA44Cf8476141CA").send();
          String  contractAddress = lotteryAContract.getContractAddress();
          BigInteger gasUsed = lotteryAContract.getTransactionReceipt().get().getGasUsed();
         // contractAddress = "0x4678ce017543f99801dd0f67c1a67e7c4792086d";0x4678ce017543f99801dd0f67c1a67e7c4792086d
          System.out.println("contractAddress:"+contractAddress);
          System.out.println("gasFee:"+Web3jUtils.bigIntegerToBigDecimal(gasPrice.multiply(gasUsed)));
          EthGetBalance ethGetBalancel2 = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameter.valueOf("latest")).send();
          System.out.println("部署后余额："+Web3jUtils.bigIntegerToBigDecimal(ethGetBalancel2.getBalance()).toPlainString());
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
@Test
  public void test3(){
      try {
          Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545/"));
          Credentials credentials = Credentials.create("3E5E0BC6DA93639AA9FA5C1E36091E552404F20A5D6F410788FA8B5CCBFF8E7F");
          String contractAddress = "0x4678ce017543f99801dd0f67c1a67e7c4792086d";
          DefaultGasProvider defaultGasProvider = new DefaultGasProvider();
          LotteryAContract lotteryAContract = LotteryAContract.load(contractAddress,web3j,credentials,defaultGasProvider);
          TransactionReceipt transactionReceipt = lotteryAContract.ContractBalanceIn(Convert.toWei("10", Convert.Unit.ETHER).toBigInteger()).send();BigInteger gasUsed = transactionReceipt.getGasUsed();
          System.out.println("充值合约gasFee:"+Web3jUtils.bigIntegerToBigDecimal(gasPrice.multiply(gasUsed)));
          System.out.println("合约余额："+Web3jUtils.bigIntegerToBigDecimal(lotteryAContract.ShowContractBalance().send()));
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  @Test
      public void showContractData(){
      try {
          //19.95225054
          Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545/"));
          Credentials credentials = Credentials.create("3E5E0BC6DA93639AA9FA5C1E36091E552404F20A5D6F410788FA8B5CCBFF8E7F");
          String contractAddress = "0xd46c4b75ac051c90231ffa5bccfe9f390397149d";
          DefaultGasProvider defaultGasProvider = new DefaultGasProvider();
          LotteryAContract lotteryAContract = LotteryAContract.load(contractAddress,web3j,credentials,defaultGasProvider);
          String luckNum = lotteryAContract.ShowLuckNum().send();
          System.out.println(luckNum);
          List<String> buyLuckNumberList = this.getBuyLuckNumber(lotteryAContract);
          String buyLuckNumberStr = "";
          for(String buyLuckNum : buyLuckNumberList){
            if("".equals(buyLuckNumberStr))
                buyLuckNumberStr += buyLuckNum;
              buyLuckNumberStr += ","+buyLuckNum;
          }
          System.out.println("buyLuckNumberList["+buyLuckNumberList+"]");
          List<Map<String,String>> buyMappingList =   this.getBuyMapping(lotteryAContract,"123");
          for(Map<String,String> bm : buyMappingList){
              for(Map.Entry<String, String> m : bm.entrySet()){
                System.out.println("key:"+m.getKey()+";value:"+m.getValue());
              }
          }
          EthGetBalance ethGetBalancel2 = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameter.valueOf("latest")).send();
           System.out.println("CHJ1余额："+Web3jUtils.bigIntegerToBigDecimal(ethGetBalancel2.getBalance()).toPlainString());

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
