package com.didu.lotteryshop.manage.service;

import com.didu.lotteryshop.common.service.GasProviderService;
import com.didu.lotteryshop.common.utils.Web3jUtils;
import com.didu.lotteryshop.manage.contract.LotteryAContract;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class Web3jService  {
    /** 主网络地址 */
    @Value("${ethwallet.web3j.url}")
    private String ethwalletWeb3jUrl;
    private Web3j web3j;
    @Autowired
    private GasProviderService gasProviderService;

    @PostConstruct
    public void init() {
        web3j = Web3j.build(new HttpService(ethwalletWeb3jUrl)); //RPC方式
        // web3j = Web3j.build(new UnixIpcService(ipcSocketPath)); //IPC方式
    }

    /**
     * 获取钱包余额
     * @param address
     * @return
     */
    public BigDecimal getBalanceByEther(String address){
        BigDecimal total = BigDecimal.ZERO;
        try {
            EthGetBalance ethGetBalancel = web3j.ethGetBalance(address, DefaultBlockParameter.valueOf("latest")).send();
            total = Web3jUtils.bigIntegerToBigDecimal(ethGetBalancel.getBalance());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;
    }

    public boolean contractBalanceIn(String contractAddress,String privateKey,BigDecimal total){
        boolean bool = false;
        if(StringUtils.isNotBlank(contractAddress) &&  StringUtils.isNotBlank(privateKey)){
            try {
                Credentials credentials = Credentials.create(privateKey);
                LotteryAContract lotteryAContract = LotteryAContract.load(contractAddress, web3j, credentials, gasProviderService.getStaticGasProvider());
                TransactionReceipt transactionReceipt = lotteryAContract.ContractBalanceIn(Convert.toWei(total.toPlainString(), Convert.Unit.ETHER).toBigInteger()).send();
                BigInteger gasUsed = transactionReceipt.getGasUsed();
                System.out.println("充值合约gasFee:"+Web3jUtils.bigIntegerToBigDecimal(gasProviderService.getGasPrice().multiply(gasUsed)));
                bool = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bool;
    }




}
