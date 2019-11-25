package com.didu.lotteryshop.wallet.api.v1.RequestEntity;

/**
 * 查询钱包明细请求实体
 * @author CHJ
 * @date 2019-11-25
 */
public class FindWalletDetailEntity {
    private String walletFileName;//钱包名称
    private String payPassword;//支付密码

    public String getWalletFileName() {
        return walletFileName;
    }

    public void setWalletFileName(String walletFileName) {
        this.walletFileName = walletFileName;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }
}
