package com.didu.lotteryshop.lotteryb.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
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
 * @since 2020-02-20
 */
@TableName("lotteryb_config")
public class LotterybConfig extends Model<LotterybConfig> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 类型
     */
	private Integer type;
    /**
     * 赔率
     */
	private BigDecimal lines;
    /**
     * 中文名称
     */
	@TableField("zh_title")
	private String zhTitle;
    /**
     * 英文名称
     */
	@TableField("en_title")
	private String enTitle;
	private Integer sort;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getLines() {
		return lines;
	}

	public void setLines(BigDecimal lines) {
		this.lines = lines;
	}

	public String getZhTitle() {
		return zhTitle;
	}

	public void setZhTitle(String zhTitle) {
		this.zhTitle = zhTitle;
	}

	public String getEnTitle() {
		return enTitle;
	}

	public void setEnTitle(String enTitle) {
		this.enTitle = enTitle;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "LotterybConfig{" +
			", id=" + id +
			", type=" + type +
			", lines=" + lines +
			", zhTitle=" + zhTitle +
			", enTitle=" + enTitle +
			", sort=" + sort +
			"}";
	}
}
