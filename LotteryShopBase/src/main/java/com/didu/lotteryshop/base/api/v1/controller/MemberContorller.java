package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.base.api.v1.service.imp.MemberServiceImp;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/v1/authorization/member")
public class MemberContorller {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MemberServiceImp memberService;

    @ResponseBody
    @RequestMapping("/register")
    public Result register(Member member){
        log.info("email:"+member.getEmail());
        memberService.register(member);
        return null;
    }
}
