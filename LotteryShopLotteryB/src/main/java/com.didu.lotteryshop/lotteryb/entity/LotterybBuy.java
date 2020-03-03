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
 * @since 2020-02-18
 */
@TableName("lotteryb_buy")
public class LotterybBuy extends Model<LotterybBuy> {

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * 购买人员Id
     */
	@TableField("member_id")
	private String memberId;
	@TableField("lotteryb_config_id")
	private Integer lotterybConfigId;
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

	public Integer getLotterybConfigId() {
		return lotterybConfigId;
	}

	public void setLotterybConfigId(Integer lotterybConfigId) {
		this.lotterybConfigId = lotterybConfigId;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "LotterybBuy{" +
			", id=" + id +
			", memberId=" + memberId +
			", lotterybConfigId=" + lotterybConfigId +
			", total=" + total +
			", lotterybInfoId=" + lotterybInfoId +
			", lotterybIssueId=" + lotterybIssueId +
			", isLuck=" + isLuck +
			", luckTotal=" + luckTotal +
			", createTime=" + createTime +
			"}";
	}
}
