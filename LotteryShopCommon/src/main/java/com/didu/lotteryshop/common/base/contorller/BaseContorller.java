package com.didu.lotteryshop.common.base.contorller;

import com.didu.lotteryshop.common.base.BaseStat;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.LoginUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class BaseContorller extends BaseStat{

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
