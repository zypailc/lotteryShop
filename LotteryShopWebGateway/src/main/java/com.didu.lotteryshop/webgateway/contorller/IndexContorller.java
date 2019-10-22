package com.didu.lotteryshop.webgateway.contorller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Web页面Contorller
 * @date 2019-10-21
 * @author CHJ
 *
 */
@Controller
@RequestMapping("/web")
public class IndexContorller {

    @RequestMapping("/index")
    public String index(Model model){
        model.addAttribute("top_login",true);
        model.addAttribute("top_login_1",false);
        return "index";
    }

    @RequestMapping("/login")
    public String login(Model model){
        return "login";
    }

    @RequestMapping("/top")
    public String top(Model model){
        return "top";
    }

    @RequestMapping("/register")
    public String register(Model model){
        return "register";
    }

    @RequestMapping("/test")
    public String test(){
        return "test";
    }

}
