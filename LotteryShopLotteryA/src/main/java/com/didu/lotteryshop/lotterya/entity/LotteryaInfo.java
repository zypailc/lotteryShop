package com.didu.lotteryshop.lotterya.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * A彩票基础信息
 * </p>
 *
 * @author ${author}
 * @since 2019-11-07
 */
@TableName("lotterya_info")
public class LotteryaInfo extends Model<LotteryaInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Integer id;
    /**
     * 中文彩票名称
     */
	private String zhtitle;
    /**
     * 彩票单价
     */
	private BigDecimal price;
    /**
     * 周期（单位分钟）
     */
	@TableField("period_date")
	private BigDecimal periodDate;
    /**
     * 间隔时间（单位分钟）
     */
	@TableField("interval_date")
	private BigDecimal intervalDate;
	/**
	 * 英文彩票名称
	 */
	private String entitle;
	/**
	 * 合约地址
	 */
	@TableField("contract_address")
	private String contractAddress;

	/**
	 * 当期奖金下限（单位ether）
	 */
	@TableField("curren_bonus_down")
	private BigDecimal currenBonusDown;

	/**
	 * 当期奖金倍数比例（单位%）
	 */
	@TableField("curren_bonus_ratio")
	private BigDecimal currenBonusRatio;

	/**
	 * 调节基金上限（单位ether）
	 */
	@TableField("adjust_bonus_up")
	private BigDecimal adjustBonusUp;

	/**
	 * 调节基金下限（单位ether）
	 */
	@TableField("adjust_bonus_down")
	private BigDecimal adjustBonusDown;

	/**
	 * 调节基金倍数比率（单位%）
	 */
	@TableField("adjust_bonus_ratio")
	private BigDecimal adjustBonusRatio;
	/**
	 * 提成有效xx期数
	 */
	@TableField("pm_vnum")
	private Integer pmVnum;
	/**
	 * 提成补签xx注数
	 */
	@TableField("pm_rnum")
	private Integer pmRnum;
	/**
	 * 购买提成比例（单位%）
	 */
	@TableField("buy_pm")
	private BigDecimal buyPm;
	/**
	 * 中奖提成比例（单位%）
	 */
	@TableField("draw_pm")
	private BigDecimal drawPm;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getZhtitle() {
		return zhtitle;
	}

	public void setZhtitle(String zhtitle) {
		this.zhtitle = zhtitle;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPeriodDate() {
		return periodDate;
	}

	public void setPeriodDate(BigDecimal periodDate) {
		this.periodDate = periodDate;
	}

	public BigDecimal getIntervalDate() {
		return intervalDate;
	}

	public void setIntervalDate(BigDecimal intervalDate) {
		this.intervalDate = intervalDate;
	}

	public String getEntitle() {
		return entitle;
	}

	public void setEntitle(String entitle) {
		this.entitle = entitle;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}

	public BigDecimal getCurrenBonusDown() {
		return currenBonusDown;
	}

	public void setCurrenBonusDown(BigDecimal currenBonusDown) {
		this.currenBonusDown = currenBonusDown;
	}

	public BigDecimal getCurrenBonusRatio() {
		return currenBonusRatio;
	}

	public void setCurrenBonusRatio(BigDecimal currenBonusRatio) {
		this.currenBonusRatio = currenBonusRatio;
	}

	public BigDecimal getAdjustBonusUp() {
		return adjustBonusUp;
	}

	public void setAdjustBonusUp(BigDecimal adjustBonusUp) {
		this.adjustBonusUp = adjustBonusUp;
	}

	public BigDecimal getAdjustBonusDown() {
		return adjustBonusDown;
	}

	public void setAdjustBonusDown(BigDecimal adjustBonusDown) {
		this.adjustBonusDown = adjustBonusDown;
	}

	public BigDecimal getAdjustBonusRatio() {
		return adjustBonusRatio;
	}

	public void setAdjustBonusRatio(BigDecimal adjustBonusRatio) {
		this.adjustBonusRatio = adjustBonusRatio;
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

	public BigDecimal getDrawPm() {
		return drawPm;
	}

	public void setDrawPm(BigDecimal drawPm) {
		this.drawPm = drawPm;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "LotteryaInfo{" +
			", id=" + id +
			", entitle=" + entitle +
			", price=" + price +
			", periodDate=" + periodDate +
			", intervalDate=" + intervalDate +
			", contractAddress=" + contractAddress +
			", currenBonusDown=" + currenBonusDown +
			", currenBonusRatio=" + currenBonusRatio +
			", adjustBonusUp=" + adjustBonusUp +
			", adjustBonusDown=" + adjustBonusDown +
			", adjustBonusRatio=" + adjustBonusRatio +
			", pmVnum=" + pmVnum +
			", pmRnum=" + pmRnum +
			", buyPm=" + buyPm +
			", drawPm=" + drawPm +
			"}";
	}
}
