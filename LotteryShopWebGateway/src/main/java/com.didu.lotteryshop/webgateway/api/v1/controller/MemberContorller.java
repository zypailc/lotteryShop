package com.didu.lotteryshop.webgateway.api.v1.controller;

import com.didu.lotteryshop.webgateway.controller.WebgatewayController;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/person")
public class MemberContorller extends WebgatewayController {

    /**
     * 个人中心
     * @param model
     * @return
     */
    @RequestMapping("/personalCenter")
    public String personalCenter(Model model){
        model = getModel(model);
        return "personalCenter";
    }


    /**
     * 手机模块显示页面
     * @param model
     * @param type
     * @return
     */
   /* @RequestMapping("/phoneType")
    public String phoneType(Model model, @Param(value = "type") String type){
        model.addAttribute("top_login",true);
        model.addAttribute("top_login_1",false);
        return type+"_phone";
    }*/

    /**
     * 手机版钱包显示
     * @param model
     * @param type
     * @return
     */
    @RequestMapping("/walletBase")
    public String walletBase(Model model,@Param(value = "type")String type){
        model = getModel(model);
        model.addAttribute("type",type);
        //eth和平台币展示记录
        return "wallet_base";
    }

    /**
     * 钱包管理
     * @param model
     * @param type
     * @return
     */
    @RequestMapping("/walletBind")
    public String walletWallet(Model model,@Param(value = "type")String type){
        model = getModel(model);
        model.addAttribute("type",type);
        return "wallet_bind";
    }

}
