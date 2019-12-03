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
 * A彩票提成明细
 * </p>
 *
 * @author ${author}
 * @since 2019-12-03
 */
@TableName("lotterya_pm_detail")
public class LotteryaPmDetail extends Model<LotteryaPmDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * A彩票提成表主键ID
     */
	@TableField("lotterya_pm_id")
	private Integer lotteryaPmId;
    /**
     * A彩票购买记录ID
     */
	@TableField("lotterya_buy_id")
	private Integer lotteryaBuyId;
    /**
     * 提成金额
     */
	private BigDecimal total;
    /**
     * 层级
     */
	private Integer level;
    /**
     * 提成比例
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

	public Integer getLotteryaPmId() {
		return lotteryaPmId;
	}

	public void setLotteryaPmId(Integer lotteryaPmId) {
		this.lotteryaPmId = lotteryaPmId;
	}

	public Integer getLotteryaBuyId() {
		return lotteryaBuyId;
	}

	public void setLotteryaBuyId(Integer lotteryaBuyId) {
		this.lotteryaBuyId = lotteryaBuyId;
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
		return "LotteryaPmDetail{" +
			", id=" + id +
			", lotteryaPmId=" + lotteryaPmId +
			", lotteryaBuyId=" + lotteryaBuyId +
			", total=" + total +
			", level=" + level +
			", ratio=" + ratio +
			", createTime=" + createTime +
			"}";
	}
}
