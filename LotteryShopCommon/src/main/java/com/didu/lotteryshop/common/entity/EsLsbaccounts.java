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
 * lsb平台账目流水记录
 * </p>
 *
 * @author ${author}
 * @since 2019-11-11
 */
@TableName("es_lsbaccounts")
public class EsLsbaccounts extends Model<EsLsbaccounts> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 用户主键ID
     */
	@TableField("member_id")
	private String memberId;
    /**
     * 交易类型，对应sys_dic表key_value值
     */
	@TableField("dic_type")
	private String dicType;
    /**
     * 交易类型，0：出，1：入
     */
	private Integer type;
    /**
     * 金额
     */
	private BigDecimal amount;
    /**
     * 余额
     */
	private BigDecimal balance;
    /**
     * 状态，0：处理中；1：成功；2：失败
     */
	private Integer status;
    /**
     * 状态更时间
     */
	@TableField("status_time")
	private Date statusTime;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 操作业务表主键ID
     */
	@TableField("oper_id")
	private String operId;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getDicType() {
		return dicType;
	}

	public void setDicType(String dicType) {
		this.dicType = dicType;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getStatusTime() {
		return statusTime;
	}

	public void setStatusTime(Date statusTime) {
		this.statusTime = statusTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOperId() {
		return operId;
	}

	public void setOperId(String operId) {
		this.operId = operId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "EsLsbaccounts{" +
			", id=" + id +
			", memberId=" + memberId +
			", dicType=" + dicType +
			", type=" + type +
			", amount=" + amount +
			", balance=" + balance +
			", status=" + status +
			", statusTime=" + statusTime +
			", createTime=" + createTime +
			", operId=" + operId +
			"}";
	}
}
