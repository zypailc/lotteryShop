package com.didu.lotteryshop.webgateway.api.v1.controller;

import com.didu.lotteryshop.webgateway.controller.WebgatewayController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Web页面Contorller
 * @date 2019-10-21
 * @author CHJ
 *
 */
@Controller
@RequestMapping("/web")
public class IndexController extends WebgatewayController {
    @Value("${security.oauth2.client.client-id}")
    private String securityClientId;
    @Value("${security.oauth2.client.client-secret}")
    private String securityClientSecret;

    /**
     * 500错误页面
     * @param model
     * @return
     */
    @RequestMapping("/500")
    public String Error500(Model model){
        return "500";
    }
    /**
     * 404错误页面
     * @param model
     * @return
     */
    @RequestMapping("/404")
    public String Error404(Model model){
        return "404";
    }
    /**
     * 主页
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model, HttpServletRequest request){
        model = getModel(model);
        return "index";
    }

    /**
     *登录页面
     * @param model
     * @return
     */
    @RequestMapping("/login")
    public String login(Model model,String rdirectUrl){
        if(StringUtils.isBlank(rdirectUrl)){
            //默认登录跳转页面 TODO 需要该成账户中心
            rdirectUrl = "/web/index";
        }
        model.addAttribute("type","login");
        model.addAttribute("client_id",securityClientId);
        model.addAttribute("client_secret",securityClientSecret);
        model.addAttribute("rdirect_url",rdirectUrl);
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
        return "lotteryServices";
    }

    /**
     * 竞彩中心
     * @param model
     * @return
     */
    @RequestMapping("/playTheLottery")
    public String playTheLottery(Model model){
        return "playTheLottery";
    }

    /**
     * 推广记录
     * @param model
     * @return
     */
    @RequestMapping("/generalizeRecord")
    public String generalizeRecord(Model model){
        return "generalize_record";
    }

    /**
     * 推广收益
     * @param model
     * @return
     */
    @RequestMapping("/generalizeEarnings")
    public String generalizeEarnings(Model model){
        return "generalize_earnings";
    }

    @RequestMapping("/forgotPassword")
    public String forgotPassword(Model model){
        return  "forgotPassword";
    }

}
