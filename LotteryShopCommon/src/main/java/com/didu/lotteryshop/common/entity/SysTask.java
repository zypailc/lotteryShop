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
 * 系统任务配置表
 * </p>
 *
 * @author ${author}
 * @since 2019-12-05
 */
@TableName("sys_task")
public class SysTask extends Model<SysTask> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 类型，1：注册送待领币；2：第一次消费送待领币；3：直属下线第一次消费送待领币
     */
	private Integer type;
    /**
     * 待领币
     */
	private BigDecimal dlb;
    /**
     * 状态，1：启用；2：停用
     */
	private Integer status;
    /**
     * 状态操作时间
     */
	@TableField("status_time")
	private Date statusTime;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getDlb() {
		return dlb;
	}

	public void setDlb(BigDecimal dlb) {
		this.dlb = dlb;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysTask{" +
			", id=" + id +
			", type=" + type +
			", dlb=" + dlb +
			", status=" + status +
			", statusTime=" + statusTime +
			", createTime=" + createTime +
			"}";
	}
}
