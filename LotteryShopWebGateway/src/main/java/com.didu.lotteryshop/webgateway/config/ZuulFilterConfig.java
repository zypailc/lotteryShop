package com.didu.lotteryshop.webgateway.config;


import cn.hutool.json.JSONUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Zuul过滤器配置
 **/
@Component
public class ZuulFilterConfig  extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";//route/post/error
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;//需要过滤，且添加的trace
    }

    @Override
    public Object run() {
        //获取当前请求
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
//        Enumeration<String> headers = request.getHeaderNames();
//        while (headers.hasMoreElements()){
//            String name = headers.nextElement();
//            System.out.println(name + ":"+ request.getHeader(name));
//        }
//        System.out.println(request.getSession().getId());
//        System.out.println(RequestContext.getCurrentContext().getRequest().getSession().getId());
//        System.out.println(request.getSession().getAttribute("CHJ"));
//        System.out.println(request.getSession().getAttribute(Constants.SESSION_LOGIN_TOKEN));
//        System.out.println(request.getContextPath());
//        System.out.println(request.getRequestURI());
        String  requestURI =  request.getRequestURI();
        if(StringUtils.isNotBlank(requestURI) && requestURI.indexOf("api/") >= 0){
            String accessToken =  (String)request.getSession().getAttribute(Constants.SESSION_LOGIN_TOKEN);
            String accessTokenType =  (String)request.getSession().getAttribute(Constants.SESSION_LOGIN_TOKEN_TYPE);
            String LoginUserName =  (String)request.getSession().getAttribute("LoginUserName");
            if(StringUtils.isNotBlank(accessToken)){
                requestContext.addZuulRequestHeader("Authorization",accessTokenType+accessToken);
                requestContext.addZuulRequestHeader("LoginUserName",LoginUserName);
            }else{
                requestContext.setSendZuulResponse(false); //验证请求不进行路由
                requestContext.setResponseStatusCode(401);
                return null;
            }
        }
        //requestContext.addZuulRequestHeader("cookie", request.getHeader("cookie"));
        // requestContext.addZuulRequestHeader("cookie", request.getHeader("cookie")+";SESSIONID:"+request.getSession().getId());
        //requestContext.set("SESSION",RequestContext.getCurrentContext().getRequest().getSession().getId());
        // HttpServletResponse response = requestContext.getResponse();
        // HttpServletRequest request = requestContext.getRequest();
//        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, OPTIONS, PATCH");
//        response.setHeader("Access-Control-Allow-Headers", "x-access-token, content-type");
//        response.setHeader("Access-Control-Expose-Headers", "X-forwared-port, X-forwarded-host");
//        response.setHeader("Vary", "Origin,Access-Control-Request-Method,Access-Control-Request-Headers");
        // 跨域请求一共会进行两次请求 先发送options 是否可以请求
//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            requestContext.setSendZuulResponse(false); //验证请求不进行路由
//            return null;
//        }

        requestContext.setSendZuulResponse(true);
        requestContext.setResponseStatusCode(200);
        return null;
    }
}
