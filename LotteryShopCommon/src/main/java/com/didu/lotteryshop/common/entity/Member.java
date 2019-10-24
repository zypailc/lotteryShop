package com.didu.lotteryshop.common.entity;

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
public class Member{

    private String id;
    private String memberName;//昵称
    private String secretKey;//秘钥
    private String password;//密码
    private String email;//电子邮箱
    private String generalizeMemberId;//推广人员Id
    private String headPortaitUrl;//头像Url
    private Date createTime;//创建时间
    private Set<Role> roles;//角色

}
