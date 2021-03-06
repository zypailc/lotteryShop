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

    /**
     * 头像类型图片
     */
    public static final  Integer IMAGE_TYPE = 1;

    /**
     * 项目图片类型
     */
    public static final  Integer PROJECT_TYPE = 2;

    /**
     * 二维码图片类型
     */
    public static final  Integer QR_BACKGROUND = 3;


    private static final long serialVersionUID = 1L;

    @TableId(type=IdType.AUTO)
    private Integer id;
    /**
     * 图片类型：1 头像 （图片类型可添加，）
     */
    private Integer type;
    /**
     * 图片的二进制码
     */
    @TableField("byte_data")
    private byte [] byteData;

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


    public byte[] getByteData() {
        return byteData;
    }

    public void setByteData(byte[] byteData) {
        this.byteData = byteData;
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
        "}";
    }
}
