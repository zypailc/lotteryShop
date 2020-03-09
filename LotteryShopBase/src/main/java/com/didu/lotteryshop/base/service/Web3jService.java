package com.didu.lotteryshop.base.service;

import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.service.GasProviderService;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.common.utils.Web3jUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
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
 * @date 2019-12-20
 */
@Service
public class Web3jService extends BaseBaseService {
    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;
    @Autowired
    private RestTemplate restTemplate;
    /** 主网络地址 */
    @Value("${ethwallet.web3j.url}")
    private String ethwalletWeb3jUrl;
    private Web3j web3j;
    @Autowired
    private GasProviderService gasProviderService;
    /** 加密后推广分成管理员私钥 */
    @Value("${divideIntoManagerPrivateKey}")
    private String pDivideIntoManagerPrivateKey;
    /** 解密后推广分成管理员私钥 **/
    private String divideIntoManagerPrivateKey;
    /** 加密后平台币管理员私钥 */
    @Value("${lsbManagerPrivateKey}")
    private String pLsbManagerPrivateKey;
    /** 解密后平台币管理员私钥 **/
    private String lsbManagerPrivateKey;
    /** 事务哈希值**/
    public static final String TRANSACTION_HASHVALUE = "transaction_hashvalue";
    /** 是否确认状态，0未确认，1已确认，2失败 */
    public static final String TRANSACTION_STATUS = "transaction_status";
    /** 实际确认产生的gas费用 */
    public static final String TRANSACTION_GASUSED = "transaction_gasUsed";

    @PostConstruct
    public void init() {
        web3j = Web3j.build(new HttpService(ethwalletWeb3jUrl)); //RPC方式
        // web3j = Web3j.build(new UnixIpcService(ipcSocketPath)); //IPC方式
        try {
            divideIntoManagerPrivateKey =  AesEncryptUtil.decrypt(pDivideIntoManagerPrivateKey, Constants.AES_ETHMANAGER_PRIVATEKEY);
        } catch (Exception e) {
            divideIntoManagerPrivateKey = null;
            e.printStackTrace();
        }
        try {
            lsbManagerPrivateKey =  AesEncryptUtil.decrypt(pLsbManagerPrivateKey, Constants.AES_ETHMANAGER_PRIVATEKEY);
        } catch (Exception e) {
            lsbManagerPrivateKey = null;
            e.printStackTrace();
        }
    }

    /**
     * 获取推广分成管理员Credentials
     * @return Credentials
     */
    public Credentials getDivideIntoManagerCredentials(){
        if(divideIntoManagerPrivateKey != null)
            return Credentials.create(divideIntoManagerPrivateKey);
        return null;
    }
    /**
     * 获取平台币管理员Credentials
     * @return Credentials
     */
    public Credentials getLsbManagerCredentials(){
        if(lsbManagerPrivateKey != null)
            return Credentials.create(lsbManagerPrivateKey);
        return null;
    }

