package com.didu.lotteryshop.lotteryb.entity;

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
 * 
 * </p>
 *
 * @author ${author}
 * @since 2020-02-29
 */
@TableName("lotteryb_info_pc")
public class LotterybInfoPc extends Model<LotterybInfoPc> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 竞猜玩法Id
     */
	@TableField("lotteryb_info_id")
	private Integer lotterybInfoId;
    /**
     * 类型：1：提成 ；2： 中奖
     */
	private Integer type;
    /**
     * 层级
     */
	private Integer level;
    /**
     * 比例（%）
     */
	private BigDecimal ratio;
	@TableField("create_time")
	private Date createTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLotterybInfoId() {
		return lotterybInfoId;
	}

	public void setLotterybInfoId(Integer lotterybInfoId) {
		this.lotterybInfoId = lotterybInfoId;
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
		return "LotterybInfoPc{" +
			", id=" + id +
			", lotterybInfoId=" + lotterybInfoId +
			", type=" + type +
			", level=" + level +
			", ratio=" + ratio +
			", createTime=" + createTime +
			"}";
	}
}
