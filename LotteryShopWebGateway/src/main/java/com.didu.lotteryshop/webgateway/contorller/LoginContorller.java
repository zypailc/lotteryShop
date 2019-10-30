package com.didu.lotteryshop.webgateway.contorller;

import com.didu.lotteryshop.webgateway.config.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录Contorller
 * @author CHJ
 * @date 2019-10-29
 *
 */
@Controller
public class LoginContorller {
    /**
     * 登录操作存储Session
     * @param request
     * @param response
     * @param accessToken
     * @param rdirectUrl
     */
    @RequestMapping("/loginSession")
    public void loginSession(HttpServletRequest request, HttpServletResponse response, String accessToken, String rdirectUrl){
        try {
            request.getSession().setAttribute(Constants.SESSION_LOGIN_TOKEN,accessToken);
            response.sendRedirect(rdirectUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
