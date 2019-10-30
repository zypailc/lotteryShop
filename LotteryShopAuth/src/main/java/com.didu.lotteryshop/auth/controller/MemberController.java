package com.didu.lotteryshop.auth.controller;

import com.didu.lotteryshop.auth.service.impl.MemberServiceImpl;
import com.didu.lotteryshop.common.entity.Result;
import com.didu.lotteryshop.common.enumeration.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 〈会员Controller〉
 *
 * @author Curise
 * @create 2018/12/13
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api")
public class MemberController {

    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @GetMapping("/member")
    public Principal user(Principal member) {
        return member;
    }

    @DeleteMapping(value = "/exit")
    public Result revokeToken(String access_token) {
        Result result = new Result();
        if (consumerTokenServices.revokeToken(access_token)) {
            result.setCode(ResultCode.SUCCESS.getCode());
            //result.setMessage("注销成功");
            result.setMessage("Logout success!");
        } else {
            result.setCode(ResultCode.FAILED.getCode());
            //result.setMessage("注销失败");
            result.setMessage("Logout failed!");
        }
        return result;
    }
}
