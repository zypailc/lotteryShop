package com.didu.lotteryshop.base.api.v1.service;

import com.didu.lotteryshop.base.service.BaseBaseService;
import com.didu.lotteryshop.base.service.Web3jService;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.*;
import com.didu.lotteryshop.common.service.form.impl.*;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.CodeUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.common.utils.Web3jUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private MemberServiceImpl memberService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

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
            String msg = "Payment password error!";
            if(super.isChineseLanguage()){
                msg = "支付密碼錯誤!";
            }
            return ResultUtil.errorJson(msg);
        }
        SysConfig sysConfig = sysConfigService.getSysConfig();
        //判断余额是否充足
        //提现一次所需要的燃气费
        BigDecimal gas = Web3jUtils.gasToEtherByBigDecimal(sysConfig.getGasPrice(),sysConfig.getGasLimit());
        if(!esEthwalletService.judgeBalance(loginUser.getId(),sum.add(gas))){
            String msg = "If the balance is insufficient or the remaining balance is not enough to cover the GAS cost, please reduce the value of withdrawing ETH！";
            if(super.isChineseLanguage()){
                msg = "余額不足或剩余余額不夠GAS燃氣費用，請降低提現ETH數值";
            }
            return ResultUtil.errorJson(msg);
        }
        //本次提现手续费
        BigDecimal serviceCharge = sum.divide(new BigDecimal("100")).multiply(sysConfig.getWithdrawRatio());
        //本次可提现实际金额
        sum = sum.subtract(serviceCharge);
        String uuId = CodeUtil.getUuid();
        boolean bool = esEthaccountsService.addOutBeingProcessed(loginUser.getId(),EsEthaccountsServiceImpl.DIC_TYPE_PLATFEE,serviceCharge,uuId);
        if(bool){
            //kafka 执行公链转账操作
            kafkaTemplate.send("kafkaWithdrawCashEth","operId",uuId);
        }else{
            String msg = "Withdrawal failed, please try again !";
            if(super.isChineseLanguage()){
                msg = "提現失敗，請重試！";
            }
            return ResultUtil.errorJson(msg);
        }
        uuId = CodeUtil.getUuid();
        bool = esEthaccountsService.addOutBeingProcessed(loginUser.getId(),EsEthaccountsServiceImpl.DIC_TYPE_DRAW,sum,uuId);
        if(bool){
            //kafka 执行公链转账操作
            kafkaTemplate.send("kafkaWithdrawCashEth","operId",uuId);
        }else{
            String msg = "Withdrawal failed, please try again !";
            if(super.isChineseLanguage()){
                msg = "提現失敗，請重試！";
            }
            return ResultUtil.errorJson(msg);
        }
        String msg = "Withdrawal successful !";
        if(super.isChineseLanguage()){
            msg = "提現成功！";
        }
        return  ResultUtil.successJson(msg);
    }

    /**
     * kafka 用户提现转账
     * @param operId
     */
    public void kafkaWithdrawCashEth(String operId){
        EsEthaccounts esEthaccounts = esEthaccountsService.findEsEthaccountsByOperId(operId);
        if(esEthaccounts != null && esEthaccounts.getId() != null){
           Member  member =  memberService.selectById(esEthaccounts.getMemberId());
           if(member != null && member.getId() != null){
                SysConfig sysConfig = sysConfigService.getSysConfig();
                Map<String,Object> rMap = null;
                if(esEthaccounts.getDicType() == EsEthaccountsServiceImpl.DIC_TYPE_PLATFEE){
                    rMap = web3jService.ethTransferAccounts(member.getWalletName(),member.getPaymentCodeWallet(),member.getPAddress(),sysConfig.getManagerAddress(),esEthaccounts.getAmount());
                }
                if(esEthaccounts.getDicType() == EsEthaccountsServiceImpl.DIC_TYPE_DRAW){
                    rMap = web3jService.ethTransferAccounts(member.getWalletName(),member.getPaymentCodeWallet(),member.getPAddress(),member.getBAddress(),esEthaccounts.getAmount());
                }
               if(rMap != null && !rMap.isEmpty()){
                   if(rMap.get(Web3jService.TRANSACTION_HASHVALUE) != null){
                      String tHValue = rMap.get(Web3jService.TRANSACTION_HASHVALUE).toString();
                      if(StringUtils.isNotBlank(tHValue)){
                          esEthaccounts.setTransferHashValue(tHValue);
                          esEthaccountsService.updateById(esEthaccounts);
                      }
                   }

                   if(Integer.valueOf(rMap.get(Web3jService.TRANSACTION_STATUS).toString()) == 0){
                       //等待确认

                   }else if(Integer.valueOf(rMap.get(Web3jService.TRANSACTION_STATUS).toString()) == 1){
                       //成功
                       esEthaccountsService.updateSuccess(esEthaccounts.getId(),new BigDecimal(rMap.get(Web3jService.TRANSACTION_GASUSED).toString()));
                   }else if(Integer.valueOf(rMap.get(Web3jService.TRANSACTION_STATUS).toString()) == 2){
                       //失败
                       esEthaccountsService.updateFail(esEthaccounts.getId());
                   }
               }
           }
        }
    }

    /**
     * 平台币转Eth
     * @param sum
     * @return
     */
    public ResultUtil withdrawCashLsbToEth(BigDecimal sum){
        SysConfig sysConfig = sysConfigService.getSysConfig();
        //判断最低兑换限制
        if(sysConfig.getLsbwithdrawMin().compareTo(sum) < 0 ){
            String msg = "The minimum exchange limit is "+sysConfig.getLsbwithdrawMin().toPlainString()+"!";
            if(super.isChineseLanguage()){
                msg = "最低兌換限額為 "+sysConfig.getLsbwithdrawMin().toPlainString()+"!";
            }
            return ResultUtil.errorJson(msg);
        }
        LoginUser loginUser = getLoginUser();//查询人员信息
        //判断平台币可用余额是否足够
        if(!esLsbwalletService.judgeBalance(loginUser.getId(),sum)){
            String msg = "not sufficient funds !";
            if(super.isChineseLanguage()){
                msg = "余額不足!";
            }
            return ResultUtil.errorJson(msg);
        }
        //计算可提现ETH
        BigDecimal lsbToEth = sum.divide(sysConfig.getLsbToEth(),4,BigDecimal.ROUND_DOWN);
        Map<String, Object> map = null;
        boolean b = false;
        if(web3jService.getLsbManagerBalanceByEther().compareTo(lsbToEth) >= 0){
//            map = web3jService.lsbManagerSendToETH(loginUser.getPAddress(), lsbToEth);
//            if (map == null) {
//                return ResultUtil.errorJson("Transfer failed !");
//            }
//            //新增平台币转Eth处理中信息
//             b = esLsbaccountsService.addOutBeingProcessed(loginUser.getId(),EsLsbaccountsServiceImpl.DIC_TYPE_DRAW,sum,"-1",map.get(web3jService.TRANSACTION_HASHVALUE) == null ? "" : map.get(web3jService.TRANSACTION_HASHVALUE).toString());

              String uuId = CodeUtil.getUuid();
              b = esLsbaccountsService.addOutBeingProcessed(loginUser.getId(),EsLsbaccountsServiceImpl.DIC_TYPE_DRAW,sum,uuId);
              if(b){
                  //kafka 执行公链转账操作
                  kafkaTemplate.send("withdrawCashLsbToEth","operId",uuId);
              }
        }else{
            // TODO 後台開發需要做預警處理
            esLsbaccountsService.addOutFail(loginUser.getId(),EsLsbaccountsServiceImpl.DIC_TYPE_DRAW,sum,"-1",EsLsbaccountsServiceImpl.STATUS_MSG_FAIL);
        }
        if(!b){
            String msg = "Exchange failed, please try again !";
            if(super.isChineseLanguage()){
                msg = "兌換失敗，請重試！";
            }
            return ResultUtil.errorJson(msg);
        }
        String msg = "Exchange successful !";
        if(super.isChineseLanguage()){
            msg = "兌換成功！";
        }
        return ResultUtil.successJson(msg);
    }
    /**
     * kafka 平台币转Eth
     * @param operId
     */
    public void kafkaWithdrawCashLsbToEth(String operId){
        EsLsbaccounts esLsbaccounts = esLsbaccountsService.findEsLsbaccountsByOperId(operId);
        if(esLsbaccounts != null && esLsbaccounts.getId() != null){
            Member  member =  memberService.selectById(esLsbaccounts.getMemberId());
            if(member != null && member.getId() != null){
                SysConfig sysConfig = sysConfigService.getSysConfig();
                BigDecimal lsbToEth = esLsbaccounts.getAmount().divide(sysConfig.getLsbToEth(),4,BigDecimal.ROUND_DOWN);
                if(web3jService.getLsbManagerBalanceByEther().compareTo(lsbToEth) >= 0){
                    Map<String, Object>  rMap = web3jService.lsbManagerSendToETH(member.getPAddress(), lsbToEth);
                    if(rMap != null && !rMap.isEmpty()){
                        if(rMap.get(Web3jService.TRANSACTION_HASHVALUE) != null){
                            String tHValue = rMap.get(Web3jService.TRANSACTION_HASHVALUE).toString();
                            if(StringUtils.isNotBlank(tHValue)){
                                esLsbaccounts.setTransferHashValue(tHValue);
                                esLsbaccountsService.updateById(esLsbaccounts);
                            }
                        }
                        if(Integer.valueOf(rMap.get(Web3jService.TRANSACTION_STATUS).toString()) == 0){
                            //等待确认

                        }else if(Integer.valueOf(rMap.get(Web3jService.TRANSACTION_STATUS).toString()) == 1){
                            //成功
                            esLsbaccountsService.updateSuccess(esLsbaccounts.getId(),new BigDecimal(rMap.get(Web3jService.TRANSACTION_GASUSED).toString()));
                            //记录一条成功的ETH账目记录(入账)
                            esEthaccountsService.addInSuccess(esLsbaccounts.getMemberId(), EsEthaccountsServiceImpl.DIC_TYPE_LSBTOETH,lsbToEth,esLsbaccounts.getId().toString());
                        }else if(Integer.valueOf(rMap.get(Web3jService.TRANSACTION_STATUS).toString()) == 2){
                            //失败
                            esLsbaccountsService.updateFail(esLsbaccounts.getId());
                        }
                    }
                }else{
                    esLsbaccountsService.updateFail(esLsbaccounts.getId());
                }
            }
        }
    }


    /**
     * Eth转平台币
     * @param sum
     * @param playCode
     * @return
     */
    public ResultUtil withdrawCashEthToLsb(BigDecimal sum,String playCode){
        LoginUser loginUser = getLoginUser();
        //验证支付密码
        playCode = AesEncryptUtil.encrypt_code(playCode, Constants.KEY_TOW);
        if(!playCode.equals(loginUser.getPaymentCode())){
            String msg = "Payment password error!";
            if(super.isChineseLanguage()){
                msg = "支付密碼錯誤!";
            }
            return ResultUtil.errorJson(msg);
        }
        SysConfig sysConfig = sysConfigService.getSysConfig();
        BigDecimal ethToLsb = sum.divide(sysConfig.getEthToLsb(),4,BigDecimal.ROUND_DOWN);
        //判断余额是否可支付
        if(!esEthwalletService.judgeBalance(loginUser.getId(),ethToLsb)){
            String msg = "If the balance is insufficient or the remaining balance is not enough to cover the GAS cost, please reduce the value of ETH！";
            if(super.isChineseLanguage()){
                msg = "余額不足或剩余余額不夠GAS燃氣費用，請降低ETH數值";
            }
            return ResultUtil.errorJson(msg);
        }
//        Map<String,Object> map = ethTransferAccounts(loginUser.getWalletName(),playCode,loginUser.getPAddress(),sysConfig.getLsbAddress(),EthToLsb);
//        if(map == null && map.isEmpty()){
//            return ResultUtil.errorJson("Top-up failure !");
//        }
//        //新新增充值记录
//        boolean b = esLsbaccountsService.addInBeingprocessed(loginUser.getId(),EsLsbaccountsServiceImpl.DIC_TYPE_IN,sum,"-1",map.get(web3jService.TRANSACTION_HASHVALUE) == null ? "" : map.get(web3jService.TRANSACTION_HASHVALUE).toString());
//        if(!b){
//            return ResultUtil.errorJson("Transfer failed !");
//        }
        String uuId = CodeUtil.getUuid();
        boolean b = esLsbaccountsService.addInBeingprocessed(loginUser.getId(),EsLsbaccountsServiceImpl.DIC_TYPE_IN,sum,uuId);
        if(b) {
            //kafka 执行公链转账操作
            kafkaTemplate.send("withdrawCashEthToLsb","operId",uuId);
        }else{
            String msg = "Purchase failed, please try again !";
            if(super.isChineseLanguage()){
                msg = "購買失敗，請重試！";
            }
            return ResultUtil.errorJson(msg);
        }
        String msg = "Purchase successful !";
        if(super.isChineseLanguage()){
            msg = "購買成功！";
        }
        return  ResultUtil.successJson(msg);
    }
    /**
     * kafka Eth转平台币
     * @param operId
     */
    public void kafkaWithdrawCashEthToLsb(String operId){
        EsLsbaccounts esLsbaccounts = esLsbaccountsService.findEsLsbaccountsByOperId(operId);
        if(esLsbaccounts != null && esLsbaccounts.getId() != null){
            Member  member =  memberService.selectById(esLsbaccounts.getMemberId());
            if(member != null && member.getId() != null){
                SysConfig sysConfig = sysConfigService.getSysConfig();
                BigDecimal ethToLsb = esLsbaccounts.getAmount().divide(sysConfig.getEthToLsb(),4,BigDecimal.ROUND_DOWN);
                Map<String, Object>  rMap = web3jService.ethTransferAccounts(member.getWalletName(),member.getPaymentCodeWallet(),member.getPAddress(),sysConfig.getLsbAddress(),ethToLsb);
                if(rMap != null && !rMap.isEmpty()){
                    if(rMap.get(Web3jService.TRANSACTION_HASHVALUE) != null){
                        String tHValue = rMap.get(Web3jService.TRANSACTION_HASHVALUE).toString();
                        if(StringUtils.isNotBlank(tHValue)){
                            esLsbaccounts.setTransferHashValue(tHValue);
                            esLsbaccountsService.updateById(esLsbaccounts);
                        }
                    }
                    if(Integer.valueOf(rMap.get(Web3jService.TRANSACTION_STATUS).toString()) == 0){
                        //等待确认

                    }else if(Integer.valueOf(rMap.get(Web3jService.TRANSACTION_STATUS).toString()) == 1){
                        //成功
                        esLsbaccountsService.updateSuccess(esLsbaccounts.getId(),new BigDecimal(rMap.get(Web3jService.TRANSACTION_GASUSED).toString()));
                        //记录一条成功的ETH账目记录(出账)
                        esEthaccountsService.addOutSuccess(esLsbaccounts.getMemberId(),EsEthaccountsServiceImpl.DIC_TYPE_ETHTOLSB,ethToLsb,esLsbaccounts.getId().toString(),new BigDecimal(rMap.get(Web3jService.TRANSACTION_GASUSED).toString()));
                    }else if(Integer.valueOf(rMap.get(Web3jService.TRANSACTION_STATUS).toString()) == 2){
                        //失败
                        esLsbaccountsService.updateFail(esLsbaccounts.getId());
                    }
                }
            }
        }
    }

//
//    public Map<String,Object> loginUserEthTransferAccounts(String toAddress,BigDecimal etherValue){
//        LoginUser loginUser = super.getLoginUser();
//        return this.ethTransferAccounts(loginUser.getWalletName(),);
//    }



}
