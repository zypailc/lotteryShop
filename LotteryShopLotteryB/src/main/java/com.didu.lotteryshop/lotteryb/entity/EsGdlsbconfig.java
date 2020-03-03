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
 * @since 2020-03-03
 */
@TableName("es_gdlsbconfig")
public class EsGdlsbconfig extends Model<EsGdlsbconfig> {

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * 周期（单位天）
     */
	@TableField("cycle_day")
	private Integer cycleDay;
    /**
     * 周期内消费总额（单位Lsb）
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
     * 分成比例
     */
	@TableField("di_ratio")
	private BigDecimal diRatio;
    /**
     * 竞猜玩法配置Id
     */
	@TableField("lotteryb_info_id")
	private Integer lotterybInfoId;


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
		return "EsGdlsbconfig{" +
			", id=" + id +
			", cycleDay=" + cycleDay +
			", consumeTotal=" + consumeTotal +
			", createTime=" + createTime +
			", cLevel=" + cLevel +
			", diRatio=" + diRatio +
			", lotterybInfoId=" + lotterybInfoId +
			"}";
	}
}
