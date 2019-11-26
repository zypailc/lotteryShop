package com.didu.lotteryshop.common.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author null123
 * @since 2019-11-22
 */
@TableName("intro_zh")
public class IntroZh extends Model<IntroZh> {

    private static final long serialVersionUID = 1L;

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "IntroZh{" +
        ", title=" + title +
        ", content=" + content +
        ", id=" + id +
        "}";
    }
}
