package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.base.api.v1.service.MemberService;
import com.didu.lotteryshop.common.base.contorller.BaseContorller;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.common.utils.VerifyETHAddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/v1/member")
public class MemberContorller extends BaseContorller {


    @Autowired
    private MemberService memberService;

    @ResponseBody
    @RequestMapping("/bindWallet")
    public ResultUtil bindWallet(String paymentCode,String bAddress){
        //获取用户信息
        LoginUser loginUser= getLoginUser();
        String userId = loginUser.getId();
        if(userId == null || "".equals(userId)){
            return ResultUtil.errorJson("Bind failed. Please try again!");
        }
        if(paymentCode == null || "".equals(paymentCode)){
            return ResultUtil.errorJson("Please enter your password!");
        }
        if(bAddress == null || "".equals(bAddress)){
            return ResultUtil.errorJson("Please enter your wallet address!");
        }
        if(!VerifyETHAddressUtil.isETHValidAddress(bAddress)){
            return ResultUtil.errorJson("Please fill in your wallet address correctly!");
        }
        return memberService.bindWallet(userId,paymentCode,bAddress);
    }

    /**
     * bangding
     * @param bAddress
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateBindWallet")
    public ResultUtil updateBindWallet(String bAddress){
        //获取用户信息
        LoginUser loginUser= getLoginUser();
        if(bAddress == null || "".equals(bAddress)){
            return ResultUtil.errorJson("Please enter your wallet address !");
        }
        if(!VerifyETHAddressUtil.isETHValidAddress(bAddress)){
            return ResultUtil.errorJson("Please fill in your wallet address correctly !");
        }
        Member member = new Member();
        member.setId(loginUser.getId());
        member.setBAddress(bAddress);
        return memberService.modifyMember(member);
    }
}
