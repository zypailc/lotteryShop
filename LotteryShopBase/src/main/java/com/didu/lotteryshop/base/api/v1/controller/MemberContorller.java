package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.base.api.v1.service.imp.MemberServiceImp;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.utils.EmailUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
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

    /**
     * 注册账号
     * @param member
     * @return
     */
    @ResponseBody
    @RequestMapping("/register")
    public ResultUtil register(Member member){
        //验证邮箱格式
        if(!EmailUtil.verificationEmail(member.getEmail())){
            return ResultUtil.jsonObject("Please enter the correct email address !", ResultUtil.ERROR_CODE);
        }
        //return memberService.register(member);
        return ResultUtil.jsonObject("success", ResultUtil.SUCCESS_CODE);
    }

    /**
     * 修改头像
     * @param member
     * @return
     */
    @ResponseBody
    @RequestMapping("/headPortrait")
    public ResultUtil headPortrait(Member member){
        return memberService.headPortrait(member);
    }

}
