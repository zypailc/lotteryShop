package com.didu.lotteryshop.common.entity;

public class EthTransfer {

    /**
     * 事物Hash值
     */
    private String transactionHashvalue;

    /**
     * 事物返回状态
     */
    private String transactionStatus;

    /**
     * 转账产生的燃气费
     */
    private String transactionGasUsed;

    public String getTransactionHashvalue() {
        return transactionHashvalue;
    }

    public void setTransactionHashvalue(String transactionHashvalue) {
        this.transactionHashvalue = transactionHashvalue;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionGasUsed() {
        return transactionGasUsed;
    }

    public void setTransactionGasUsed(String transactionGasUsed) {
        this.transactionGasUsed = transactionGasUsed;
    }
}
