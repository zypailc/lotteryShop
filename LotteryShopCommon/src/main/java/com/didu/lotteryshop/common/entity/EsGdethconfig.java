package com.didu.lotteryshop.common.entity;

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
 * 推广分成eth钱包分成配置表
 * </p>
 *
 * @author ${author}
 * @since 2019-12-18
 */
@TableName("es_gdethconfig")
public class EsGdethconfig extends Model<EsGdethconfig> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 周期（单位天）
     */
	@TableField("cycle_day")
	private Integer cycleDay;
    /**
     * 周期内消费总额（单位ether）
     */
	@TableField("consume_total")
	private BigDecimal consumeTotal;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 计算活跃用户层级,-1为无限层级
     */
	@TableField("c_level")
	private Integer cLevel;
	/**
	 * 分成比例（%）
	 */
	@TableField("di_ratio")
	private BigDecimal diRatio;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCycleDay() {
		return cycleDay;
	}

	public void setCycleDay(Integer cycleDay) {
		this.cycleDay = cycleDay;
	}

	public BigDecimal getConsumeTotal() {
		return consumeTotal;
	}

	public void setConsumeTotal(BigDecimal consumeTotal) {
		this.consumeTotal = consumeTotal;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getcLevel() {
		return cLevel;
	}

	public void setcLevel(Integer cLevel) {
		this.cLevel = cLevel;
	}

	public BigDecimal getDiRatio() {
		return diRatio;
	}

	public void setDiRatio(BigDecimal diRatio) {
		this.diRatio = diRatio;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "EsGdethconfig{" +
			", id=" + id +
			", cycleDay=" + cycleDay +
			", consumeTotal=" + consumeTotal +
			", createTime=" + createTime +
			", cLevel=" + cLevel +
			", diRatio=" + diRatio +
			"}";
	}
}
