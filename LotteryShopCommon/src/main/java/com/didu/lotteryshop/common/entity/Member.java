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

    /**
     * 推广类型: 1 初始用户
     */
    public static final String generalizeMemberType_1 = "1";

    /**
     * 推广类型: 2 推广用户
     */
    public static final  String generalizeMemberType_2 = "2";

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
    @TableField("generalize_member_ids")
    private String generalizeMemberIds;//推广人员的所有上级Id
    @TableField("generalize_member_level")
    private Integer generalizeMemberLevel;//推广層級
    @TableField("head_portrait_url")
    private String headPortraitUrl;//头像Url
    @TableField("create_time")
    private Date createTime;//创建时间
    @TableField("p_address")
    private String pAddress;//平台钱包地址
    @TableField("b_address")
    private String bAddress;//绑定钱包地址
    @TableField("wallet_name")
    private String walletName;//钱包名称

    @TableField(exist = false)
    private Set<Role> roles;//角色

}
