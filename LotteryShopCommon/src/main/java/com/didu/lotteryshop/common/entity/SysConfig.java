package com.didu.lotteryshop.common.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author ${author}
 * @since 2019-11-25
 */
@TableName("sys_config")
public class SysConfig extends Model<SysConfig> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Integer id;
    /**
     * 燃气费单价
     */
	@TableField("gas_price")
	private BigDecimal gasPrice;
    /**
     * 燃气限制
     */
	@TableField("gas_limit")
	private BigDecimal gasLimit;
	/**
	 * 以太币兑换平台币，1：xx
	 */
	@TableField("eth_to_lsb")
	private BigDecimal ethToLsb;
	/**
	 * 平台币兑换以太币，xx：1
	 */
	@TableField("lsb_to_eth")
	private BigDecimal lsbToEth;
	/**
	 *系统推广分成钱包地址
	 */
	@TableField("di_address")
	private String diAddress;
	/**
	 *系统平台币钱包地址
	 */
	@TableField("lsb_address")
	private String lsbAddress;
	/**
	 *系统平台管理员ETH钱包地址
	 */
	@TableField("manager_address")
	private String managerAddress;
	/**
	 * ETH钱包提现手续费比例（%）
	 */
	@TableField("withdraw_ratio")
	private BigDecimal withdrawRatio;
	/**
	 *平台币兑换Eth最低限制（平台币）
	 */
	@TableField("lsbwithdraw_min")
	private BigDecimal lsbwithdrawMin;

	/**
	 * ETH转平台币的最小限制
	 */
	@TableField("ethwithdraw_min")
	private BigDecimal ethwithdrawMin;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getGasPrice() {
		return gasPrice;
	}

	public void setGasPrice(BigDecimal gasPrice) {
		this.gasPrice = gasPrice;
	}

	public BigDecimal getGasLimit() {
		return gasLimit;
	}

	public void setGasLimit(BigDecimal gasLimit) {
		this.gasLimit = gasLimit;
	}

	public BigDecimal getEthToLsb() {
		return ethToLsb;
	}

	public void setEthToLsb(BigDecimal ethToLsb) {
		this.ethToLsb = ethToLsb;
	}

	public BigDecimal getLsbToEth() {
		return lsbToEth;
	}

	public void setLsbToEth(BigDecimal lsbToEth) {
		this.lsbToEth = lsbToEth;
	}

	public String getDiAddress() {
		return diAddress;
	}

	public void setDiAddress(String diAddress) {
		this.diAddress = diAddress;
	}

	public String getLsbAddress() {
		return lsbAddress;
	}

	public void setLsbAddress(String lsbAddress) {
		this.lsbAddress = lsbAddress;
	}

	public String getManagerAddress() {
		return managerAddress;
	}

	public void setManagerAddress(String managerAddress) {
		this.managerAddress = managerAddress;
	}

	public BigDecimal getWithdrawRatio() {
		return withdrawRatio;
	}

	public void setWithdrawRatio(BigDecimal withdrawRatio) {
		this.withdrawRatio = withdrawRatio;
	}

	public BigDecimal getLsbwithdrawMin() {
		return lsbwithdrawMin;
	}

	public void setLsbwithdrawMin(BigDecimal lsbwithdrawMin) {
		this.lsbwithdrawMin = lsbwithdrawMin;
	}

	public BigDecimal getEthwithdrawMin() {
		return ethwithdrawMin;
	}

	public void setEthwithdrawMin(BigDecimal ethwithdrawMin) {
		this.ethwithdrawMin = ethwithdrawMin;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysConfig{" +
			", id=" + id +
			", gasPrice=" + gasPrice +
			", gasLimit=" + gasLimit +
			", ethToLsb=" + ethToLsb +
			", lsbToEth=" + lsbToEth +
			", diAddress=" + diAddress +
			", lsbAddress=" + lsbAddress +
			", managerAddress=" + managerAddress +
			", withdrawRatio=" + withdrawRatio +
			", lsbwithdrawMin=" + lsbwithdrawMin +
			", ethwithdrawMin=" + ethwithdrawMin +
			"}";
	}
}
