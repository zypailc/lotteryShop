package com.didu.lotteryshop.lotteryb.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2020-02-20
 */
@TableName("lotteryb_issue")
public class LotterybIssue extends Model<LotterybIssue> {

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * 开始时间
     */
	@TableField("start_time")
	private Date startTime;
    /**
     * 结束时间
     */
	@TableField("end_time")
	private Date endTime;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 期数
     */
	@TableField("issue_num")
	private String issueNum;
    /**
     * 购买状态 ：0 开启； 1 关闭
     */
	@TableField("by_status")
	private Integer byStatus;
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
     * 当期中奖号码
     */
	@TableField("luck_num")
	private String luckNum;
    /**
     * 奖金状态，0：未发放；1：已发放
     */
	@TableField("bonus_status")
	private String bonusStatus;
    /**
     * 奖金状态操作时间
     */
	@TableField("bonus_status_time")
	private Date bonusStatusTime;
    /**
     * 允许发放奖金，0：不允许；1：允许发放
     */
	@TableField("bonus_grant")
	private Integer bonusGrant;
	/**
	 * 玩法ID
	 */
	@TableField("lotteryb_info_id")
	private Integer lotterybInfoId;

	/**
	 * 奖励配置Id
	 */
	@TableField("lotteryb_proportion_id")
	private Integer lotterybProportionId;

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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIssueNum() {
		return issueNum;
	}

	public void setIssueNum(String issueNum) {
		this.issueNum = issueNum;
	}

	public Integer getByStatus() {
		return byStatus;
	}

	public void setByStatus(Integer byStatus) {
		this.byStatus = byStatus;
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

	public Integer getBonusGrant() {
		return bonusGrant;
	}

	public void setBonusGrant(Integer bonusGrant) {
		this.bonusGrant = bonusGrant;
	}

	public Integer getLotterybInfoId() {
		return lotterybInfoId;
	}

	public void setLotterybInfoId(Integer lotterybInfoId) {
		this.lotterybInfoId = lotterybInfoId;
	}

	public Integer getLotterybProportionId() {
		return lotterybProportionId;
	}

	public void setLotterybProportionId(Integer lotterybProportionId) {
		this.lotterybProportionId = lotterybProportionId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "LotterybIssue{" +
			", id=" + id +
			", startTime=" + startTime +
			", endTime=" + endTime +
			", createTime=" + createTime +
			", issueNum=" + issueNum +
			", byStatus=" + byStatus +
			", bsTime=" + bsTime +
			", luckTotal=" + luckTotal +
			", luckNum=" + luckNum +
			", bonusStatus=" + bonusStatus +
			", bonusStatusTime=" + bonusStatusTime +
			", bonusGrant=" + bonusGrant +
			", lotterybInfoId=" + lotterybInfoId +
			", lotterybProportionId=" + lotterybProportionId +
			"}";
	}
}
