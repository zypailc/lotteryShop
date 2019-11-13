package com.didu.lotteryshop.base.api.v1.service.form.imp;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.base.api.v1.mapper.MemberMapper;
import com.didu.lotteryshop.common.base.service.BaseService;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.ConfigurationUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.common.utils.CodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MemberServiceImp extends ServiceImpl<MemberMapper, Member> {

    Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    private MailServiceImp mailServiceImp;
    @Autowired
    private BaseService baseService;

    public ResultUtil register(Member member) {
        //判断邮箱是否被注册
        Member member_verification = new Member();
        member_verification.setEmail(member.getEmail());
        Member m = baseMapper.selectOne(member_verification);
        if(m != null && m.getEmail() != null){
            return ResultUtil.errorJson("The email address has been registered !");
        }
        //随机生成一个秘钥jsonObjectjsonObject
        String secretKey = ConfigurationUtil.KEY_TOW;
        //随机生成随机密码
        String password = CodeUtil.getCode(18);
        try {
            //秘钥和密码加密生成暗文
            String ciphertext = AesEncryptUtil.encrypt_code(password,secretKey);
            member.setPassword(ciphertext);
        }catch (Exception e){
            e.printStackTrace();
        }
        member.setId(CodeUtil.getUuid());
        member.setSecretKey(secretKey);
        member.setCreateTime(new Date());
        //判断用户是否有推荐用户 如果没有设定为初始用户
        if(member.getGeneralizeMemberType() == null ){
            member.setGeneralizeMemberType(Member.generalizeMemberType_1);
        }
        mailServiceImp.sendSimpleMail(member,"密码",password);
        //保存用户信息
        boolean b = insert(member);
        if(b){
            return ResultUtil.successJson("Registered successfully , please log in !");
        }else {
            return ResultUtil.errorJson("Registered error , please operate again !");
        }
    }

    /**
     * 修改头像
     * @param member
     * @return
     */
    public ResultUtil headPortrait(Member member){
        boolean flag = updateById(member);
        if(flag) {
            return ResultUtil.successJson("modify successfully !");
        }else {
            return ResultUtil.successJson("fail to modify !");
        }
    }



}