    /**
     * 推广分成管理员转账ETH
     * @param toAddress
     * @param etherValue
     * @return
     */
    public Map<String,Object> divideIntoManagerSendToETH(String toAddress,BigDecimal etherValue){
        Map<String,Object> reMap = new HashMap<>();
        try {
            Credentials credentials = this.getDivideIntoManagerCredentials();
            reMap = this.transfer(credentials,toAddress,etherValue.toPlainString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return reMap;
    }
    /**
     * 平台币管理员转账ETH
     * @param toAddress
     * @param etherValue
     * @return
     */
    public Map<String,Object> lsbManagerSendToETH(String toAddress,BigDecimal etherValue){
        Map<String,Object> reMap = new HashMap<>();
        try {
            Credentials credentials = this.getLsbManagerCredentials();
            reMap = this.transfer(credentials,toAddress,etherValue.toPlainString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return reMap;
    }

    /**
     * 获取平台币ETH账户余额
     * @return ether
     */
    public BigDecimal getLsbManagerBalanceByEther(){
        BigDecimal total = BigDecimal.ZERO;
        try {
            EthGetBalance ethGetBalancel = web3j.ethGetBalance(this.getLsbManagerCredentials().getAddress(), DefaultBlockParameter.valueOf("latest")).send();
            total = Web3jUtils.bigIntegerToBigDecimal(ethGetBalancel.getBalance());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;
    }

    /**
     * 获取推广ETH账户余额
     * @return ether
     */
    public BigDecimal geGdManagerBalanceByEther(){
        BigDecimal total = BigDecimal.ZERO;
        try {
            EthGetBalance ethGetBalancel = web3j.ethGetBalance(this.getDivideIntoManagerCredentials().getAddress(), DefaultBlockParameter.valueOf("latest")).send();
            total = Web3jUtils.bigIntegerToBigDecimal(ethGetBalancel.getBalance());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;
    }

    /**
     * 转账
     * @param credentials
     * @param toAddress
     * @param etherValue
     * @return 事务HashValue
     * @throws IOException
     */
    public Map<String,Object>  transfer(Credentials credentials,String toAddress,String etherValue) throws IOException{
        Map<String,Object> rMap = new HashMap<>();
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(
                nonce, gasProviderService.getGasPrice(),
                gasProviderService.getGasLimit(),
                toAddress,
                Convert.toWei(etherValue, Convert.Unit.ETHER).toBigInteger()
        );
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
        String transactionHashValue = ethSendTransaction.getTransactionHash();
        rMap.put(TRANSACTION_HASHVALUE,transactionHashValue);
        Map<String,Object> sMap = this.findTransactionStatus(transactionHashValue);
        rMap.putAll(sMap);
        return rMap;
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
    /**
     * ETh转账
     * @param walletFileName 钱包名称
     * @param payPassword 支付密码
     * @param formAddress 出账地址
     * @param toAddress 入账地址
     * @param etherValue 金额
     * @return
     */
    public Map<String,Object> ethTransferAccounts(String walletFileName,String payPassword,String formAddress,String toAddress,BigDecimal etherValue){
        return ethTransferAccounts(walletFileName,payPassword,formAddress,toAddress,etherValue,null);
    }

    /**
     * ETh转账
     * @param walletFileName 钱包名称
     * @param payPassword 支付密码
     * @param formAddress 出账地址
     * @param toAddress 入账地址
     * @param etherValue 金额
     * @param   authorizationStr 验证令牌
     * @return
     */
    public Map<String,Object> ethTransferAccounts(String walletFileName,String payPassword,String formAddress,String toAddress,BigDecimal etherValue,String authorizationStr){
       Map<String,Object> resultMap = new HashMap<>();
        resultMap.put(TRANSACTION_STATUS,2);
        resultMap.put(TRANSACTION_HASHVALUE,"");
        resultMap.put(TRANSACTION_GASUSED,null);
        try {
            //转账
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("walletFileName",walletFileName);
            map.put("payPassword",payPassword);
            map.put("formAddress",formAddress);//出
            map.put("toAddress",toAddress);//入
            map.put("etherValue",etherValue);
            String reStr = null;
            if(StringUtils.isBlank(authorizationStr)){
                reStr = oAuth2RestTemplate.postForObject("http://wallet-service/v1/wallet/transfer", super.getEncryptRequestHttpEntity(map), String.class);
            }else{
                reStr  = restTemplate.postForObject("http://wallet-service/v1/wallet/transfer",super.getEncryptRequestHttpEntity(map,authorizationStr), String.class);
            }
            if(StringUtils.isNotBlank(reStr)){
                ResultUtil result = super.getDecryptResponseToResultUtil(reStr);
                if(result != null && result.getSuccess() && result.getExtend() != null){
                    Map<String,Object> rMap = (Map<String,Object>) result.getExtend().get(ResultUtil.DATA_KEY);
                    if(rMap != null && !rMap.isEmpty()){
                        resultMap.put(TRANSACTION_STATUS,rMap.get("transaction_status").toString());
                        resultMap.put(TRANSACTION_HASHVALUE,rMap.get("transaction_hashvalue").toString());
                        resultMap.put(TRANSACTION_GASUSED,rMap.get("transaction_casUsed") == null ? "" : rMap.get("transaction_casUsed").toString());
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

}
