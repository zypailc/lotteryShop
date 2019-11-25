package com.didu.lotteryshop.wallet.api.v1.RequestEntity;

/**
 * 创建钱包请求实体
 * @author CHJ
 * @Date 2019-11-25
 */
public class GenerateWalletEntity {
    private String userId;//用户ID
    private String paymentCode; //交易密码

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }
}
