package com.didu.lotteryshop.common.entity;

import com.baomidou.mybatisplus.enums.IdType;
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
 * @since 2019-11-21
 */
@TableName("m_intro")
public class MIntro extends Model<MIntro> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 标题
     */
	private String title;
    /**
     * 内容
     */
	private String content;
    /**
     * 类型：1 项目特点 ，2 资金分配
     */
	private Integer type;
    /**
     * 排序
     */
	private Integer sort;
	@TableField("create_time")
	private Date createTime;
	@TableField("language_id")
	private Integer languageId;
	@TableField("ls_image_id")
	private Integer lsImageId;
	@TableField("play_type")
	private Integer playType;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public Integer getLsImageId() {
		return lsImageId;
	}

	public void setLsImageId(Integer lsImageId) {
		this.lsImageId = lsImageId;
	}

	public Integer getPlayType() {
		return playType;
	}

	public void setPlayType(Integer playType) {
		this.playType = playType;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MIntro{" +
			", id=" + id +
			", title=" + title +
			", content=" + content +
			", type=" + type +
			", sort=" + sort +
			", createTime=" + createTime +
			", languageId=" + languageId +
			", lsImageId=" + lsImageId +
			"}";
	}
}
