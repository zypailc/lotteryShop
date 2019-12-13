package com.didu.lotteryshop.lotterya.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

/**
 * <p>
 * A彩票期数表
 * </p>
 *
 * @author ${author}
 * @since 2019-11-07
 */
@TableName("lotterya_issue")
public class LotteryaIssue extends Model<LotteryaIssue> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 开启时间
     */
	@TableField("start_time")
	private Date startTime;
    /**
     * 结束时间
     */
	@TableField("end_time")
	private Date endTime;
    /**
     * 期数
     */
	@TableField("issue_num")
	private Integer issueNum;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 购买状态，0：开启，1：关闭
     */
	@TableField("buy_status")
	private String buyStatus;
    /**
     * 购买状态操作时间
     */
	@TableField("bs_time")
	private Date bsTime;
	/**
	 * 当期中奖金额
	 */
	@TableField("luck_total")
	private BigDecimal luckTotal;
	/**
	 *当期中奖号码
	 */
	@TableField("luck_num")
	private String luckNum;
	/**
	 *当期合约奖金
	 */
	@TableField("current_total")
	private BigDecimal currentTotal;
	/**
	 *当调节基金调节金额
	 */
	@TableField("adjust_total")
	private BigDecimal adjustTotal;
	/**
	 *奖金状态，0：未发放；1：已发放
	 */
	@TableField("bonus_status")
	private String bonusStatus;
	/**
	 * 奖金状态操作时间
	 */
	@TableField("bonus_status_time")
	private Date bonusStatusTime;
	/**
	 *允许发放奖金，0：不允许；1：允许发放
	 */
	@TableField("bonus_grant")
	private String bonusGrant;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getIssueNum() {
		return issueNum;
	}

	public void setIssueNum(Integer issueNum) {
		this.issueNum = issueNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBuyStatus() {
		return buyStatus;
	}

	public void setBuyStatus(String buyStatus) {
		this.buyStatus = buyStatus;
	}

	public Date getBsTime() {
		return bsTime;
	}

	public void setBsTime(Date bsTime) {
		this.bsTime = bsTime;
	}

	public BigDecimal getLuckTotal() {
		return luckTotal;
	}

	public void setLuckTotal(BigDecimal luckTotal) {
		this.luckTotal = luckTotal;
	}

	public String getLuckNum() {
		return luckNum;
	}

	public void setLuckNum(String luckNum) {
		this.luckNum = luckNum;
	}

	public BigDecimal getCurrentTotal() {
		return currentTotal;
	}

	public void setCurrentTotal(BigDecimal currentTotal) {
		this.currentTotal = currentTotal;
	}

	public BigDecimal getAdjustTotal() {
		return adjustTotal;
	}

	public void setAdjustTotal(BigDecimal adjustTotal) {
		this.adjustTotal = adjustTotal;
	}

	public String getBonusStatus() {
		return bonusStatus;
	}

	public void setBonusStatus(String bonusStatus) {
		this.bonusStatus = bonusStatus;
	}

	public Date getBonusStatusTime() {
		return bonusStatusTime;
	}

	public void setBonusStatusTime(Date bonusStatusTime) {
		this.bonusStatusTime = bonusStatusTime;
	}

	public String getBonusGrant() {
		return bonusGrant;
	}

	public void setBonusGrant(String bonusGrant) {
		this.bonusGrant = bonusGrant;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "LotteryaIssue{" +
			", id=" + id +
			", startTime=" + startTime +
			", endTime=" + endTime +
			", issueNum=" + issueNum +
			", createTime=" + createTime +
			", buyStatus=" + buyStatus +
			", bsTime=" + bsTime +
			", luckTotal=" + luckTotal +
			", luckNum=" + luckNum +
			", currentTotal=" + currentTotal +
			", adjustTotal=" + adjustTotal +
			", bonusStatus=" + bonusStatus +
			", bsTbonusStatusTimeime=" + bonusStatusTime +
			", bonusGrant=" + bonusGrant +
			"}";
	}
}
