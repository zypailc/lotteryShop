package com.didu.lotteryshop.common.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
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
 * @since 2019-11-18
 */
@TableName("ls_image")
public class LsImage extends Model<LsImage> {

    public static final  Integer IMAGE_TYPE = 1;

    private static final long serialVersionUID = 1L;

    @TableId(type=IdType.AUTO)
    private Integer id;
    /**
     * 图片类型：1 头像 （图片类型可添加，）
     */
    private Integer type;
    /**
     * 图片地址
     */
    private String url;

    /**
     * 图片保存的本地地址
     * @return
     */
    @TableField("localhost_url")
    private String localhostUrl;
    @TableField("file_name")
    private String fileName;


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalhostUrl() {
        return localhostUrl;
    }

    public void setLocalhostUrl(String localhostUrl) {
        this.localhostUrl = localhostUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
        ", localhostUrl=" + localhostUrl +
        ", fileName=" + fileName +
        "}";
    }
}
