package com.didu.lotteryshop.webgateway.contorller;

import org.beetl.sql.core.annotatoin.Param;
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

    /**
     *登录页面
     * @param model
     * @return
     */
    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("type","login");
        return "login";
    }

    /**
     * 注册页面
     * @param model
     * @return
     */
    @RequestMapping("/register")
    public String register(Model model){
        model.addAttribute("type","register");
        return "register";
    }

    /**
     * 竞彩详细页面
     * @param model
     * @return
     */
    @RequestMapping("/lotteryServices")
    public String services(Model model){
        model.addAttribute("top_login",true);
        model.addAttribute("top_login_1",false);
        return "lotteryServices";
    }

    /**
     * 竞彩中心
     * @param model
     * @return
     */
    @RequestMapping("/playTheLottery")
    public String playTheLottery(Model model){
        model.addAttribute("top_login",true);
        model.addAttribute("top_login_1",false);
        return "playTheLottery";
    }

    /**
     * 个人中心
     * @param model
     * @return
     */
    @RequestMapping("/personalCenter")
    public String personalCenter(Model model){
        model.addAttribute("top_login",true);
        model.addAttribute("top_login_1",false);
        return "personalCenter";
    }

    /**
     * 手机模块显示页面
     * @param model
     * @param type
     * @return
     */
    @RequestMapping("/phoneType")
    public String phoneType(Model model, @Param(value = "type")String type){
        model.addAttribute("top_login",true);
        model.addAttribute("top_login_1",false);
        return type+"_phone";
    }

    /**
     * 平台币提取记录页面
     * @param model
     * @return
     */
    @RequestMapping("/walletRecordPhone")
    public String walletRecordPhone(Model model){
        model.addAttribute("top_login",true);
        model.addAttribute("top_login_1",false);
        return "wallet_record_phone";
    }

    /**
     * 推广记录
     * @param model
     * @return
     */
    @RequestMapping("/generalizeRecord")
    public String generalizeRecord(Model model){
        model.addAttribute("top_login",true);
        model.addAttribute("top_login_1",false);
        return "generalize_record";
    }

    /**
     * 推广收益
     * @param model
     * @return
     */
    @RequestMapping("/generalizeEarnings")
    public String generalizeEarnings(Model model){
        model.addAttribute("top_login",true);
        model.addAttribute("top_login_1",false);
        return "generalize_earnings";
    }

}
