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
@TableName("lotteryb_info")
public class LotterybInfo extends Model<LotterybInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
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
    /**
     * 购买最小金额
     */
    @TableField("min_money")
	private BigDecimal minMoney;
	/**
	 * 购买最大金额
	 */
	@TableField("max_money")
	private BigDecimal maxMoney;
    /**
     * 周期单位分钟
     */
	@TableField("period_date")
	private Integer periodDate;
    /**
     * 间隔时间
     */
	@TableField("interval_date")
	private Integer intervalDate;

	/**
	 * 提成的有效期数
	 * pm_vnum
	 * @return
	 */
	@TableField("pm_vnum")
	public Integer pmVnum;

    /**
     * 提成补签数
     */
	@TableField("pm_rnum")
    public Integer pmRnum;

	/**
	 * 购买提成比例
	 * @return
	 */
	@TableField("buy_pm")
	public BigDecimal buyPm;

	/**
	 * 中奖提成比例
	 * @return
	 */
	@TableField("winning_pm")
	public BigDecimal winningPm;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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


	public Integer getPeriodDate() {
		return periodDate;
	}

	public void setPeriodDate(Integer periodDate) {
		this.periodDate = periodDate;
	}

	public Integer getIntervalDate() {
		return intervalDate;
	}

	public void setIntervalDate(Integer intervalDate) {
		this.intervalDate = intervalDate;
	}

	public BigDecimal getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(BigDecimal minMoney) {
		this.minMoney = minMoney;
	}

	public BigDecimal getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(BigDecimal maxMoney) {
		this.maxMoney = maxMoney;
	}

    public Integer getPmVnum() {
        return pmVnum;
    }

    public void setPmVnum(Integer pmVnum) {
        this.pmVnum = pmVnum;
    }

    public Integer getPmRnum() {
        return pmRnum;
    }

    public void setPmRnum(Integer pmRnum) {
        this.pmRnum = pmRnum;
    }

	public BigDecimal getBuyPm() {
		return buyPm;
	}

	public void setBuyPm(BigDecimal buyPm) {
		this.buyPm = buyPm;
	}

	public BigDecimal getWinningPm() {
		return winningPm;
	}

	public void setWinningPm(BigDecimal winningPm) {
		this.winningPm = winningPm;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "LotterybInfo{" +
			", id=" + id +
			", zhTitle=" + zhTitle +
			", enTitle=" + enTitle +
			", minMoney=" + minMoney +
			", maxMoney=" + maxMoney +
			", periodDate=" + periodDate +
			", intervalDate=" + intervalDate +
			", pmVnum=" + pmVnum +
			", pmRnum=" + pmRnum +
			", buyPm=" + buyPm +
			", winningPm=" + winningPm +
			"}";
	}
}
