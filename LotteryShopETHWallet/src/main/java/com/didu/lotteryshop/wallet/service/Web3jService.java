package com.didu.lotteryshop.wallet.service;

import com.didu.lotteryshop.common.service.GasProviderService;
import com.didu.lotteryshop.common.utils.Web3jUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
 * @date 2019-11-25
 */
@Service
public class Web3jService extends WalletBaseService {
    /** 生成钱包文件地址*/
    @Value("${ethwallet.web3j.url}")
    private String ethwalletWeb3jUrl;
    private Web3j web3j;

    @Autowired
    private GasProviderService gasProviderService;
    /** 事务哈希值**/
    public static final String TRANSACTION_HASHVALUE = "transaction_hashvalue";
    /** 是否确认状态，0为确认，1已确认 */
    public static final String TRANSACTION_STATUS = "transaction_status";
    /** 实际确认产生的gas费用 */
    public static final String TRANSACTION_CASUSED = "transaction_casUsed";
    @PostConstruct
    public void init() {
        web3j = Web3j.build(new HttpService(ethwalletWeb3jUrl)); //RPC方式
       // web3j = Web3j.build(new UnixIpcService(ipcSocketPath)); //IPC方式
    }

    /**
     * 获取钱包余额
     * @param address
     * @return ether
     */
    public BigDecimal getBalanceByEther(String address) throws IOException{
        EthGetBalance ethGetBalancel2 = web3j.ethGetBalance(address, DefaultBlockParameter.valueOf("latest")).send();
        return Web3jUtils.bigIntegerToBigDecimal(ethGetBalancel2.getBalance());
    }

    /**
     * 转账
     * @param credentials
     * @param toAddress
     * @param etherValue
     * @return 事务HashValue
     * @throws IOException
     */
    public String  transfer(Credentials credentials,String toAddress,String etherValue) throws IOException{
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                    credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            // create our transaction
            // DefaultGasProvider defaultGasProvider = new DefaultGasProvider();
            // StaticGasProvider defaultGasProvider = new StaticGasProvider(BigInteger.valueOf(22000000000L),BigInteger.valueOf(4300000L));
            RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(
                    nonce, gasProviderService.getGasPrice(),
                    gasProviderService.getGasLimit(),
                    toAddress,
                    Convert.toWei(etherValue, Convert.Unit.ETHER).toBigInteger()
            );
            // sign & send our transaction
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
            return  ethSendTransaction.getTransactionHash();
    }

    /**
     * 查询交易状态
     * @param transactionHashValue 事务哈希码
     * @return Map
     */
    public Map<String,Object> findTransactionStatus(String transactionHashValue) throws IOException{
        Map<String,Object> reMap = new HashMap<>();
        EthGetTransactionReceipt transactionReceipt =  web3j.ethGetTransactionReceipt(transactionHashValue).send();
        //是否确认状态，0未确认，1已确认，2失败
        reMap.put(TRANSACTION_STATUS,0);
        reMap.put(TRANSACTION_CASUSED,BigDecimal.ZERO);
        if (transactionReceipt.getTransactionReceipt().isPresent()) {
            //状态，需要实际数据，修改状态
            String status = transactionReceipt.getTransactionReceipt().get().getStatus();
            if(Web3jUtils.transactionReceiptStatusSuccess(status)){
                reMap.put(TRANSACTION_STATUS,1);
                //实际确认产生的gas费用
                reMap.put(TRANSACTION_CASUSED,Web3jUtils.bigIntegerToBigDecimal(gasProviderService.getGasPrice().multiply(transactionReceipt.getTransactionReceipt().get().getGasUsed())).toPlainString());
            }else if(Web3jUtils.transactionReceiptStatusFail(status)){
                reMap.put(TRANSACTION_STATUS,2);
                reMap.put(TRANSACTION_CASUSED,Web3jUtils.bigIntegerToBigDecimal(gasProviderService.getGasPrice().multiply(transactionReceipt.getTransactionReceipt().get().getGasUsed())).toPlainString());
            }
        }
        return reMap;
    }

}
