package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.base.controller.BaseBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * api.v1 测试Controller
 * 测试不需要授权即可访问的
 */
@Controller
@RequestMapping("/v1/authorization/test")
public class TestController extends BaseBaseController {
    /**
     * 测试json数据
     * @return
     */
    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        System.out.println("请求到了");
        return "test";
    }

    /**
     * 测试返回view
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return "index";
    }
}
