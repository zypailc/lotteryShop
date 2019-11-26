package com.didu.lotteryshop.wallet.api.v1.RequestEntity;

/**
 * 查询钱包余额
 * @author CHJ
 * @date 2019-11-26
 */
public class FindBalanceEntity {
    private String address;//钱包地址

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
