package com.didu.lotteryshop.auth.controller;

import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 授权码模式.自定义登录页面以及自定授权页面
 * @author CHJ
 */
@SessionAttributes("authorizationRequest")
@Controller
public class LoginController {

    /**
     * 自定登录页面
     * @return
     */
    @GetMapping("/custom/login")
    public String loginPage(){
        return "login";
    }

    /**
     * 自定义授权页面
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/custom/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView view = new ModelAndView();
        view.addObject("clientId", authorizationRequest.getClientId());
        view.addObject("scopes",authorizationRequest.getScope());
        view.setViewName("authorize");
        return view;
    }

}
