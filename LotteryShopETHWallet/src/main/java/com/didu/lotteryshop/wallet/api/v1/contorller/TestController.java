package com.didu.lotteryshop.wallet.api.v1.contorller;

import com.didu.lotteryshop.common.utils.Result;
import com.didu.lotteryshop.wallet.annotation.SecurityParameter;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * 测试Controller
 */
@RestController
@RequestMapping("/v1/test")
public class TestController {

    /*
     * 测试返回数据，会自动加密
     * @return
     */
    @GetMapping("/get")
    @ResponseBody
    @SecurityParameter
    private Result get() {
        Persion info = new Persion();
        info.setName("好看");
        return Result.successJson(info);
    }
    /*
     * 自动解密，并将返回信息加密
     * @param info
     * @return
     */
    @PostMapping(value = "/save",consumes = "application/json")
    @ResponseBody
    @SecurityParameter
    public Result save(@RequestBody Persion info) {
        System.out.println(info.getName());
        return Result.successJson(info);
    }

    @ResponseBody
    @RequestMapping("/test")
    public Result test(@PathParam(value = "name") String name){
        return  Result.successJson(name);
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