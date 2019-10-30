package com.didu.lotteryshop.base.api.v1.service.imp;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.base.api.v1.mapper.MemberMapper;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.Result;
import com.didu.lotteryshop.common.utils.CodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class MemberServiceImp extends ServiceImpl<MemberMapper, Member> {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MailServiceImp mailServiceImp;

    public Result register(Member member) {
        //判断邮箱是否被注册
        Member member_verification = new Member();
        member_verification.setEmail(member.getEmail());
        Member m = baseMapper.selectOne(member_verification);
        if(m != null && m.getEmail() != null){
            return Result.jsonObject("The email address has been registered !",Result.ERROR_CODE);
        }
        //随机生成一个秘钥jsonObjectjsonObject
        String secretKey = AesEncryptUtil.KEY_TOW;
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
        insert(member);
        return Result.jsonObject("registered successfully , please log in !",Result.SUCCESS_CODE);
    }

    public Result headPortrait(Member member){
        //TODO 获取用户信息暂时未完成
        //获取用户Id
        updateById(member);
        return  null;
    }
}
