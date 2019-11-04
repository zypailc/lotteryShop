package com.didu.lotteryshop.lotterya.service;

import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.lotterya.contract.LotteryAContract;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class Web3jService {
    /** 主网络地址 */
    @Value("${ethwallet.web3j.url}")
    private String ethwalletWeb3jUrl;
    /** 钱包地址文件 */
    @Value("${ethwallet.filePath}")
    private String  ethwalletFilePath;

    private Web3j web3j;
    @Autowired
    private GasProviderService gasProviderService;
    /** 加密部署管理员私钥 */
    @Value("${managerPrivateKey}")
    private String pManagerPrivateKey;
    /** 解密后的关于员私钥 **/
    private String managerPrivateKey;


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
            LotteryAContract  lotteryAContract = LotteryAContract.deploy(web3j,credentials,gasProviderService.getStaticGasProvider()).send();
            contractAddress = lotteryAContract.getContractAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contractAddress;
    }

    /**
     * 根据合约地址获取合约
     * @param contractAddress
     * @return 合约对象
     */
    public LotteryAContract loadContract(String contractAddress){
        LotteryAContract lotteryAContract = null;
        if(StringUtils.isBlank(contractAddress) &&  StringUtils.isBlank(managerPrivateKey)){
            //部署管理员私钥
            Credentials credentials = Credentials.create(managerPrivateKey);
            lotteryAContract = LotteryAContract.load(contractAddress, web3j, credentials, gasProviderService.getStaticGasProvider());
        }
        return lotteryAContract;
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
     * 获取地址
     * @param walletFilePath
     * @param password
     * @return
     */
    public String getWalletAddress(String walletFilePath,String password){
        String address = null;
        try {
            Credentials  credentials = WalletUtils.loadCredentials(password,ethwalletFilePath+walletFilePath);
            address = credentials.getAddress();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }
        return address;
    }

    /**
     * 获取钱包证明文件
     * @param walletFilePath
     * @param password
     * @return
     */
    public  Credentials getCredentials(String walletFilePath,String password){
        Credentials  credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(password,ethwalletFilePath+walletFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }
        return credentials;
    }


}
