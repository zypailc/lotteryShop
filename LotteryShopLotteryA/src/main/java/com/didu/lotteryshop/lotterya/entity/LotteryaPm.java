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
 * A彩票提成
 * </p>
 *
 * @author ${author}
 * @since 2019-12-03
 */
@TableName("lotterya_pm")
public class LotteryaPm extends Model<LotteryaPm> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 会员ID
     */
	@TableField("member_id")
	private String memberId;
    /**
     * A彩票期数ID
     */
	@TableField("lotterya_issue_id")
	private Integer lotteryaIssueId;
    /**
     * 金额(待领币)
     */
	private BigDecimal total;
    /**
     * 状态，0：待领取，1：已领取；2：作废
     */
	private String status;
    /**
     * 状态操作时间
     */
	@TableField("status_time")
	private Date statusTime;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 类型，1：购买提成，2：中奖提成
     */
	private Integer type;

	/**
	 * 转账状态，-1：未转账,0：等待确认，1：已经确，2：失败
	 */
	@TableField("transfer_status")
	private String transferStatus;
	/**
	 * 转账操作时间
	 */
	@TableField("transfer_status_time")
	private Date transferStatusTime;
	/**
	 * 转账has码，用于异步查询转账状态
	 */
	@TableField("transfer_hash_value")
	private String transferHashValue;
	/**
	 * 转账燃气费（ether）
	 */
	@TableField("transfer_gasfee")
	private BigDecimal transferGasfee;
	/**
	 * 金额（ether）
	 */
	@TableField("total_ether")
	private BigDecimal totalEther;


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

	public Integer getLotteryaIssueId() {
		return lotteryaIssueId;
	}

	public void setLotteryaIssueId(Integer lotteryaIssueId) {
		this.lotteryaIssueId = lotteryaIssueId;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStatusTime() {
		return statusTime;
	}

	public void setStatusTime(Date statusTime) {
		this.statusTime = statusTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getTransferHashValue() {
		return transferHashValue;
	}

	public void setTransferHashValue(String transferHashValue) {
		this.transferHashValue = transferHashValue;
	}

	public BigDecimal getTransferGasfee() {
		return transferGasfee;
	}

	public void setTransferGasfee(BigDecimal transferGasfee) {
		this.transferGasfee = transferGasfee;
	}

	public BigDecimal getTotalEther() {
		return totalEther;
	}

	public void setTotalEther(BigDecimal totalEther) {
		this.totalEther = totalEther;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "LotteryaPm{" +
			", id=" + id +
			", memberId=" + memberId +
			", lotteryaIssueId=" + lotteryaIssueId +
			", total=" + total +
			", status=" + status +
			", statusTime=" + statusTime +
			", createTime=" + createTime +
			", type=" + type +
			", transferStatus=" + transferStatus +
			", transferStatusTime=" + transferStatusTime +
			", transferHashValue=" + transferHashValue +
			", transferGasfee=" + transferGasfee +
			", totalEther=" + totalEther +
			"}";
	}
}
