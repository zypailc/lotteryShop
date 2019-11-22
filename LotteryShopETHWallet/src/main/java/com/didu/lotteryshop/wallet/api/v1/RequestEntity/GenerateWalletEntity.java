package com.didu.lotteryshop.wallet.api.v1.RequestEntity;

import javax.validation.constraints.NotNull;

public class GenerateWalletEntity {
    private String userId;
    private String paymentCode;

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
