package com.didu.lotteryshop.webgateway.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义个一个authenticationEntryPoint，实现如果无访问权限跳转到登录页面
 * oauth2 资源服务器，无权限操作拦截
 * @author CHJ
 */
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws ServletException {
        try {
             //System.out.print(request.getContextPath());
            //登录SESSION_token
            String access_token =  (String) request.getSession().getAttribute(Constants.SESSION_LOGIN_TOKEN);
           // System.out.println("进入Entry方法"+response.getStatus());
            //System.out.println(request.getRequestURI());
            String redirectUrl = request.getRequestURI();

            if(response.getStatus()==500){
                response.sendRedirect("/web/500");
            }else if(response.getStatus()==404){
                response.sendRedirect("/web/404");
            }else{
                if(access_token != null){
                    //access_token 过期处理
                    if(redirectUrl.indexOf("access_token") >= 0){
                        redirectUrl = redirectUrl.replace("?access_token="+access_token,"");
                        redirectUrl = redirectUrl.replace("&access_token="+access_token,"");
                        response.sendRedirect("/web/login?rdirectUrl="+redirectUrl);
                        return;
                    }

                    if(redirectUrl.indexOf("?") >= 0){
                            redirectUrl = redirectUrl+"&access_token="+access_token;
                    }else{
                        redirectUrl = redirectUrl+"?access_token="+access_token;
                    }
                    response.sendRedirect(redirectUrl);
                    //response.setHeader("X-XSRF-TOKEN",access_token);
                    //response.setHeader("access_token",access_token);
                }else{
                    response.sendRedirect("/web/login?rdirectUrl="+redirectUrl);
                }
            }
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}

