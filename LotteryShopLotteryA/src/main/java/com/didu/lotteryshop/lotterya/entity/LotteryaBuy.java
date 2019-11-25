package com.didu.lotteryshop.lotterya.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * A彩票购买记录
 * </p>
 *
 * @author ${author}
 * @since 2019-11-11
 */
@TableName("lotterya_buy")
public class LotteryaBuy extends Model<LotteryaBuy> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 购买人ID，对应es_member主键ID
     */
	@TableField("member_id")
	private String memberId;
    /**
     * 彩票号码
     */
	@TableField("luck_num")
	private String luckNum;
    /**
     * 第二关，0：未购买，1：极小，2：极大
     */
	@TableField("second_num")
	private String secondNum;
    /**
     * 第三关，0：未购买，1：顺子，2：豹子，3：龙角，4：幅面，5：两对
     */
	@TableField("thirdly_num")
	private String thirdlyNum;
    /**
     * 购买金额
     */
	private BigDecimal total;
    /**
     * 转账has码，用于异步查询转账状态
     */
	@TableField("transfer_hash_value")
	private String transferHashValue;
    /**
     * 转账状态，0：等待确认，1：已经确，2：失败
     */
	@TableField("transfer_status")
	private String transferStatus;
    /**
     * 转账操作时间
     */
	@TableField("transfer_status_time")
	private Date transferStatusTime;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getLuckNum() {
		return luckNum;
	}

	public void setLuckNum(String luckNum) {
		this.luckNum = luckNum;
	}

	public String getSecondNum() {
		return secondNum;
	}

	public void setSecondNum(String secondNum) {
		this.secondNum = secondNum;
	}

	public String getThirdlyNum() {
		return thirdlyNum;
	}

	public void setThirdlyNum(String thirdlyNum) {
		this.thirdlyNum = thirdlyNum;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getTransferHashValue() {
		return transferHashValue;
	}

	public void setTransferHashValue(String transferHashValue) {
		this.transferHashValue = transferHashValue;
	}

	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}

	public Date getTransferStatusTime() {
		return transferStatusTime;
	}

	public void setTransferStatusTime(Date transferStatusTime) {
		this.transferStatusTime = transferStatusTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "LotteryaBuy{" +
			", id=" + id +
			", memberId=" + memberId +
			", luckNum=" + luckNum +
			", secondNum=" + secondNum +
			", thirdlyNum=" + thirdlyNum +
			", total=" + total +
			", transferHashValue=" + transferHashValue +
			", transferStatus=" + transferStatus +
			", transferStatusTime=" + transferStatusTime +
			", createTime=" + createTime +
			"}";
	}
}
