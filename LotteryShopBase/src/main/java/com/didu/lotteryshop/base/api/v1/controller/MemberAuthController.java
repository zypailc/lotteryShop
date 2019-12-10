package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.base.api.v1.service.MemberService;
import com.didu.lotteryshop.base.controller.BaseBaseController;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.enumeration.ResultCode;
import com.didu.lotteryshop.common.utils.EmailUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/authorization/v1/member")
public class MemberAuthController extends BaseBaseController {

    Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    private MemberService memberService;


    /**
     * 注册账号
     * @param member
     * @return
     */
    @ResponseBody
    @RequestMapping("/register")//可不过验证访问
    public ResultUtil register(Member member){
        //判断昵称是否为空
        if(member.getMemberName() == null || "".equals(member.getMemberName())){
            return ResultUtil.errorJson("pet name cannot be empty !");
        }
        //验证邮箱格式
        if(!EmailUtil.verificationEmail(member.getEmail())){
            return ResultUtil.errorJson("Please enter the correct email address !");
        }
        return memberService.register(member);
    }

    @ResponseBody
    @RequestMapping("/forgotPassword")
    public ResultUtil forgotPassword(String email){
        //验证邮箱格式
        if(!EmailUtil.verificationEmail(email)){
            return ResultUtil.jsonObject("Please enter the correct email address !", ResultCode.FAILED.getCode());
        }
        return memberService.forgotPassword(email);
    }
}
