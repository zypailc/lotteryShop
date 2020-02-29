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
@TableName("lotteryb_pm_detail")
public class LotterybPmDetail extends Model<LotterybPmDetail> {

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * 竞猜提成Id
     */
	@TableField("lotteryb_pm_id")
	private Integer lotterybPmId;
    /**
     * 购买Id
     */
	@TableField("lotteryb_buy_id")
	private Integer lotterybBuyId;
    /**
     * 提成金额（代领币）
     */
	private BigDecimal total;
    /**
     * 层级
     */
	private Integer level;
    /**
     * 比例（%）
     */
	private BigDecimal ratio;
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

	public Integer getLotterybPmId() {
		return lotterybPmId;
	}

	public void setLotterybPmId(Integer lotterybPmId) {
		this.lotterybPmId = lotterybPmId;
	}

	public Integer getLotterybBuyId() {
		return lotterybBuyId;
	}

	public void setLotterybBuyId(Integer lotterybBuyId) {
		this.lotterybBuyId = lotterybBuyId;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
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
		return "LotterybPmDetail{" +
			", id=" + id +
			", lotterybPmId=" + lotterybPmId +
			", lotterybBuyId=" + lotterybBuyId +
			", total=" + total +
			", level=" + level +
			", ratio=" + ratio +
			", createTime=" + createTime +
			"}";
	}
}
