package com.didu.lotteryshop.webgateway.api.v1.controller;

import com.didu.lotteryshop.common.service.form.impl.MemberServiceImpl;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.webgateway.config.Constants;
import com.didu.lotteryshop.webgateway.controller.WebgatewayBaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录Contorller
 * @author CHJ
 * @date 2019-10-29
 *
 */
@Controller
public class LoginController extends WebgatewayBaseController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MemberServiceImpl memberService;

    @RequestMapping("/web/authUserLogin")
    @ResponseBody
    public ResultUtil authUserLogin(String username,String password,String rdirectUrl){
        Map<String,String> map = new HashMap<>();
        if(StringUtils.isBlank(rdirectUrl)) rdirectUrl = "/web/authIndex";
        if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)){
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
            paramMap.add("username",username);
            paramMap.add("password",password);
            paramMap.add("grant_type","password");
            paramMap.add("client_id","browser");
            paramMap.add("client_secret","browser");
            Map<String,String> rMap = restTemplate.postForObject("http://auth-service/auth/oauth/token",paramMap,Map.class);
            if(rMap != null && !rMap.isEmpty()){
                String accessToken = rMap.get("access_token");
                String tokenType = rMap.get("token_type");
                if(StringUtils.isNotBlank(accessToken) && StringUtils.isNotBlank(tokenType)){
                    super.getRequest().getSession().setAttribute(Constants.SESSION_LOGIN_TOKEN,accessToken);
                    super.getRequest().getSession().setAttribute(Constants.SESSION_LOGIN_TOKEN_TYPE,tokenType);
                    super.getRequest().getSession().setAttribute("LoginUserName",username);
                    memberService.updateMemberLoginInfoByEmail(username,super.getRequestIp());
                    if(super.isChineseLanguage()){
                        map.put("msg","登錄成功！");
                    }else{
                        map.put("msg","Login the success！");
                    }
                    map.put("rdirectUrl",rdirectUrl);
                    return ResultUtil.successJson(map);
                }
            }
        }
        if(super.isChineseLanguage()){
            map.put("msg","用戶或密碼錯誤！");
        }else{
            map.put("msg","User or password error!");
        }
        return ResultUtil.errorJson(map);
    }

    /**
     * 登录操作存储Session
     * @param request
     * @param response
     * @param accessToken
     * @param rdirectUrl
     */
//    @RequestMapping("/web/loginSession")
//    public String loginSession(Model model,HttpServletRequest request, HttpServletResponse response, String accessToken, String rdirectUrl, Principal user){
//        //try {
//            request.getSession().setAttribute(Constants.SESSION_LOGIN_TOKEN,accessToken);
//            //登陆之后只会有调到两个页面（主页和竞彩中心，个人中心只有登陆之后才可以进，如果是登陆注册直接跳转主页）
//            //解析需要跳转的地址，如果是需要跳转竞彩中心，不需要管，如果是其他，直接跳转主页
//            //rdirectUrl = rdirectUrl.substring(rdirectUrl.indexOf("/"),rdirectUrl.length());
//            model = getModel(model);
//            memberService.updateMemberLoginInfo(getLoginUser().getId(),getRequestIp(request));
//            if(Constants.PLAYTHELOTTERY_URL.equals(rdirectUrl)){
//                return Constants.PLAYTHELOTTERY;
//            }
//            return Constants.INDEX;
//            //response.sendRedirect(rdirectUrl);
//        /*} catch (IOException e) {
//            e.printStackTrace();
//        }*/
//    }

    /**
     * 取消登录
     * @return
     */
    @ResponseBody
    @RequestMapping("/web/loginOut")
    public ResultUtil loginOut(){
        HttpSession session = super.getRequest().getSession();
        String accessToken = (String)session.getAttribute(Constants.SESSION_LOGIN_TOKEN);
        if(StringUtils.isNotBlank(accessToken)){
            //清除Session
            session.removeAttribute(Constants.SESSION_LOGIN_TOKEN);//清除令牌
            session.removeAttribute(Constants.SESSION_LOGIN_TOKEN_TYPE);
            session.removeAttribute("LoginUserName");
            session.removeAttribute(com.didu.lotteryshop.common.config.Constants.LOGIN_USER);//清除用戶信息

            session.removeAttribute(com.didu.lotteryshop.common.config.Constants.LOGIN_SESSION_KEY);
            session.removeAttribute(com.didu.lotteryshop.common.config.Constants.LOGIN_SESSION_UPDATE_KEY);
            //验证服务器删除TOken
            restTemplate.delete("http://auth-service/auth/api/exit?access_token="+accessToken);
        }
        String msg = "Exit the success！";
        if(super.isChineseLanguage()){
            msg = "退出成功!";
        }
        return ResultUtil.successJson(msg);
    }



}
