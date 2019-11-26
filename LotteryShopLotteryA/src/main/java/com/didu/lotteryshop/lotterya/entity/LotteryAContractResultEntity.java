package com.didu.lotteryshop.lotterya.entity;

import java.math.BigDecimal;

/**
 * A彩票智能合约操作返回实体
 * @author CHJ
 * @date 2019-11-26
 */
public class LotteryAContractResultEntity {
    /** 延迟处理中 */
    public static final int STATUS_WAIT = 0;
    /** 成功 */
    public static final int STATUS_SUCCESS = 1;
    /** 失败 */
    public static final int STATUS_FAIL = 2;
    /** 状态 */
    private int status;
    /** 事务哈希值，可用于异步查询交易状态（延迟处理，等待旷工接单处理的事务） */
    private String transactionHash;
    /** 信息 */
    private String msg;
    /** 燃气费（单位ether） */
    private BigDecimal gasUsed;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BigDecimal getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(BigDecimal gasUsed) {
        this.gasUsed = gasUsed;
    }
}
