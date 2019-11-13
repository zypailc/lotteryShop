package com.didu.lotteryshop.webgateway.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
            //获取请求参数
            String redirectUrl = request.getRequestURI();
            Map<String,String> map = getAllRequestParam(request);
            //遍历键值对
            Set<Map.Entry<String,String>> entires=map.entrySet();  //键值对的集合,entries代表这个集合
            for(Map.Entry<String,String> entry:entires){  //entry代表每一个取到的键值对，相当于i
                if(redirectUrl.indexOf("?") >= 0){
                    redirectUrl = redirectUrl + "&"+ entry.getKey() +"=" + entry.getValue();
                }else {
                    redirectUrl = redirectUrl + "?"+ entry.getKey() +"=" + entry.getValue();
                }
                System.out.println(entry.getKey());//取键值对中的key
                System.out.println(entry.getValue());//取键值对中的value
            }

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

    private Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
            }
        }
        return res;
    }
}

