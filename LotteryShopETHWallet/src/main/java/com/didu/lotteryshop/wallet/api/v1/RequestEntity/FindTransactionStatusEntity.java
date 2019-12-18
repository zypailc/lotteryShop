package com.didu.lotteryshop.wallet.api.v1.RequestEntity;
/**
 * 查询钱包转账状态请求实体
 * @author CHJ
 * @date 2019-12-18
 */
public class FindTransactionStatusEntity {
    private String transactionHashValue; //转账事务哈希码

    public String getTransactionHashValue() {
        return transactionHashValue;
    }

    public void setTransactionHashValue(String transactionHashValue) {
        this.transactionHashValue = transactionHashValue;
    }
}
