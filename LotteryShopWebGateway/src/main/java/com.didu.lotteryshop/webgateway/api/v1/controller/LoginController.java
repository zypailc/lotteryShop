package com.didu.lotteryshop.webgateway.api.v1.controller;

import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.webgateway.config.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

/**
 * 登录Contorller
 * @author CHJ
 * @date 2019-10-29
 *
 */
@Controller
public class LoginController {
    @Autowired
    private RestTemplate restTemplate;
    /**
     * 登录操作存储Session
     * @param request
     * @param response
     * @param accessToken
     * @param rdirectUrl
     */
    @RequestMapping("/loginSession")
    public void loginSession(HttpServletRequest request, HttpServletResponse response, String accessToken, String rdirectUrl, Principal user){
        try {
            request.getSession().setAttribute(Constants.SESSION_LOGIN_TOKEN,accessToken);
            response.sendRedirect(rdirectUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消登录
     * @param request
     * @return
     */
    @RequestMapping("/loginOut")
    public ResultUtil loginOut(HttpServletRequest request){
        HttpSession session = request.getSession();
        String accessToken = (String)session.getAttribute(Constants.SESSION_LOGIN_TOKEN);
        if(StringUtils.isNotBlank(accessToken)){
            restTemplate.delete("http://auth-service/auth/api/exit?access_token="+accessToken);
            //清除Session
            session.removeAttribute(Constants.SESSION_LOGIN_TOKEN);
        }
        return ResultUtil.successJson("Exit the success！");
    }

}
