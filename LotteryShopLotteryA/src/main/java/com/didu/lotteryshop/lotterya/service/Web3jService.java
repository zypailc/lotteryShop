package com.didu.lotteryshop.lotterya.service;

import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.enumeration.ResultCode;
import com.didu.lotteryshop.common.service.GasProviderService;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.lotterya.contract.LotteryAContract;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class Web3jService extends LotteryABaseService {
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
            LotteryAContract  lotteryAContract =LotteryAContract.deploy(web3j,credentials,gasProviderService.getStaticGasProvider(),adjustFundAddress).send();
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
        if(StringUtils.isBlank(contractAddress) &&  StringUtils.isBlank(managerPrivateKey)){
            //部署管理员私钥
            Credentials credentials = Credentials.create(managerPrivateKey);
            lotteryAContract = LotteryAContract.load(contractAddress, web3j, credentials, gasProviderService.getStaticGasProvider());
        }
        return lotteryAContract;
    }

    public LotteryAContract loadLoginMemberContract(String contractAddress){


        return null;
    }

    public Credentials getLoginMemberCredentials(){
        try{
            LoginUser loginUser = super.getLoginUser();
            Map<String,Object> map = new HashMap<>();
            map.put("walletFileName",loginUser.getWalletName());
            map.put("payPassword",loginUser.getpAddress());
            JSONObject jObject = new JSONObject(map);
            String str = jObject.toString();
            str = super.getEncryptRequest(str);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
            HttpEntity<String> strEntity = new HttpEntity<String>(str,headers);
            //查询钱包明细
            String reStr =   oAuth2RestTemplate.postForObject("http://wallet-service/v1/wallet/findWalletDetail",strEntity,String.class);
            ResultUtil result = super.getDecryptRequestToResultUtil(reStr);
            if(result != null){
                if(result.getCode() == ResultCode.SUCCESS.getCode() && result.getExtend() != null){
                    result.getExtend().get(ResultUtil.DATA_KEY);
                }
            }
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


}
