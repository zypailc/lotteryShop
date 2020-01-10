package com.didu.lotteryshop.common.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String memberName;//昵称
    private String email;//邮箱
    private String headPortraitUrl;//头像地址
    private String pAddress;//平台钱包地址
    private String bAddress;//绑定钱包地址
    private String WalletName;//钱包名称
    private String paymentCode;//支付密码
    private String password;//登陆密码
    private String paymentCodeWallet;//生成钱包的密文
    private String generalizeType;// 推广类型
    private String moneyView;//金额是否显示
    private String noticeView;//公告是否已读
    private String loginDate;//上次登录时间

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPaymentCodeWallet() {
        return paymentCodeWallet;
    }

    public void setPaymentCodeWallet(String paymentCodeWallet) {
        this.paymentCodeWallet = paymentCodeWallet;
    }

    public String getGeneralizeType() {
        return generalizeType;
    }

    public void setGeneralizeType(String generalizeType) {
        this.generalizeType = generalizeType;
    }

    public String getMoneyView() {
        return moneyView;
    }

    public void setMoneyView(String moneyView) {
        this.moneyView = moneyView;
    }

    public String getNoticeView() {
        return noticeView;
    }

    public void setNoticeView(String noticeView) {
        this.noticeView = noticeView;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }
}
