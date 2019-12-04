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
 * A彩票提成配置
 * </p>
 *
 * @author ${author}
 * @since 2019-12-03
 */
@TableName("lotterya_info_pc")
public class LotteryaInfoPc extends Model<LotteryaInfoPc> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * A彩票基本信息ID
     */
	@TableField("lotterya_info_id")
	private Integer lotteryaInfoId;
    /**
     * 类型，1：购买；2：中奖
     */
	private Integer type;
    /**
     * 层级
     */
	private Integer level;
    /**
     * 提成比例（单位%）
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

	public Integer getLotteryaInfoId() {
		return lotteryaInfoId;
	}

	public void setLotteryaInfoId(Integer lotteryaInfoId) {
		this.lotteryaInfoId = lotteryaInfoId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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
		return "LotteryaInfoPc{" +
			", id=" + id +
			", lotteryaInfoId=" + lotteryaInfoId +
			", type=" + type +
			", level=" + level +
			", ratio=" + ratio +
			", createTime=" + createTime +
			"}";
	}
}
