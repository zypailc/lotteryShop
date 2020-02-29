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
 * @since 2020-02-29
 */
@TableName("lotteryb_pm")
public class LotterybPm extends Model<LotterybPm> {

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * 人员Id
     */
	@TableField("member_id")
	private String memberId;
    /**
     * 竞猜期数Id
     */
	@TableField("lotteryb_issue_id")
	private Integer lotterybIssueId;
    /**
     * 金额（待领币）
     */
	private BigDecimal total;
    /**
     * 状态 ：0：待领取 ；1：已领取；2：作废
     */
	private Integer status;
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
     * 类型：1：购买提成，2：中奖提成
     */
	private Integer type;

	/**
	 * 玩法Id
	 */
	@TableField("lotteryb_info_id")
	private Integer lotterybInfoId;


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

	public Integer getLotterybIssueId() {
		return lotterybIssueId;
	}

	public void setLotterybIssueId(Integer lotterybIssueId) {
		this.lotterybIssueId = lotterybIssueId;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
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

	public Integer getLotterybInfoId() {
		return lotterybInfoId;
	}

	public void setLotterybInfoId(Integer lotterybInfoId) {
		this.lotterybInfoId = lotterybInfoId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "LotterybPm{" +
			", id=" + id +
			", memberId=" + memberId +
			", lotterybIssueId=" + lotterybIssueId +
			", total=" + total +
			", status=" + status +
			", statusTime=" + statusTime +
			", createTime=" + createTime +
			", type=" + type +
			", lotterybInfoId=" + lotterybInfoId +
			"}";
	}
}
