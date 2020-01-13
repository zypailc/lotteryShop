package com.didu.lotteryshop.base.api.v1.service;

import com.didu.lotteryshop.base.service.BaseBaseService;
import com.didu.lotteryshop.base.service.WalletEthService;
import com.didu.lotteryshop.base.service.Web3jService;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.*;
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
    private EsEthwalletServiceImpl esEthwalletService;
    @Autowired
    private EsEthaccountsServiceImpl esEthaccountsService;
    @Autowired
    private EsLsbwalletServiceImpl esLsbwalletService;
    @Autowired
    private EsLsbaccountsServiceImpl esLsbaccountsService;
    @Autowired
    private Web3jService web3jService;
    @Autowired
    private WalletEthService walletEthService;

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
        EthTransfer ethTransfer = walletEthService.ethTransferAccounts(playCode,loginUser.getPAddress(),sysConfig.getManagerAddress(),serviceCharge);//提取手续费
        if(ethTransfer == null){
            return ResultUtil.errorJson("Transfer failed !");
        }
       boolean b = esEthaccountsService.addOutBeingProcessed(loginUser.getId(),EsEthaccountsServiceImpl.DIC_TYPE_PLATFEE,serviceCharge,"-1",ethTransfer.getTransactionHashvalue());
        if(!b){
            return ResultUtil.errorJson("Transfer failed !");
        }
        //转账到用用户外部钱包
        EthTransfer ethTransfer1 = walletEthService.ethTransferAccounts(playCode,loginUser.getPAddress(),loginUser.getBAddress(),sum);//转到外部钱包
        if(ethTransfer1 == null){
            return ResultUtil.errorJson("Transfer failed !");
        }
        //新增出账明细
        b = esEthaccountsService.addOutBeingProcessed(loginUser.getId(),EsEthaccountsServiceImpl.DIC_TYPE_DRAW,sum,"-1",ethTransfer.getTransactionHashvalue());
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
        EthTransfer ethTransfer = walletEthService.ethTransferAccounts(playCode,loginUser.getPAddress(),sysConfig.getLsbAddress(),EthToLsb);
        if(ethTransfer == null){
            return ResultUtil.errorJson("Top-up failure !");
        }
        //新新增充值记录
        boolean b = esLsbaccountsService.addInBeingprocessed(loginUser.getId(),EsLsbaccountsServiceImpl.DIC_TYPE_IN,sum,"-1",ethTransfer.getTransactionHashvalue());
        if(!b){
            return ResultUtil.errorJson("Transfer failed !");
        }
        return  ResultUtil.successJson("Transfer successful !");
    }

}
