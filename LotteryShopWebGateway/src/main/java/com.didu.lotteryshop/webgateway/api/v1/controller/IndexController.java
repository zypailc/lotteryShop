package com.didu.lotteryshop.webgateway.api.v1.controller;

import com.didu.lotteryshop.webgateway.config.Constants;
import com.didu.lotteryshop.webgateway.controller.WebgatewayBaseController;
import org.apache.ibatis.annotations.Param;
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
@RequestMapping("")
public class IndexController extends WebgatewayBaseController {
    @Value("${security.oauth2.client.client-id}")
    private String securityClientId;
    @Value("${security.oauth2.client.client-secret}")
    private String securityClientSecret;

    /**
     * 500错误页面
     * @param model
     * @return
     */
    @RequestMapping("/web/auth500")
    public String Error500(Model model){
        model = getModel(model);
        return "500";
    }
    /**
     * 404错误页面
     * @param model
     * @return
     */
    @RequestMapping("/web/auth404")
    public String Error404(Model model){
        model = getModel(model);
        return "404";
    }
    /**
     * 主页
     * @param model
     * @param generalizePersonId 推广人员的Id
     * @return
     */
    @RequestMapping("/web/"+Constants.INDEX_URL)
    public String index(Model model, HttpServletRequest request,String generalizePersonId){
        model = getModel(model);
        model.addAttribute("generalizePersonId",generalizePersonId);
        return Constants.INDEX;
    }

    /**
     *登录页面
     * @param model
     * @return
     */
    @RequestMapping("/web/authLogin")
    public String login(Model model,String rdirectUrl){
        //if(StringUtils.isBlank(rdirectUrl)){
        //默认登录跳转页面 如果不固定页面跳转地址，地址错误之后，会过滤器截器，跳转到404页面
        rdirectUrl = "/web/authIndex";
        //}
        model = getModel(model);
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
    @RequestMapping("/web/authRegister")
    public String register(Model model){
        model = getModel(model);
        model.addAttribute("type","register");
        return "register";
    }

    /**
     * 竞彩中心
     * @param model
     * @return
     */
    @RequestMapping("/web/"+ Constants.PLAYTHELOTTERY_URL)
    public String playTheLottery(Model model){
        model = getModel(model);
        return Constants.PLAYTHELOTTERY;
    }

    /**
     * 竞彩详细页面
     * @param model
     * @return
     */
    @RequestMapping("/web/authLotteryServices")
    public String services(Model model){
        model = getModel(model);
        return "lotteryServices";
    }


    /**
     * 找回密码
     * @param model
     * @return
     */
    @RequestMapping("/web/authForgotPassword")
    public String forgotPassword(Model model){
        model = getModel(model);
        return  "forgotPassword";
    }

    /**
     * 个人中心
     * @param model
     * @return
     */
    @RequestMapping("/web/personalCenter")
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
    @RequestMapping("/web/personalPhoneType")
    public String phoneType(Model model, @Param(value = "type") String type){
        model = getModel(model);
        return type+"_phone";
    }

    /**
     * 手机版钱包显示
     * @param model
     * @param type
     * @return
     */
    @RequestMapping("/web/personalWalletBase")
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
    @RequestMapping("/web/personalWalletBind")
    public String walletWallet(Model model,@Param(value = "type")String type){
        model = getModel(model);
        model.addAttribute("type",type);
        return "wallet_bind";
    }

    /**
     * 推广记录
     * @param model
     * @return
     */
    @RequestMapping("/web/personalGeneralizeRecord")
    public String generalizeRecord(Model model){
        model = getModel(model);
        return "generalize_record";
    }

    /**
     * 推广收益 暂时弃用
     * @param model
     * @return
     */
    @RequestMapping("/person/generalizeEarnings")
    public String generalizeEarnings(Model model){
        model = getModel(model);
        return "generalize_earnings";
    }

}
