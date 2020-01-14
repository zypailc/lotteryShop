package com.didu.lotteryshop.webgateway.controller;

import com.didu.lotteryshop.common.base.contorller.BaseContorller;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.service.form.impl.MemberServiceImpl;
import com.didu.lotteryshop.webgateway.config.WebGatewayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebgatewayBaseController extends BaseContorller {

    @Autowired
    private WebGatewayConfig webGatewayConfig;

    public Model getModel(Model model){
        LoginUser loginUser= getLoginUser();
        model.addAttribute("loginUser",loginUser);
        model.addAttribute("defaultHeadImg", Constants.HEAD_PORTRAIT_URL);
        model.addAttribute("projectPath",webGatewayConfig.getProjectContent());
        model.addAttribute("access_token",super.getSession().getAttribute(com.didu.lotteryshop.webgateway.config.Constants.SESSION_LOGIN_TOKEN));
        boolean pAddress = true;
        if(loginUser != null){
            if(loginUser.getPAddress() == null || "".equals(loginUser.getPAddress())){
                pAddress = false;
            }
        }else {
            pAddress = false;
        }
        model.addAttribute("pAddress",pAddress);
        boolean generalizeType = false;
        if(loginUser != null){
            if(loginUser.getGeneralizeType() == null || MemberServiceImpl.GENERALIZE_TYPE_1.equals(loginUser.getGeneralizeType())){
                generalizeType = true;
            }
        }
        model.addAttribute("generalizeType",generalizeType);

        if(loginUser == null){
            model.addAttribute("whetherLogin",false);
        }else{
            model.addAttribute("whetherLogin",true);
        }
        return model;
    }

    /**
     * 获取用户Ip
     * @return
     */
    public String getRequestIp(){
        HttpServletRequest request = super.getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
