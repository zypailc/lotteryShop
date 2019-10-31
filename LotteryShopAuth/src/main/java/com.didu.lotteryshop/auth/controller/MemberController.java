package com.didu.lotteryshop.auth.controller;

import com.didu.lotteryshop.auth.service.impl.MemberServiceImpl;
import com.didu.lotteryshop.common.enumeration.ResultCode;
import com.didu.lotteryshop.common.utils.ResultUtil;
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
    public ResultUtil revokeToken(String access_token) {
        ResultUtil result = new ResultUtil();
        if (consumerTokenServices.revokeToken(access_token)) {
            //result.setMessage("注销成功");
            return ResultUtil.errorJson("Logout success!");
        } else {
            result.setCode(ResultCode.FAILED.getCode());
            //result.setMessage("注销失败");
            return ResultUtil.errorJson("Logout failed!");
        }
    }
}
