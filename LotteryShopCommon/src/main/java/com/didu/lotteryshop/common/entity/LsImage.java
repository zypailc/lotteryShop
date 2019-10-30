package com.didu.lotteryshop.common.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author null123
 * @since 2019-10-29
 */
@TableName("ls_image")
public class LsImage extends Model<LsImage> {


    /**
     * 图像类型： 1 头像类别
     */
    public static final Integer IMAGE_TYPE = 1;

    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 图片类型：1 头像 （图片类型可添加，）
     */
    private Integer type;
    /**
     * 图片地址
     */
    private String url;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "LsImage{" +
        ", id=" + id +
        ", type=" + type +
        ", url=" + url +
        "}";
    }
}
