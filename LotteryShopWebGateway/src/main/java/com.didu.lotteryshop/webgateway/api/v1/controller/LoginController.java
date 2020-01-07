package com.didu.lotteryshop.webgateway.api.v1.controller;

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

    @RequestMapping("/web/authUserLogin")
    @ResponseBody
    public ResultUtil authUserLogin(String username,String password,String rdirectUrl){
        if(StringUtils.isBlank(rdirectUrl)) rdirectUrl = "/web/authIndex";
        if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)){
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
            paramMap.add("username",username);
            paramMap.add("password",password);
            paramMap.add("grant_type","password");
            paramMap.add("client_id","browser");
            paramMap.add("client_secret","browser");
            //HttpHeaders headers = new HttpHeaders();
            //headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
           // HttpEntity<Map> httpEntity = new HttpEntity<Map>(paramMap,headers);
           // ResponseEntity<Map> rMapList = restTemplate.postForEntity("http://auth-service/auth/oauth/token",httpEntity,Map.class);
            Map<String,String> rMap = restTemplate.postForObject("http://auth-service/auth/oauth/token",paramMap,Map.class);
            if(rMap != null && !rMap.isEmpty()){
                String accessToken = rMap.get("access_token");
                String tokenType = rMap.get("token_type");
                if(StringUtils.isNotBlank(accessToken) && StringUtils.isNotBlank(tokenType)){
                    super.getRequest().getSession().setAttribute(Constants.SESSION_LOGIN_TOKEN,accessToken);
                    super.getRequest().getSession().setAttribute(Constants.SESSION_LOGIN_TOKEN_TYPE,tokenType);
                    super.getRequest().getSession().setAttribute("LoginUserName",username);
                    Map<String,String> map = new HashMap<>();
                    map.put("msg","Login the success！");
                    map.put("rdirectUrl",rdirectUrl);
                    return ResultUtil.successJson(map);
                }
            }
        }
        return ResultUtil.errorJson("User or password error!");
    }

    /**
     * 登录操作存储Session
     * @param request
     * @param response
     * @param accessToken
     * @param rdirectUrl
     */
    @RequestMapping("/web/loginSession")
    public String loginSession(Model model,HttpServletRequest request, HttpServletResponse response, String accessToken, String rdirectUrl, Principal user){
        //try {
            request.getSession().setAttribute(Constants.SESSION_LOGIN_TOKEN,accessToken);
            //登陆之后只会有调到两个页面（主页和竞彩中心，个人中心只有登陆之后才可以进，如果是登陆注册直接跳转主页）
            //解析需要跳转的地址，如果是需要跳转竞彩中心，不需要管，如果是其他，直接跳转主页
            //rdirectUrl = rdirectUrl.substring(rdirectUrl.indexOf("/"),rdirectUrl.length());
            model = getModel(model);
            if(Constants.PLAYTHELOTTERY_URL.equals(rdirectUrl)){
                return Constants.PLAYTHELOTTERY;
            }
            return Constants.INDEX;
            //response.sendRedirect(rdirectUrl);
        /*} catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 取消登录
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/web/loginOut")
    public ResultUtil loginOut(HttpServletRequest request){
        HttpSession session = request.getSession();
        String accessToken = (String)session.getAttribute(Constants.SESSION_LOGIN_TOKEN);
        if(StringUtils.isNotBlank(accessToken)){
            restTemplate.delete("http://auth-service/auth/api/exit?access_token="+accessToken);
            //清除Session
            session.removeAttribute(Constants.SESSION_LOGIN_TOKEN);//清除令牌
            session.removeAttribute(com.didu.lotteryshop.common.config.Constants.LOGIN_USER);//清除用戶信息
        }
        return ResultUtil.successJson("Exit the success！");
    }

}
