package com.didu.lotteryshop.lotterya.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * A彩票基础信息
 * </p>
 *
 * @author ${author}
 * @since 2019-11-07
 */
@TableName("lotterya_info")
public class LotteryaInfo extends Model<LotteryaInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Integer id;
    /**
     * 彩票名称
     */
	private String title;
    /**
     * 彩票单价
     */
	private BigDecimal price;
    /**
     * 周期（单位小时）
     */
	@TableField("period_date")
	private BigDecimal periodDate;
    /**
     * 间隔时间（单位小时）
     */
	@TableField("interval_date")
	private BigDecimal intervalDate;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPeriodDate() {
		return periodDate;
	}

	public void setPeriodDate(BigDecimal periodDate) {
		this.periodDate = periodDate;
	}

	public BigDecimal getIntervalDate() {
		return intervalDate;
	}

	public void setIntervalDate(BigDecimal intervalDate) {
		this.intervalDate = intervalDate;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "LotteryaInfo{" +
			", id=" + id +
			", title=" + title +
			", price=" + price +
			", periodDate=" + periodDate +
			", intervalDate=" + intervalDate +
			"}";
	}
}
