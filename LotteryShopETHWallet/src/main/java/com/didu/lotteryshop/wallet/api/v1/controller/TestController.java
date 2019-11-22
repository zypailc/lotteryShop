package com.didu.lotteryshop.wallet.api.v1.controller;

import com.didu.lotteryshop.common.base.contorller.BaseContorller;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.wallet.annotation.SecurityParameter;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

/**
 * 测试Controller
 */
@RestController
@RequestMapping("/v1/test")
public class TestController extends BaseContorller {

    /*
     * 测试返回数据，会自动加密
     * @return
     */
    @GetMapping("/get")
    @ResponseBody
    @SecurityParameter
    private ResultUtil get() {
        Persion info = new Persion();
        info.setName("好看");
        return ResultUtil.successJson(info);
    }
    /*
     * 自动解密，并将返回信息加密
     * @param info
     * @return
     */
    @PostMapping(value = "/save",consumes = "application/json")
    @ResponseBody
    @SecurityParameter
    public ResultUtil save(@RequestBody Persion info) {
        System.out.println(info.getName());
        return ResultUtil.successJson(info);
    }

    @ResponseBody
    @RequestMapping("/test")
    public ResultUtil  test(@PathParam(value = "name") String name){
        return  ResultUtil.successJson(name);
    }

    @ResponseBody
    @RequestMapping("/test1")
    @SecurityParameter
    public ResultUtil  test1(String requestData){
        HttpServletRequest request = super.getRequest();
        return  ResultUtil.successJson("");
    }

    class Persion {
        private String name;
        private String sex;
        private String age;
         public String getName() {
             return name;
         }

         public void setName(String name) {
             this.name = name;
         }

         public String getSex() {
             return sex;
         }

         public void setSex(String sex) {
             this.sex = sex;
         }

         public String getAge() {
             return age;
         }

         public void setAge(String age) {
             this.age = age;
         }

    }
}