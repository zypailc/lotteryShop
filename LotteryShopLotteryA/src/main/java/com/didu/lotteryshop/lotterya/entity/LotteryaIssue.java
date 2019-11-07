package com.didu.lotteryshop.lotterya.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

/**
 * <p>
 * A彩票期数表
 * </p>
 *
 * @author ${author}
 * @since 2019-11-07
 */
@TableName("lotterya_issue")
public class LotteryaIssue extends Model<LotteryaIssue> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Integer id;
    /**
     * 开启时间
     */
	@TableField("start_time")
	private Date startTime;
    /**
     * 结束时间
     */
	@TableField("end_time")
	private Date endTime;
    /**
     * 期数
     */
	@TableField("issue_num")
	private String issueNum;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 购买状态，0：开启，1：关闭
     */
	@TableField("buy_status")
	private String buyStatus;
    /**
     * 购买状态操作时间
     */
	@TableField("bs_time")
	private Date bsTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getIssueNum() {
		return issueNum;
	}

	public void setIssueNum(String issueNum) {
		this.issueNum = issueNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBuyStatus() {
		return buyStatus;
	}

	public void setBuyStatus(String buyStatus) {
		this.buyStatus = buyStatus;
	}

	public Date getBsTime() {
		return bsTime;
	}

	public void setBsTime(Date bsTime) {
		this.bsTime = bsTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "LotteryaIssue{" +
			", id=" + id +
			", startTime=" + startTime +
			", endTime=" + endTime +
			", issueNum=" + issueNum +
			", createTime=" + createTime +
			", buyStatus=" + buyStatus +
			", bsTime=" + bsTime +
			"}";
	}
}
