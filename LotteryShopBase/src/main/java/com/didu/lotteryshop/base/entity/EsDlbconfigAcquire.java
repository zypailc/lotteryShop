package com.didu.lotteryshop.base.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 平台待领币配置百分比提取表
 * </p>
 *
 * @author ${author}
 * @since 2019-12-04
 */
@TableName("es_dlbconfig_acquire")
public class EsDlbconfigAcquire extends Model<EsDlbconfigAcquire> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Integer id;
    /**
     * 平台待领币提取配置表ID
     */
	@TableField("dlbconfig_id")
	private Integer dlbconfigId;
    /**
     * 活跃人数
     */
	@TableField("active_num")
	private Integer activeNum;
    /**
     * 提取比例（单位%）
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

	public Integer getDlbconfigId() {
		return dlbconfigId;
	}

	public void setDlbconfigId(Integer dlbconfigId) {
		this.dlbconfigId = dlbconfigId;
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
		return "EsDlbconfigAcquire{" +
			", id=" + id +
			", dlbconfigId=" + dlbconfigId +
			", activeNum=" + activeNum +
			", ratio=" + ratio +
			", createTime=" + createTime +
			"}";
	}
}
