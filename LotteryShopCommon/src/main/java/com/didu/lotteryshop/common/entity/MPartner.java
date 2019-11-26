package com.didu.lotteryshop.common.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author null123
 * @since 2019-11-19
 */
@TableName("m_partner")
public class MPartner extends Model<MPartner> {

    public static final String TYPE_EXTERNAL = "1";
    public static final String TYPE_PARTNER = "2";

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 链接地址
     */
    @TableField("link_address")
    private String linkAddress;
    @TableField("ls_image_id")
    private String lsImageId;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 类型： 1：外部链接 ； 2：合作伙伴
     */
    public Integer type;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public String getLsImageId() {
        return lsImageId;
    }

    public void setLsImageId(String lsImageId) {
        this.lsImageId = lsImageId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }



    @Override
    public String toString() {
        return "MPartner{" +
        ", id=" + id +
        ", linkAddress=" + linkAddress +
        ", lsImageId=" + lsImageId +
        ", sort=" + sort +
        ", createTime=" + createTime +
        ", type=" + type +
        "}";
    }
}
