package com.didu.lotteryshop.webgateway.controller;

import com.didu.lotteryshop.common.base.contorller.BaseContorller;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.LoginUser;
import org.springframework.ui.Model;

public class WebgatewayController extends BaseContorller {
    public Model getModel(Model model){
        LoginUser loginUser= getLoginUser();
        model.addAttribute("loginUser",loginUser);
        model.addAttribute("defaultHeadImg", Constants.HEAD_PORTRAIT_URL);
        if(loginUser == null){
            model.addAttribute("whetherLogin",false);
        }else{
            model.addAttribute("whetherLogin",true);
        }
        return model;
    }
}
