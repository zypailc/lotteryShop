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

import java.util.UUID;

@Service
public class MemberServiceImp extends ServiceImpl<MemberMapper, Member> {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MailServiceImp mailServiceImp;

    public Result register(Member member) {
        //随机生成一个秘钥
        String secretKey = CodeUtil.getUuid();
        //随机生成随机密码
        String password = CodeUtil.getCode(18);

        try {
            //秘钥和密码加密生成暗文
            String ciphertext = AesEncryptUtil.encrypt(password,secretKey);
            member.setPassword(ciphertext);
        }catch (Exception e){
            log.info(e.getMessage());
            e.printStackTrace();
        }
        member.setId(UUID.randomUUID().toString().replaceAll("-",""));
        member.setSecretKey(secretKey);
        mailServiceImp.sendSimpleMail(member,"密码","123456");
        //保存用户信息
        insert(member);
        return Result.successJson("success!");
    }
}
