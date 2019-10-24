package com.didu.lotteryshop.common.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * 〈会员实体〉
 *
 * @author Curise
 * @create 2018/12/13
 * @since 1.0.0
 */
@Data
@TableName("es_member")
public class Member{

    private String id;
    @TableField("member_name")
    private String memberName;//昵称
    @TableField("secret_key")
    private String secretKey;//秘钥
    @TableField("payment_code")
    private String paymentCode;//支付密码
    private String password;//密码
    private String email;//电子邮箱
    @TableField("generalize_member_id")
    private String generalizeMemberId;//推广人员Id
    @TableField("generalize_member_type")
    private String generalizeMemberType;//推广类型 1 三层推广 2 七层推广
    @TableField("head_portrait_url")
    private String headPortraitUrl;//头像Url
    @TableField("create_time")
    private Date createTime;//创建时间
    @TableField(exist = false)
    private Set<Role> roles;//角色

}
