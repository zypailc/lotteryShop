package com.didu.lotteryshop.lotteryb.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2020-02-18
 */
@TableName("lotteryb_buy")
public class LotterybBuy extends Model<LotterybBuy> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 购买人员Id
     */
	@TableField("member_id")
	private String memberId;
	@TableField("lotteryb_config_ids")
	private String lotterybConfigIds;
    /**
     * 购买金额（平台币）
     */
	private BigDecimal total;
    /**
     * lotteryB的Id
     */
	@TableField("lotteryb_info_id")
	private Integer lotterybInfoId;
    /**
     * 竞猜期数Id
     */
	@TableField("lotteryb_issue_id")
	private Integer lotterybIssueId;
    /**
     * 是否中奖；0：否；1：是
     */
	@TableField("is_luck")
	private Integer isLuck;
    /**
     * 中奖金额
     */
	@TableField("luck_total")
	private BigDecimal luckTotal;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;

	/**
	 * 操作流水号
	 */
	@TableField("serial_number")
	private String serialNumber;


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

	public String getLotterybConfigIds() {
		return lotterybConfigIds;
	}

	public void setLotterybConfigIds(String lotterybConfigIds) {
		this.lotterybConfigIds = lotterybConfigIds;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Integer getLotterybInfoId() {
		return lotterybInfoId;
	}

	public void setLotterybInfoId(Integer lotterybInfoId) {
		this.lotterybInfoId = lotterybInfoId;
	}

	public Integer getLotterybIssueId() {
		return lotterybIssueId;
	}

	public void setLotterybIssueId(Integer lotterybIssueId) {
		this.lotterybIssueId = lotterybIssueId;
	}

	public Integer getIsLuck() {
		return isLuck;
	}

	public void setIsLuck(Integer isLuck) {
		this.isLuck = isLuck;
	}

	public BigDecimal getLuckTotal() {
		return luckTotal;
	}

	public void setLuckTotal(BigDecimal luckTotal) {
		this.luckTotal = luckTotal;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "LotterybBuy{" +
			", id=" + id +
			", memberId=" + memberId +
			", lotterybConfigIds=" + lotterybConfigIds +
			", total=" + total +
			", lotterybInfoId=" + lotterybInfoId +
			", lotterybIssueId=" + lotterybIssueId +
			", isLuck=" + isLuck +
			", luckTotal=" + luckTotal +
			", createTime=" + createTime +
			"}";
	}
}
