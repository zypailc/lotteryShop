package com.didu.lotteryshop.lotterya.service;

import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.service.GasProviderService;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.Web3jUtils;
import com.didu.lotteryshop.lotterya.contract.LotteryAContract;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * web3jService
 * @author CHJ
 * @date 2019-11-26
 */
@Service
public class Web3jService extends LotteryABaseService {
    /** 生成钱包文件地址*/
    @Value("${ethwallet.filePath}")
    private String walletFilePath;
    /** 主网络地址 */
    @Value("${ethwallet.web3j.url}")
    private String ethwalletWeb3jUrl;
    private Web3j web3j;
    @Autowired
    private GasProviderService gasProviderService;
    /** 加密部署管理员私钥 */
    @Value("${managerPrivateKey}")
    private String pManagerPrivateKey;
    /** 解密后的关于员私钥 **/
    private String managerPrivateKey;
    /** 调节基金地址*/
    @Value("${adjustFundAddress}")
    private String adjustFundAddress;
    /** 事务哈希值**/
    public static final String TRANSACTION_HASHVALUE = "transaction_hashvalue";
    /** 是否确认状态，0未确认，1已确认，2失败 */
    public static final String TRANSACTION_STATUS = "transaction_status";
    /** 实际确认产生的gas费用 */
    public static final String TRANSACTION_GASUSED = "transaction_gasUsed";

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;


    @PostConstruct
    public void init() {
        web3j = Web3j.build(new HttpService(ethwalletWeb3jUrl)); //RPC方式
     // web3j = Web3j.build(new UnixIpcService(ipcSocketPath)); //IPC方式
        try {
            managerPrivateKey =  AesEncryptUtil.decrypt(pManagerPrivateKey, Constants.AES_ETHMANAGER_PRIVATEKEY);
        } catch (Exception e) {
            managerPrivateKey = null;
            e.printStackTrace();
        }
    }

