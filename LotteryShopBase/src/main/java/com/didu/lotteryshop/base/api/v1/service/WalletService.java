package com.didu.lotteryshop.base.api.v1.service;

import com.didu.lotteryshop.base.service.BaseBaseService;
import com.didu.lotteryshop.base.service.Web3jService;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.EsEthwallet;
import com.didu.lotteryshop.common.entity.EsLsbwallet;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.entity.SysConfig;
import com.didu.lotteryshop.common.service.form.impl.*;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.common.utils.Web3jUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class WalletService extends BaseBaseService {


    @Autowired
    private SysConfigServiceImpl sysConfigService;
    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;
    @Autowired
    private EsEthwalletServiceImpl esEthwalletService;
    @Autowired
    private EsEthaccountsServiceImpl esEthaccountsService;
    @Autowired
    private EsLsbwalletServiceImpl esLsbwalletService;
    @Autowired
    private EsLsbaccountsServiceImpl esLsbaccountsService;
    @Autowired
    private Web3jService web3jService;


    /**
     * 获取钱包配置信息
     * @return
     */
    public ResultUtil getWalletConfig(){
        SysConfig sysConfig = sysConfigService.getSysConfig();
        return ResultUtil.successJson(sysConfig);
    }

    /**
     * Eth提现
     * @param sum 数量
     * @param playCode 支付密码
     * @return
     */
    public ResultUtil withdrawCashEth(BigDecimal sum,String playCode){
        LoginUser loginUser = getLoginUser();
        //判断支付密码是否正确
        playCode = AesEncryptUtil.encrypt_code(playCode, Constants.KEY_TOW);
        if(!playCode.equals(loginUser.getPaymentCode())){
            return ResultUtil.errorJson(" wrong password !");
        }
//        EsEthwallet ethwallet = esEthwalletService.findByMemberId(loginUser.getId());
//        if(ethwallet == null){//查询到的Eth数据为空
//            return ResultUtil.errorJson(" The system is busy, please try again later !");
//        }
        SysConfig sysConfig = sysConfigService.getSysConfig();
        //判断余额是否充足
        //提现一次所需要的燃气费
        BigDecimal gas = Web3jUtils.gasToEtherByBigDecimal(sysConfig.getGasPrice(),sysConfig.getGasLimit());
        if(!esEthwalletService.judgeBalance(loginUser.getId(),sum.add(gas))){
            return ResultUtil.errorJson("The extracted ETH is not sufficient to support the GAS consumption !");
        }
        //本次提现手续费
        BigDecimal serviceCharge = sum.divide(new BigDecimal("100")).multiply(sysConfig.getWithdrawRatio());
        //本次可提现实际金额
        sum = sum.subtract(serviceCharge);
        //转平台手续费
        Map<String,Object> map2 = this.ethTransferAccounts(loginUser.getWalletName(),playCode,loginUser.getPAddress(),sysConfig.getManagerAddress(),serviceCharge);//提取手续费
        if(map2 == null || map2.isEmpty()){
            return ResultUtil.errorJson("Transfer failed !");
        }
       boolean b = esEthaccountsService.addOutBeingProcessed(loginUser.getId(),EsEthaccountsServiceImpl.DIC_TYPE_PLATFEE,serviceCharge,"-1",map2.get(web3jService.TRANSACTION_HASHVALUE) == null ? "" : map2.get(web3jService.TRANSACTION_HASHVALUE).toString());
        if(!b){
            return ResultUtil.errorJson("Transfer failed !");
        }
        //转账到用用户外部钱包
        Map<String,Object> map1 = this.ethTransferAccounts(loginUser.getWalletName(),playCode,loginUser.getPAddress(),loginUser.getBAddress(),sum);//转到外部钱包
        if(map1 == null  || map1.isEmpty()){
            return ResultUtil.errorJson("Transfer failed !");
        }
        //新增出账明细
        b = esEthaccountsService.addOutBeingProcessed(loginUser.getId(),EsEthaccountsServiceImpl.DIC_TYPE_DRAW,sum,"-1",map1.get(web3jService.TRANSACTION_HASHVALUE) == null ? "" : map1.get(web3jService.TRANSACTION_HASHVALUE).toString());
        if(!b){
            return ResultUtil.errorJson("Transfer failed !");
        }

        return  ResultUtil.successJson("Transfer successful !");
    }

    /**
     * 平台币转Eth
     * @param sum
     * @return
     */
    public ResultUtil withdrawCashLsbToEth(BigDecimal sum){

        LoginUser loginUser = getLoginUser();//查询人员信息
        EsLsbwallet esLsbwallet = esLsbwalletService.findByMemberId(loginUser.getId());//查询平台币钱包信息
        SysConfig sysConfig = sysConfigService.getSysConfig();
        //判断平台币可用余额是否足够
        if(!esLsbwalletService.judgeBalance(loginUser.getId(),sum)){
            return ResultUtil.errorJson("not sufficient funds !");
        }
        //计算可提现ETH
        BigDecimal lsbToEth = sum.divide(sysConfig.getLsbToEth(),50,BigDecimal.ROUND_DOWN);
        Map<String, Object> map = null;
        boolean b = false;
        if(web3jService.getLsbManagerBalanceByEther().compareTo(lsbToEth) >= 0){
            map = web3jService.lsbManagerSendToETH(loginUser.getPAddress(), lsbToEth);
            if (map == null) {
                return ResultUtil.errorJson("Transfer failed !");
            }
            //新增平台币转Eth处理中信息
             b = esLsbaccountsService.addOutBeingProcessed(loginUser.getId(),EsLsbaccountsServiceImpl.DIC_TYPE_DRAW,sum,"-1",map.get(web3jService.TRANSACTION_HASHVALUE) == null ? "" : map.get(web3jService.TRANSACTION_HASHVALUE).toString());
        }else{
            // TODO 後台開發需要做預警處理
            esLsbaccountsService.addOutFail(loginUser.getId(),EsLsbaccountsServiceImpl.DIC_TYPE_DRAW,sum,"-1",EsLsbaccountsServiceImpl.STATUS_MSG_FAIL);
        }
        if(!b){
            return ResultUtil.errorJson("Transfer failed !");
        }
        return ResultUtil.successJson("Transfer successful !");
    }

    /**
     * Eth转平台币
     * @param sum
     * @param playCode
     * @return
     */
    public ResultUtil withdrawCashEthToLsb(BigDecimal sum,String playCode){
        LoginUser loginUser = getLoginUser();
        EsEthwallet esEthwallet = esEthwalletService.findByMemberId(loginUser.getId());
        SysConfig sysConfig = sysConfigService.getSysConfig();
        //验证支付密码
        playCode = AesEncryptUtil.encrypt_code(playCode, Constants.KEY_TOW);
        if(!playCode.equals(loginUser.getPaymentCode())){
            return  ResultUtil.errorJson(" wrong password !");
        }
        BigDecimal EthToLsb = sum.divide(sysConfig.getEthToLsb(),50,BigDecimal.ROUND_DOWN);
        //判断余额是否可支付
        if(!esEthwalletService.judgeBalance(loginUser.getId(),EthToLsb)){
            return ResultUtil.errorJson("not sufficient funds !");
        }
        Map<String,Object> map = ethTransferAccounts(loginUser.getWalletName(),playCode,loginUser.getPAddress(),sysConfig.getLsbAddress(),EthToLsb);
        if(map == null && map.isEmpty()){
            return ResultUtil.errorJson("Top-up failure !");
        }
        //新新增充值记录
        boolean b = esLsbaccountsService.addInBeingprocessed(loginUser.getId(),EsLsbaccountsServiceImpl.DIC_TYPE_IN,sum,"-1",map.get(web3jService.TRANSACTION_HASHVALUE) == null ? "" : map.get(web3jService.TRANSACTION_HASHVALUE).toString());
        if(!b){
            return ResultUtil.errorJson("Transfer failed !");
        }
        return  ResultUtil.successJson("Transfer successful !");
    }
//
//    public Map<String,Object> loginUserEthTransferAccounts(String toAddress,BigDecimal etherValue){
//        LoginUser loginUser = super.getLoginUser();
//        return this.ethTransferAccounts(loginUser.getWalletName(),);
//    }


    /**
     * ETh转账
     * @param walletFileName 钱包名称
     * @param payPassword 支付密码
     * @param formAddress 出账地址
     * @param toAddress 入账地址
     * @param etherValue 金额
     * @return
     */
    private Map<String,Object> ethTransferAccounts(String walletFileName,String payPassword,String formAddress,String toAddress,BigDecimal etherValue){
        try {
            //转账
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("walletFileName",walletFileName);
            map.put("payPassword",payPassword);
            map.put("formAddress",formAddress);//出
            map.put("toAddress",toAddress);//入
            map.put("etherValue",etherValue);
            String reStr = oAuth2RestTemplate.postForObject("http://wallet-service/v1/wallet/transfer", super.getEncryptRequestHttpEntity(map), String.class);

//            if (reStr == null || "".equals(reStr)) {
//                return null;
//            }
//            ResultUtil  result = super.getDecryptResponseToResultUtil(reStr); //解密
//            //判斷是否成功
//            if (result != null && result.getCode() != ResultUtil.SUCCESS_CODE) {
//                return null;
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
       // return (Map<String, Object>) result.getExtend().get(ResultUtil.DATA_KEY);
        return null;
    }
}
