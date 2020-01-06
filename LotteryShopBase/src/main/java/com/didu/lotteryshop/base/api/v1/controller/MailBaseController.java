package com.didu.lotteryshop.base.api.v1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/v1/sendMail")
public class MailBaseController {

    @PostMapping(value = "/sendMail",consumes = "application/json")
    @ResponseBody
    public void sendMail(){

    }
}