    /**
     *  部署智能合约
     * @return 返回合约地址
     */
    public  String deployContract(){
        String contractAddress = null;
        try {
            //部署管理员私钥
            Credentials credentials = Credentials.create(managerPrivateKey);
            LotteryAContract  lotteryAContract = LotteryAContract.deploy(web3j,credentials,gasProviderService.getStaticGasProvider(),adjustFundAddress).send();
            contractAddress = lotteryAContract.getContractAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contractAddress;
    }

    /**
     * 根据合约地址获取合约(管理员)
     * @param contractAddress
     * @return 合约对象
     */
    public LotteryAContract loadManagerContract(String contractAddress){
        LotteryAContract lotteryAContract = null;
        if(StringUtils.isNotBlank(contractAddress) &&  StringUtils.isNotBlank(managerPrivateKey)){
            //部署管理员私钥
            Credentials credentials = Credentials.create(managerPrivateKey);
            lotteryAContract = LotteryAContract.load(contractAddress, web3j, credentials, gasProviderService.getStaticGasProvider());
        }
        return lotteryAContract;
    }

    /**
     * 加载登录用户合约
     * @param contractAddress 合约地址
     * @return LotteryAContract
     */
    public LotteryAContract loadLoginMemberContract(String contractAddress){
        if(StringUtils.isNotBlank(contractAddress)){
            Credentials credentials = this.getLoginMemberCredentials();
            if(credentials != null)
            return LotteryAContract.load(contractAddress, web3j, credentials, gasProviderService.getStaticGasProvider());
        }
        return null;
    }

    /**
     * 获取登录用户Credentials
     * @return Credentials
     */
    public Credentials getLoginMemberCredentials(){
        try{
            LoginUser loginUser = super.getLoginUser();
            return WalletUtils.loadCredentials(loginUser.getPaymentCode(), walletFilePath+loginUser.getWalletName());

// TODO 2019-12-11 lyl 注释，因返回钱包秘钥，直接用秘钥生成Credentials 无法进行操作。需要进一步研究。

//            Map<String,Object> map = new HashMap<>();
//            map.put("walletFileName",loginUser.getWalletName());
//            map.put("payPassword",loginUser.getPaymentCode());
//            //查询钱包明细
//            String reStr =   oAuth2RestTemplate.postForObject("http://wallet-service/v1/wallet/findWalletDetail",super.getEncryptRequestHttpEntity(map),String.class);
//            ResultUtil result = super.getDecryptResponseToResultUtil(reStr);
//            if(result != null){
//                if(result.getCode() == ResultCode.SUCCESS.getCode() && result.getExtend() != null){
//                    Map<String,Object> rMap = (Map<String,Object>) result.getExtend().get(ResultUtil.DATA_KEY);
//                    if(rMap != null && !rMap.isEmpty()){
//                       return Credentials.create(rMap.get("ecKeyPair").toString());
//                    }
//                }
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取管理员地址
     * @return
     */
    public String getManagerAddress(){
        //部署管理员私钥
        Credentials credentials = Credentials.create(managerPrivateKey);
        return credentials.getAddress();
    }

    /**
     * 获取管理员Credentials
     * @returnCredentials
     */
    public Credentials getManagerCredentials(){
        return Credentials.create(managerPrivateKey);
    }

    /**
     * 获取登录用户钱包余额
     * @return ether
     */
    public BigDecimal getLoginMmeberBalanceByEther(){
        BigDecimal total = BigDecimal.ZERO;
        try {
            EthGetBalance ethGetBalancel = web3j.ethGetBalance(super.getLoginUser().getPAddress(), DefaultBlockParameter.valueOf("latest")).send();
            total = Web3jUtils.bigIntegerToBigDecimal(ethGetBalancel.getBalance());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;
    }

    /**
     * 获取调节基金账户余额
     * @return ether
     */
    public BigDecimal getAdjustFundBalanceByEther(){
        BigDecimal total = BigDecimal.ZERO;
        try {
            EthGetBalance ethGetBalancel = web3j.ethGetBalance(adjustFundAddress, DefaultBlockParameter.valueOf("latest")).send();
            total = Web3jUtils.bigIntegerToBigDecimal(ethGetBalancel.getBalance());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;
    }

    /**
     * 获取管理员账户余额
     * @return ether
     */
    public BigDecimal getManagerBalanceByEther(){
        BigDecimal total = BigDecimal.ZERO;
        try {
            EthGetBalance ethGetBalancel = web3j.ethGetBalance(this.getManagerAddress(), DefaultBlockParameter.valueOf("latest")).send();
            total = Web3jUtils.bigIntegerToBigDecimal(ethGetBalancel.getBalance());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;
    }

    /**
     * 获取合约账户余额
     * @param contractAddress 合约地址
     * @return ether
     */
    public BigDecimal getContractBalanceByEther(String contractAddress){
        BigDecimal total = BigDecimal.ZERO;
        try {
            LotteryAContract lotteryAContract = this.loadManagerContract(contractAddress);
            BigInteger balance = lotteryAContract.ShowContractBalance().send();
            total = Web3jUtils.bigIntegerToBigDecimal(balance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    /**
     * 管理员转账ETH
     * @param toAddress
     * @param etherValue
     * @return
     */
    public Map<String,Object> managerSendToETH(String toAddress,BigDecimal etherValue){
        Map<String,Object> reMap = new HashMap<>();
        try {
            Credentials credentials = this.getManagerCredentials();
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                    credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                    nonce, gasProviderService.getGasPrice(),
                    gasProviderService.getGasLimit(),
                    toAddress,
                    Convert.toWei(etherValue, Convert.Unit.ETHER).toBigInteger()
            );
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
            String  transactionHashValue = ethSendTransaction.getTransactionHash();
            //转账事务hash值，可用来查看交易是否被确认
            reMap.put(TRANSACTION_HASHVALUE,transactionHashValue);
            Map<String,Object> sMap = this.findTransactionStatus(transactionHashValue);
            reMap.putAll(sMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return reMap;
    }

    /**
     * 查询交易状态
     * @param transactionHashValue 事务哈希码
     * @return Map
     */
    public Map<String,Object> findTransactionStatus(String transactionHashValue){
        Map<String,Object> reMap = new HashMap<>();
        EthGetTransactionReceipt transactionReceipt = null;
        try {
            transactionReceipt = web3j.ethGetTransactionReceipt(transactionHashValue).send();
            //是否确认状态，0未确认，1已确认，2失败
            reMap.put(TRANSACTION_STATUS,0);
            reMap.put(TRANSACTION_GASUSED,BigDecimal.ZERO);
            if (transactionReceipt.getTransactionReceipt().isPresent()) {
                //状态，需要实际数据，修改状态
                String status = transactionReceipt.getTransactionReceipt().get().getStatus();
                if(Web3jUtils.transactionReceiptStatusSuccess(status)){
                    reMap.put(TRANSACTION_STATUS,1);
                    //实际确认产生的gas费用
                    reMap.put(TRANSACTION_GASUSED,Web3jUtils.bigIntegerToBigDecimal(gasProviderService.getGasPrice().multiply(transactionReceipt.getTransactionReceipt().get().getGasUsed())).toPlainString());
                }else if(Web3jUtils.transactionReceiptStatusFail(status)){
                    reMap.put(TRANSACTION_STATUS,2);
                    reMap.put(TRANSACTION_GASUSED,Web3jUtils.bigIntegerToBigDecimal(gasProviderService.getGasPrice().multiply(transactionReceipt.getTransactionReceipt().get().getGasUsed())).toPlainString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reMap;
    }


}
