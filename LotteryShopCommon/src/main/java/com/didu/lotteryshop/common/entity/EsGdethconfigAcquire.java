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
 * 推广分成eth钱包配置百分比分成表
 * </p>
 *
 * @author ${author}
 * @since 2019-12-18
 */
@TableName("es_gdethconfig_acquire")
public class EsGdethconfigAcquire extends Model<EsGdethconfigAcquire> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 推广分成eth钱包分成配置表ID
     */
	@TableField("gdethconfig_id")
	private Integer gdethconfigId;
    /**
     * 活跃人数
     */
	@TableField("active_num")
	private Integer activeNum;
    /**
     * 分成比例（单位%）
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

	public Integer getGdethconfigId() {
		return gdethconfigId;
	}

	public void setGdethconfigId(Integer gdethconfigId) {
		this.gdethconfigId = gdethconfigId;
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
		return "EsGdethconfigAcquire{" +
			", id=" + id +
			", gdethconfigId=" + gdethconfigId +
			", activeNum=" + activeNum +
			", ratio=" + ratio +
			", createTime=" + createTime +
			"}";
	}
}
