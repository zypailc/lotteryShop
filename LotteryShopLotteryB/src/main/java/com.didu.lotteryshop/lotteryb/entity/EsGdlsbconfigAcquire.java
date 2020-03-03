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
@TableName("es_gdlsbconfig_acquire")
public class EsGdlsbconfigAcquire extends Model<EsGdlsbconfigAcquire> {

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * Lsb分成配置表
     */
	@TableField("es_gdlsbconfig_id")
	private Integer esGdlsbconfigId;
    /**
     * 活跃人数
     */
	@TableField("active_num")
	private Integer activeNum;
    /**
     * 分成比例(单位%)
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

	public Integer getEsGdlsbconfigId() {
		return esGdlsbconfigId;
	}

	public void setEsGdlsbconfigId(Integer esGdlsbconfigId) {
		this.esGdlsbconfigId = esGdlsbconfigId;
	}

	public Integer getActiveNum() {
		return activeNum;
	}

	public void setActiveNum(Integer activeNum) {
		this.activeNum = activeNum;
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
		return "EsGdlsbconfigAcquire{" +
			", id=" + id +
			", esGdlsbconfigId=" + esGdlsbconfigId +
			", activeNum=" + activeNum +
			", ratio=" + ratio +
			", createTime=" + createTime +
			"}";
	}
}
