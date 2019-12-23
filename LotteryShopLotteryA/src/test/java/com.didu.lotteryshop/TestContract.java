package com.didu.lotteryshop;

import com.didu.lotteryshop.lotterya.contract.LotteryAContract;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TestContract {
    @Test
    public void  mainxxx() {
        try {
            Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545/"));
            //查询所有账户余额
            fianAccountsBalance(web3j);
            //部署智能合约
            String managerAddress = "0x0B723f261cdd874Bd2dF700a95d01E907c245EF0";
            String managerPrivateKey = "0x49d5ad76622376539a9cd9aa00b3b154ae55bc5aeb998ea2c6f1e8b74d2dd364";

            //EthGetBalance ethGetBalance1 = web3j.ethGetBalance(managerAddress, DefaultBlockParameter.valueOf("latest")).send();
            //System.out.println("部署前账户余额："+bigIntegerToBigDecimal(ethGetBalance1.getBalance()));
           // System.out.println("gas书续费："+bigIntegerToBigDecimal(defaultGasProvider.getGasPrice().multiply(defaultGasProvider.getGasLimit())));*/
            //部署逻辑合约
            DefaultGasProvider defaultGasProvider = new DefaultGasProvider();
            Credentials credentials = Credentials.create(managerPrivateKey);
            //调节基金账户地址
            String adjustFundAdr = "0x5aA5883FEaFE021d716b120B98350082010CaA21";
            //奖金池账户地址
            String bonusPoolAdr = "0x058F39b60A6e17784c0A89b0EDF509Bf5377FE44";
            LotteryAContract lotteryAContractDeploy  =  LotteryAContract.deploy(web3j,credentials,defaultGasProvider,adjustFundAdr).send();

          //EthGetBalance ethGetBalance2 = web3j.ethGetBalance(managerAddress, DefaultBlockParameter.valueOf("latest")).send();
           //System.out.println("部署后账户余额："+bigIntegerToBigDecimal(ethGetBalance2.getBalance()));
            BigInteger lotteryPrice = Convert.toWei("0.1", Convert.Unit.ETHER).toBigInteger();
            lotteryAContractDeploy.updatePrice(lotteryPrice).send();
            System.out.println("ShowPrice:"+ lotteryAContractDeploy.ShowPrice().send());
            //合约账户地址
           String contractAddress = lotteryAContractDeploy.getContractAddress();
            //String contractAddress = "0x3aad3fef3a4071360e39a6a76f83659baf935b3b";
            System.out.println("contractAddress:"+contractAddress);
            //11618217269991821615218593357079731003858423955559408825389419986837277478149220225557283957473406432356002690773835466465284860131754276005873533539057535
            //37870671883850778949080478822205071849234530571768223490210874449181176623464
            //37870671883850778949080478822205071849234530571768223490210874449181176623464
            //37870671883850778949080478822205071849234530571768223490210874449181176623464
           Credentials credentials1 = Credentials.create("37870671883850778949080478822205071849234530571768223490210874449181176623464");
            //Credentials credentials2 = new Credentials();
//            ECKeyPair ecKeyPair = new ECKeyPair();
//            Credentials.create();
            //Credentials credentials1 = Credentials.create("37870671883850778949080478822205071849234530571768223490210874449181176623464","11618217269991821615218593357079731003858423955559408825389419986837277478149220225557283957473406432356002690773835466465284860131754276005873533539057535");
            LotteryAContract lotteryAContractLoad = LotteryAContract.load(contractAddress, web3j, credentials1, defaultGasProvider);

           String buyNum = randomLuckNum(3);
            BigInteger BuyValue = Convert.toWei("0.1", Convert.Unit.ETHER).toBigInteger();
            TransactionReceipt transactionReceipt = lotteryAContractLoad.BuyLottery(buyNum,BigInteger.valueOf(1), BuyValue).sendAsync().get();
            System.out.println("xxxxxxxx+++:"+transactionReceipt.getStatus());
//            List<LotteryAContract.BuyLotteryEventEventResponse> buyLotteryEventResponse =  lotteryAContractLoad.getBuyLotteryEventEvents(transactionReceipt);
//            if(buyLotteryEventResponse != null && buyLotteryEventResponse.size() > 0){
//                LotteryAContract.BuyLotteryEventEventResponse buyLotteryEventResponse1 = buyLotteryEventResponse.get(0);
//                System.out.println(
//                        "[address:"+buyLotteryEventResponse1.buyer
//                        +";lunckNum:"+buyLotteryEventResponse1.buyLuckNumber
//                        +",money:"+bigIntegerToBigDecimal(buyLotteryEventResponse1.money)
//                        +",multipleNumber:"+buyLotteryEventResponse1.multipleNumber+"]");
//            }
//            List<LotteryAContract.ContractBalanceOutEventEventResponse> buyContractBalanceOutEventEventResponse =  lotteryAContractLoad.getContractBalanceOutEventEvents(transactionReceipt);
//            if(buyContractBalanceOutEventEventResponse != null && buyContractBalanceOutEventEventResponse.size() > 0){
//                LotteryAContract.ContractBalanceOutEventEventResponse buyContractBalanceOut = buyContractBalanceOutEventEventResponse.get(0);
//                System.out.println(
//                        "[address:"+buyContractBalanceOut.winner
//                        +",money:"+bigIntegerToBigDecimal(buyContractBalanceOut.money) +"]");
//            }

            //查询合约账户余额
            findContractBalance(lotteryAContractDeploy);
            //查询所有账户余额
            fianAccountsBalance(web3j);
           /* System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("合约地址："+contractAddress);
            System.out.println();
            System.out.println();
            System.out.println();
            findContractMoney(lotteryShop);

            *//* System.out.println("合约地址2："+lotteryShop.ShowContractAdr().send());
            System.out.println("管理员地址："+lotteryShop.ShowManageAdr().send());*//*

                 *//*
            Credentials credentials2 = Credentials.create("0xb294ab3fe17554a791b38e00b99b101a34f432f95cac008e42898f7a1b21d1d4");
            LotteryShop lotteryShop2 = LotteryShop.load(contractAddress, web3j, credentials2, defaultGasProvider);
            findManageMoney(lotteryShop);  //显示管理员账号余额
            System.out.println("其他账号余额："+bigIntegerToBigDecimal(lotteryShop2.ShowInvokerBalance().send()));*//*
           // findDataContractMoney(dataContract);//查询数据合约账号余额
            findContractMoney(lotteryShop); //查询逻辑合约账户余额
            //购买彩票
            buyCaiPiao(web3j,contractAddress,defaultGasProvider);
            //查询所有购买彩票用户列表
            // findAllUsrAddress(lotteryShop);

            //查询数据合约账号余额
            //findDataContractMoney(dataContract);
            //查询合约账户余额
            findContractMoney(lotteryShop);

            //查询所有账户余额
            fianAccountsBalance(web3j);
            //开奖
            findKaiJian(lotteryShop);
            //销毁合约，合约中的eth发送给管理员账户
           // lotteryShop.kill().send();

            //查询数据合约账号余额
            //findDataContractMoney(dataContract);
            //查询合约账户余额
            findContractMoney(lotteryShop);

            //查询所有账户余额
            fianAccountsBalance(web3j);
            //重置彩票
            lotteryShop.resetData().send();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询合约余额
    public  void findContractBalance(LotteryAContract lotteryAContractDeploy){
        try{
            BigInteger bl = lotteryAContractDeploy.ShowContractBalance().send();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("合约账户余额："+bigIntegerToBigDecimal(bl));
            System.out.println();
            System.out.println();
            System.out.println();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
//        public  void findKaiJian(LotteryShop lotteryShop){
//            try{
//                BigInteger initValue2 = Convert.toWei("20.0", Convert.Unit.ETHER).toBigInteger();
//                // TransactionReceipt transactionReceipt = lotteryShop.KaiJiang(initValue2).send();
//                TransactionReceipt transactionReceipt = lotteryShop.KaiJiang().send();
//                List<LotteryShop.DrawLotteryEventResponse> drawLotteryEventResponses = lotteryShop.getDrawLotteryEvents(transactionReceipt);
//                if(drawLotteryEventResponses != null && drawLotteryEventResponses.size() > 0){
//                    // LotteryShop.DrawLotteryEventResponse drawLotteryEventResponse = drawLotteryEventResponses.get(0);
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                    for(LotteryShop.DrawLotteryEventResponse drawLotteryEventResponse : drawLotteryEventResponses){
//                        System.out.println("<====================[中奖账户]========================>");
//                        System.out.println("<====================["+ drawLotteryEventResponse.winner+"]========================>");
//                        System.out.println("<====================["+ drawLotteryEventResponse.luckNum+"]========================>");
//                        System.out.println("<====================["+ bigIntegerToBigDecimal(drawLotteryEventResponse.money)+"]========================>");
//                        System.out.println("<====================[中奖账户]========================>");
//                    }
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                }else{
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                    System.out.println("没有用户中奖！");
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
    //获取中奖玩家地址
//        public  void getBuyerLuckList(LotteryAContract lotteryAContractDeploy){
//            try{
//                //查询购买列表
//                List testList =  lotteryAContractDeploy.;
//                System.out.println();
//                System.out.println();
//                System.out.println();
//                System.out.println("购买彩票用用户地址："+testList);
//                System.out.println();
//                System.out.println();
//                System.out.println();
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//        }

//        public void buyCaiPiao(Web3j web3j,String contractAddress,DefaultGasProvider defaultGasProvider){
//            try{
//           /* EthAccounts ethAccounts = web3j.ethAccounts().send();
//            List<String> accountsList =  ethAccounts.getAccounts();*/
//                Integer num = 0;
//                String[] accountsArra = new String[]{
//                        /*"0x0c9e76bd63ffc85ddee6f10c1cd1c32f7e89901fb29df94dfe5b950431022de1",*/
//                        "0x9132cf46d4dd03c8d82660f891714b0ccf9e84bb5d47a833b71d6e4a2e53254f",
//                        "0xd4493580750c1f698c732f9862400d76cf157fa1275b1a5d3a31502a35651e70",
//                        "0xa3b7a0617b0adb32022f57abf6d21352bc42a152d96c2c9d3456d58ad5569852",
//                        "0xeb8ce2c209cb3abbefd283b0ee0cf5c8ea1ca1d22e675c5b4aff6b01ff396f07",
//                        "0x38fc2e72a983cbe78513b08b111d3eec3b8d10c0da2ae29a888b3e4a0fd4bb11",
//                        "0x093829b1eeb8b32fbd6c805ce858ee79348cc6b671ee3562b623a635986f5e98",
//                        "0x01fbed2019e144a7b183cfea13b1843d910945cb56ce0eaa377206990d319e7b",
//                        "0x91c606828017c08fc9fe56e6bbce4492d90caceed9cbb20487255d8bd5f58ca7",
//                        "0x01f0cb0d60c99cd7656769e0533cf63cb783d80b0e61b95ce560482481bd24da",
//                };
//                Map<String,String> printMap = new HashMap<>();
//                for (String account : accountsArra) {
//                    Credentials credentials2 = Credentials.create(account);
//                    LotteryShop lotteryShop2 = LotteryShop.load(contractAddress, web3j, credentials2, defaultGasProvider);
//                    // LotteryShop.load(contractAddress,web3j,transactionManager,defaultGasProvider);
//                    BigInteger buyNum = new BigInteger((num++).toString());
//                    BigInteger BuyValue = Convert.toWei("1.0", Convert.Unit.ETHER).toBigInteger();
//                    TransactionReceipt transactionReceipt = lotteryShop2.BuyCaiPiao(buyNum, BuyValue).send();
//                    List<LotteryShop.BuyLotteryEventResponse> buyLotteryEventResponse =  lotteryShop2.getBuyLotteryEvents(transactionReceipt);
//                    if(buyLotteryEventResponse != null && buyLotteryEventResponse.size() > 0){
//                        LotteryShop.BuyLotteryEventResponse buyLotteryEventResponse1 = buyLotteryEventResponse.get(0);
//                        printMap.put(buyLotteryEventResponse1.buyer,"[lunckNum:"+buyLotteryEventResponse1.luckNum+",money:"+bigIntegerToBigDecimal(buyLotteryEventResponse1.money)+"]");
//                    }
//                }
//                System.out.println();
//                System.out.println();
//                System.out.println();
//                System.out.println("《==================  所有账户购买彩票.start  ==========================》");
//                if(printMap != null && !printMap.isEmpty()){
//                    for(Map.Entry<String,String> entry : printMap.entrySet()){
//                        System.out.println(entry.getKey()+":"+entry.getValue());
//                    }
//                }
//                System.out.println("《==================  所有账户购买彩票.end  ==========================》");
//                System.out.println();
//                System.out.println();
//                System.out.println();
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//        }

    //查询所有账户余额
    public void fianAccountsBalance(Web3j web3j){
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


    /**
     * eth转换工具
     * @param bigInteger
     * @return
     */
    public BigDecimal  bigIntegerToBigDecimal(BigInteger bigInteger){
        BigDecimal integerToDecimalBalance = new BigDecimal(bigInteger.toString());
        BigDecimal bigDecimal = Convert.fromWei(integerToDecimalBalance,Convert.Unit.ETHER);
        return bigDecimal;
    }

    public String randomLuckNum(int num){
        Random random = new Random();
        String luckNum = "";
        for(int i=0;i < num ; i++){
            luckNum += random.nextInt(10);
        }
        return luckNum;
    }

}
