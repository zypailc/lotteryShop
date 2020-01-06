package com.didu.lotteryshop.webgateway.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class WebInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
        String requestURI = request.getRequestURI();
        boolean bool = true;
        if (requestURI.indexOf("/web/") >= 0) {
            if (requestURI.indexOf("/web/auth") == -1) {
                String token = (String) request.getSession().getAttribute("session_login_token");
                if(StringUtils.isBlank(token)){
                    response.sendRedirect("/web/authLogin?rdirectUrl=" + requestURI);
                    bool = false;
                }
            }
        }
        return bool;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (response.getStatus() == 500) {
            if(modelAndView != null) {
                modelAndView.setViewName("error/500");
            }
        } else if (response.getStatus() == 404) {
            if(modelAndView != null){
                modelAndView.setViewName("error/404");
            }
            //response.sendRedirect("/web/auth404");
        }
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}
