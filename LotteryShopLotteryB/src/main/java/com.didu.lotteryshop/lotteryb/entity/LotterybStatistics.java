package com.didu.lotteryshop.lotteryb.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2020-02-22
 */
@TableName("lotteryb_statistics")
public class LotterybStatistics extends Model<LotterybStatistics> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 玩法配置Id
     */
	@TableField("lotteryb_number")
	private Integer lotterybNumber;
    /**
     * 期数Id
     */
	@TableField("lotteryb_issue_id")
	private Integer lotterybIssueId;
	/**
	 * 购买统计金额
	 */
	private BigDecimal amount;

	/**
	 * 玩法ID
	 */
	@TableField("lotteryb_info_id")
	private Integer lotterybInfoId;

	/**
	 * 创建时间
	 */
	@TableField("create_date")
	public Date createDate;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getLotterybNumber() {
		return lotterybNumber;
	}

	public void setLotterybNumber(Integer lotterybNumber) {
		this.lotterybNumber = lotterybNumber;
	}

	public Integer getLotterybIssueId() {
		return lotterybIssueId;
	}

	public void setLotterybIssueId(Integer lotterybIssueId) {
		this.lotterybIssueId = lotterybIssueId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getLotterybInfoId() {
		return lotterybInfoId;
	}

	public void setLotterybInfoId(Integer lotterybInfoId) {
		this.lotterybInfoId = lotterybInfoId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "LotterybStatistics{" +
			", id=" + id +
			", lotterybNumber=" + lotterybNumber +
			", lotterybIssueId=" + lotterybIssueId +
			", amount=" + amount +
			", createDate=" + createDate +
			", lotterybInfoId=" + lotterybInfoId +
			"}";
	}
}
