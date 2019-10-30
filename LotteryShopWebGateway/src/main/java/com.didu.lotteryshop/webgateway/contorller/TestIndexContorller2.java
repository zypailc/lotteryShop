package com.didu.lotteryshop.webgateway.contorller;

import org.beetl.sql.core.annotatoin.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 测试Web页面Contorller
 * @date 2019-10-21
 * @author CHJ
 *
 */
@Controller
@RequestMapping("/web2")
public class TestIndexContorller2 {

    /**
     * 主页
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model){
        model.addAttribute("top_login",true);
        model.addAttribute("top_login_1",false);
        return "index";
    }

    @RequestMapping("/testPost")
    @ResponseBody
    public String testPost(String name,String sex){
        System.out.println("name："+name);
        System.out.println("sex:"+name);
        return "CHJ";
    }



}
