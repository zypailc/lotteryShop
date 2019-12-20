package com.didu.lotteryshop.base.api.v1.service;

import com.didu.lotteryshop.base.service.BaseBaseService;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.entity.SysConfig;
import com.didu.lotteryshop.common.service.form.impl.SysConfigServiceImpl;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService extends BaseBaseService {


    @Autowired
    private SysConfigServiceImpl sysConfigService;
    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

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
        if(!playCode.equals(loginUser.getPaymentCode())){
            return ResultUtil.errorJson(" wrong password !");
        }
        //TODO 调用提现接口
        return  ResultUtil.successJson("");
    }

    /**
     * 平台币转Eth
     * @param sum
     * @return
     */
    public ResultUtil withdrawCashLsbToEth(BigDecimal sum){
        //TODO 调用平台币转Eth接口
        return ResultUtil.successJson("");
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
        if(!playCode.equals(loginUser.getPaymentCode())){
            return  ResultUtil.errorJson(" wrong password !");
        }
        //TODO 调用Eth转平台币接口
        return  ResultUtil.successJson("");
    }

}
