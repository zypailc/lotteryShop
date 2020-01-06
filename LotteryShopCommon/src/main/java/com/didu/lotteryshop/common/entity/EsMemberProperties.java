package com.didu.lotteryshop.common.entity;

import com.baomidou.mybatisplus.enums.IdType;
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
 * @since 2020-01-03
 */
@TableName("es_member_properties")
public class EsMemberProperties extends Model<EsMemberProperties> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 是否显示
     */
	@TableField("is_view")
	private Integer isView;
    /**
     * 公告是否提醒
     */
	@TableField("type")
	private Integer type;
    /**
     * es_member主键Id
     */
	@TableField("member_id")
	private String memberId;

	/**
	 * 关联Id
	 */
	@TableField("relevance_id")
	private String relevanceId;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIsView() {
		return isView;
	}

	public void setIsView(Integer isView) {
		this.isView = isView;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getRelevanceId() {
		return relevanceId;
	}

	public void setRelevanceId(String relevanceId) {
		this.relevanceId = relevanceId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "EsMemberProperties{" +
			", id=" + id +
			", isView=" + isView +
			", type=" + type +
			", memberId=" + memberId +
			"}";
	}
}
