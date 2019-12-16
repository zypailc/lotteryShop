package com.didu.lotteryshop.common.entity;

import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import lombok.Data;

@Data
public class LoginUser {

    private String id;
    private String memberName;//昵称
    private String email;//邮箱
    private String headPortraitUrl;//头像地址
    private String pAddress;//平台钱包地址
    private String bAddress;//绑定钱包地址
    private String WalletName;//钱包名称
    private String paymentCode;//支付密码

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadPortraitUrl() {
        return headPortraitUrl;
    }

    public void setHeadPortraitUrl(String headPortraitUrl) {
        this.headPortraitUrl = headPortraitUrl;
    }

    public String getPAddress() {
        return pAddress;
    }

    public void setPAddress(String pAddress) {
        this.pAddress = pAddress;
    }

    public String getBAddress() {
        return bAddress;
    }

    public void setBAddress(String bAddress) {
        this.bAddress = bAddress;
    }

    public String getWalletName() {
        return WalletName;
    }

    public void setWalletName(String walletName) {
        WalletName = walletName;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }
}
