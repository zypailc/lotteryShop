package com.didu.lotteryshop.webgateway.controller;

import com.didu.lotteryshop.common.base.contorller.BaseContorller;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.webgateway.config.WebGatewayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class WebgatewayController extends BaseContorller {

    @Autowired
    private WebGatewayConfig webGatewayConfig;

    public Model getModel(Model model){
        LoginUser loginUser= getLoginUser();
        model.addAttribute("loginUser",loginUser);
        model.addAttribute("defaultHeadImg", Constants.HEAD_PORTRAIT_URL);
        model.addAttribute("projectPath",webGatewayConfig.getProjectContent());
        model.addAttribute("access_token",super.getSession().getAttribute(com.didu.lotteryshop.webgateway.config.Constants.SESSION_LOGIN_TOKEN));
        if(loginUser == null){
            model.addAttribute("whetherLogin",false);
        }else{
            model.addAttribute("whetherLogin",true);
        }
        return model;
    }
}
