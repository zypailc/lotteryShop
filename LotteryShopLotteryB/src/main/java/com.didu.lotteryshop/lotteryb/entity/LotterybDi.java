package com.didu.lotteryshop.lotteryb.entity;

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
 * 
 * </p>
 *
 * @author ${author}
 * @since 2020-03-12
 */
@TableName("lotteryb_di")
public class LotterybDi extends Model<LotterybDi> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 会员Id
     */
	@TableField("member_id")
	private String memberId;
    /**
     * 期数Id
     */
	@TableField("lotteryb_issue_id")
	private Integer lotterybIssueId;
    /**
     * 购买总金额
     */
	@TableField("buy_total")
	private BigDecimal buyTotal;
    /**
     * 推广分成比例(%)
     */
	@TableField("di_ratio")
	private BigDecimal diRatio;
    /**
     * 推广分成总额(lsb)
     */
	@TableField("di_total")
	private BigDecimal diTotal;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 经营分成比例（%）
     */
	@TableField("operation_ratio")
	private BigDecimal operationRatio;
    /**
     * 经营分成总额(lsb)
     */
	@TableField("operation_total")
	private BigDecimal operationTotal;

	/**
	 * 结算状态 : 0 已结算 ；1 未结算
	 */
	private Integer status;


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

	public BigDecimal getBuyTotal() {
		return buyTotal;
	}

	public void setBuyTotal(BigDecimal buyTotal) {
		this.buyTotal = buyTotal;
	}

	public BigDecimal getDiRatio() {
		return diRatio;
	}

	public void setDiRatio(BigDecimal diRatio) {
		this.diRatio = diRatio;
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

	public BigDecimal getOperationRatio() {
		return operationRatio;
	}

	public void setOperationRatio(BigDecimal operationRatio) {
		this.operationRatio = operationRatio;
	}

	public BigDecimal getOperationTotal() {
		return operationTotal;
	}

	public void setOperationTotal(BigDecimal operationTotal) {
		this.operationTotal = operationTotal;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "LotterybDi{" +
			", id=" + id +
			", memberId=" + memberId +
			", lotterybIssueId=" + lotterybIssueId +
			", buyTotal=" + buyTotal +
			", diRatio=" + diRatio +
			", diTotal=" + diTotal +
			", createTime=" + createTime +
			", operationRatio=" + operationRatio +
			", operationTotal=" + operationTotal +
			", status=" + status +
			"}";
	}
}
