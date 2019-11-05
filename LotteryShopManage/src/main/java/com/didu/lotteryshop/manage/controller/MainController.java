package com.didu.lotteryshop.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 管理后台Controller
 * @author CHJ
 * @date 2019-11-05
 */
@Controller
@RequestMapping("/main")
public class MainController {
    @RequestMapping("/main1")
    public String main1(){
        return "index";
    }
    @RequestMapping("/main2")
    public String main2(){
        return "index2";
    }
    @RequestMapping("/main3")
    public String main3(){
        return "index3";
    }
}
