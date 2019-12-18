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
 * A彩票推广账户分成表
 * </p>
 *
 * @author ${author}
 * @since 2019-12-17
 */
@TableName("lotterya_di")
public class LotteryaDi extends Model<LotteryaDi> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 会员ID
     */
	@TableField("member_id")
	private String memberId;
    /**
     * 彩票期数ID
     */
	@TableField("lotterya_issue_id")
	private Integer lotteryaIssueId;
    /**
     * 购买总额（ether）
     */
	@TableField("buy_total")
	private BigDecimal buyTotal;
    /**
     * 购买总注数
     */
	@TableField("buy_cnts")
	private Integer buyCnts;
    /**
     * 中奖总额（ether）
     */
	@TableField("luck_total")
	private BigDecimal luckTotal;
    /**
     * 中奖总注数
     */
	@TableField("luck_cnts")
	private Integer luckCnts;
    /**
     * 活跃总人数
     */
	@TableField("active_mcnts")
	private Integer activeMcnts;
    /**
     * 中奖上级提成比例（%）
     */
	@TableField("luck_ratio")
	private BigDecimal luckRatio;
    /**
     * 推广分成比例（%）
     */
	@TableField("di_ratio")
	private BigDecimal diRatio;
    /**
     * 中奖上级提成总额（ether）
     */
	@TableField("luck_di_total")
	private BigDecimal luckDiTotal;
    /**
     * 推广分成总额（ether）
     */
	@TableField("di_total")
	private BigDecimal diTotal;
    /**
     * 创建日期
     */
	@TableField("create_time")
	private Date createTime;
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
     * 转账has码，用于异步查询转账状态
     */
	@TableField("transfer_hash_value")
	private String transferHashValue;


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

	public BigDecimal getBuyTotal() {
		return buyTotal;
	}

	public void setBuyTotal(BigDecimal buyTotal) {
		this.buyTotal = buyTotal;
	}

	public Integer getBuyCnts() {
		return buyCnts;
	}

	public void setBuyCnts(Integer buyCnts) {
		this.buyCnts = buyCnts;
	}

	public BigDecimal getLuckTotal() {
		return luckTotal;
	}

	public void setLuckTotal(BigDecimal luckTotal) {
		this.luckTotal = luckTotal;
	}

	public Integer getLuckCnts() {
		return luckCnts;
	}

	public void setLuckCnts(Integer luckCnts) {
		this.luckCnts = luckCnts;
	}

	public Integer getActiveMcnts() {
		return activeMcnts;
	}

	public void setActiveMcnts(Integer activeMcnts) {
		this.activeMcnts = activeMcnts;
	}

	public BigDecimal getLuckRatio() {
		return luckRatio;
	}

	public void setLuckRatio(BigDecimal luckRatio) {
		this.luckRatio = luckRatio;
	}

	public BigDecimal getDiRatio() {
		return diRatio;
	}

	public void setDiRatio(BigDecimal diRatio) {
		this.diRatio = diRatio;
	}

	public BigDecimal getLuckDiTotal() {
		return luckDiTotal;
	}

	public void setLuckDiTotal(BigDecimal luckDiTotal) {
		this.luckDiTotal = luckDiTotal;
	}

	public BigDecimal getDiTotal() {
		return diTotal;
	}

	public void setDiTotal(BigDecimal diTotal) {
		this.diTotal = diTotal;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "LotteryaDi{" +
			", id=" + id +
			", memberId=" + memberId +
			", lotteryaIssueId=" + lotteryaIssueId +
			", buyTotal=" + buyTotal +
			", buyCnts=" + buyCnts +
			", luckTotal=" + luckTotal +
			", luckCnts=" + luckCnts +
			", activeMcnts=" + activeMcnts +
			", luckRatio=" + luckRatio +
			", diRatio=" + diRatio +
			", luckDiTotal=" + luckDiTotal +
			", diTotal=" + diTotal +
			", createTime=" + createTime +
			", transferStatus=" + transferStatus +
			", transferStatusTime=" + transferStatusTime +
			", transferHashValue=" + transferHashValue +
			"}";
	}
}
