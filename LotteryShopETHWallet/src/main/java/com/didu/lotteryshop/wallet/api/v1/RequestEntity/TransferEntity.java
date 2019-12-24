package com.didu.lotteryshop.wallet.api.v1.RequestEntity;

/**
 * 钱包转账请求实体
 * @author CHJ
 * @date 2019-12-18
 */
public class TransferEntity {
    private String walletFileName;//钱包名字
    private String payPassword;//支付密码
    private String formAddress;//出账地址
    private String toAddress;//入账地址
    private String etherValue;//etherValue

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

    public String getFormAddress() {
        return formAddress;
    }

    public void setFormAddress(String formAddress) {
        this.formAddress = formAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getEtherValue() {
        return etherValue;
    }

    public void setEtherValue(String etherValue) {
        this.etherValue = etherValue;
    }
}
