package com.didu.lotteryshop.wallet.contorller;

import com.didu.lotteryshop.wallet.annotation.SecurityParameter;
import com.didu.lotteryshop.wallet.model.Persion;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * 测试Controller
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /*
     * 测试返回数据，会自动加密
     * @return
     */
    @GetMapping("/get")
    @ResponseBody
    @SecurityParameter
    private Object get() {
        Persion info = new Persion();
        info.setName("好看");
        return info;
    }
    /*
     * 自动解密，并将返回信息加密
     * @param info
     * @return
     */
    @PostMapping(value = "/save",consumes = "application/json")
    @ResponseBody
    @SecurityParameter
    public Object save(@RequestBody Persion info) {
        System.out.println(info.getName());
        return info;
    }

    @ResponseBody
    @RequestMapping("/test")
    public String test(@PathParam(value = "name") String name){
        return name;
    }

}